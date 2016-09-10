package com.mrkinnoapps.myordershopadmin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrkinnoapps.myordershopadmin.bean.constant.ActiveFlag;
import com.mrkinnoapps.myordershopadmin.bean.entity.MeltingAndSeal;
import com.mrkinnoapps.myordershopadmin.bean.entity.Wholesaler;
import com.mrkinnoapps.myordershopadmin.dao.MeltingAndSealDAO;
import com.mrkinnoapps.myordershopadmin.dao.UserDAO;
import com.mrkinnoapps.myordershopadmin.exception.EntityDoseNotExistException;
import com.mrkinnoapps.myordershopadmin.exception.EntityNotPersistedException;

@Service
public class MeltingAndSealServiceImpl implements MeltingAndSealService {

	@Autowired
	private MeltingAndSealDAO meltingAndSealDAO;
	@Autowired
	private UserDAO userDAO;

	@Override
	public MeltingAndSeal get(int id) throws EntityDoseNotExistException {
		return meltingAndSealDAO.get(id);
	}

	@Override
	public List<MeltingAndSeal> findByWholesalerId(String wholesalerId) throws EntityDoseNotExistException {
		Wholesaler wholesaler = (Wholesaler) userDAO.get(wholesalerId);
		return this.find(wholesaler);
	}

	@Override
	public List<MeltingAndSeal> find(Wholesaler wholesaler) throws EntityDoseNotExistException {
		return meltingAndSealDAO.find(wholesaler);
	}
	
	@Override
	public MeltingAndSeal updateMeltAndSeal(MeltingAndSeal meltingAndSeal, String userId)
			throws EntityDoseNotExistException, EntityNotPersistedException {
		if (meltingAndSeal.getId() != null) {
			MeltingAndSeal seal = meltingAndSealDAO.get(meltingAndSeal.getId());
			if (meltingAndSeal.getActiveFlag().equals(ActiveFlag.ACTIVE) || meltingAndSeal.getActiveFlag().equals(ActiveFlag.INACTIVE)) {
				seal.setMelting(meltingAndSeal.getMelting());
				seal.setSeal(meltingAndSeal.getSeal());
				if (meltingAndSeal.getActiveFlag().equals(ActiveFlag.ACTIVE) )
				seal.setActiveFlag(ActiveFlag.ACTIVE);
				else
					seal.setActiveFlag(ActiveFlag.INACTIVE);
				meltingAndSealDAO.update(seal);
			}
			return seal;
		} else {
			if (meltingAndSeal.getActiveFlag().equals(ActiveFlag.ACTIVE) || meltingAndSeal.getActiveFlag().equals(ActiveFlag.INACTIVE)){
			Wholesaler wholesaler = new Wholesaler();
			wholesaler.setId(userId);
			meltingAndSeal.setWholesaler(wholesaler);
			meltingAndSeal.setCreateTimestamp(new Date());
			meltingAndSealDAO.save(meltingAndSeal);
			return meltingAndSeal;
			}
			return null;
		}

	}
}
