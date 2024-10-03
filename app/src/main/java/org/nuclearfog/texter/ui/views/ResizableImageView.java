package org.nuclearfog.texter.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import org.nuclearfog.texter.model.Image;


@SuppressLint("AppCompatCustomView")
public class ResizableImageView extends ImageView implements OnLayoutChangeListener {

	@Nullable
	private Image image;

	private PointF pos = new PointF(0.0f, 0.0f);
	private PointF dist = new PointF(0.0f, 0.0f);
	private boolean moveLock = false;

	private LayoutParams params;


	public ResizableImageView(Context context) {
		this(context, null);
	}


	public ResizableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		setAdjustViewBounds(true);
		addOnLayoutChangeListener(this);
	}


	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {


		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				getParent().requestDisallowInterceptTouchEvent(true);
				if (event.getPointerCount() == 1) {
					pos.set(getX(), getY());
				}
				return true;

			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getPointerCount() == 2) {
					moveLock = true;
					float distX = event.getX(0) - event.getX(1);
					float distY = event.getY(0) - event.getY(1);
					dist.set(distX, distY);
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if (event.getPointerCount() == 1) {
					if (!moveLock) {
						pos.set(pos.x + event.getX() - getWidth() / 2f, pos.y + event.getY() - getHeight() / 2f);
						setPosition(pos.x, pos.y);
						onImageChange(Math.round(pos.x), Math.round(pos.y), 0, 0);
					}
				} else if (event.getPointerCount() == 2) {
					float distX = event.getX(0) - event.getX(1);
					float distY = event.getY(0) - event.getY(1);
					PointF current = new PointF(distX, distY);
					float scale = current.length() / dist.length();
					float xPos = getX() + getWidth() * (1 - scale) / 2;
					float yPos = getY() + getHeight() * (1 - scale) / 2;
					int width = Math.round(getWidth() * scale);
					int height = Math.round(getHeight() * scale);
					setPosition(xPos, yPos);
					onImageChange(Math.round(xPos), Math.round(yPos), width, height);
					setMeasurements(width, height);
					dist.set(distX, distY);
				}
				return true;

			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				moveLock = false;
				return true;
		}
		return super.onTouchEvent(event);
	}


	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		onImageChange(left, top, right - left, bottom - top);
	}


	public void setImage(@Nullable Image image) {
		this.image = image;
		if (image != null) {
			setImageURI(Uri.parse(image.getPath()));
			if (image.getX() > 0 && image.getY() > 0) {
				setPosition(image.getX(), image.getY());
				if (image.getHeight() > 0 && image.getWidth() > 0) {
					setMeasurements(image.getWidth(), image.getHeight());
				}
			}
		}
	}


	private void onImageChange(int x, int y, int width, int height) {
		if (image != null) {
			image.setPosition(x, y);
			if (width > 0 && height > 0) {
				image.setMeasurements(width, height);
			}
		}
	}


	private void setPosition(float x, float y) {
		params.leftMargin = Math.round(x);
		params.topMargin = Math.round(y);
		requestLayout();
	}


	private void setMeasurements(int width, int height) {
		params.width = width;
		params.height = height;
		requestLayout();
	}
}