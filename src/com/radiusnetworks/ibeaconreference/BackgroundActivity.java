package com.radiusnetworks.ibeaconreference;

import android.app.Activity;
import android.os.Bundle;

public class BackgroundActivity extends Activity {
	protected static final String TAG = "BackgroundActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background);
	}

}