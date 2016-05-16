package com.coollime.coollime.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SHIM on 16. 5. 9..
 */
public class RankDAO {

    private static RankDAO _instance = null;
    private static Activity _activity = null;
    private SQLiteDatabase _db = null;

    private RankDAO() throws Exception{
        super();
        OpenDatabase();
    }

    public static synchronized RankDAO GetInstance(Activity act) throws Exception{

        if(_instance == null) {
            _activity = act;
            _instance = new RankDAO();
        }

        return _instance;
    }

    private void OpenDatabase() throws Exception {
        try{
            if(_db == null){
                _db = _activity.openOrCreateDatabase("DBhelper", Activity.MODE_PRIVATE, null);
                //_db.execSQL("drop table T_RANK");
                _db.execSQL("Create Table IF NOT EXISTS G1to50_RANK (name varchar(30), score DECIMAL(7,2), inst_dt date) ");
                _db.execSQL("Create Index IF NOT EXISTS G1_IDX_SCORE ON G1to50_RANK (score asc)");
                _db.execSQL("Create Table IF NOT EXISTS Gard_RANK (name varchar(30), score DECIMAL(1000), inst_dt date) ");
                _db.execSQL("Create Index IF NOT EXISTS G2_IDX_SCORE ON Card_RANK (score asc)");

            }
        }catch(Exception ig){
            throw ig;
        }
    }

    public void Append(String pName, double pScore, String d) throws Exception{
        _db.execSQL("insert into "+d+" (name, score, inst_dt) values('" + pName+ "'," + pScore + ",date('now')) ");
    }

    public ArrayList<HashMap<String,String>> GetRankData(String db){
        ArrayList<HashMap<String,String>> _Items = new ArrayList<HashMap<String,String>>();
        Cursor _cursor = null;
        if(db.equals("g1to50")) {
            _cursor = _db.rawQuery("Select name, inst_dt || '   ' || score || '초' From G1to50_RANK Order By score asc ", null);
        }
        else if(db.equals("card")){
            _cursor =  _db.rawQuery("Select name, inst_dt || '   ' || score || '초' From Card_RANK Order By score asc ", null);
        }

        int nRank = 1;
        if(_cursor.moveToFirst()){
            do{
                HashMap<String,String> _Item = new HashMap<String,String>();
                _Item.put("Name", "No."+ nRank + "\t" + _cursor.getString(0).toString());
                _Item.put("Score", _cursor.getString(1).toString());

                _Items.add(_Item);
                nRank++;
            }while(_cursor.moveToNext());

            _cursor.close();
        }

        return _Items;
    }
}
