package org.nuclearfog.texter.worker;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.nuclearfog.texter.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;


public class ImageSaver extends AsyncExecutor<Bitmap, Uri> {

	private static final String TAG = "ImageSaver";

	private File imageFolder;

	public ImageSaver(Context context) {
		super(context);
		imageFolder = new File(context.getCacheDir(), "out");
		imageFolder.mkdir();
	}


	@Override
	protected Uri doInBackground(Bitmap param) {
		Uri result = null;
		Context context = getContext();
		try {
			File imagefile = new File(imageFolder, "oc.png");
			FileOutputStream fos = new FileOutputStream(imagefile);
			param.compress(Bitmap.CompressFormat.PNG, 100, fos);
			if (context != null)
				result = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, imagefile);
			fos.close();
		} catch (Exception exception) {
			Log.e(TAG, "failed to save image", exception);
		}
		return result;
	}
}