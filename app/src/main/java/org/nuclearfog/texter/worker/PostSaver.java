package org.nuclearfog.texter.worker;

import android.content.Context;

import org.nuclearfog.texter.model.Post;
import org.nuclearfog.texter.store.database.Database;


public class PostSaver extends AsyncExecutor<Post, Post> {

	private Database db;


	public PostSaver(Context context) {
		super(context);
		db = new Database(context);
	}


	@Override
	protected Post doInBackground(Post param) {
		db.savePost(param);
		return param;
	}
}