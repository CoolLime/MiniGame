package com.coollime.coollime.db;

import android.provider.BaseColumns;

/**
 * Created by SHIM on 16. 5. 12..
 */
public class Database {

    public static final class CreateDB implements BaseColumns {

        public static final String ID = "id";
        public static final String Name = "name";

        public static final String CREATEDB =
                "CREATE TABLE Game ("
                        + ID + " INTEGER PRIMARY KEY, "
                        + Name + "VARCHAR(100) NOT NULL) ";
    }

}
