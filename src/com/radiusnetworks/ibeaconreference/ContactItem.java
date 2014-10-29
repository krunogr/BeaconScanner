package com.radiusnetworks.ibeaconreference;

public class ContactItem {

	String phoneNumber, name, email, location;

	public ContactItem(String name, String phoneNumber, String email,
			String location) {
		super();
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.email = email;
		this.location = location;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
