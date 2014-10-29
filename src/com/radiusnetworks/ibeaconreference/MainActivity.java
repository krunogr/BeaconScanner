package com.radiusnetworks.ibeaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.file_utils.Config;

public class MainActivity extends Activity implements OnClickListener {
	
	Button btnStartScan;
	
	private ImageView startBackgroundImageView;
	
	Config config;
	
	static boolean USE_SIMULATED_IBEACONS = false;
	
	static long BETWEEN_SCANNING_PERIOD = 3000;
	
	static long SCAN_PERIOD = 500;
	
	static boolean USE_BACKGROUND_MODE = false;
	
	static boolean SHOW_BY_DISTANCE = true;
	
	static long BETWEEN_BACKGROUND_SCANNING_PERIOD = 3000;
	
	static long SCAN_BACKGROUND_PERIOD = 500;
	
	static int NUMBER_OF_DISTANCE_FOR_AVERAGE = 5;
	
	static float DISTANCE_IMMEDIATE = 1 / 2;
	
	static float DISTANCE_NEAR = 2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startBackgroundImageView = (ImageView) findViewById(R.id.start_background_image_view);
		startBackgroundImageView.setOnClickListener(this);
		config = new Config((Environment.getExternalStorageDirectory() + "/" + getResources().getString(
				R.string.application_path)));
		USE_SIMULATED_IBEACONS = config.getUseSimulatedIBeacons();
		BETWEEN_SCANNING_PERIOD = config.getBetweenScanningPeriod();
		SCAN_PERIOD = config.getScanPeriod();

		USE_BACKGROUND_MODE = config.getUseBackgroundMode();
		BETWEEN_BACKGROUND_SCANNING_PERIOD = config.getBetweenBackgroundScanningPeriod();
		SCAN_BACKGROUND_PERIOD = config.getScanBackgroundPeriod();
		NUMBER_OF_DISTANCE_FOR_AVERAGE = config.getNumberOfDistanceForAverage();
		DISTANCE_IMMEDIATE=config.getDistanceImmediate();
		DISTANCE_NEAR = config.getDistanceNear();
		SHOW_BY_DISTANCE=config.getShowByDistance();
	}


	public void onClick(View v) {
		int viewId = v.getId();

		switch (viewId) {
		case R.id.start_background_image_view:
			Intent intent = new Intent(this, ScanActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
