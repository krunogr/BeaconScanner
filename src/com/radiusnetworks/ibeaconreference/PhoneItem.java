package com.radiusnetworks.ibeaconreference;

public class PhoneItem {

	String number, name;

	public PhoneItem(String name, String number) {
		super();
		this.number = number;
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
