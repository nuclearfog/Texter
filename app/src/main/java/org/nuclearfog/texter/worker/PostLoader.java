package org.nuclearfog.texter.worker;

import android.content.Context;

import org.nuclearfog.texter.model.Post;
import org.nuclearfog.texter.store.database.Database;

public class PostLoader extends AsyncExecutor<Integer, Post> {

	private Database db;

	public PostLoader(Context context) {
		super(context);
		db = new Database(context);
	}


	@Override
	protected Post doInBackground(Integer param) {
		return db.getPost(param);
	}
}