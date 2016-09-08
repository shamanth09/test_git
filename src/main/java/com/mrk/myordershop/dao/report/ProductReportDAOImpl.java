package com.mrk.myordershop.dao.report;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.dto.CategorySummaryOrderWise;
import com.mrk.myordershop.bean.dto.ProductSummaryOrderWise;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.resource.OrderReportResource;

@Repository
public class ProductReportDAOImpl implements ProductReportDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public OrderReportResource getCategoryWiseOrderReports(User user) {
		OrderReportResource orderReportResource = new OrderReportResource();
		Session session = sessionFactory.getCurrentSession();
		try {
			String userRestrict = null;
			if (user instanceof Wholesaler)
				userRestrict = "o.WHOLESALER=:userId";
			else
				userRestrict = "o.USER_ID=:userId";
			SQLQuery query = session
					.createSQLQuery("SELECT c.id as id,  c.name as name,  COUNT(o.ID) as count,  SUM(d.WEIGTH) as weight,  SUM(d.QUANTITY)  as quantity "
							+ "FROM  MOS_CATEGORY c  "
							+ "LEFT JOIN MOS_PRODUCT p  ON p.CATEGORY_ID = c.ID "
							+ "LEFT JOIN MOS_ITEM i    ON i.PRODUCT_ID = p.ID  "
							+ "LEFT JOIN MOS_ORDER o    ON i.ORDER_ID = o.ID  and "
							+ userRestrict
							+ " and o.ACTIVE_FLAG = :activeFlage "
							+ "LEFT JOIN MOS_DETAIL d   ON d.ID = i.DETAIL_ID GROUP BY c.ID ORDER BY count DESC");
			query.addScalar("id", new IntegerType());
			query.addScalar("name", new StringType());
			query.addScalar("count", new LongType());
			query.addScalar("weight", new DoubleType());
			query.addScalar("quantity", new LongType());
			query.setParameter("userId", user.getId());
			query.setParameter("activeFlage", ActiveFlag.ACTIVE.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(CategorySummaryOrderWise.class));
			List<CategorySummaryOrderWise> list = query.list();
			orderReportResource.setOrderStatusSummaries(list);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return orderReportResource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrderReportResource getProductWiseOrderReports(int categoryId,
			User user) {
		OrderReportResource orderReportResource = new OrderReportResource();
		Session session = sessionFactory.getCurrentSession();
		try {
			String userRestrict = null;
			if (user instanceof Wholesaler)
				userRestrict = "o.WHOLESALER=:userId";
			else
				userRestrict = "o.USER_ID=:userId";
			SQLQuery query = session
					.createSQLQuery("SELECT p.id as id,  p.name as name,  COUNT(o.ID) as count,  SUM(d.WEIGTH) as weight,  SUM(d.QUANTITY)  as quantity "
							+ "FROM  MOS_PRODUCT p  "
							+ "LEFT JOIN MOS_CATEGORY c  ON p.CATEGORY_ID = c.ID "
							+ "LEFT JOIN MOS_ITEM i    ON i.PRODUCT_ID = p.ID  "
							+ "LEFT JOIN MOS_ORDER o    ON i.ORDER_ID = o.ID  and "
							+ userRestrict
							+ " and o.ACTIVE_FLAG = :activeFlage "
							+ "LEFT JOIN MOS_DETAIL d  ON d.ID = i.DETAIL_ID where p.CATEGORY_ID = :categoryId GROUP BY p.ID ORDER BY count DESC");
			query.addScalar("id", new IntegerType());
			query.addScalar("name", new StringType());
			query.addScalar("count", new LongType());
			query.addScalar("weight", new DoubleType());
			query.addScalar("quantity", new LongType());
			query.setParameter("userId", user.getId());
			query.setParameter("activeFlage", ActiveFlag.ACTIVE.toString());
			query.setParameter("categoryId", categoryId);
			query.setResultTransformer(Transformers
					.aliasToBean(ProductSummaryOrderWise.class));
			List<ProductSummaryOrderWise> list = query.list();
			orderReportResource.setOrderStatusSummaries(list);
		} catch (HibernateException e) {
			e.printStackTrace();
		} 
		return orderReportResource;
	}

}
