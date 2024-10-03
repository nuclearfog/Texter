package org.nuclearfog.texter.worker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author nuclearfog
 */
public abstract class AsyncExecutor<Parameter, Result> {

	private static final String TAG = "AsyncExecutor";

	/**
	 * maximum task count to run in the background
	 */
	private static final int N_THREAD = 4;

	/**
	 * timeout for queued processes
	 */
	private static final long P_TIMEOUT = 4L;

	/**
	 * thread pool executor
	 */
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(N_THREAD, N_THREAD, P_TIMEOUT, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

	/**
	 * handler used to send result back to activity/fragment
	 */
	private Handler uiHandler = new Handler(Looper.getMainLooper());

	/**
	 * contains all tasks used by an instance
	 */
	private Queue<Future<?>> futureTasks = new LinkedBlockingQueue<>();

	private WeakReference<Context> mContext;

	/**
	 *
	 */
	protected AsyncExecutor(@Nullable Context context) {
		mContext = new WeakReference<>(context);
	}

	/**
	 * start background task
	 *
	 * @param parameter parameter to send to the background task
	 * @param callback  result from the background task
	 */
	public final void execute(final Parameter parameter, @Nullable AsyncCallback<Result> callback) {
		final WeakReference<AsyncCallback<Result>> callbackReference = new WeakReference<>(callback);
		try {
			Future<?> future = THREAD_POOL.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Result result = doInBackground(parameter);
						onPostExecute(result, callbackReference);
					} catch (RuntimeException exception) {
						Log.e(TAG, "error while executing task");
					}
				}
			});
			futureTasks.add(future);
		} catch (RejectedExecutionException exception) {
			Log.e(TAG, "failed to submit task");
		}
	}

	/**
	 * send signal to the tasks executed by this instance
	 */
	public final void cancel() {
		while (!futureTasks.isEmpty()) {
			Future<?> future = futureTasks.remove();
			future.cancel(true);
		}
	}

	@Nullable
	protected Context getContext() {
		return mContext.get();
	}

	/**
	 * send result to main thread
	 *
	 * @param result result of the background task
	 */
	private synchronized void onPostExecute(@Nullable final Result result, WeakReference<AsyncCallback<Result>> callbackReference) {
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if (!futureTasks.isEmpty())
					futureTasks.remove();
				AsyncCallback<Result> reference = callbackReference.get();
				if (reference != null && result != null) {
					reference.onResult(result);
				}
			}
		});
	}

	/**
	 * This method is called in a background thread
	 *
	 * @param param parameter containing information for the background task
	 * @return result of the background task
	 */
	@WorkerThread
	protected abstract Result doInBackground(Parameter param);

	/**
	 * Callback used to send task result to main thread
	 */
	public interface AsyncCallback<Result> {

		/**
		 * @param result result of the task
		 */
		void onResult(@NonNull Result result);
	}
}
