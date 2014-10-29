package com.radiusnetworks.ibeaconreference;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllFilesBeaconAdapter extends ArrayAdapter<File> {

	Context context;
	int layoutResourceId;
	int rowId;
	List<File> data = null;
	String basePath;

	Drawable drawable;

	static class FileItemHolder {

		TextView fileName, fileFormat;

		ImageView image;
	}

	public AllFilesBeaconAdapter(Context context, int layoutResourceId,
			List<File> data, String basePath) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.basePath = basePath;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FileItemHolder holder = null;
		final File tempFile = data.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FileItemHolder();
			holder.fileName = (TextView) row
					.findViewById(R.id.textview_file_name);
			holder.fileFormat = (TextView) row
					.findViewById(R.id.textView_file_format);
			holder.image = (ImageView) row.findViewById(R.id.file_icon);

			row.setTag(holder);
		} else {
			holder = (FileItemHolder) row.getTag();
		}

		String name = tempFile.getAbsolutePath().replace(basePath + "/", "");

		String[] parts = name.split("\\.");

		holder.fileName.setText(parts[0]);
		holder.fileFormat.setText(parts[1]);

		if (parts[1].contains("jpg") || parts[1].contains("jpeg")
				|| parts[1].contains("png")) {

			Drawable tempDrawable = context.getResources().getDrawable(
					R.drawable.image_icon);
			holder.image.setImageDrawable(tempDrawable);

		} else if (parts[1].contains("mp4")) {

			Drawable tempDrawable = context.getResources().getDrawable(
					R.drawable.video_icon);
			holder.image.setImageDrawable(tempDrawable);

		} else if (parts[1].contains("3gp")) {

			Drawable tempDrawable = context.getResources().getDrawable(
					R.drawable.microphone_icon);
			holder.image.setImageDrawable(tempDrawable);

		} else if (parts[1].contains("url")) {

			Drawable tempDrawable = context.getResources().getDrawable(
					R.drawable.browser_icon);
			holder.image.setImageDrawable(tempDrawable);

		} else {
			Drawable tempDrawable = context.getResources().getDrawable(
					R.drawable.text_icon);
			holder.image.setImageDrawable(tempDrawable);

		}

		return row;
	}
}
