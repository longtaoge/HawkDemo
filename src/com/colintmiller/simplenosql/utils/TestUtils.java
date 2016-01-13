package com.colintmiller.simplenosql.utils;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.OperationObserver;

/**
 * Created by cmiller on 8/12/14.
 */
public class TestUtils {

    public static CountDownLatch cleanBucket(String bucket, Context context) {
        final CountDownLatch signal = new CountDownLatch(1);

        NoSQL.with(context).using(TestUtils.class)
                .bucketId(bucket)
                .addObserver(new OperationObserver() {
                    @Override
                    public void hasFinished() {
                        signal.countDown();
                    }
                })
                .delete();
        return signal;
    }
}
