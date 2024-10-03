package org.nuclearfog.texter.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.nuclearfog.texter.R;


public class ColorPickerDialog extends DialogFragment implements OnClickListener {

	private static final String TAG = "ColorPickerDialog";


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.colorpicker, container, false);
		ImageView color1 = view.findViewById(R.id.color_1);
		ImageView color2 = view.findViewById(R.id.color_2);
		ImageView color3 = view.findViewById(R.id.color_3);
		ImageView color4 = view.findViewById(R.id.color_4);
		ImageView color5 = view.findViewById(R.id.color_5);
		ImageView color6 = view.findViewById(R.id.color_6);
		ImageView color7 = view.findViewById(R.id.color_7);
		ImageView color8 = view.findViewById(R.id.color_8);
		ImageView color9 = view.findViewById(R.id.color_9);
		ImageView color10 = view.findViewById(R.id.color_10);

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
		return view;
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.color_1) {
			setColor(R.color.banned);
		} else if (v.getId() == R.id.color_2) {
			setColor(R.color.tile);
		} else if (v.getId() == R.id.color_3) {
			setColor(R.color.newfag);
		} else if (v.getId() == R.id.color_4) {
			setColor(R.color.fag);
		} else if (v.getId() == R.id.color_5) {
			setColor(R.color.sponsor);
		} else if (v.getId() == R.id.color_6) {
			setColor(R.color.middle_old_fag);
		} else if (v.getId() == R.id.color_7) {
			setColor(R.color.old_fag);
		} else if (v.getId() == R.id.color_8) {
			setColor(R.color.mod);
		} else if (v.getId() == R.id.color_9) {
			setColor(R.color.old_mod);
		} else if (v.getId() == R.id.color_10) {
			setColor(R.color.admin);
		}
	}


	private void setColor(@ColorRes int colorRes) {
		Activity activity = getActivity();
		if (activity instanceof OnColorSelectedListener) {
			int color = getResources().getColor(colorRes);
			((OnColorSelectedListener) activity).onColorSelected(color);
		}
		dismiss();
	}


	public static void show(FragmentActivity activity) {
		ColorPickerDialog dialog = new ColorPickerDialog();
		dialog.show(activity.getSupportFragmentManager(), TAG);
	}


	public interface OnColorSelectedListener {

		void onColorSelected(int color);
	}
}