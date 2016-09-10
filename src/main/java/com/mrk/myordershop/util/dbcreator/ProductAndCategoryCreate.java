package com.mrk.myordershop.util.dbcreator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.dao.CategoryDAO;
import com.mrk.myordershop.dao.MeasurementDAO;
import com.mrk.myordershop.dao.ProductDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.exception.EntityNotPersistedException;

/**
 * RetailerCreate.java Naveen Apr 7, 2015
 */
@Component
public class ProductAndCategoryCreate {

	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private MeasurementCreate measurementCreate;
	@Autowired
	private MeasurementDAO measurementDAO;

	private Map<Category, Product[]> productName = new HashMap<Category, Product[]>();

	public void init() {
		Measurement length = null;
		try {
			length = measurementDAO.get(Measurement.v.LENGTH);
		} catch (EntityDoseNotExistException e) {
			Measurement m = new Measurement(Measurement.v.LENGTH);
			measurementDAO.save(m);
		}
		Measurement size = null;
		try {
			size = measurementDAO.get(Measurement.v.SIZE);
		} catch (EntityDoseNotExistException e) {
			Measurement m = new Measurement(Measurement.v.SIZE);
			measurementDAO.save(m);
		}
		Measurement weight = null;
		try {
			weight = measurementDAO.get(Measurement.v.WEIGHT);
		} catch (EntityDoseNotExistException e) {
			Measurement m = new Measurement(Measurement.v.WEIGHT);
			measurementDAO.save(m);
		}
		productName.put(Category.fillCategory("Bali"), new Product[] { Product.fillProduct("Rajkot Bali", length) });

		productName.put(Category.fillCategory("Bangles"),
				new Product[] { Product.fillProduct("Baby Bangles", size), Product.fillProduct("Bombay Bangles", size),
						Product.fillProduct("Hand Cutting Bangles", size), Product.fillProduct("Coorgi Bangles", size),
						Product.fillProduct("Nakash Kada", size) });

		productName.put(Category.fillCategory("Bracelate"),
				new Product[] { Product.fillProduct("Cartier Bracelate", length),
						Product.fillProduct("Delhi Cartier Bracelate", length),
						Product.fillProduct("Delhi Fancy Bracelate", length),
						Product.fillProduct("Hollow Bracelate", length),
						Product.fillProduct("Hollow Bracelate 603", length),
						Product.fillProduct("Indian Singapore Bracelate", length),
						Product.fillProduct("Indo Italian Bracelate", length),
						Product.fillProduct("Lotus Bracelate", length), Product.fillProduct("Nawabi Bracelate", length),
						Product.fillProduct("Singapore Stamping Bracelate", length),
						Product.fillProduct("Tendulkar Bracelate", length) });

		productName.put(Category.fillCategory("Chain"), new Product[] { Product.fillProduct("Avalakki Chain", length),
				Product.fillProduct("Baby Gajje Udadhara Chain", length), Product.fillProduct("Batani Chain", length),
				Product.fillProduct("Necklace Back Chain", length), Product.fillProduct("Ball Badsha Chain", length),
				Product.fillProduct("Ball Gajra Chain", length), Product.fillProduct("Ball Gajra Solid Chain", length),
				Product.fillProduct("Ball Khusbu Chain", length), Product.fillProduct("Ball Leaf Chain", length),
				Product.fillProduct("Ball Matar Chain", length), Product.fillProduct("Ball Rajam Teeka Chain", length),
				Product.fillProduct("Bulb Chain", length), Product.fillProduct("Cutting Rope Chain", length),
				Product.fillProduct("Cycle Chain", length), Product.fillProduct("Delhi Fancy Chain", length),
				Product.fillProduct("Delhi Handmade Chain", length), Product.fillProduct("Hollow Fancy Chain", length),
				Product.fillProduct("Hollow Leaf Chain", length), Product.fillProduct("Hollow Rope Chain", length),
				Product.fillProduct("HTC Casting PC Chain", length), Product.fillProduct("HTC Plain Chain", length),
				Product.fillProduct("Indo Italian Chain", length), Product.fillProduct("Kerala Chain", length),
				Product.fillProduct("Laxmi Kas Chain", length), Product.fillProduct("Lotus Chain", length),
				Product.fillProduct("Machine Chain", length), Product.fillProduct("Mangalsutra", length),
				Product.fillProduct("Mohan Mala", length), Product.fillProduct("Nawabi Chain", length),
				Product.fillProduct("Nano Chain", length), Product.fillProduct("Parinda Chain", length),
				Product.fillProduct("Pineapple Chain", length), Product.fillProduct("Satya Chain", length),
				Product.fillProduct("Sridevi Pyramid Chain", length), Product.fillProduct("Tendulkar Chain", length),
				Product.fillProduct("Tyre Chain", length), Product.fillProduct("V Dalbati Chain", length),
				Product.fillProduct("Bulb Anjali Chain", length), Product.fillProduct("Bulb Batani Mix", length) });

		productName.put(Category.fillCategory("Dollar"), new Product[] { Product.fillProduct("Dollar") });

		productName.put(Category.fillCategory("Hangings"), new Product[] { Product.fillProduct("Bombay Hangings"),
				Product.fillProduct("Ad Hangings"), Product.fillProduct("Kasse Flat Drops Hangings") });

		productName.put(Category.fillCategory("Jhumki"),
				new Product[] { Product.fillProduct("Antique Jhumki"), Product.fillProduct("Kasse Jhumki 3 Line"),
						Product.fillProduct("Loose Balls Jhumki"), Product.fillProduct("Moti Jhumki"),
						Product.fillProduct("Nakash Jhumki") });

		productName.put(Category.fillCategory("Long Haar"), new Product[] { Product.fillProduct("Long Haar") });

		productName.put(Category.fillCategory("Matal"),
				new Product[] { Product.fillProduct("Delhi Gajje Matal"), Product.fillProduct("Jahangiri Matal Plain"),
						Product.fillProduct("Jahangiri Matal Stone"), Product.fillProduct("S Matal Plain"),
						Product.fillProduct("S Matal Stone"), Product.fillProduct("Single Line Delhi Gajje Matal") });

		productName.put(Category.fillCategory("Mope"),
				new Product[] { Product.fillProduct("Plain Mope"), Product.fillProduct("Stone Mope") });

		productName.put(Category.fillCategory("Necklace"),
				new Product[] { Product.fillProduct("Bombay Necklace"), Product.fillProduct("CB NECKLACE") });

		productName.put(Category.fillCategory("Ring"),
				new Product[] { Product.fillProduct("AD Stone Ring", size), Product.fillProduct("BOLL TV RING", size),
						Product.fillProduct("CBE Ring", size), Product.fillProduct("Double Step Boat Ring", size),
						Product.fillProduct("Elegant Ring", size), Product.fillProduct("Elephant Hair Ring", size),
						Product.fillProduct("Gatti TV Ring", size), Product.fillProduct("Gents Sonata Ring", size),
						Product.fillProduct("Ladies Sonata Ring", size), Product.fillProduct("Navratna Ring", size),
						Product.fillProduct("Single Step Boat Ring", size),
						Product.fillProduct("Single Stone Casting Ring", size),
						Product.fillProduct("Single Stone Ring", size),
						Product.fillProduct("Sonata Close Ring", size) });

		productName.put(Category.fillCategory("Tali"),
				new Product[] { Product.fillProduct("Batlu Tali Plain"), Product.fillProduct("Batlu Tali Stone"),
						Product.fillProduct("Kalasham Tali"), Product.fillProduct("Laxmi Kas"),
						Product.fillProduct("Mysore Tali"), Product.fillProduct("Pakshi Tali"),
						Product.fillProduct("Rajam Kas"), Product.fillProduct("Tatte Tali") });

		productName.put(Category.fillCategory("Teeka"), new Product[] { Product.fillProduct("Stone Teeka") });

		productName.put(Category.fillCategory("Teeka Chain"),
				new Product[] { Product.fillProduct("Cut Jahangiri Teeka Chain", length),
						Product.fillProduct("JAHANGIRI CHAIN PLAIN", length),
						Product.fillProduct("Delhi Teeka Chain Half Round", length),
						Product.fillProduct("S Teeka Chain", length) });

		productName.put(Category.fillCategory("Tops"),
				new Product[] { Product.fillProduct("AD Kamal Tops"), Product.fillProduct("AD Stone Tops"),
						Product.fillProduct("Ball Tops"), Product.fillProduct("Bunch Tops"),
						Product.fillProduct("Bunch Tops Close"), Product.fillProduct("CBE Tops"),
						Product.fillProduct("Kasse Tops"), Product.fillProduct("Laxmi Tops"),
						Product.fillProduct("Moti Tops") });
		productName.put(Category.fillCategory("Mangalsutra"),
				new Product[] { Product.fillProduct("Short"), Product.fillProduct("Long") });

		productName.put(Category.fillCategory("Teeka"), new Product[] {});

		productName.put(Category.fillCategory("Kada"), new Product[] {});

		productName.put(Category.fillCategory("Mala"), new Product[] {});

		productName.put(Category.fillCategory("Parinda Chain"), new Product[] {});

		productName.put(Category.fillCategory("Maang Teeka"), new Product[] {});

		productName.put(Category.fillCategory("Jabka"), new Product[] {});

		productName.put(Category.fillCategory("Urdhara"), new Product[] {});
	}

	public ProductAndCategoryCreate(ApplicationContext appContext) {
		this.categoryDAO = (CategoryDAO) appContext.getBean(CategoryDAO.class);
		this.productDAO = (ProductDAO) appContext.getBean(ProductDAO.class);
		this.measurementCreate = (MeasurementCreate) appContext.getBean(MeasurementCreate.class);
		this.measurementCreate.create();
		init();
	}

	public ProductAndCategoryCreate() {
	}

	@PersistTransactional
	public void create(boolean shouldCreateMeasurements) {
		if (shouldCreateMeasurements)
			this.measurementCreate.create();
		init();
		try {
			createAllType();
		} catch (EntityNotPersistedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createAllType() throws EntityNotPersistedException {
		for (Category type : productName.keySet()) {
			Category productType = null;
			try {
				productType = categoryDAO.getCategoryByName(type.getName());
				for (Product name : productName.get(type)) {
					name.setCategory(productType);
					saveName(name);
				}
			} catch (EntityDoseNotExistException e) {
				categoryDAO.save(type);
				for (Product name : productName.get(type)) {
					name.setCategory(type);
					saveName(name);
				}
			}
		}
	}

	private void saveName(Product name) throws EntityNotPersistedException {
		try {
			name = productDAO.getByName(name.getName());
		} catch (EntityDoseNotExistException e) {
			productDAO.save(name);
		}
	}

	public static void main(String arg[]) throws EntityDoseNotExistException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");
		ProductAndCategoryCreate retailerCreate = appContext.getBean(ProductAndCategoryCreate.class);

		retailerCreate.create(true);
	}
}
