package com.radiusnetworks.ibeaconreference;


public class Beacon extends IBeacon {

	String name, avgOfDistance, distanceCategory;

	protected Beacon() {

	}

	protected Beacon(String proximityUuid, String name,
			String distanceCategory, int major, int minor, int txPower,
			int rssi, String avgOfDistance) {
		this.proximityUuid = proximityUuid.toLowerCase();
		this.major = major;
		this.minor = minor;
		this.rssi = rssi;
		this.txPower = txPower;
		this.name = name;
		this.avgOfDistance = avgOfDistance;
		this.distanceCategory = distanceCategory;
	}

	public Beacon(String proximityUuid, String name, String distanceCategory,
			int major, int minor) {
		this.proximityUuid = proximityUuid.toLowerCase();
		this.major = major;
		this.minor = minor;
		this.name = name;
		this.rssi = rssi;
		this.txPower = -59;
		this.rssi = 0;
		this.distanceCategory = distanceCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvgOfDistance() {
		return avgOfDistance;
	}

	public void setAvgOfDistance(String avgOfDistance) {
		this.avgOfDistance = avgOfDistance;
	}

	public String getDistanceCategory() {
		return distanceCategory;
	}

	public void setDistanceCategory(String distanceCategory) {
		this.distanceCategory = distanceCategory;
	}

}
