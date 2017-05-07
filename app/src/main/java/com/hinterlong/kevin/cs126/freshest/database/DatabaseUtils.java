package com.hinterlong.kevin.cs126.freshest.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hinterlong.kevin.cs126.freshest.model.ScoreRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for reading from and writing to a {@link DatabaseReference}
 */
public class DatabaseUtils {
	public static final String TAG = DatabaseUtils.class.getSimpleName();
	private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

	private DatabaseUtils() {

	}

	/**
	 * Appends a value to a table.
	 *
	 * @param ref the database reference to append value to
	 * @param e   object to be appended to database
	 * @param <E> type of object
	 * @return the key
	 */
	public static <E> String append(DatabaseReference ref, E e) {
		String key = ref.push().getKey();
		ref.child(key).setValue(e);
		ref.push();
		return key;
	}

	/**
	 * Returns scores table
	 *
	 * @return a reference to the table of scores
	 */
	public static DatabaseReference getScoresTable() {
		return database.getReference().child(Constants.SCORES_TABLE);
	}

	public static <E> List<E> blockingReadAll(DatabaseReference ref, Class<E> eClass) {
		return blockingReadAll(ref, eClass, 5, TimeUnit.SECONDS);

	}

	/**
	 * Reads all values from a firebase database blocking until the information has been retrieved.
	 *
	 * @param ref      the reference to read from
	 * @param eClass   the type of object to be read
	 * @param timeout  how long to wait
	 * @param timeUnit units of time to wait
	 * @param <E>      type to be read from database
	 * @return list of objects read from database
	 */
	public static <E> List<E> blockingReadAll(DatabaseReference ref, final Class<E> eClass, int timeout, TimeUnit timeUnit) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		final List<E> toReturn = new ArrayList<>();
		ValueEventListener listener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					E value = postSnapshot.getValue(eClass);
					toReturn.add(value);
				}
				Log.d(TAG, "Found " + toReturn.size() + " results");
				countDownLatch.countDown();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				Log.w(TAG, "Failed to read value.", error.toException());
			}
		};
		ref.addValueEventListener(listener);

		try {
			countDownLatch.await(timeout, timeUnit);
		} catch (InterruptedException ignored) {
		}
		ref.removeEventListener(listener);
		return toReturn;
	}

	/**
	 * Gets an id for the current user
	 *
	 * @param ref table to push to
	 * @return the user's id
	 */
	public static String getPlayerId(DatabaseReference ref) {
		String key = ref.push().getKey();
		ref.child(key).setValue(new ScoreRecord("", 0));
		Log.d(TAG, "Generating player id: " + key);
		return key;
	}
}
