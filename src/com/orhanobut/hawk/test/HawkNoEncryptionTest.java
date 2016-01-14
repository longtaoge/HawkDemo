package com.orhanobut.hawk.test;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

public class HawkNoEncryptionTest extends HawkTest {

  @Override public void init() {
    Hawk.init(context)
        .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
        .build();
  }
}
