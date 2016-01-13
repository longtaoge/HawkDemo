package com.colintmiller.simplenosql.test2;

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





	public void testSaveEntities2() throws Throwable {

		NoSQLEntity<SampleBean2> entity = new NoSQLEntity<SampleBean2>(
				"bucket", "entityId");
		SampleBean2 data = new SampleBean2();
		data.setField1("abc");
		data.setId(9);
		List<User> mUsers = new ArrayList<User>();
		for (int i = 0; i < 100; i++) {

			User mUser = new User();
			mUser.setAge("15" + i);
			mUser.setName("name" + i);
			mUsers.add(mUser);

		}
		
		data.setmUsers(mUsers);
		entity.setData(data);
		//NoSQL.with(context).using(SampleBean2.class).bucketId("bucket")
	   // .entityId("entityId").delete();
		NoSQL.with( getInstrumentation().getTargetContext()).using(SampleBean2.class).save(entity);

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

	
}
