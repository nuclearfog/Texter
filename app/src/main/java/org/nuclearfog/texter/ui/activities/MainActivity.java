package org.nuclearfog.texter.ui.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.nuclearfog.texter.R;
import org.nuclearfog.texter.model.Image;
import org.nuclearfog.texter.model.Post;
import org.nuclearfog.texter.store.preferences.Preferences;
import org.nuclearfog.texter.ui.dialogs.ColorPickerDialog;
import org.nuclearfog.texter.ui.dialogs.ColorPickerDialog.OnColorSelectedListener;
import org.nuclearfog.texter.ui.spans.FontSpan;
import org.nuclearfog.texter.ui.views.ResizableImageView;
import org.nuclearfog.texter.ui.views.TextInput;
import org.nuclearfog.texter.ui.views.TextInput.OnTextChangeListener;
import org.nuclearfog.texter.worker.AsyncExecutor.AsyncCallback;
import org.nuclearfog.texter.worker.ImageSaveWorker;
import org.nuclearfog.texter.worker.PostLoader;
import org.nuclearfog.texter.worker.PostSaveWorker;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements ActivityResultCallback<ActivityResult>, OnLayoutChangeListener, OnColorSelectedListener, OnTextChangeListener {

	private static final String TAG = "MainActivity";

	private static final String MIME_IMAGE = "image/*";

	private static final int REQUEST_PERMISSION_WRITE = 288;
	private static final int REQUEST_PERMISSION_READ = 378;

	private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);

	private AsyncCallback<Post> onPostLoaded = this::onPostLoaded;
	private AsyncCallback<String> onImageLoaded = this::OnImageLoaded;

	private FrameLayout container;
	private TextInput postText;
	private TextView sizeText;

	private Preferences mPref;

	private PostLoader postLoader;
	private PostSaveWorker postSaveWorker;
	private ImageSaveWorker imageSaveWorker;

	private Post post = new Post();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		container = findViewById(R.id.container);
		postText = findViewById(R.id.text);
		sizeText = findViewById(R.id.view_size);

		postSaveWorker = new PostSaveWorker(this);
		imageSaveWorker = new ImageSaveWorker(this);
		postLoader = new PostLoader(this);
		mPref = Preferences.getInstance(this);

		container.addOnLayoutChangeListener(this);
		postText.addOnTextChangeListener(this);

		postLoader.execute(Post.ID_SKETCH, onPostLoaded);
	}


	@Override
	public void onBackPressed() {
		postSaveWorker.execute(post, null);
		super.onBackPressed();
	}


	@Override
	protected void onDestroy() {
		postLoader.cancel();
		postSaveWorker.cancel();
		imageSaveWorker.cancel();
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.menu_save) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
				ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				saveImage();
			} else {
				ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
			}
		} else if (item.getItemId() == R.id.menu_font_medium) {
			CharacterStyle s = new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_medium));
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_font_big) {
			CharacterStyle s = new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_big));
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_font_small) {
			CharacterStyle s = new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_small));
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_font_huge) {
			CharacterStyle s = new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_huge));
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}  else if (item.getItemId() == R.id.menu_typeface_reg) {
			CharacterStyle s = new FontSpan(this, R.font.texgyreheros);
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_typeface_bold) {
			CharacterStyle s = new FontSpan(this, R.font.texgyreheros_bold);
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_typeface_italic) {
			CharacterStyle s = new FontSpan(this, R.font.texgyreheros_italic);
			postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		} else if (item.getItemId() == R.id.menu_font_color) {
			ColorPickerDialog.show(this);
		} else if (item.getItemId() == R.id.menu_add_image) {
			if ((ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
				|| (ActivityCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)) {
				selectImage();
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES}, REQUEST_PERMISSION_READ);
			} else {
				ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ);
			}
		}
		return true;
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_WRITE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				saveImage();
			}
		} else if (requestCode == REQUEST_PERMISSION_READ) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				selectImage();
			}
		}
	}


	@Override
	public void onColorSelected(int color) {
		CharacterStyle s = new ForegroundColorSpan(color);
		postText.getText().setSpan(s, postText.getSelectionStart(), postText.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	}


	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		sizeText.setText(getString(R.string.size_format, right - left, bottom - top));
	}


	@Override
	public void onActivityResult(ActivityResult result) {
		if (result.getResultCode() == RESULT_OK) {
			Intent intent = result.getData();
			if (intent != null && intent.getData() != null) {
				imageSaveWorker.execute(intent.getData(), onImageLoaded);
			}
		}
	}


	private void onPostLoaded(@NonNull Post post) {
		this.post = post;
		postText.setText(Html.fromHtml(post.getText()));
		for (Image image: post.getImages()) {
			ResizableImageView imageView = new ResizableImageView(this);
			imageView.setImage(image);
			container.addView(imageView);
		}
	}


	private void OnImageLoaded(@NonNull String path) {
		Image image = new Image(path);
		post.addImage(image);
		ResizableImageView imageView = new ResizableImageView(this);
		imageView.setImage(image);
		container.addView(imageView);
	}


	@Override
	public void onTextChange(TextInput textInput, Spannable text) {
		post.setText(Html.toHtml(text));
	}


	private void selectImage() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setType(MIME_IMAGE);
		activityResultLauncher.launch(intent);
	}


	private void saveImage() {
		Bitmap returnedBitmap = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		canvas.drawColor(getResources().getColor(R.color.true_grey));
		postText.setEnabled(false);
		container.draw(canvas);
		postText.setEnabled(true);
		try {
			File imagefile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "oc.png");
			FileOutputStream fos = new FileOutputStream(imagefile);
			returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception exception) {
			Log.e(TAG, "failed to save image", exception);
		}
	}
}