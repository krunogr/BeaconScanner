package com.file_utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.Parcel;
import android.os.Parcelable;

public class Config implements Parcelable {

	private static final String KEY_ITEM = "config";

	private static final String KEY_USE_SIMULATED_IBEACONS = "useSimulatedIBeacons";
	private static final String KEY_BETWEEN_SCANNING_PERIOD = "betweenScanningPeriod";
	private static final String KEY_SCAN_PERIOD = "scanPeriod";
	private static final String KEY_USE_BACKGROUND_MODE = "useBackgroundMode";
	private static final String BETWEEN_BACKGROUND_SCANNING_PERIOD = "betweenBackgroundScanningPeriod";
	private static final String SCAN_BACKGROUND_PERIOD = "scanBackgroundPeriod";
	private static final String NUMBER_OF_DISTANCE_FOR_AVERAGE = "numberOfDistanceForAverage";
	private static final String DISTANCE_IMMEDIATE = "distanceImmediate";
	private static final String DISTANCE_NEAR = "distanceNear";
	private static final String SHOW_BY_DISTANCE = "showByDistance";

	public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
		public Config createFromParcel(Parcel in) {
			return new Config(in);
		}

		public Config[] newArray(int size) {
			return new Config[size];
		}
	};

	private boolean successfullyLoaded = false;

	private XMLParser xmlParser;

	private Document doc;

	private NodeList nodeList;

	private String abbContactEmail = "";

	private int locationTimeout = 10;

	private int numberOfDistanceForAverage = 5;

	private boolean useSimulatedIBeacons = false;

	private long betweenScanningPeriod;

	private long scanPeriod;

	private boolean useBackgroundMode = false;

	private long betweenBackgroundScanningPeriod;

	private long scanBackgroundPeriod;

	private float distanceImmediate;

	private long distanceNear;

	private boolean showByDistance = false;

	public Config() {
		super();
	}

	public Config(String applicationConfigPath) {

		xmlParser = new XMLParser();

		String configFilePath = applicationConfigPath + "/Config/Config.xml";
		String xmlString = xmlParser.getXmlFromPath(configFilePath);

		doc = xmlParser.getDomElement(xmlString);

		nodeList = doc.getElementsByTagName(KEY_ITEM);

		try {

			for (int i = 0; i < nodeList.getLength(); i++) {
				Element e = (Element) nodeList.item(i);
				useSimulatedIBeacons = Boolean.valueOf(xmlParser.getValue(e,
						KEY_USE_SIMULATED_IBEACONS));
				scanPeriod = Long.valueOf(xmlParser
						.getValue(e, KEY_SCAN_PERIOD));
				betweenScanningPeriod = Long.valueOf(xmlParser.getValue(e,
						KEY_BETWEEN_SCANNING_PERIOD));

				useBackgroundMode = Boolean.valueOf(xmlParser.getValue(e,
						KEY_USE_BACKGROUND_MODE));
				scanBackgroundPeriod = Long.valueOf(xmlParser.getValue(e,
						SCAN_BACKGROUND_PERIOD));
				betweenBackgroundScanningPeriod = Long.valueOf(xmlParser
						.getValue(e, BETWEEN_BACKGROUND_SCANNING_PERIOD));
				numberOfDistanceForAverage = Integer.parseInt(xmlParser
						.getValue(e, NUMBER_OF_DISTANCE_FOR_AVERAGE));
				distanceNear = Long.valueOf(xmlParser
						.getValue(e, DISTANCE_NEAR));
				distanceImmediate = Float.valueOf(xmlParser.getValue(e,
						DISTANCE_IMMEDIATE));
				showByDistance = Boolean.valueOf(xmlParser.getValue(e,
						SHOW_BY_DISTANCE));

			}
			successfullyLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			successfullyLoaded = false;
		}
	}

	public boolean isSuccessfullyLoaded() {
		return successfullyLoaded;
	}

	public boolean isUseSimulatedIBeacons() {
		return useSimulatedIBeacons;
	}

	public void setUseSimulatedIBeacons(boolean useSimulatedIBeacons) {
		this.useSimulatedIBeacons = useSimulatedIBeacons;
	}

	public boolean getUseSimulatedIBeacons() {
		return useSimulatedIBeacons;
	}

	public long getBetweenScanningPeriod() {
		return betweenScanningPeriod;
	}

	public void setBetweenScanningPeriod(long betweenScanningPeriod) {
		this.betweenScanningPeriod = betweenScanningPeriod;
	}

	public long getScanPeriod() {
		return scanPeriod;
	}

	public void setScanPeriod(long scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public boolean getUseBackgroundMode() {
		return useBackgroundMode;
	}

	public void setUseBackgroundMode(boolean useBackgroundMode) {
		this.useBackgroundMode = useBackgroundMode;
	}

	public long getBetweenBackgroundScanningPeriod() {
		return betweenBackgroundScanningPeriod;
	}

	public void setBetweenBackgroundScanningPeriod(
			long betweenBackgroundScanningPeriod) {
		this.betweenBackgroundScanningPeriod = betweenBackgroundScanningPeriod;
	}

	public long getScanBackgroundPeriod() {
		return scanBackgroundPeriod;
	}

	public void setScanBackgroundPeriod(long scanBackgroundPeriod) {
		this.scanBackgroundPeriod = scanBackgroundPeriod;
	}

	public int getNumberOfDistanceForAverage() {
		return numberOfDistanceForAverage;
	}

	public void setNumberOfDistanceForAverage(int numberOfDistanceForAverage) {
		this.numberOfDistanceForAverage = numberOfDistanceForAverage;
	}

	public int describeContents() {
		return 0;
	}

	public void setDistanceImmediate(float distanceImmediate) {
		this.distanceImmediate = distanceImmediate;
	}

	public long getDistanceNear() {
		return distanceNear;
	}

	public void setDistanceNear(long distanceNear) {
		this.distanceNear = distanceNear;
	}

	public float getDistanceImmediate() {
		return distanceImmediate;
	}

	public Config(Parcel source) {
		useSimulatedIBeacons = source.readByte() != 0;
		scanPeriod = source.readLong();

	}

	public boolean getShowByDistance() {
		return showByDistance;
	}

	public void setShowByDistance(boolean showByDistance) {
		this.showByDistance = showByDistance;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (useSimulatedIBeacons ? 1 : 0));
		dest.writeLong(scanPeriod);
		dest.writeLong(betweenScanningPeriod);

		dest.writeByte((byte) (useBackgroundMode ? 1 : 0));
		dest.writeByte((byte) (showByDistance ? 1 : 0));
		dest.writeLong(scanBackgroundPeriod);
		dest.writeLong(betweenBackgroundScanningPeriod);
		dest.writeLong(distanceNear);
		dest.writeFloat(distanceImmediate);
		dest.writeInt(numberOfDistanceForAverage);
	}
}
