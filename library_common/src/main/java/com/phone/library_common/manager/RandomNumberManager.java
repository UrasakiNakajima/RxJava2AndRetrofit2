package com.phone.library_common.manager;

import com.phone.library_base.manager.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class RandomNumberManager {

    private static final String TAG = RandomNumberManager.class.getSimpleName();

    public static void generateRandomNumber() {
        List<Integer> integerList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int num = random.nextInt(30);
            if (integerList.size() > 0) {
                boolean isGenerated = false;
//                for (int j = 0; j < integerList.size() - 1; j++) {
                for (int j = integerList.size() - 1; j >= 0; j--) {
                    if (num == integerList.get(j)) {
//                        LogManager.i(TAG, "num*****" + num);
//                        LogManager.i(TAG, "integerList i*****" + i);
//                        LogManager.i(TAG, "integerList i value*****" + integerList.get(j));
                        isGenerated = true;
                        break;
                    }
                }
                if (!isGenerated) {
//                    LogManager.i(TAG, "!!!!!!!!!!!!!!!!!!!!!*****");
//                    LogManager.i(TAG, "integerList a*****" + integerList.toString());
//                    LogManager.i(TAG, "num*****" + num);
                    integerList.add(num);
                }
            } else {
//                LogManager.i(TAG, "num*****" + num);
                integerList.add(num);
            }
        }

        LogManager.i(TAG, "integerList*****" + integerList.toString());
        LogManager.i(TAG, "integerList.size()*****" + integerList.size());
    }



    public static void generateRandomNumber2() {
        Vector<Integer> integerVector = new Vector<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int num = random.nextInt(20);
            if (integerVector.size() > 0) {
                boolean isGenerated = false;
//                for (int j = 0; j < integerVector.size() - 1; j++) {
                for (int j = integerVector.size() - 1; j >= 0; j--) {
                    if (num == integerVector.get(j)) {
//                        LogManager.i(TAG, "num*****" + num);
//                        LogManager.i(TAG, "integerVector i*****" + i);
//                        LogManager.i(TAG, "integerVector i value*****" + integerVector.get(j));
                        isGenerated = true;
                        break;
                    }
                }
                if (!isGenerated) {
//                    LogManager.i(TAG, "!!!!!!!!!!!!!!!!!!!!!*****");
//                    LogManager.i(TAG, "integerVector a*****" + integerVector.toString());
//                    LogManager.i(TAG, "num*****" + num);
                    integerVector.add(num);
                }
            } else {
//                LogManager.i(TAG, "num*****" + num);
                integerVector.add(num);
            }
        }

        LogManager.i(TAG, "integerVector*****" + integerVector.toString());
        LogManager.i(TAG, "integerVector.size()*****" + integerVector.size());
    }


}
