package com.orhanobut.hawk.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.HawkBuilder.EncryptionMethod;
import com.orhanobut.hawk.LogLevel;
import com.orhanobut.hawk.Logger;
import com.orhanobut.hawk.bean.SampleBean2;
import com.orhanobut.hawk.bean.User;

public class HawkTest extends AndroidTestCase {

	private static final String KEY = "TAG";
	private static final long LATCH_TIMEOUT_IN_SECONDS = 5;

	protected final Context context;

	public HawkTest() {
		context = getContext();
		initSqlite();
	}

	public void setUp() {
		init();
	}

	public void init() {
		// Hawk.init(context).build();
	}

	public void initSqlite() {

		String databasesPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hawktest.db";

		if (getContext() != null) {
			Hawk.init(getContext())
					.setStorage(
							HawkBuilder.newSqliteStorage(getContext(),
									databasesPath)).build();
		} else {
			Logger.i("error");
		}

	}

	public void tearDown() {
		if (Hawk.isBuilt()) {
			// 测试完成后执行
			// Hawk.clear();
		}
	}

	public void initWithInvalidValues() {
		try {
			Hawk.init(null);
			fail();
		} catch (Exception e) {

		}
	}

	public void testSingleItem() {

		Log.i("TEEE", "ddddddd");
		String databasesPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hawktest.db";

		if (getContext() != null) {
			Hawk.init(getContext())

					.setStorage(
							HawkBuilder.newSqliteStorage(getContext(),
									databasesPath)).build();
		} else {
			Logger.i("error");

		}

		Hawk.put("boolean", true);

		Hawk.put("string", "string");

		Hawk.put("float", 1.5f);

		Hawk.put("integer", 10);

		User mUser = new User();

		mUser.setAge("adge");
		mUser.setFristname("fristname");
		mUser.setLastname("lastname");

		mUser.setName("name");
		Hawk.put("user", mUser);

		User user = Hawk.get("user");
		Log.i("TEEE", user.getLastname() + "_____");

		Hawk.put("char", 'A');
		// Logger.i(Hawk.put("char", 'A')+"TEEE");
		// Hawk.put("object", new FooBar());

		// Log.i("TEEE", Hawk.put("char", "e")+"");

		SampleBean2 sampleBean2 = new SampleBean2();

		sampleBean2.setField1("id");
		sampleBean2.setId(5);

		sampleBean2.setName("made");

		List<User> mUsers = new ArrayList<User>();

		for (int i = 0; i < 10000; i++) {

			user = new User();
			user.setAge(i + "");
			user.setFristname("fristname" + i);
			user.setLastname("lastname" + i);
			user.setName("name" + i);
			mUsers.add(user);

		}

		sampleBean2.setmUsers(mUsers);

		for (int i = 0; i < 50; i++) {

			Log.i("TEEE_musers"+i, Hawk.put("mUsers" + i, sampleBean2) + "");

		}

		Log.i("TEEE", Hawk.get("char") + "");
	}

	public void testSingleItemDefault() {

		Log.i("TEEE", "ddddddd");
		String databasesPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/hawktest.db";

		if (getContext() != null) {
			Hawk.init(getContext())
					.setStorage(
							HawkBuilder.newSqliteStorage(getContext(),
									databasesPath)).build();
		} else {
			Logger.i("error");

		}
		boolean result = Hawk.get("tag", true);

		SampleBean2 sampleBean2 = Hawk.get("mUsers49");

		Log.i("TEEE_SampleBean2", sampleBean2.getmUsers().get(99)
				.getFristname()
				+ "((((");

	}

	public void testList() {
		List<String> list = new ArrayList<String>();
		list.add("foo");
		list.add("bar");

		Hawk.put("tag", list);

		List<String> list1 = Hawk.get("tag");

	}

	public void testEmptyList() {
		List<FooBar> list = new ArrayList<FooBar>();
		Hawk.put("tag", list);

		List<FooBar> list1 = Hawk.get("tag");

	}

	public void testMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", "value");
		Hawk.put("map", map);

		Map<String, String> map1 = Hawk.get("map");

	}

	public void testEmptyMap() {
		Map<String, FooBar> map = new HashMap<String, FooBar>();
		Hawk.put("tag", map);

		Map<String, FooBar> map1 = Hawk.get("tag");

	}

	public void testSet() {
		Set<String> set = new HashSet<String>();
		set.add("foo");
		Hawk.put("set", set);

		Set<String> set1 = Hawk.get("set");

	}

	public void testEmptySet() {
		Set<FooBar> set = new HashSet<FooBar>();
		Hawk.put("tag", set);

		Set<FooBar> set1 = Hawk.get("tag");

	}

	public void testNullKeyPut() {
		try {
			Hawk.put(null, "test");
			fail();
		} catch (Exception e) {
		}
	}

	public void testNullKeyGet() {
		try {
			Hawk.get(null);
			fail();
		} catch (Exception e) {
		}
	}

	public void testNullValuePut() {
		try {
			Hawk.put("tag", "something");

		} catch (Exception e) {
			fail();
		}
	}

	public void testCount() {
		Hawk.clear();
		String value = "test";
		Hawk.put("tag", value);
		Hawk.put("tag1", value);
		Hawk.put("tag2", value);
		Hawk.put("tag3", value);
		Hawk.put("tag4", value);

	}

	public void testClear() {
		String value = "test";
		Hawk.put("tag", value);
		Hawk.put("tag1", value);
		Hawk.put("tag2", value);

		Hawk.clear();

	}

	public void testRemove() {
		Hawk.clear();
		String value = "test";
		Hawk.put("tag", value);
		Hawk.put("tag1", value);
		Hawk.put("tag2", value);

		Hawk.remove("tag");

		String result = Hawk.get("tag");

	}

	public void testBulkRemoval() {
		Hawk.clear();
		Hawk.put("tag", "test");
		Hawk.put("tag1", 1);
		Hawk.put("tag2", Boolean.FALSE);

		Hawk.remove("tag", "tag1");

		String result = Hawk.get("tag");

	}

	public void testContains() {
		String value = "test";
		String key = "tag";
		Hawk.put(key, value);

		Hawk.remove(key);

	}

	public void testChain() {
		Hawk.chain().put("tag", 1).put("tag1", "yes")
				.put("tag2", Boolean.FALSE).commit();

	}

	public void testChainWithCapacity() {
		Hawk.chain(10).put("tag", 1).put("tag1", "yes")
				.put("tag2", Boolean.FALSE).commit();

	}

	public void testChainWithLists() {
		List<String> items = new ArrayList<String>();
		items.add("fst");
		items.add("snd");
		items.add("trd");

		Hawk.chain().put("tag", 1).put("tag1", "yes")
				.put("tag2", Boolean.FALSE).put("lst", items).commit();

		List<String> stored = Hawk.get("lst");

	}

	public void testHugeData() {
		for (int i = 0; i < 100; i++) {
			Hawk.put("" + i, "" + i);
		}

	}

	public void testHugeDataWithBulk() {
		Hawk.Chain chain = Hawk.chain();
		for (int i = 0; i < 10000; i++) {
			chain.put("" + i, "" + i);
		}
		chain.commit();

	}

	public void testLogLevel() {
		Hawk.init(context).setLogLevel(LogLevel.NONE).build();

		Hawk.init(context).setLogLevel(LogLevel.FULL).build();

	}

	public void statusNotBuiltBeforeBuild() {
		Hawk.init(context);
	}

	public void statusBuiltAfterBuild() {
		Hawk.init(context).build();
	}

	public void testGetThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.get(KEY);
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testPutThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.put(KEY, "value");
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testClearThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.clear();
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testContainsThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.contains(KEY);
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testRemoveThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.remove(KEY);
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testRemoveMultiKeysThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.remove(KEY, KEY);
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testResetCryptoThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.resetCrypto();
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testCountThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.count();
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}

	public void testPutInChainThrowsExceptionWhenNotBuilt() {
		Hawk.init(context);
		try {
			Hawk.chain().put(KEY, "value");
			fail("Did not throw an exception");
		} catch (IllegalStateException ignored) {
		}
	}
}
