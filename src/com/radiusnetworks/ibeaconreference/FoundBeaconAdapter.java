package com.radiusnetworks.ibeaconreference;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FoundBeaconAdapter extends ArrayAdapter<Beacon> {


	Context context;
	int layoutResourceId;
	int rowId;
	List<Beacon> data = null;

	static class FileItemHolder {
		TextView uuid, other, name;
	}

	public FoundBeaconAdapter(Context context, int layoutResourceId, List<Beacon> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileItemHolder holder = null;
		final Beacon tempBeacon = data.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

		
			holder = new FileItemHolder();
			holder.uuid = (TextView) row.findViewById(R.id.textView_uuid);
			holder.other = (TextView) row.findViewById(R.id.textView_other);
			holder.name = (TextView) row.findViewById(R.id.textView_name);

			row.setTag(holder);
		} else {
			holder = (FileItemHolder) row.getTag();
		}

		if (tempBeacon.getName() == "") {
			tempBeacon.setName("No name");
		}

		String accuracy = String.valueOf(tempBeacon.getAccuracy());
		if (accuracy.length() > 5) {
			accuracy = accuracy.substring(0, 4);
		}
		holder.name.setText(String.valueOf(tempBeacon.getName()));
		holder.uuid.setText(String.valueOf(tempBeacon.getProximityUuid()));
		holder.other.setText("M:" + String.valueOf(tempBeacon.getMajor()) + "  " + "minor:"
				+ String.valueOf(tempBeacon.getMinor()) + "  " + "dist: " + tempBeacon.avgOfDistance.substring(0, 4)
				+ " " + tempBeacon.distanceCategory);

		return row;
	}

}
