package com.radiusnetworks.ibeacon;

import java.util.ArrayList;
import java.util.List;

public class SimulatedScanData {

	public static List<IBeacon> iBeacons;

	public static boolean USE_SIMULATED_IBEACONS = false;

	static {
		iBeacons = new ArrayList<IBeacon>();

		if (USE_SIMULATED_IBEACONS) {

			IBeacon iBeacon1 = new IBeacon(
					"842af9c4-08f5-11e3-9282-f23c91aec051", 1, 1, -55, -55);
			IBeacon iBeacon2 = new IBeacon(
					"842af9c4-08f5-11e3-9282-f23c91aec052", 1, 2, -55, -55);
			iBeacons.add(iBeacon1);
			iBeacons.add(iBeacon2);

		}
	}
}
