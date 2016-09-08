package com.mrk.myordershop.util.dbcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mrk.myordershop.bean.Catalog;
import com.mrk.myordershop.bean.CatalogDesign;
import com.mrk.myordershop.bean.Image;
import com.mrk.myordershop.config.annotation.PersistTransactional;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.dao.CatalogDAO;
import com.mrk.myordershop.exception.EntityDoseNotExistException;

@Component
public class CatalogCreate {

	private static Logger log = Logger.getLogger(CatalogCreate.class);

	private static final String DIRECTORY = "D:" + File.separator + "Shared"
			+ File.separator + "catalogs";

	@Autowired
	private CatalogDAO catalogDAO;

	private Image getImage(File file) {
		Image image = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] byarray = new byte[(int) file.length()];
			fis.read(byarray);
			image = new Image(byarray);
			image.setActiveFlag(ActiveFlag.ACTIVE);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return image;
	}

	private void createAndSaveDesign(File file, Catalog catalog) {
		String name = file.getName().replace(".", "@").split("@")[0];
		CatalogDesign design = null;
		try {
			design = catalogDAO.getDesignByDesignNo(
					new Integer(name.substring(0, 2)), name.substring(2));
		} catch (EntityDoseNotExistException e) {
			design = new CatalogDesign();
			design.setActiveFlag(ActiveFlag.ACTIVE);
			design.setPage(new Integer(name.substring(0, 2)));
			design.setCatalog(catalog);
			design.setDesignNo(name.substring(2));
			design.setImage(getImage(file));
			catalogDAO.saveDesign(design);
		}

	}

	private void upload(File directory, Catalog catalog) {
		if (directory.isDirectory()) {
			File[] fils = directory.listFiles();
			for (File file : fils) {
				if (file.isFile() && catalog != null) {
					createAndSaveDesign(file, catalog);
				} else if (file.isDirectory() && catalog == null) {
					Catalog newCategory = createCatalog(file.getName());
					upload(file, newCategory);
				}
			}
		}
	}

	private Catalog createCatalog(String name) {
		Catalog catalog = null;
		try {
			catalog = catalogDAO.getCatalogByName(name);
		} catch (EntityDoseNotExistException e) {
			catalog = new Catalog();
			catalog.setActiveFlag(ActiveFlag.ACTIVE);
			catalog.setName(name);
			catalogDAO.saveCatalog(catalog);
		}
		return catalog;
	}


	@PersistTransactional
	public void create(String path) {
		upload(new File(path), null);
	}
}
