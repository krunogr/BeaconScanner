package com.radiusnetworks.ibeaconreference;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PhoneListAdapter extends ArrayAdapter<PhoneItem> {

	Context context;
	int layoutResourceId;
	int rowId;
	List<PhoneItem> data = null;

	static class FileItemHolder {

		TextView name, number;
	}

	public PhoneListAdapter(Context context, int layoutResourceId,
			List<PhoneItem> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileItemHolder holder = null;
		final PhoneItem tempItem = data.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FileItemHolder();
			holder.number = (TextView) row
					.findViewById(R.id.textView_file_format);
			holder.name = (TextView) row.findViewById(R.id.textview_file_name);

			row.setTag(holder);
		} else {
			holder = (FileItemHolder) row.getTag();
		}

		holder.name.setText(tempItem.getName());
		holder.number.setText(tempItem.getNumber());

		return row;
	}

}
