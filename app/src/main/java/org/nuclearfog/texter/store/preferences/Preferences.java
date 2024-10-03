package org.nuclearfog.texter.store.preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {

	private static final String NAME = "settings";


	private static Preferences instance;

	private SharedPreferences mPref;


	private Preferences(Context context) {
		mPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}


	public static Preferences getInstance(Context context) {
		if (instance == null)
			instance = new Preferences(context.getApplicationContext());
		return instance;
	}
}