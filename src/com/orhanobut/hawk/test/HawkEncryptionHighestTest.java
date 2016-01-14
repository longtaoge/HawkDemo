package com.orhanobut.hawk.test;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

public class HawkEncryptionHighestTest extends HawkTest {

  @Override public void init() {
    Hawk.init(context)
        .setEncryptionMethod(HawkBuilder.EncryptionMethod.HIGHEST)
        .setPassword("password")
        .build();

  }
}
