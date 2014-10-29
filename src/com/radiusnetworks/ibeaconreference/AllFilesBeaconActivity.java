package com.radiusnetworks.ibeaconreference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AllFilesBeaconActivity extends Activity implements
		OnItemClickListener {

	public static final String BASE_FILE_PATH = "base_file_path";

	public static final String UUID_FOR_SELECTED_IBEACON = "uuid_for_selected_iBeacon";

	public static final String DISTANCE_OF_SELECTED_IBEACON = "distance_of_selected_iBeacon";

	ListView filesList;

	TextView titleForSelectedIBeacon;

	String basePath, uuid_for_selected_iBeacon,
			distance_category_for_selected_iBeacon;

	File allFileForSelectedIBeacon[];

	AllFilesBeaconAdapter allFilesBeaconAdapter;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_files_beacon);
		context = this;
		filesList = (ListView) findViewById(R.id.files_for_beacon_listview);
		titleForSelectedIBeacon = (TextView) findViewById(R.id.textView_title_for_selected_iBeacon);
		getFilesForSelectedIBeacon();

	}

	@Override
	protected void onStop() {
		ScanActivity.sound = false;
		super.onStop();
	}

	@Override
	protected void onPause() {
		ScanActivity.sound = false;
		super.onPause();
	}

	public void getFilesForSelectedIBeacon() {

		Bundle extra = getIntent().getExtras();
		basePath = extra.getString(BASE_FILE_PATH);
		uuid_for_selected_iBeacon = extra.getString(UUID_FOR_SELECTED_IBEACON);
		distance_category_for_selected_iBeacon = extra
				.getString(DISTANCE_OF_SELECTED_IBEACON);

		titleForSelectedIBeacon.setText("Files for iBeacon "
				+ uuid_for_selected_iBeacon + ":");
		File file = new File(basePath);

		allFileForSelectedIBeacon = file.listFiles();
		Arrays.sort(allFileForSelectedIBeacon);

		allFilesBeaconAdapter = new AllFilesBeaconAdapter(this,
				R.layout.file_row,
				checkFilesForCategory(allFileForSelectedIBeacon), basePath);
		filesList.setAdapter(allFilesBeaconAdapter);
		filesList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String path = ((File) parent.getItemAtPosition(position)).getPath();
		Log.d("myTag", path);

		File myFile = new File(path);
		try {
			FileOpen.openFile(this, myFile);
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<File> checkFilesForCategory(File allFileForSelectedIBeacon[]) {
		List<File> listOfAllFiles = Arrays.asList(allFileForSelectedIBeacon);
		List<File> listOfSpecificFiles = new ArrayList<File>();

		for (File element : listOfAllFiles) {
			if (element.getAbsolutePath().contains(
					distance_category_for_selected_iBeacon)
					|| element.getAbsolutePath().contains("_CONTACT")
					|| element.getAbsolutePath().contains("_TEL LIST")) {
				listOfSpecificFiles.add(element);
			}
		}
		if (listOfSpecificFiles.isEmpty()
				|| MainActivity.SHOW_BY_DISTANCE == false) {
			listOfSpecificFiles = listOfAllFiles;
		}
		return listOfSpecificFiles;
	}

}
