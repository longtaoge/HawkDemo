package com.orhanobut.hawk.test;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

public class HawkEncryptionMediumTest extends HawkTest {

  @Override public void init() {
    Hawk.init(context)
        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
        .build();
  }
}
