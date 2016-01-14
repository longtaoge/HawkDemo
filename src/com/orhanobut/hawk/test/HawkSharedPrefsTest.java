package com.orhanobut.hawk.test;

import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

public class HawkSharedPrefsTest extends HawkTest {

	@Override
	public void init() {
		Hawk.init(getContext())
				.setEncryptionMethod(HawkBuilder.EncryptionMethod.HIGHEST)
				.setStorage(HawkBuilder.newSharedPrefStorage(getContext()))
				.setPassword("besfjlkj").build();
	}

	public void testSP() {

		Hawk.put("STRING", "DDDDD44444555");
		Log.i("TAGS", Hawk.get("STRING") + "___");

	};

}
