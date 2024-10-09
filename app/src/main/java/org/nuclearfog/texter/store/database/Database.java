package org.nuclearfog.texter.store.database;

import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_HEIGHT;
import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_PATH;
import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_POS_X;
import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_POS_Y;
import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_RANK;
import static org.nuclearfog.texter.store.database.tables.ImageTable.IMAGE_WIDTH;
import static org.nuclearfog.texter.store.database.tables.PostTable.POST_TEXT;
import static org.nuclearfog.texter.store.database.tables.PostTable.POST_TIME;
import static org.nuclearfog.texter.store.database.tables.PostTable.POST_TITLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import org.nuclearfog.texter.model.Image;
import org.nuclearfog.texter.model.Post;
import org.nuclearfog.texter.store.database.tables.ImageTable;
import org.nuclearfog.texter.store.database.tables.PostTable;

import java.util.LinkedList;
import java.util.List;


public class Database {

	private static final String SELECTION_POSTS = PostTable.POST_ID + "," + POST_TIME + "," + POST_TITLE + "," + POST_TEXT;
	private static final String SELECTION_IMAGES = IMAGE_POS_X + "," + IMAGE_POS_Y + "," + IMAGE_WIDTH + "," + IMAGE_HEIGHT + "," + IMAGE_RANK + "," + IMAGE_PATH;

	private static final String QUERY_GET_POST = "SELECT " + SELECTION_POSTS + " FROM " + PostTable.NAME + " WHERE " + PostTable.POST_ID + "=?";
	private static final String QUERY_GET_POSTS = "SELECT " + SELECTION_POSTS + " FROM " + PostTable.NAME + ";";
	private static final String QUERY_GET_IMAGES = "SELECT " + SELECTION_IMAGES + " FROM " + ImageTable.NAME + " WHERE " + ImageTable.POST_ID + "=? ORDER BY " + IMAGE_RANK + " ASC;";

	private static final String SELECT_POST = PostTable.POST_ID + "=?";
	private static final String QUERY_DEL_IMAGE = ImageTable.POST_ID + "=?";


	private SQLiteConnector connector;


	public Database(Context context) {
		connector = SQLiteConnector.getInstance(context, ImageTable.QUERY_CREATE_TABLE, PostTable.QUERY_CREATE_TABLE);
	}


	@Nullable
	public Post getPost(int id) {
		Post result = null;
		String[] args = {Integer.toString(id)};
		SQLiteDatabase db = connector.getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY_GET_POST, args);
		if (cursor.moveToFirst()) {
			long timestamp = cursor.getLong(1);
			String title = cursor.getString(2);
			String text = cursor.getString(3);
			result = new Post(id, timestamp, title, text);
			result.addImages(getImages(db, id));
		}
		cursor.close();
		return result;
	}


	public List<Post> getPosts() {
		List<Post> posts = new LinkedList<>();
		SQLiteDatabase db = connector.getReadableDatabase();
		Cursor cursor = db.rawQuery(QUERY_GET_POSTS, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				long timestamp = cursor.getLong(1);
				String title = cursor.getString(2);
				String text = cursor.getString(3);
				Post post = new Post(id, timestamp, title, text);
				post.addImages(getImages(db, id));
				posts.add(post);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return posts;
	}


	public void savePost(Post post) {
		SQLiteDatabase db = connector.getWritableDatabase();
		ContentValues columnPost = new ContentValues();
		columnPost.put(PostTable.POST_ID, post.getId());
		columnPost.put(POST_TIME, post.getTimestamp());
		columnPost.put(POST_TITLE, post.getTitle());
		columnPost.put(POST_TEXT, post.getText());
		db.insertWithOnConflict(PostTable.NAME, "", columnPost, SQLiteDatabase.CONFLICT_REPLACE);
		String[] param = {Integer.toString(post.getId())};
		db.delete(ImageTable.NAME, QUERY_DEL_IMAGE, param);
		for (Image image : post.getImages()) {
			ContentValues columnImage = new ContentValues();
			columnImage.put(ImageTable.POST_ID, post.getId());
			columnImage.put(IMAGE_PATH, image.getPath());
			columnImage.put(IMAGE_POS_X, image.getX());
			columnImage.put(IMAGE_POS_Y, image.getY());
			columnImage.put(IMAGE_WIDTH, image.getWidth());
			columnImage.put(IMAGE_HEIGHT, image.getHeight());
			columnImage.put(IMAGE_RANK, image.getRank());
			db.insertWithOnConflict(ImageTable.NAME, "", columnImage, SQLiteDatabase.CONFLICT_REPLACE);
		}
		connector.commit();
	}


	public void removePost(int id) {
		String[] param = {Integer.toString(id)};
		SQLiteDatabase db = connector.getWritableDatabase();
		db.delete(ImageTable.NAME, QUERY_DEL_IMAGE, param);
		db.delete(ImageTable.NAME, SELECT_POST, param);
		connector.commit();
	}


	private List<Image> getImages(SQLiteDatabase db, int postId) {
		Cursor cursor = db.rawQuery(QUERY_GET_IMAGES, new String[]{Integer.toString(postId)});
		List<Image> images = new LinkedList<>();
		if (cursor.moveToFirst()) {
			do {
				int img_x = cursor.getInt(0);
				int img_y = cursor.getInt(1);
				int width = cursor.getInt(2);
				int height = cursor.getInt(3);
				int rank = cursor.getInt(4);
				String img_path = cursor.getString(5);
				images.add(new Image(img_path, img_x, img_y, width, height, rank));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return images;
	}
}