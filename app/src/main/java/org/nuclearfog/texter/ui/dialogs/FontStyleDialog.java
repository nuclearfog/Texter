package org.nuclearfog.texter.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.nuclearfog.texter.R;
import org.nuclearfog.texter.utils.FontSpan;


public class FontStyleDialog extends DialogFragment implements OnClickListener {

	private static final String TAG = "FontStyleDialog";


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.dialog_font_style, container, false);
		View color1 = view.findViewById(R.id.dialog_font_color_1);
		View color2 = view.findViewById(R.id.dialog_font_color_2);
		View color3 = view.findViewById(R.id.dialog_font_color_3);
		View color4 = view.findViewById(R.id.dialog_font_color_4);
		View color5 = view.findViewById(R.id.dialog_font_color_5);
		View color6 = view.findViewById(R.id.dialog_font_color_6);
		View color7 = view.findViewById(R.id.dialog_font_color_7);
		View color8 = view.findViewById(R.id.dialog_font_color_8);
		View color9 = view.findViewById(R.id.dialog_font_color_9);
		View color10 = view.findViewById(R.id.dialog_font_color_10);
		View size_huge = view.findViewById(R.id.dialog_font_huge);
		View size_big = view.findViewById(R.id.dialog_font_big);
		View size_medium = view.findViewById(R.id.dialog_font_medium);
		View size_small = view.findViewById(R.id.dialog_font_small);
		View font_regular = view.findViewById(R.id.dialog_font_regular);
		View font_bold = view.findViewById(R.id.dialog_font_bold);
		View font_italic = view.findViewById(R.id.dialog_font_italic);

		color1.setOnClickListener(this);
		color2.setOnClickListener(this);
		color3.setOnClickListener(this);
		color4.setOnClickListener(this);
		color5.setOnClickListener(this);
		color6.setOnClickListener(this);
		color7.setOnClickListener(this);
		color8.setOnClickListener(this);
		color9.setOnClickListener(this);
		color10.setOnClickListener(this);
		size_huge.setOnClickListener(this);
		size_big.setOnClickListener(this);
		size_medium.setOnClickListener(this);
		size_small.setOnClickListener(this);
		font_regular.setOnClickListener(this);
		font_bold.setOnClickListener(this);
		font_italic.setOnClickListener(this);
		return view;
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.dialog_font_color_1) {
			setColor(R.color.banned);
		} else if (v.getId() == R.id.dialog_font_color_2) {
			setColor(R.color.tile);
		} else if (v.getId() == R.id.dialog_font_color_3) {
			setColor(R.color.newfag);
		} else if (v.getId() == R.id.dialog_font_color_4) {
			setColor(R.color.fag);
		} else if (v.getId() == R.id.dialog_font_color_5) {
			setColor(R.color.sponsor);
		} else if (v.getId() == R.id.dialog_font_color_6) {
			setColor(R.color.middle_old_fag);
		} else if (v.getId() == R.id.dialog_font_color_7) {
			setColor(R.color.old_fag);
		} else if (v.getId() == R.id.dialog_font_color_8) {
			setColor(R.color.mod);
		} else if (v.getId() == R.id.dialog_font_color_9) {
			setColor(R.color.old_mod);
		} else if (v.getId() == R.id.dialog_font_color_10) {
			setColor(R.color.admin);
		} else if (v.getId() == R.id.dialog_font_huge) {
			setFontSize(getResources().getDimensionPixelSize(R.dimen.text_huge));
		} else if (v.getId() == R.id.dialog_font_big) {
			setFontSize(getResources().getDimensionPixelSize(R.dimen.text_big));
		} else if (v.getId() == R.id.dialog_font_medium) {
			setFontSize(getResources().getDimensionPixelSize(R.dimen.text_medium));
		} else if (v.getId() == R.id.dialog_font_small) {
			setFontSize(getResources().getDimensionPixelSize(R.dimen.text_small));
		} else if (v.getId() == R.id.dialog_font_regular) {
			setFontType(FontSpan.TEXGYREHEROS_REGULAR);
		} else if (v.getId() == R.id.dialog_font_bold) {
			setFontType(FontSpan.TEXGYREHEROS_BOLT);
		} else if (v.getId() == R.id.dialog_font_italic) {
			setFontType(FontSpan.TEXGYREHEROS_ITALIC);
		}
	}


	private void setColor(@ColorRes int colorRes) {
		Activity activity = getActivity();
		if (activity instanceof OnFontStyleChangeListener) {
			int color = getResources().getColor(colorRes);
			((OnFontStyleChangeListener) activity).onColorChanged(color);
		}
	}


	private void setFontType(String font) {
		Activity activity = getActivity();
		if (activity instanceof OnFontStyleChangeListener) {
			((OnFontStyleChangeListener) activity).onFontChanged(font);
		}
	}


	private void setFontSize(int fontSize) {
		Activity activity = getActivity();
		if (activity instanceof OnFontStyleChangeListener) {
			((OnFontStyleChangeListener) activity).onSizeChanged(fontSize);
		}
	}


	public static void show(FragmentActivity activity) {
		FontStyleDialog dialog = new FontStyleDialog();
		dialog.show(activity.getSupportFragmentManager(), TAG);
	}


	public interface OnFontStyleChangeListener {

		void onColorChanged(int color);

		void onFontChanged(String font);

		void onSizeChanged(int size);
	}
}