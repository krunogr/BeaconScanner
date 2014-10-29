package com.radiusnetworks.ibeaconreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.csvreader.CsvReader;


public class ContactActivity extends Activity implements ActionMode.Callback, OnItemClickListener {

	private ContactListAdapter contactListAdapter;

	List<ContactItem> contactList;

	private static CsvReader csvReader;

	ListView contactListView;

	String basePath;

	private int mSelectedPointPosition;

	private View mSelectedPointView;

	private ActionMode mActionMode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		contactListView = (ListView) findViewById(R.id.contact_listview);
		Bundle extra = getIntent().getExtras();
		basePath = extra.getString(AllFilesBeaconActivity.BASE_FILE_PATH);
		loadFromCSV();
		contactListAdapter = new ContactListAdapter(this, R.layout.contact_row, contactList);
		contactListView.setAdapter(contactListAdapter);
		// contactListView.setOnItemLongClickListener(this);
		contactListView.setOnItemClickListener(this);

	}

	private void loadFromCSV() {
		contactList = new ArrayList<ContactItem>();

		try {
			csvReader = new CsvReader(basePath);

			csvReader.readHeaders();
			while (csvReader.readRecord()) {

				contactList.add(new ContactItem(csvReader.get("Name"), csvReader.get("Phone"), csvReader.get("Email"),
						csvReader.get("Location")));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		csvReader.close();
	}

	// if we want use longClick for show menu. Also, it is neccessery to
	// implement longclickListener
	// @Override
	// public boolean onItemLongClick(AdapterView<?> parent, View view, int
	// position, long id) {
	// if (mActionMode != null) {
	// return false;
	// }
	// // Start the CAB using the ActionMode.Callback defined above
	//
	// mActionMode = this.startActionMode(this);
	// mSelectedPointPosition = position;
	// mSelectedPointView = view;
	// mSelectedPointView.setActivated(true);
	// mSelectedPointView.setBackgroundColor(Color.rgb(51, 181, 230));
	// return true;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.ActionMode.Callback#onCreateActionMode(android.view.ActionMode
	 * , android.view.Menu)
	 */
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.contextual_action_bar_menu, menu);
		return true;
	}

	public void onDestroyActionMode(ActionMode mode) {
		mSelectedPointView.setActivated(false);
		mActionMode = null;
		mSelectedPointView.setActivated(false);
		mSelectedPointView.setBackgroundColor(Color.TRANSPARENT);
	}


	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		boolean result = false;
		Intent intent = null;
		switch (item.getItemId()) {

		case R.id.action_send_email:
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
			emailIntent.setType("application/image");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { contactList.get(mSelectedPointPosition).getEmail() });
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			result = true;
			break;
		case R.id.action_call_number:
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + contactList.get(mSelectedPointPosition).getPhoneNumber()));

			startActivity(intent);
			result = true;
			break;

		default:

		}
		mode.finish();
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mActionMode != null) {

		} else {
			mActionMode = this.startActionMode(this);
			mSelectedPointPosition = position;
			mSelectedPointView = view;
			mSelectedPointView.setActivated(true);
			mSelectedPointView.setBackgroundColor(Color.rgb(51, 181, 230));
		}

	}
}
