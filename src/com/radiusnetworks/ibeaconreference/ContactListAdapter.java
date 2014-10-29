package com.radiusnetworks.ibeaconreference;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactListAdapter extends ArrayAdapter<ContactItem> {

	Context context;
	int layoutResourceId;
	int rowId;
	List<ContactItem> data = null;

	static class FileItemHolder {

		TextView name, number, location, email;
	}

	public ContactListAdapter(Context context, int layoutResourceId,
			List<ContactItem> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileItemHolder holder = null;
		final ContactItem tempItem = data.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FileItemHolder();
			holder.name = (TextView) row
					.findViewById(R.id.textview_contact_name);
			holder.number = (TextView) row.findViewById(R.id.textView_phone);
			holder.email = (TextView) row.findViewById(R.id.textView_email);
			holder.location = (TextView) row
					.findViewById(R.id.textView_location);

			row.setTag(holder);
		} else {
			holder = (FileItemHolder) row.getTag();
		}

		holder.name.setText(tempItem.getName());
		holder.number.setText(tempItem.getPhoneNumber());
		holder.email.setText(tempItem.getEmail());
		holder.location.setText(tempItem.getLocation());

		return row;
	}

}
