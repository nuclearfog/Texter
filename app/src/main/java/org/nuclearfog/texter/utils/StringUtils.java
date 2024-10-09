package org.nuclearfog.texter.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class StringUtils {


	private StringUtils() {
	}


	public static String toHtml(Spannable s) {
		SpannableStringBuilder builder = new SpannableStringBuilder(s);
		builder.append("\u200b");
		return Html.toHtml(builder);
	}


	public static CharSequence fromHtml(String text) {
		Spanned s = Html.fromHtml(text);
		if (s.length() > 2 && Character.isWhitespace(s.charAt(s.length() - 1))
				&& Character.isWhitespace(s.charAt(s.length() - 2))) {
			return s.subSequence(0, s.length() - 2);
		}
		return s;
	}
}