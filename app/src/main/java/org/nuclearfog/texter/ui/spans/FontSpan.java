package org.nuclearfog.texter.ui.spans;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;


public class FontSpan extends MetricAffectingSpan {

	private Typeface font;


	public FontSpan(Context context, @FontRes int fontRes) {
		font = ResourcesCompat.getFont(context, fontRes);
		if (font == null) {
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
}