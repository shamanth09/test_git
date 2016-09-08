package com.mrk.myordershop.dao.report;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.resource.MeltingAndSealSummaryResource;

@Repository
public class OrderReportDAOImpl extends OrderReportAbstractDAO {

	@Override
	public List<MeltingAndSealSummaryResource> getMeltingSummay(
			Wholesaler wholesaler) {
		List<MeltingAndSealSummaryResource> list = null;
		int totalOrderCount = 0;
		Session session = super.sessionFactory.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Order.class, "o");
			criteria.add(Restrictions.eq("o.wholesaler", wholesaler));
			criteria.add(Restrictions.eq("o.activeFlag", ActiveFlag.ACTIVE));
			criteria.createAlias("o.item", "i");
			criteria.createAlias("i.meltingAndSeal", "m");
			totalOrderCount = super.getOrderCount(criteria);
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount(), "count");
			projectionList.add(Projections.property("m.seal"), "seal");
			projectionList.add(Projections.property("m.melting"), "melting");
			projectionList.add(Projections.groupProperty("m.id"));
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers
					.aliasToBean(MeltingAndSealSummaryResource.class));
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		for (MeltingAndSealSummaryResource resource : list) {
			resource.setTotalOrderCount(totalOrderCount);
		}
		return list;
	}

}
