package com.radiusnetworks.ibeaconreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.csvreader.CsvReader;


public class PhoneActivity extends Activity implements OnItemClickListener {
	
	private PhoneListAdapter phoneListAdapter;
	
	List<PhoneItem> phoneList;
	
	private static CsvReader csvReader;

	ListView phoneListView;
	
	String basePath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		phoneListView = (ListView) findViewById(R.id.telefone_number_listview);
		Bundle extra = getIntent().getExtras();
		basePath = extra.getString(AllFilesBeaconActivity.BASE_FILE_PATH);
		loadFromCSV();
		phoneListAdapter = new PhoneListAdapter(this, R.layout.file_row, phoneList);
		phoneListView.setAdapter(phoneListAdapter);
		phoneListView.setOnItemClickListener(this);

	}

	
	private void loadFromCSV() {
		phoneList = new ArrayList<PhoneItem>();

		try {
			csvReader = new CsvReader(basePath);

			csvReader.readHeaders();
			while (csvReader.readRecord()) {

				phoneList.add(new PhoneItem(csvReader.get("Name"), csvReader.get("Number")));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		csvReader.close();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + ((PhoneItem) parent.getItemAtPosition(position)).getNumber()));
		startActivity(intent);

	}

}
