package org.nuclearfog.texter.ui.dialogs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.nuclearfog.texter.model.Post;
import org.nuclearfog.texter.worker.AsyncExecutor.AsyncCallback;
import org.nuclearfog.texter.worker.PostSaveWorker;


public class PostDialog extends DialogFragment implements OnClickListener, AsyncCallback<Post> {

	private static final String TAG = "PostDialog";
	private static final String KEY_POST = "post";

	private static final InputFilter[] filter = {new InputFilter.LengthFilter(20)};

	private EditText title_input;

	private PostSaveWorker postWorker;

	@Nullable
	private Post post;


	public static void show(FragmentManager fm, Post post) {
		Fragment fragment = fm.findFragmentByTag(TAG);
		Bundle args = new Bundle();
		PostDialog postDialog;

		if (fragment instanceof PostDialog) {
			postDialog = (PostDialog) fragment;
		} else {
			postDialog = new PostDialog();
		}
		args.putSerializable(KEY_POST, post);
		postDialog.setArguments(args);
		postDialog.show(fm, TAG);
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(requireContext());
		title_input = new EditText(requireContext());
		postWorker = new PostSaveWorker(requireContext());

		title_input.setLines(1);
		title_input.setBackgroundColor(0);

		title_input.setFilters(filter);
		title_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		builder.setView(title_input);
		builder.setPositiveButton(android.R.string.ok, this);
		builder.setNegativeButton(android.R.string.cancel, null);

		if (savedInstanceState != null)
			savedInstanceState = getArguments();
		if (savedInstanceState != null) {
			post = (Post) savedInstanceState.getSerializable(KEY_POST);
		}

		return builder.create();
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (post != null) {
				post.setTitle(title_input.getText().toString());
				postWorker.execute(post, this);
			}
		}
	}


	@Override
	public void onResult(@NonNull Post post) {
		dismiss();
	}
}