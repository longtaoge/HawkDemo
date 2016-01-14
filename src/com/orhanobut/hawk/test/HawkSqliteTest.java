package com.orhanobut.hawk.test;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

public class HawkSqliteTest extends HawkTest {

	@Override
	public void init() {
		Hawk.init(context).setStorage(HawkBuilder.newSqliteStorage(context))
				.build();
	}
	
	
	
	
}
