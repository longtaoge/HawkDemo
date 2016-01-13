package com.colintmiller.simplenosql.test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityUnitTestCase;

import com.colintmiller.simplenosql.GsonSerialization;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.OperationObserver;
import com.colintmiller.simplenosql.bean.SampleBean;
import com.colintmiller.simplenosql.bean.SampleBean2;
import com.colintmiller.simplenosql.bean.User;
import com.colintmiller.simplenosql.db.SimpleNoSQLContract;
import com.colintmiller.simplenosql.db.SimpleNoSQLDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests for saving entities to the DB. This includes saving a single entity or
 * saving multiple entities.
 */
public class NoSQLSaveTaskTest extends ActivityUnitTestCase<Activity> {
	private GsonSerialization serialization;
	private CountDownLatch signal;
	private Context context;

	public NoSQLSaveTaskTest() {
		super(Activity.class);
		serialization = new GsonSerialization();
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		signal = new CountDownLatch(1);
		this.context = getInstrumentation().getTargetContext();
	}

	public void testSaveEntity() throws Throwable {
		final NoSQLEntity<SampleBean> entity = new NoSQLEntity<SampleBean>(
				"test", "first");
		SampleBean data = new SampleBean();
		data.setName("SimpleNoSQL");
		data.setId(1);
		entity.setData(data);

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				NoSQL.with(context).using(SampleBean.class)
						.addObserver(getObserver()).save(entity);
			}
		});
		signal.await(3, TimeUnit.SECONDS);

		assertNotNull("Activity is null when it should not have been",
				getInstrumentation().getTargetContext());
		SimpleNoSQLDBHelper sqldbHelper = new SimpleNoSQLDBHelper(
				getInstrumentation().getTargetContext(), serialization,
				serialization);
		SQLiteDatabase db = sqldbHelper.getReadableDatabase();
		String[] columns = {
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_BUCKET_ID,
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_ENTITY_ID,
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_DATA };
		String[] selectionArgs = { "test", "first" };
		Cursor cursor = db.query(SimpleNoSQLContract.EntityEntry.TABLE_NAME,
				columns, SimpleNoSQLContract.EntityEntry.COLUMN_NAME_BUCKET_ID
						+ "=? and "
						+ SimpleNoSQLContract.EntityEntry.COLUMN_NAME_ENTITY_ID
						+ "=?", selectionArgs, null, null, null);
		assertNotNull(cursor);
		assertEquals(cursor.getCount(), 1);
	}

	public void testSaveEntities() throws Throwable {
		final List<NoSQLEntity<SampleBean>> allEntities = new ArrayList<NoSQLEntity<SampleBean>>(
				3);
		for (int i = 0; i < 5; i++) {
			NoSQLEntity<SampleBean> entity = new NoSQLEntity<SampleBean>(
					"sample", "entity" + i);
			SampleBean data = new SampleBean();
			data.setId(i);
			data.setExists(i % 2 == 0);
			entity.setData(data);
			allEntities.add(entity);
		}

		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				NoSQL.with(context).using(SampleBean.class)
						.addObserver(getObserver()).save(allEntities);
			}
		});

		signal.await(2, TimeUnit.SECONDS);

		SimpleNoSQLDBHelper sqldbHelper = new SimpleNoSQLDBHelper(
				getInstrumentation().getTargetContext(), serialization,
				serialization);
		SQLiteDatabase db = sqldbHelper.getReadableDatabase();
		String[] columns = {
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_BUCKET_ID,
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_ENTITY_ID,
				SimpleNoSQLContract.EntityEntry.COLUMN_NAME_DATA };
		String[] selectionArgs = { "sample" };
		Cursor cursor = db.query(SimpleNoSQLContract.EntityEntry.TABLE_NAME,
				columns, SimpleNoSQLContract.EntityEntry.COLUMN_NAME_BUCKET_ID
						+ "=?", selectionArgs, null, null, null);
		assertNotNull(cursor);

		assertEquals(cursor.getCount(), 5);
		int counter = 0;
		while (cursor.moveToNext()) {
			String data = cursor
					.getString(cursor
							.getColumnIndex(SimpleNoSQLContract.EntityEntry.COLUMN_NAME_DATA));
			NoSQLEntity<SampleBean> bean = new NoSQLEntity<SampleBean>(
					"bucket", "id");
			bean.setData(serialization.deserialize(data, SampleBean.class));
			assertEquals(counter, bean.getData().getId());
			assertEquals(counter % 2 == 0, bean.getData().isExists());
			counter++;
		}
	}

	public void testSaveEntities2() throws Throwable {

		NoSQLEntity<SampleBean2> entity = new NoSQLEntity<SampleBean2>(
				"bucket", "entityId");
		SampleBean2 data = new SampleBean2();
		data.setField1("abc");
		data.setId(9);
		List<User> mUsers = new ArrayList<User>();

		for (int i = 0; i < 1000; i++) {

			User mUser = new User();
			mUser.setAge("15" + i);
			mUser.setName("name" + i);
			mUsers.add(mUser);

		}

		
		data.setmUsers(mUsers);
		entity.setData(data);
		//NoSQL.with(context).using(SampleBean2.class).bucketId("bucket")
	   // .entityId("entityId").delete();
		NoSQL.with(context).using(SampleBean2.class).save(entity);

	}

	private OperationObserver getObserver() {
		return new OperationObserver() {
			@Override
			public void hasFinished() {
				signal.countDown();
				;
			}
		};
	}

	// TODO: Write an "update" test (overrides an already saved entity)
}
