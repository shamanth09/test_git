package com.mrk.myordershop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrk.myordershop.bean.Address;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.bean.Register;
import com.mrk.myordershop.bean.Retailer;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.Token;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.UserRole;
import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.bean.dto.UserCredential;
import com.mrk.myordershop.bean.dto.UserFilter;
import com.mrk.myordershop.bean.dto.UserSearchFilter;
import com.mrk.myordershop.bean.dto.UserSearchResource;
import com.mrk.myordershop.bean.mail.MailMessageFactory;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.config.annotation.ReadTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.RelationStatus;
import com.mrk.myordershop.constant.Role;
import com.mrk.myordershop.dao.AddressDAO;
import com.mrk.myordershop.dao.ImageDAO;
import com.mrk.myordershop.dao.OrderDAO;
import com.mrk.myordershop.dao.RelationDAO;
import com.mrk.myordershop.dao.UserDAO;
import com.mrk.myordershop.dao.WholesalerInstantOrderDAOImpl;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;
import com.mrk.myordershop.exception.InvalidDataException;
import com.mrk.myordershop.security.oauth.service.OauthUserDetailsService;

/**
 * UserServiceImpl.java Naveen Apr 10, 2015
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDao;
	@Autowired
	private ImageDAO imageDAO;
	@Autowired
	private RelationDAO relationDAO;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	TokenService tokenService;
	@Autowired
	OauthUserDetailsService oauthUserDetailsService;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	@Qualifier("mailMessageFactory")
	MailMessageFactory mailMessageFactory;

	@Autowired
	private OauthUserDetailsService aDetailsService;

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	@PersistTransactional
	public void saveUserRole(UserRole user) {
		userDao.saveUserRole(user);
	}

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	@PersistTransactional
	public void save(User user) throws EntityNotPersistedException {
		userDao.save(user);
	}

	@Override
	@PersistTransactional
	public User update(String id, User user) throws EntityDoseNotExistException {
		User userfdb = userDao.get(id, ActiveFlag.ACTIVE);
		String oldMoblie = userfdb.getMobile();
		userfdb.setName(user.getName());
		userfdb.setMobile(user.getMobile());
		userDao.update(userfdb);
		orderDAO.updateCustomerNumberAndUserReference(oldMoblie, userfdb);
		return userfdb;
	}

	@Override
	@PersistTransactional
	public User register(Register register) throws EntityNotPersistedException {

		User retailer = register.getRole().equals(Role.ROLE_RETAIL) ? new Retailer()
				: new Supplier();
		retailer.setActiveFlag(ActiveFlag.INPROCESS);
		retailer.setEmail(register.getEmail());
		retailer.setName(register.getName());
		retailer.setPassword(register.getPassword());
		retailer.setMobile(register.getMobile());

		try {
			UserRole userRole = userDao.getUserRole(register.getRole());
			retailer.getUserRoles().add(userRole);
		} catch (EntityDoseNotExistException e) {
			e.printStackTrace();
		}
		userDao.save(retailer);
		Address address = new Address();
		address.setTitle(register.getFirmName());
		address.setUser(retailer);
		addressDAO.save(address);

		if (retailer instanceof Retailer) {
			orderDAO.updateCustomerNumberAndUserReference(retailer.getMobile(),
					retailer);
		}
		if (register.getEmail() != null
				&& !register.getEmail().trim().equals("")) {
			Token token = tokenService.createToken(2000, Token.Type.ACTIVATION,
					retailer);
			mailMessageFactory.getActivationMessage().send(
					register.getActivationUrl().replace(":token",
							token.getToken()), retailer);
		}
		return retailer;
	}

	/*
	 * Naveen Apr 10, 2015
	 */
	@Override
	@ReadTransactional
	public UserRole getUserRole(Role role) throws EntityDoseNotExistException {
		return userDao.getUserRole(role);
	}

	@Override
	@ReadTransactional
	public User getActiveUser(String id) throws EntityDoseNotExistException {
		return userDao.get(id, ActiveFlag.ACTIVE);
	}

	@Override
	@ReadTransactional
	public User get(String id) throws EntityDoseNotExistException {
		return userDao.get(id);
	}

	@Override
	@PersistTransactional
	public void changePassword(UserCredential userCredential, User user)
			throws InvalidDataException {
		aDetailsService.changePassword(userCredential, user);
	}

	@Override
	@PersistTransactional
	public User updateUserImage(String id, Image image)
			throws EntityDoseNotExistException {
		User userfdb = userDao.get(id, ActiveFlag.ACTIVE);
		if (userfdb.getImage() != null)
			imageDAO.delete(userfdb.getImage());
		userfdb.setImage(image);
		userDao.update(userfdb);
		return userfdb;
	}

	@Override
	@ReadTransactional
	public Page<User> find(UserFilter filter, Pageable pageable, User user) {
		return userDao.find(filter, pageable, user);
	}

	@Override
	@ReadTransactional
	public List<UserSearchResource> search(UserSearchFilter filter, User user,
			Pageable pageable) {
		List<UserSearchResource> result = new ArrayList<UserSearchResource>();
		List<String> excludeList = new ArrayList<String>();
		if (filter.getSearchIn().isEmpty())
			filter.fillSearchOnUser();
		for (SearchIn searchIn : filter.getSearchIn()) {
			String[] excludeUsers1 = addToSearchResult(result, searchIn,
					filter.getQuery(), filter.getRole(), filter.getStatus(),
					user, relationDAO);

			if (filter.getStatus() == null || filter.getStatus().size() < 1)
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

	private String[] addToSearchResult(List<UserSearchResource> result,
			SearchIn field, String query, Role role,
			List<RelationStatus> status, User user, RelationDAO searchFieldDAO) {
		List<UserSearchResource> list = searchFieldDAO.search(role, status,
				query, field, user);
		String[] relationList = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			UserSearchResource order = list.get(i);
			order.setLable(field.getSuffix());
			result.add(order);
			if (order.getResultId() != null)
				relationList[i] = order.getResultId();
		}
		return relationList;
	}

	@Override
	@ReadTransactional
	public List<User> getByRole(Role roleWsaler) {
		return userDao.getByRole(roleWsaler);
	}

	@Override
	@PersistTransactional
	public void activateUser(String tokenString)
			throws EntityDoseNotExistException {
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
	@PersistTransactional
	public User forgotPassword(String emailOrMobile, String actionUrl)
			throws EntityDoseNotExistException {
		User user = userDao.getUserByMobieOrEmail(emailOrMobile);
		user.setActiveFlag(ActiveFlag.INPROCESS);
		userDao.update(user);
		oauthUserDetailsService.revokeAllToken(emailOrMobile);
		Token token = tokenService.createToken(2000, Token.Type.FORGOTPASSWORD,
				user);
		mailMessageFactory.getForgotPasswordMessage().send(
				actionUrl.replace(":token", token.getToken()), user);
		return user;
	}

	@Override
	@PersistTransactional
	public User changePassword(String password, String tokenString)
			throws EntityDoseNotExistException {
		Token token = tokenService.get(tokenString);
		if (token.isExpire()) {
			throw new InvalidDataException("token expired");
		}
		User user = token.getUser();
		user.setActiveFlag(ActiveFlag.ACTIVE);
		user.setPassword(password);
		userDao.update(user);
		tokenService.acceptToken(token);
		return user;
	}

	@Override
	@ReadTransactional
	public void resentToken(String userId, Token.Type type, String actionUrl)
			throws EntityDoseNotExistException {
		Token token = tokenService.getToken(type, userId);
		if (token.getType().equals(Token.Type.FORGOTPASSWORD)) {
			mailMessageFactory.getForgotPasswordMessage().send(
					actionUrl.replace(":token", token.getToken()),
					token.getUser());
		} else if (token.getType().equals(Token.Type.ACTIVATION)) {
			mailMessageFactory.getActivationMessage().send(
					actionUrl.replace(":token", token.getToken()),
					token.getUser());
		}
	}

	@Override
	@ReadTransactional
	public User getUserByMobieOrEmail(String mobieOrEmail)
			throws EntityDoseNotExistException {
		return userDao.getUserByMobieOrEmail(mobieOrEmail);
	}
}
