package com.mrk.myordershop;



enum CustomerField implements Field {

	CUSTOMER_NAME;

	@Override
	public String getLable() {
		return this.toString();
	}

	@Override
	public boolean equalsField(Field other) {
		return this.equals(other);
	}
}
enum UserNameField implements Field {

	USER_NAME;

	@Override
	public String getLable() {
		return this.toString();
	}

	@Override
	public boolean equalsField(Field other) {
		return this.equals(other);
	}
	
}
public class TestEnum {
	public static void main(String[] args) {
		Enum a = CustomerField.CUSTOMER_NAME;
		Enum b = UserNameField.USER_NAME;
		
	}
	
}
