package org.nuclearfog.texter.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ImageSaveWorker extends AsyncExecutor<Uri, String> {

	private static final String TAG = "ImageSaveWorker";

	private File imageFolder;


	public ImageSaveWorker(Context context) {
		super(context);
		imageFolder = new File(context.getExternalCacheDir(), "cache");
		imageFolder.mkdir();
	}


	@Override
	protected String doInBackground(Uri uri) {
		Context context = getContext();
		if (context != null) {
			ContentResolver resolver = context.getContentResolver();
			try {
				InputStream src = resolver.openInputStream(uri);
				if (src != null) {
					String filename = Integer.toString(uri.hashCode());
					File destMediaFile = new File(imageFolder, filename);
					OutputStream dest = new FileOutputStream(destMediaFile);
					int length;
					byte[] buffer = new byte[4096];
					while ((length = src.read(buffer)) > 0) {
						dest.write(buffer, 0, length);
					}
					src.close();
					dest.close();
					return destMediaFile.toString();
				}
			} catch (IOException e) {
				Log.e(TAG, "could not cache image!", e);
			}
		}
		return null;
	}
}