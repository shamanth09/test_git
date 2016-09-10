package com.mrkinnoapps.myordershopadmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.constant.Role;
import com.mrkinnoapps.myordershopadmin.bean.dto.Register;
import com.mrkinnoapps.myordershopadmin.bean.dto.SearchFilter.SearchIn;
import com.mrkinnoapps.myordershopadmin.bean.dto.NotRegisteredUser;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchFilter;
import com.mrkinnoapps.myordershopadmin.bean.dto.UserSearchResource;
import com.mrkinnoapps.myordershopadmin.bean.entity.Address;
import com.mrkinnoapps.myordershopadmin.bean.entity.Image;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Retailer;
import com.mrkinnoapps.myordershopadmin.bean.entity.Supplier;
import com.mrkinnoapps.myordershopadmin.bean.entity.Token;
import com.mrkinnoapps.myordershopadmin.bean.entity.User;
import com.mrkinnoapps.myordershopadmin.bean.entity.UserRole;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.dao.AddressDAO;
import com.mrkinnoapps.myordershopadmin.dao.MeltingAndSealDAO;
import com.mrkinnoapps.myordershopadmin.dao.OrderDAO;
import com.mrkinnoapps.myordershopadmin.dao.TokenDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserSearch;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;
import com.mrkinnoapps.myordershopadmin.exception.InvalidDataException;
import com.mrkinnoapps.myordershopadmin.mail.MailMessageFactory;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchEngine;
import com.mrkinnoapps.myordershopadmin.util.searchengine.SearchResult;

/**
 * UserServiceImpl.java Naveen Apr 10, 2015
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private MeltingAndSealDAO meltingAndSealingDAO;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	@Qualifier("mailMessageFactory")
	MailMessageFactory mailMessageFactory;


	@Autowired
	public UserServiceImpl(UserSearch userSearch, SearchEngine engine) {
		engine.searcherRegister(userSearch);
	}

	@Override
	public void saveUserRole(UserRole user) {
		userDao.saveUserRole(user);
	}

	@Override
	public void save(User user) throws EntityNotPersistedException,
	EntityDoseNotExistException {
		User old=user;
		String pass = UUID.randomUUID().toString().substring(0, 8);
		UserRole userRole=userDao.getUserRole(user.getUserRoles().get(0).getId());
		Address address=null;
		if(userRole.getName().equalsIgnoreCase("Supplier"))
		{
			Supplier supplier=new Supplier();
			supplier.setName(user.getName());
			supplier.setEmail(user.getEmail());
			supplier.setMobile(user.getMobile());
			supplier.setPassword(pass);
			supplier.setActiveFlag(ActiveFlag.ACTIVE);
			supplier.setCreateTimestamp(new Date());
			supplier.setUserRoles(user.getUserRoles());
			address=user.getAddress();
			user=supplier;
			userDao.save(user);
			address.setUser(user);
		}
		else
			if(userRole.getName().equalsIgnoreCase("Wholesaler"))
			{
				Wholesaler wholesaler=new Wholesaler();
				wholesaler.setName(user.getName());
				wholesaler.setEmail(user.getEmail());
				wholesaler.setMobile(user.getMobile());
				wholesaler.setPassword(pass);
				wholesaler.setActiveFlag(ActiveFlag.ACTIVE);
				wholesaler.setCreateTimestamp(new Date());
				wholesaler.setUserRoles(user.getUserRoles());
				address=user.getAddress();
				user=wholesaler;
				userDao.save(user);
				address.setUser(user);
			}
			else if(userRole.getName().equalsIgnoreCase("Retailer")){
				Retailer retailer=new Retailer();
				retailer.setName(user.getName());
				retailer.setEmail(user.getEmail());
				retailer.setMobile(user.getMobile());
				retailer.setPassword(pass);
				retailer.setActiveFlag(ActiveFlag.ACTIVE);
				retailer.setCreateTimestamp(new Date());
				retailer.setUserRoles(user.getUserRoles());
				address=user.getAddress();
				user=retailer;
				userDao.save(user);
				address.setUser(user);
			}
		addressDAO.save(address);
		mailMessageFactory.getActivationMessage().send(get(user.getId()));
		old.setAddress(address);
		old.setId(user.getId());
	}

	@Override
	public User update(User user) throws EntityDoseNotExistException {
		User userfdb = userDao.get(user.getId());
		userfdb.setName(user.getName());
		userfdb.setMobile(user.getMobile());
		userfdb.setEmail(user.getEmail());
		userfdb.setActiveFlag(user.getActiveFlag());
		userfdb.setUpdateTimestamp(new Date());
		userDao.update(userfdb);
		if(user.getAddress().getId()!=null){
			Address address=addressDAO.getAddress(user.getAddress().getId());	
			address.setCity(user.getAddress().getCity());
			address.setArea(user.getAddress().getArea());
			address.setState(user.getAddress().getState());
			address.setPincode(user.getAddress().getPincode());
			address.setCountry(user.getAddress().getCountry());
			address.setUpdateTimestamp(new Date());
			address.setTitle(user.getAddress().getTitle());
			address.setLandmark(user.getAddress().getLandmark());
			addressDAO.update(address);
		}
		else
		{
			Address address=user.getAddress();
			address.setUser(user);
			addressDAO.save(address);
		}
		return userfdb;
	}

	@Override
	public User register(Register register) throws EntityNotPersistedException {

		User retailer = register.getRole().equals(Role.ROLE_RETAIL) ? new Retailer()
		: new Supplier();
		retailer.setActiveFlag(ActiveFlag.INPROCESS);
		retailer.setEmail(register.getEmail());
		retailer.setName(register.getName());
		retailer.setPassword(register.getPassword());
		retailer.setMobile(register.getMobile());

		try {
			UserRole userRole = userDao.getUserRoleByRole(register.getRole());
			retailer.getUserRoles().add(userRole);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		userDao.save(retailer);

		Address address = new Address();
		address.setTitle(register.getFirmName());
		address.setUser(retailer);
		addressDAO.save(address);

		return retailer;
	}

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	public UserRole getUserRole(Role role) throws EntityDoseNotExistException {
		return userDao.getUserRoleByRole(role);
	}

	@Override
	public List<UserRole> getUserRoles() throws EntityDoseNotExistException {
		return userDao.getUserRoles();
	}

	@Override
	public User getActiveUser(String id) throws EntityDoseNotExistException {
		return userDao.get(id, ActiveFlag.ACTIVE);
	}

	@Override
	public User get(String id) throws EntityDoseNotExistException {
		return userDao.get(id);
	}

	@Override
	public User updateUserImage(String id, Image image)
			throws EntityDoseNotExistException {
		User userfdb = userDao.get(id, ActiveFlag.ACTIVE);
		userfdb.setImage(image);
		userDao.update(userfdb);
		return userfdb;
	}

	@Override
	public Page<User> find(UserFilter filter, Pageable pageable, User user) {
		return userDao.find(filter, pageable, user);
	}

	@Override
	public List<UserSearchResource> search(UserSearchFilter filter, User user,
			Pageable pageable) {
		List<UserSearchResource> result = new ArrayList<UserSearchResource>();
		List<String> excludeList = new ArrayList<String>();
		if (filter.getSearchIn().isEmpty())
			filter.fillSearchOnUser();
		for (SearchIn searchIn : filter.getSearchIn()) {
			String[] excludeUsers1 = new String[] {};

			addToSearchResult(result, searchIn, filter.getQuery(),
					filter.getRole(), user, excludeUsers1, userDao);
		}

		return result;
	}

	private void addToSearchResult(List<UserSearchResource> result,
			SearchIn field, String query, Role role, User user,
			String[] excludeUsers, UserDAO... dao) {
		for (UserDAO searchFieldDAO : dao) {
			List<UserSearchResource> orderNo = searchFieldDAO.search(role,
					query, field, user, excludeUsers);
			for (UserSearchResource order : orderNo) {
				order.setLable(field.getSuffix());
				result.add(order);
			}
		}

	}

	@Override
	public List<User> getByRole(Role roleWsaler) {
		return userDao.getByRole(roleWsaler);
	}

	@Override
	public void activateUser(String tokenString)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		Token token = tokenService.get(tokenString);
		if (token.isExpire()) {
			throw new InvalidDataException("token expired");
		}
		User user = token.getUser();
		user.setActiveFlag(ActiveFlag.ACTIVE);
		userDao.update(user);
		tokenService.acceptToken(token);
	}

	@Override
	public User forgotPassword(String emailOrMobile, String actionUrl)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		User user = userDao.getUserByMobieOrEmail(emailOrMobile);
		user.setActiveFlag(ActiveFlag.INPROCESS);
		userDao.update(user);
		Token token = tokenService.createToken(2000, Token.Type.FORGOTPASSWORD,
				user);
		return user;
	}

	// @Override
	// public User changePassword(String password, String tokenString)
	// throws EntityDoseNotExistException, EntityNotPersistedException {
	// Token token = tokenService.get(tokenString);
	// if (token.isExpire()) {
	// throw new InvalidDataException("token expired");
	// }
	// User user = token.getUser();
	// user.setActiveFlag(ActiveFlag.ACTIVE);
	// user.setPassword(password);
	// userDao.update(user);
	// tokenService.acceptToken(token);
	// return user;
	// }

	@Override
	public void resentToken(String userId, Token.Type type, String actionUrl)
			throws EntityDoseNotExistException {
		Token token = tokenService.getToken(type, userId);
		if (token.getType().equals(Token.Type.FORGOTPASSWORD)) {
			// mailMessageFactory.getForgotPasswordMessage().send(
			// actionUrl.replace(":token", token.getToken()),
			// token.getUser());
		} else if (token.getType().equals(Token.Type.ACTIVATION)) {
			// mailMessageFactory.getActivationMessage().send(
			// actionUrl.replace(":token", token.getToken()),
			// token.getUser());
		}
	}

	@Override
	public User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException {
		return userDao.getUserByMobieOrEmail(mobieOrEmail);
	}

	@Override
	public void changePassword(String password, String userID)
			throws InvalidDataException, EntityDoseNotExistException {
		userDao.changePassword(password, userID);
	}

	@Override
	public void changeRole(String userId, UserRole role)
			throws EntityNotPersistedException, EntityDoseNotExistException {
		UserRole userRole = userDao.getUserRole(role.getId());


		User user = userDao.get(userId);
		Integer total = orderDAO.getOrderCount(user);
		if (total > 0)
			throw new EntityNotPersistedException("Order",
					"This User has pending orders");
		else {
			addressDAO.delete(user.getAddress());
			userDao.delete(user);

			if (userRole.getRole().equals(Role.ROLE_RETAIL)) {
				Retailer retailer = new Retailer();
				retailer.setId(userId);
				retailer.setActiveFlag(user.getActiveFlag());
				retailer.setAddress(user.getAddress());
				retailer.setCreateTimestamp(user.getCreateTimestamp());
				retailer.setDob(user.getDob());
				retailer.setEmail(user.getEmail());
				retailer.setGender(user.getGender());
				retailer.setImage(user.getImage());
				retailer.setMobile(user.getMobile());
				retailer.setName(user.getName());
				retailer.setPassword(user.getPassword());
				retailer.setTelephone(user.getTelephone());
				retailer.setUpdateTimestamp(user.getUpdateTimestamp());
				retailer.getUserRoles().add(userRole);

				userDao.save(retailer);
				addressDAO.save(retailer.getAddress());
			}

			else if (userRole.getRole().equals(Role.ROLE_WSALER)) {
				Wholesaler wholesaler = new Wholesaler();
				wholesaler.setId(userId);

				wholesaler.setActiveFlag(user.getActiveFlag());
				wholesaler.setAddress(user.getAddress());
				wholesaler.setCreateTimestamp(user.getCreateTimestamp());
				wholesaler.setDob(user.getDob());
				wholesaler.setEmail(user.getEmail());
				wholesaler.setGender(user.getGender());
				wholesaler.setImage(user.getImage());
				wholesaler.setMobile(user.getMobile());
				wholesaler.setName(user.getName());
				wholesaler.setPassword(user.getPassword());
				wholesaler.setTelephone(user.getTelephone());
				wholesaler.setUpdateTimestamp(user.getUpdateTimestamp());

				wholesaler.getUserRoles().add(userRole);

				userDao.save(wholesaler);
				addressDAO.save(wholesaler.getAddress());
			} else if (userRole.getRole().equals(Role.ROLE_SUPPLIER)) {
				Supplier supplier = new Supplier();
				supplier.setId(userId);

				supplier.setActiveFlag(user.getActiveFlag());
				supplier.setAddress(user.getAddress());
				supplier.setCreateTimestamp(user.getCreateTimestamp());
				supplier.setDob(user.getDob());
				supplier.setEmail(user.getEmail());
				supplier.setGender(user.getGender());
				supplier.setImage(user.getImage());
				supplier.setMobile(user.getMobile());
				supplier.setName(user.getName());
				supplier.setPassword(user.getPassword());
				supplier.setTelephone(user.getTelephone());
				supplier.setUpdateTimestamp(user.getUpdateTimestamp());

				supplier.getUserRoles().add(userRole);

				userDao.save(supplier);
				addressDAO.save(supplier.getAddress());
			}
		}
	}

	@Override
	public void changeUserStatus(String userId, User user)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		User user2 = userDao.get(userId);
		user2.setActiveFlag(user.getActiveFlag());
		List<Token> list = tokenDAO.findByUser(userId);
		if (list.size() == 0) {
			userDao.update(user2);
		} else
			throw new EntityNotPersistedException(User.class,
					"This user's Token have not experied/deleted.");
	}

	@Override
	public List<SearchResult> searchUser(UserFilter filter, String q) {
		return userDao.searchUser(filter,q);
	}

	/* 
	 * Mallinath Jun 20, 2016
	 */
	@Override
	public void delete(User user) {
		if(user instanceof Wholesaler){
			List<MeltingAndSeal> list=meltingAndSealingDAO.find((Wholesaler)user);
			for(MeltingAndSeal ms:list)
			{
				meltingAndSealingDAO.delete(ms);
			}
		}
		addressDAO.delete(user.getAddress());
		userDao.delete(user);
	}

	/* 
	 * Mallinath Jul 22, 2016
	 */
	@Override
	public Page<NotRegisteredUser> getNotRegisteredUsers(Pageable pageable) {
		return userDao.getNotRegisteredUsers(pageable);
	}

}
