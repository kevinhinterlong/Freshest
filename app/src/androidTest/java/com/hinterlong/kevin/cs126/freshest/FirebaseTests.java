package com.hinterlong.kevin.cs126.freshest;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hinterlong.kevin.cs126.freshest.database.Constants;
import com.hinterlong.kevin.cs126.freshest.database.DatabaseUtils;
import com.hinterlong.kevin.cs126.freshest.model.ScoreRecord;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTests {
	private DatabaseReference testDb = FirebaseDatabase
			.getInstance()
			.getReference()
			.child(Constants.TEST_TABLE)
			.child(Constants.SCORES_TABLE);

	private void clear() {
		testDb.removeValue();
		testDb.push();
	}

	@Test
	public void writeAndRead() {
		clear();
		int rand = new Random().nextInt(100);
		String myName = "Kevin";
		ScoreRecord scoreRecord = new ScoreRecord(myName, rand);
		DatabaseUtils.append(testDb, scoreRecord);

		List<ScoreRecord> allRecords = DatabaseUtils.blockingReadAll(testDb, ScoreRecord.class);
		assertEquals(1, allRecords.size());
		assertEquals(myName, allRecords.get(0).getName());
		assertEquals(rand, allRecords.get(0).getScore());
	}

	@Test
	public void writeMany() {
		clear();
		int numEntries = 100;
		for (int i = 0; i < numEntries; i++) {
			String myName = "Kevin";
			int rand = new Random().nextInt(100);
			ScoreRecord scoreRecord = new ScoreRecord(myName, rand);
			DatabaseUtils.append(testDb, scoreRecord);
		}
		List<ScoreRecord> all = DatabaseUtils.blockingReadAll(testDb,ScoreRecord.class);
		assertEquals(numEntries, all.size());
	}
}
