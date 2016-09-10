package com.mrk.myordershop.util;

import java.util.ArrayList;
import java.util.List;

import com.mrk.myordershop.bean.Category;
import com.mrk.myordershop.bean.Product;

/**
 * ProductUtil.java Naveen Mar 30, 2015
 */
public class ProductUtil {

	public static boolean isLengthAvailable(Product product) {
		switch (product.getCategory().getName()) {
		case "Bali":
			return false;
		case "Bangle":
			return false;
		case "Bracelate":
			return true;
		case "Chain":
			return true;
		case "Hanging":
			return false;
		case "Jumki":
			return false;
		case "Long Chain":
			return true;
		case "Mangalsutra":
			return true;
		case "Matal":
			return true;
		case "Mope":
			return false;
		case "Necklace":
			return true;
		case "Parinda Chain":
			return true;
		case "Pendant":
			return false;
		case "Ring":
			return false;
		case "Teeka Chain":
			return true;
		case "Thali":
			return false;
		case "Tops":
			return false;
		default:
			return false;
		}
	}

	public static boolean isSizeAvailable(Product product) {
		switch (product.getCategory().getName()) {
		case "Bali":
			return false;
		case "Bangle":
			return true;
		case "Bracelate":
			return false;
		case "Chain":
			return false;
		case "Hanging":
			return false;
		case "Jumki":
			return false;
		case "Long Chain":
			return false;
		case "Mangalsutra":
			return false;
		case "Matal":
			return false;
		case "Mope":
			return false;
		case "Necklace":
			return false;
		case "Parinda Chain":
			return false;
		case "Pendant":
			return false;
		case "Ring":
			return true;
		case "Teeka Chain":
			return false;
		case "Thali":
			return false;
		case "Tops":
			return false;
		default:
			return false;
		}
	}

	public static List<Category> shortCategories(List<Category> categories) {
		List<Category> categories2 = new ArrayList<Category>();
		categories2.addAll(categories);
		for (Category category : categories) {
			switch (category.getName()) {
			case "Bali":
				categories2.set(1, category);
			case "Bangles":
				categories2.set(2, category);
			case "Bracelate":
				categories2.set(3, category);
			case "Chain":
				categories2.set(4, category);
			case "Hangings":
				categories2.set(5, category);
			case "Jhumki":
				categories2.set(6, category);
			case "Long Haar":
				categories2.set(0, category);
			case "Mangalsutra":
				categories2.set(7, category);
			case "Matal":
				categories2.set(11, category);
			case "Mope":
				categories2.set(8, category);
			case "Necklace":
				categories2.set(9, category);
			case "Dollar":
				categories2.set(9, category);
			case "Ring":
				categories2.set(13, category);
			case "Teeka Chain":
				categories2.set(14, category);
			case "Tali":
				categories2.set(12, category);
			case "Tops":
				categories2.set(15, category);
			}

		}
		return categories2;
	}
}
