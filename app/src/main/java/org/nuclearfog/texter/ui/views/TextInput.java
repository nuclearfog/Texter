package org.nuclearfog.texter.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class TextInput extends EditText implements TextWatcher {

	@Nullable
	private OnTextChangeListener listener;


	public TextInput(Context context) {
		this(context, null);
	}


	public TextInput(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT);
		setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		if (VERSION.SDK_INT >= VERSION_CODES.O) {
			setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO);
		}
		addTextChangedListener(this);
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}


	@Override
	public void afterTextChanged(Editable s) {
		if (listener != null && hasFocus()) {
			listener.onTextChange(this, s);
		}
	}


	public void addOnTextChangeListener(@Nullable OnTextChangeListener listener) {
		this.listener = listener;
	}


	public interface OnTextChangeListener {

		void onTextChange(TextInput textInput, Spannable text);
	}
}