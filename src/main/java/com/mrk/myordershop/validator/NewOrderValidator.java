package com.mrk.myordershop.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mrk.myordershop.bean.Detail;
import com.mrk.myordershop.bean.InstantWholesalerOrder;
import com.mrk.myordershop.bean.Measurement;
import com.mrk.myordershop.bean.MeltingAndSeal;
import com.mrk.myordershop.bean.Order;
import com.mrk.myordershop.bean.Product;
import com.mrk.myordershop.bean.Supplier;
import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.bean.Wholesaler;
import com.mrk.myordershop.bean.WholesalerOrder;
import com.mrk.myordershop.exception.EntityDoseNotExistException;
import com.mrk.myordershop.security.oauth.service.OauthUserDetailsService;
import com.mrk.myordershop.service.OrderService;
import com.mrk.myordershop.service.ProductService;
import com.mrk.myordershop.service.RelationService;
import com.mrk.myordershop.service.UserService;

@Component
public class NewOrderValidator implements Validator {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RelationService relationService;
	@Autowired
	private UserService userService;

	User currentUser = null;

	@Override
	public boolean supports(Class<?> clazz) {
		return Order.class.isAssignableFrom(clazz) || InstantWholesalerOrder.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors error) {

		currentUser = OauthUserDetailsService.getCurrentDomainUser();

		// Order check
		if (obj instanceof Order) {
			retailerOrderValidation(obj, error);
		}

		// wholesaler Order check
		if (obj instanceof WholesalerOrder) {
			wholesalerOrderValidation(obj, error);
		}
	}

	private void wholesalerOrderValidation(Object obj, Errors error) {

		WholesalerOrder instantWholesalerOrder = (WholesalerOrder) obj;
		Product product = null;
		if (instantWholesalerOrder.getOrder() != null) {
			try {
				Order order = orderService.getOrder(instantWholesalerOrder.getOrder().getId(), currentUser);
				product = order.getItem().getProduct();
			} catch (EntityDoseNotExistException e1) {
				error.rejectValue("order", "required", new Object[] { "order " }, "error.required.field");
			}
		} else
			error.rejectValue("order", "required", new Object[] { "order " }, "error.required.field");

		Supplier supplier = instantWholesalerOrder.getSupplier();
		Detail detail = instantWholesalerOrder.getItem().getDetail();
		MeltingAndSeal meltingAndSeal = instantWholesalerOrder.getItem().getMeltingAndSeal();
		List<Measurement> measurement = null;

		// productID check
		if (product != null) {
			if (product.getId() != null) {
				try {
					Product product2 = productService.get(product.getId());
					measurement = product2.getMeasurements();
				} catch (EntityDoseNotExistException e) {
					error.rejectValue("product.id", "required", new Object[] { "product" }, "error.required.field");
				}
			} else
				error.rejectValue("product.id", "required", new Object[] { "product id " }, "error.required.field");
		} else {
			error.rejectValue("item.product", "required", new Object[] { "product " }, "error.required.field");
		}
		// supplier check
		if (supplier != null) {
			if (supplier.getId() == null || supplier.getId().equals("")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "supplier.id", "required", new Object[] { "supplier" },
						"error.required.field");
			}
		} else
			error.rejectValue("supplier", "required", new Object[] { "supplier " }, "error.required.field");

		// detail check
		if (detail != null) {
			error.pushNestedPath("item");

			error.pushNestedPath("detail");
			if (detail.getQuantity() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "quantity", "required", new Object[] { "quantity" },
						"error.required.field");
			if (detail.getWeight() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "weight", "required", new Object[] { "weight" },
						"error.required.field");

			if (product != null) {
				if (product.getId() != null) {
					for (Measurement measurement2 : measurement) {
						if (measurement2.getMeasurementValue().equals(Measurement.v.LENGTH.getValue())) {
							if (detail.getLength() == null || detail.getLength().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "length", "required",
										new Object[] { "Length" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.SIZE.getValue())) {
							if (detail.getSize() == null || detail.getSize().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "size", "required",
										new Object[] { "size" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.BACK_CHAIN.getValue())) {
							if (detail.getBackChain() == null || detail.getBackChain().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "backChain", "required",
										new Object[] { "backChain" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.SIZE_TYPE.getValue())) {
							if (detail.getSizeType() == null || detail.getSizeType().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "sizeType", "required",
										new Object[] { "sizeType" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.INITIAL.getValue())) {
							if (detail.getInitial() == null || detail.getInitial().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "initial", "required",
										new Object[] { "initial" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.WITH_SCREW.getValue())) {
							if (detail.getWithScrew() == null || detail.getWithScrew().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "withScrew", "required",
										new Object[] { "withScrew" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.LINES.getValue())) {
							if (detail.getLines() == null || detail.getLines().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "lines", "required",
										new Object[] { "lines" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.HUCK.getValue())) {
							if (detail.getHuck() == null || detail.getHuck().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "huck", "required",
										new Object[] { "huck" }, "error.required.field");
						}
					}
				}
			}

			// if (!(detail.getStoneWeight() instanceof Double))
			// ValidationUtils.rejectIfEmptyOrWhitespace(error, "stoneWeight",
			// "required", "Required field");

			error.popNestedPath();
		} else
			error.rejectValue("item.detail", "required", new Object[] { "product detail" }, "error.required.field");

		// melting and seal check
		if (meltingAndSeal != null) {
			error.pushNestedPath("meltingAndSeal");
			if (meltingAndSeal.getMelting() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "melting", "required", new Object[] { "melting" },
						"error.required.field");
			error.popNestedPath();
		} else
			error.rejectValue("item.meltingAndSeal", "required", new Object[] { "meltingAndSeal" },
					"error.required.field");

	}

	private void retailerOrderValidation(Object obj, Errors error) {

		Order order = (Order) obj;
		Product product = order.getItem().getProduct();
		Wholesaler wholesaler = order.getWholesaler();
		Detail detail = order.getItem().getDetail();
		MeltingAndSeal meltingAndSeal = order.getItem().getMeltingAndSeal();
		List<Measurement> measurement = null;

		// productID and categoryId check
		if (product != null) {
			if (product.getId() != null) {
				try {
					Product product2 = productService.get(product.getId());
					measurement = product2.getMeasurements();
				} catch (EntityDoseNotExistException e) {
					error.rejectValue("product.id", "required", new Object[] { "product" }, "error.required.field");
				}
			} else
				error.rejectValue("item.product", "required", new Object[] { "product " }, "error.required.field");
		} else
			error.rejectValue("item.product", "required", new Object[] { "product " }, "error.required.field");

		// wholesalerInstantOrder check
		if (wholesaler != null) {
			if (wholesaler.getId() == null || wholesaler.getId().equals("")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "wholesaler.id", "required",
						new Object[] { "wholesaler" }, "error.required.field");
			}
		} else {
			error.rejectValue("wholesaler", "required", new Object[] { "wholesaler " }, "error.required.field");
		}

		// detail check
		if (detail != null) {
			error.pushNestedPath("item");

			error.pushNestedPath("detail");
			// try {
			// Relation relation = relationService.get(wholesaler.getId(),
			// currentUser);
			// Double minvalue = relation.getSetting().getMinWeight();
			// if (minvalue != null) {
			// if (minvalue > detail.getWeight()) {
			// error.rejectValue("weight", "required", new Object[] {
			// "weight ",
			// minvalue },
			// "error.minWeight.field");
			// }
			// }
			// } catch (EntityDoseNotExistException e1) {
			// Wholesaler relatedWholesaler = null;
			// try {
			// relatedWholesaler = (Wholesaler)
			// userService.get(wholesaler.getId());
			// } catch (EntityDoseNotExistException e) {
			// e.printStackTrace();
			// }
			// error.rejectValue("weight", "required",
			// new Object[] { currentUser.getFirmName(),
			// relatedWholesaler.getFirmName() },
			// "error.userNotInRelation.field");
			// }
			if (detail.getQuantity() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "quantity", "required", new Object[] { "quantity" },
						"error.required.field");
			if (detail.getWeight() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "weight", "required", new Object[] { "weight" },
						"error.required.field");

			if (product != null) {
				if (product.getId() != null) {
					for (Measurement measurement2 : measurement) {
						if (measurement2.getMeasurementValue().equals(Measurement.v.LENGTH.getValue())) {
							if (detail.getLength() == null || detail.getLength().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "length", "required",
										new Object[] { "Length" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.SIZE.getValue())) {
							if (detail.getSize() == null || detail.getSize().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "size", "required",
										new Object[] { "size" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.BACK_CHAIN.getValue())) {
							if (detail.getBackChain() == null || detail.getBackChain().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "backChain", "required",
										new Object[] { "backChain" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.SIZE_TYPE.getValue())) {
							if (detail.getSizeType() == null || detail.getSizeType().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "sizeType", "required",
										new Object[] { "sizeType" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.INITIAL.getValue())) {
							if (detail.getInitial() == null || detail.getInitial().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "initial", "required",
										new Object[] { "initial" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.WITH_SCREW.getValue())) {
							if (detail.getWithScrew() == null || detail.getWithScrew().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "withScrew", "required",
										new Object[] { "withScrew" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.LINES.getValue())) {
							if (detail.getLines() == null || detail.getLines().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "lines", "required",
										new Object[] { "lines" }, "error.required.field");
						}
						if (measurement2.getMeasurementValue().equals(Measurement.v.HUCK.getValue())) {
							if (detail.getHuck() == null || detail.getHuck().equals(""))
								ValidationUtils.rejectIfEmptyOrWhitespace(error, "huck", "required",
										new Object[] { "huck" }, "error.required.field");
						}
					}
				}
			}
			// if (!(detail.getStoneWeight() instanceof Double))
			// ValidationUtils.rejectIfEmptyOrWhitespace(error,
			// "stoneWeight",
			// "required", "Required field");

			error.popNestedPath();
		} else
			error.rejectValue("item.detail", "required", new Object[] { "product detail" }, "error.required.field");

		// melting and seal check
		if (meltingAndSeal != null) {
			error.pushNestedPath("meltingAndSeal");
			if (meltingAndSeal.getMelting() == null)
				ValidationUtils.rejectIfEmptyOrWhitespace(error, "melting", "required", new Object[] { "melting" },
						"error.required.field");
			error.popNestedPath();
		} else
			error.rejectValue("item.meltingAndSeal", "required", new Object[] { "meltingAndSeal" },
					"error.required.field");

	}

}
