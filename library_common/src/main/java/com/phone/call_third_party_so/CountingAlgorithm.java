package com.phone.call_third_party_so;

import com.phone.library_base.manager.LogManager;

public class CountingAlgorithm {

    private static final String TAG = CountingAlgorithm.class.getSimpleName();

    public static void countingAlgorithm() {
        StringBuilder countStr = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            if (i % 6 == 0) {
                if (i / 6 % 3 == 1) {
                    countStr.append("red").append(",");
                } else if (i / 6 % 3 == 2) {
                    countStr.append("green").append(",");
                } else {
                    countStr.append("blue").append(",");
                }
            } else {
                if (i != 100) {
                    countStr.append(i).append(",");
                } else {
                    countStr.append(i);
                }
            }
        }
        LogManager.i(TAG, countStr.toString());
    }


}
