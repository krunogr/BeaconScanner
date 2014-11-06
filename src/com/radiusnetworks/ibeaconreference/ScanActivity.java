package com.radiusnetworks.ibeaconreference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.csvreader.CsvReader;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class ScanActivity extends Activity implements IBeaconConsumer, OnItemClickListener, OnItemLongClickListener,
		OnCheckedChangeListener {

	public static final String CSV_EXT = ".csv";

	protected static final String TAG = "MonitoringActivity";

	boolean runThread = false, refreshUI = true;

	private static CsvReader csvReader;

	private File csvFile, csvFileForExport;

	private final String mCSVDataHeader = "UUID,Name,ServN,SN,LT,PL,PN,Time,IMEI,Latitude,Longitude,Altitude\n";

	private String mCSVData = mCSVDataHeader;

	ListView scanIDList;
	private List<Beacon> foundIBeacons = null;

	private List<String> dataForExport = new ArrayList<String>();

	private FoundBeaconAdapter foundBeaconAdapter;

	String basePath;

	String uuudForSelectedIBeacon;

	String distanceCategoryOfSelectedIBeacon;

	private int requestCode = 2;

	int counterForSim = 0;

	private Map<String, String> map;

	private AlertDialog beaconDialog;

	private View BeaconObjectDialogView;

	EditText nameOfBeacon;

	private ToggleButton toggleButton;

	SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd/ hh:mm:ss");

	SimpleDateFormat dtName = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

	ImageButton takePictureButton;

	Button takeNoteButton;

	private static final int PICTURE_MEDIA = 0;

	File[] listOfFiles;

	String picturePath = "";

	public static boolean sound = false;

	File folder;

	HashMap<String, ArrayList<Double>> mapForAverage = new HashMap<String, ArrayList<Double>>();

	Intent intent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		BeaconObjectDialogView = LayoutInflater.from(this).inflate(R.layout.beacon_dialog_layout, null);
		setContentView(R.layout.activity_scan);
	
		scanIDList = (ListView) findViewById(R.id.found_beacon_listview);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
		takePictureButton = (ImageButton) BeaconObjectDialogView.findViewById(R.id.take_picutre_btn);
	
		verifyBluetooth();
	
		iBeaconManager.setForegroundBetweenScanPeriod(MainActivity.BETWEEN_SCANNING_PERIOD);
		iBeaconManager.setForegroundScanPeriod(MainActivity.SCAN_PERIOD);
		iBeaconManager.setBackgroundBetweenScanPeriod(15000);
		iBeaconManager.setBackgroundScanPeriod(MainActivity.SCAN_BACKGROUND_PERIOD);
		iBeaconManager.bind(this);

		foundIBeacons = new ArrayList<Beacon>();
		/**
		 * path of application´s folder
		 */
		basePath = Environment.getExternalStorageDirectory() + "/"
				+ getResources().getString(R.string.application_path);
		timer.start();

		csvFile = new File(basePath + "/" + "Data/" + "ServNToSN" + CSV_EXT);
		nameOfBeacon = (EditText) BeaconObjectDialogView.findViewById(R.id.name_beacon_edit_text);
		intent = new Intent(this, ScanActivity.class);

	}

	/**
	 * thread which refreshing view of scanIdList every 3 seconds.
	 */
	/** The timer. */
	Thread timer = new Thread() { // new thread
		public void run() {
			try {
				do {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (runThread) {
								reloadBeaconAndView();
								Log.d("myTag", "RELOAD UI: " + new Date().toString());
								runThread = false;
							}
						}
					});
					sleep(MainActivity.BETWEEN_SCANNING_PERIOD / 2);
				} while (true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
			}
		};
	};


	private void verifyBluetooth() {

		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
						System.exit(0);
					}
				});
				builder.show();
			}
		} catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
					System.exit(0);
				}

			});
			builder.show();

		}

	}

	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);


	@Override
	protected void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
	}


	@Override
	protected void onPause() {
		super.onPause();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, MainActivity.USE_BACKGROUND_MODE);
		sound = true;
	}


	@Override
	protected void onResume() {
		super.onResume();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, false);

	}


	@Override
	protected void onStart() {
		if (sound && MainActivity.USE_BACKGROUND_MODE == true) {
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);

			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
			sound = false;

		}
		super.onStart();
	}

	private void reloadBeaconAndView() {

		if (refreshUI) {
			foundBeaconAdapter = new FoundBeaconAdapter(this, R.layout.found_beacon_row, foundIBeacons);
			scanIDList.setAdapter(foundBeaconAdapter);
			scanIDList.setOnItemClickListener(this);
			scanIDList.setOnItemLongClickListener(this);
			foundBeaconAdapter.notifyDataSetChanged();
			toggleButton.setOnCheckedChangeListener(this);
		}
	}

	@Override
	public void onIBeaconServiceConnect() {
		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region region) {

			}

			@Override
			public void didExitRegion(Region region) {

			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {

			}

		});

		try {

			iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));

		} catch (RemoteException e) {
		}
		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
		

				if (MainActivity.USE_BACKGROUND_MODE && !iBeacons.isEmpty()) {

					startActivity(intent);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				}

				foundIBeacons.clear();
				if (MainActivity.USE_SIMULATED_IBEACONS) {
					Beacon simulatedIBeacon_1 = new Beacon("842af9c4-08f5-11e3-9282-f23c91aec051", "", "IMMEDIATE", 1,
							1);
					Beacon simulatedIBeacon_2 = new Beacon("842af9c4-08f5-11e3-9282-f23c91aec052", "", "IMMEDIATE", 1,
							1);
					iBeacons.add(simulatedIBeacon_1);
					iBeacons.add(simulatedIBeacon_2);

				}
				if (iBeacons.size() > 0) {

					for (IBeacon element : iBeacons) {

					
						if (mapForAverage.containsKey(element.getProximityUuid())) {
							mapForAverage.get(element.getProximityUuid()).add(element.getAccuracy());
						} else {
							ArrayList<Double> listForAverage = new ArrayList<Double>();
							listForAverage.add(element.getAccuracy());
							mapForAverage.put(element.getProximityUuid(), listForAverage);

						}

					}
					for (IBeacon element : iBeacons) {
						foundIBeacons.add(new Beacon(element.getProximityUuid(), "",
								checkDistanceCategory(calculateAvg(element.getProximityUuid())), element.getMajor(),
								element.getMinor(), element.getTxPower(), element.getRssi(), String
										.valueOf(calculateAvg(element.getProximityUuid()))));

					
						dataForExport.add(element.getProximityUuid() + ", "
								+ String.valueOf(element.getAccuracy()).substring(0, 4)
								+ ", ABCXYZ11,3.251618.5,,,,,,,," + dt.format(new Date()) + "\n");

					}
					Log.d("myTag", "MAKE LIST: " + new Date().toString());
				}
				loadFromCSV();
				checkInCSV();
				runThread = true;

			}

		});

		try {
			iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
		} catch (RemoteException e) {
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		uuudForSelectedIBeacon = ((Beacon) parent.getItemAtPosition(position)).getProximityUuid();
		distanceCategoryOfSelectedIBeacon = checkDistanceCategory(((Beacon) parent.getItemAtPosition(position))
				.getAvgOfDistance());
		Log.d("myTag", uuudForSelectedIBeacon);

		File uuidPathFile = new File(basePath + "/" + "Files/" + uuudForSelectedIBeacon + "/");
		Log.d("myTag", uuidPathFile.toString());
	
		if (!uuidPathFile.exists()) {
			uuidPathFile.mkdir();

		}
		loadFilesForSelectedIBeacon(uuidPathFile.getAbsolutePath());

		// new
		// AlertDialog.Builder(this).setTitle(R.string.warning).setMessage(R.string.no_files_for_iBeacon)
		// .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialogInterface, int i) {
		// // dismiss the dialog
		// }
		// }).show();

	}


	private void loadFilesForSelectedIBeacon(String uuidPath) {
		Intent intent = new Intent(this, AllFilesBeaconActivity.class);
		intent.putExtra(AllFilesBeaconActivity.BASE_FILE_PATH, uuidPath);
		intent.putExtra(AllFilesBeaconActivity.UUID_FOR_SELECTED_IBEACON, uuudForSelectedIBeacon);
		intent.putExtra(AllFilesBeaconActivity.DISTANCE_OF_SELECTED_IBEACON, distanceCategoryOfSelectedIBeacon);
		startActivityForResult(intent, requestCode);

	}


	private void loadFromCSV() {
		map = new HashMap<String, String>();

		try {
			csvReader = new CsvReader(csvFile.getAbsolutePath());

			csvReader.readHeaders();
			while (csvReader.readRecord()) {

				Log.d("myTag", csvReader.get("UUID") + csvReader.get("Name"));
				map.put(csvReader.get("UUID"), csvReader.get("Name"));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		csvReader.close();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		beaconObjectDialog(foundBeaconAdapter.getItem(position));
		return true;
	}


	private void beaconObjectDialog(final Beacon tempBeacon) {

		takePictureButton.setOnClickListener(clickMediaListener(PICTURE_MEDIA, tempBeacon));
		Log.d("myTag", tempBeacon.getProximityUuid());
		nameOfBeacon.setText(tempBeacon.getName());
		String pathOfUUID = basePath + "/" + "Files/" + tempBeacon.getProximityUuid();
		folder = new File(pathOfUUID);
		listOfFiles = folder.listFiles();
		beaconDialog = new AlertDialog.Builder(this).setTitle("Edit").setView(BeaconObjectDialogView)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try {

							csvReader = new CsvReader(csvFile.getAbsolutePath());
							csvReader.readHeaders();
							while (csvReader.readRecord()) {
								if (csvReader.get("Name").equals(tempBeacon.getName())) {
									removeLineFromFile(csvFile.getAbsolutePath(), tempBeacon.getName());
								}

							}
							listOfFiles = folder.listFiles();
							try {

								for (int i = 0; i < listOfFiles.length && i < 20; i++) {
									if (listOfFiles[i].getAbsolutePath().endsWith("~")) {
										File file = new File(listOfFiles[i].getAbsolutePath());
										file.renameTo(new File(file.getAbsoluteFile().toString().replace("~", "")));
									}

								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							mCSVData = tempBeacon.getProximityUuid() + "," + nameOfBeacon.getText().toString()
									+ ",ABCXYZ11,3.251618.5,,,," + dt.format(new Date()) + "\n";
							writeCSVFile();
							loadFromCSV();
							checkInCSV();
							reloadBeaconAndView();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						listOfFiles = folder.listFiles();
						for (int i = 0; i < listOfFiles.length && i < 20; i++) {
							if (listOfFiles[i].getAbsolutePath().endsWith("~")) {
								File file = new File(listOfFiles[i].getAbsolutePath());
								file.delete();
							}

						}

						beaconDialog.dismiss();
					}

				}).create();
		beaconDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {

				((ViewGroup) BeaconObjectDialogView.getParent()).removeView(BeaconObjectDialogView);
			}
		});
		beaconDialog.setTitle("Edit (" + tempBeacon.getProximityUuid() + ")");
		beaconDialog.show();
	}

	public void removeLineFromFile(String file, String lineToRemove) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			
			while ((line = br.readLine()) != null) {

				if (!line.trim().contains(lineToRemove)) {

					pw.println(line);
					pw.flush();

				}
			}
			pw.close();
			br.close();

			
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void writeCSVFile() throws IOException {
	
		FileWriter fw = new FileWriter(csvFile.getAbsolutePath(), true);
	
		fw.write(mCSVData);
	
		fw.close();
	}

	
	public void checkInCSV() {
		for (Beacon element : foundIBeacons) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (element.getProximityUuid().contains(key)) {
					element.setName(value);
				}
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			refreshUI = false;
		} else {
			refreshUI = true;
		}

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_about_action:
		
			new AlertDialog.Builder(this).setTitle(R.string.about).setMessage(R.string.app_about)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
					
						}
					}).show();
			return true;
		case R.id.action_exit_action:

			finish();
			return true;
		case R.id.change_state_of_background_mode:
			if (MainActivity.USE_BACKGROUND_MODE == true) {

				MainActivity.USE_BACKGROUND_MODE = false;
			} else {
				MainActivity.USE_BACKGROUND_MODE = true;
			}
			return true;
		case R.id.action_export_action:

			makeExportDataToCSV();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	public void makeExportDataToCSV() {
		String path = basePath + "/" + "Data";
		String name = "DynamicCSV_" + dtName.format(new Date()) + CSV_EXT;
		csvFileForExport = new File(path, name);
		FileWriter fw;
		try {
			csvFileForExport.createNewFile();
			fw = new FileWriter(csvFileForExport.getAbsolutePath(), false);
			int i = 0;
			fw.write("UUID, dist, ServN,SN,LT,PL,PN,IMEI,Latitude,Longitude,Altitude,Time\n");
			do {
				fw.write(dataForExport.get(i).toString());

				i++;
			} while (i < dataForExport.size());

			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String calculateAvg(String uuid) {
		Double sum = 0.0;
		if (mapForAverage.get(uuid).size() > MainActivity.NUMBER_OF_DISTANCE_FOR_AVERAGE) {

			for (int i = mapForAverage.get(uuid).size(); i > mapForAverage.get(uuid).size()
					- MainActivity.NUMBER_OF_DISTANCE_FOR_AVERAGE; i--) {
				sum += mapForAverage.get(uuid).get(i - 1);
			}
			return String.valueOf(sum / MainActivity.NUMBER_OF_DISTANCE_FOR_AVERAGE);
		} else {

			for (int i = 0; i < mapForAverage.get(uuid).size(); i++) {
				sum += mapForAverage.get(uuid).get(i);
			}

			return String.valueOf(sum / mapForAverage.get(uuid).size());
		}

	}


	private OnClickListener clickMediaListener(int typeOfMedia, Beacon tempBeacon) {
		final Beacon beacon = tempBeacon;

		switch (typeOfMedia) {

		case PICTURE_MEDIA:
			return new OnClickListener() {
				public void onClick(View v) {
					String pathOfUUID = basePath + "/" + "Files/" + beacon.getProximityUuid();

					do {
						picturePath = pathOfUUID + "/" + listOfFiles.length + "_"
								+ checkDistanceCategory(beacon.avgOfDistance) + ".jpg" + "~";

					} while (new File(picturePath).exists());
					Uri destination = Uri.fromFile(new File(picturePath));
				
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, destination);
				
					startActivity(cameraIntent);

				}
			};

		default:
			return new OnClickListener() {
				public void onClick(View v) {
				
				}
			};
		}
	}


	public String checkDistanceCategory(String distance) {

		if (Float.parseFloat(distance) < MainActivity.DISTANCE_IMMEDIATE) {
			distance = "IMMEDIATE";
		} else if (Float.parseFloat(distance) > MainActivity.DISTANCE_IMMEDIATE
				&& Float.parseFloat(distance) < MainActivity.DISTANCE_NEAR) {
			distance = "NEAR";
		} else {
			distance = "FAR";
		}

		return distance;

	}


	@Override
	protected void onStop() {
		sound = true;
		super.onStop();
	}

}
