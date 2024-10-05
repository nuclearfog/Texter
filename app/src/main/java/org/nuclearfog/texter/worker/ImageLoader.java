package org.nuclearfog.texter.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.nuclearfog.texter.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageLoader extends AsyncExecutor<Uri, Image> {

	private static final String TAG = "ImageSaveWorker";

	private File imageFolder;

	private int max_width, max_height;


	public ImageLoader(Context context) {
		super(context);
		max_width  = Resources.getSystem().getDisplayMetrics().widthPixels;
		max_height = Resources.getSystem().getDisplayMetrics().heightPixels;
		imageFolder = new File(context.getExternalCacheDir(), "images");
		imageFolder.mkdir();
	}


	@Override
	protected Image doInBackground(Uri uri) {
		Context context = getContext();
		if (context != null) {
			ContentResolver resolver = context.getContentResolver();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
				String filename = Integer.toString(uri.hashCode());
				File destMediaFile = new File(imageFolder, filename);
				FileOutputStream fos = new FileOutputStream(destMediaFile);
				if (bitmap.getWidth() > max_width || bitmap.getHeight() > max_height) {
					int downscale = Math.max(bitmap.getWidth() / max_width, bitmap.getHeight() / max_height) + 1;
					bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / downscale, bitmap.getHeight() / downscale, true);
				}
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
				return new Image(destMediaFile.toString());
			} catch (IOException e) {
				Log.e(TAG, "could not cache image!", e);
			}
		}
		return null;
	}
}