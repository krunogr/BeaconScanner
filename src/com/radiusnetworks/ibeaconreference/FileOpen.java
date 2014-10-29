package com.radiusnetworks.ibeaconreference;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class FileOpen {

	public static void openFile(Context context, File url) throws IOException {

		File file = url;
		Uri uri = Uri.fromFile(file);

		Intent intent = new Intent(Intent.ACTION_VIEW);

		if (url.toString().contains(".doc") || url.toString().contains(".docx")) {

			intent.setDataAndType(uri, "application/msword");

		} else if (url.toString().contains(".ppt")
				|| url.toString().contains(".pptx")) {

			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		} else if (url.toString().contains(".xls")
				|| url.toString().contains(".xlsx")) {

			intent.setDataAndType(uri, "application/vnd.ms-excel");
		} else if (url.toString().contains(".zip")
				|| url.toString().contains(".rar")) {

			intent.setDataAndType(uri, "application/x-wav");
		} else if (url.toString().contains(".rtf")) {

			intent.setDataAndType(uri, "application/rtf");
		} else if (url.toString().contains(".wav")
				|| url.toString().contains(".mp3")) {

			intent.setDataAndType(uri, "audio/x-wav");
		} else if (url.toString().contains(".gif")) {

			intent.setDataAndType(uri, "image/gif");
		} else if (url.toString().contains(".jpg")
				|| url.toString().contains(".jpeg")
				|| url.toString().contains(".png")) {

			intent.setDataAndType(uri, "image/jpeg");
		} else if (url.toString().contains(".txt")) {

			intent.setDataAndType(uri, "text/plain");
		} else if (url.toString().contains("TEL LIST.csv")) {

			intent = new Intent(context, PhoneActivity.class);
			intent.putExtra(AllFilesBeaconActivity.BASE_FILE_PATH,
					file.getAbsolutePath());
		} else if (url.toString().contains("CONTACT.csv")) {

			intent = new Intent(context, ContactActivity.class);
			intent.putExtra(AllFilesBeaconActivity.BASE_FILE_PATH,
					file.getAbsolutePath());
		} else if (url.toString().contains(".3gp")
				|| url.toString().contains(".mpg")
				|| url.toString().contains(".mpeg")
				|| url.toString().contains(".mpe")
				|| url.toString().contains(".mp4")
				|| url.toString().contains(".avi")) {

			intent.setDataAndType(uri, "video/*");
		} else if (url.toString().contains(".vcf")) {
			String vcfMimeType = MimeTypeMap.getSingleton()
					.getMimeTypeFromExtension("vcf");
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(uri, vcfMimeType);
		} else if (url.toString().contains(".url")) {

			FileInputStream fstream = null;
			DataInputStream in = null;
			String text = "", urlString = "";
			try {
				fstream = new FileInputStream(file);

				in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					text += strLine + "\n";

				}
				urlString = text.split("URL=")[1];
				in.close();
			} catch (IOException e) {

			}

			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));

		} else {
			intent.setDataAndType(uri, "*/*");
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}