package org.nuclearfog.texter.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import org.nuclearfog.texter.R;


public class FontSpan extends MetricAffectingSpan {

	public static final String TEXGYREHEROS_REGULAR = "texgyreheros-regular";
	public static final String TEXGYREHEROS_BOLT = "texgyreheros-bolt";
	public static final String TEXGYREHEROS_ITALIC = "texgyreheros-italic";

	private Typeface font;
	private String name;


	public FontSpan(Context context, String name) {
		this.name = name;
		switch (name) {
			case TEXGYREHEROS_REGULAR:
				font = ResourcesCompat.getFont(context, R.font.texgyreheros);
				break;

			case TEXGYREHEROS_BOLT:
				font = ResourcesCompat.getFont(context, R.font.texgyreheros_bold);
				break;

			case TEXGYREHEROS_ITALIC:
				font = ResourcesCompat.getFont(context, R.font.texgyreheros_italic);
				break;

			default:
				font = Typeface.DEFAULT;
		}
	}


	@Override
	public void updateMeasureState(@NonNull TextPaint textPaint) {
		textPaint.setTypeface(font);
	}


	@Override
	public void updateDrawState(TextPaint textPaint) {
		textPaint.setTypeface(font);
	}


	public String getFontName() {
		return name;
	}
}