package org.nuclearfog.texter.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class StringUtils {

	private static final String ATTR_TEXT = "text";
	private static final String ATTR_COLOR = "color";
	private static final String ATTR_SIZE = "size";
	private static final String ATTR_FONT = "font";
	private static final String ATTR_SPAN_START = "start";
	private static final String ATTR_SPAN_END = "end";
	private static final String ARRAY_SPAN_COLOR = "colorSpans";
	private static final String ARRAY_SPAN_SIZE = "sizeSpans";
	private static final String ARRAY_SPAN_FONT = "fontSpans";

	private StringUtils() {
	}


	public static String spanToJson(Spannable span) {
		try {
			JSONObject json = new JSONObject();
			JSONArray colorArray = new JSONArray();
			JSONArray sizeArray = new JSONArray();
			JSONArray typeArray = new JSONArray();
			ForegroundColorSpan[] colorSpans = span.getSpans(0, span.length(), ForegroundColorSpan.class);
			AbsoluteSizeSpan[] sizeSpans = span.getSpans(0, span.length(), AbsoluteSizeSpan.class);
			FontSpan[] fontSpans = span.getSpans(0, span.length(), FontSpan.class);

			json.put(ATTR_TEXT, span.toString());
			for (ForegroundColorSpan colorSpan : colorSpans) {
				int color = colorSpan.getForegroundColor();
				int start = span.getSpanStart(colorSpan);
				int end = span.getSpanEnd(colorSpan);
				JSONObject ij = new JSONObject();
				ij.put(ATTR_COLOR, color);
				ij.put(ATTR_SPAN_START, start);
				ij.put(ATTR_SPAN_END, end);
				colorArray.put(ij);
			}
			for (AbsoluteSizeSpan sizeSpan : sizeSpans) {
				int size = sizeSpan.getSize();
				int start = span.getSpanStart(sizeSpan);
				int end = span.getSpanEnd(sizeSpan);
				JSONObject ij = new JSONObject();
				ij.put(ATTR_SIZE, size);
				ij.put(ATTR_SPAN_START, start);
				ij.put(ATTR_SPAN_END, end);
				sizeArray.put(ij);
			}
			for (FontSpan fontSpan : fontSpans) {
				String fontName = fontSpan.getFontName();
				int start = span.getSpanStart(fontSpan);
				int end = span.getSpanEnd(fontSpan);
				JSONObject ij = new JSONObject();
				ij.put(ATTR_FONT, fontName);
				ij.put(ATTR_SPAN_START, start);
				ij.put(ATTR_SPAN_END, end);
				typeArray.put(ij);
			}
			json.put(ARRAY_SPAN_COLOR, colorArray);
			json.put(ARRAY_SPAN_SIZE, sizeArray);
			json.put(ARRAY_SPAN_FONT, typeArray);
			return json.toString();
		} catch (JSONException e) {
			return span.toString();
		}
	}


	public static Spannable jsonToSpan(Context context, String jsonStr) {
		SpannableStringBuilder span = new SpannableStringBuilder();
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray colorArray = json.getJSONArray(ARRAY_SPAN_COLOR);
			JSONArray sizeArray = json.getJSONArray(ARRAY_SPAN_SIZE);
			JSONArray fontArray = json.getJSONArray(ARRAY_SPAN_FONT);

			span.append(json.getString(ATTR_TEXT));
			for (int i = 0; i < colorArray.length(); i++) {
				JSONObject jo = colorArray.getJSONObject(i);
				int color = jo.getInt(ATTR_COLOR);
				int start = jo.getInt(ATTR_SPAN_START);
				int end = jo.getInt(ATTR_SPAN_END);
				span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for (int i = 0; i < sizeArray.length(); i++) {
				JSONObject jo = sizeArray.getJSONObject(i);
				int size = jo.getInt(ATTR_SIZE);
				int start = jo.getInt(ATTR_SPAN_START);
				int end = jo.getInt(ATTR_SPAN_END);
				span.setSpan(new AbsoluteSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for (int i = 0; i < fontArray.length(); i++) {
				JSONObject jo = fontArray.getJSONObject(i);
				String font = jo.getString(ATTR_FONT);
				int start = jo.getInt(ATTR_SPAN_START);
				int end = jo.getInt(ATTR_SPAN_END);
				span.setSpan(new FontSpan(context, font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		} catch (JSONException exception) {
			//
		}
		return span;
	}
}