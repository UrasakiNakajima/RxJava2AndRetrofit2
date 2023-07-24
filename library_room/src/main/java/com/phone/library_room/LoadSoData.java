package com.phone.library_room;

import com.phone.library_base.BaseApplication;

import net.sqlcipher.database.SQLiteDatabase;

public class LoadSoData {

    public static void loadRoomLibs() {
        SQLiteDatabase.loadLibs(BaseApplication.instance());
    }
}
