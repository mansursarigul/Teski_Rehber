package com.example.teski_rehber.teskirehber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "rehberdb";

    private static final String TABLE_NAME = "rehber";
    public static Context context;
    private static String Kisi_Id = "id";
    private static String Kisi_AdSoyad = "AdSoyad";
    private static String Kisi_Unvan = "Unvan";
    private static String Kisi_Mobil = "Mobil";
    private static String Kisi_Daire = "Daire";
    private static String Kisi_Sube = "Sube";
    private static String Kisi_DID = "DID";

    private static String Parametre_Id = "id";
    private static String Parametre_Ver = "versiyon";


    public DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void DeleteTable(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String DELETE_TABLE = "DELETE FROM " + TABLE_NAME;
            db.execSQL(DELETE_TABLE);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Kisi_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Kisi_AdSoyad + " TEXT,"
                + Kisi_Unvan + " TEXT,"
                + Kisi_Mobil + " TEXT,"
                + Kisi_Daire + " TEXT,"
                + Kisi_Sube + " TEXT,"
                + Kisi_DID + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
        String CREATE_TABLE2 = "CREATE TABLE parametre("
                + Parametre_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "versiyon INTEGER" + ")";
        db.execSQL(CREATE_TABLE2);
        String CREATE_TABLE3 = "INSERT INTO parametre(versiyon) values(0)";
        db.execSQL(CREATE_TABLE3);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Integer DBVersiyonAl(){
        String selectQuery = "SELECT versiyon FROM parametre";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Integer VerNo=Integer.parseInt(cursor.getString(1));
        Log.i("dikkat",String.valueOf(VerNo));
    return DBVersiyonAl();
        //SQLiteDatabase db = this.getWritableDatabase();
    //        String DELETE_TABLE = "DELETE FROM " + TABLE_NAME;
//        db.execSQL(DELETE_TABLE);

    }
    public void VeriEkle(String AdSoyad, String Unvan, String Mobil, String Daire, String Sube, String DID, Integer Tip,Integer Ver){
        SQLiteDatabase db = this.getWritableDatabase();
        switch (Tip) {
            case 1:
                try {
                    ContentValues cv = new ContentValues();
                    cv.put(Kisi_AdSoyad, AdSoyad);
                    cv.put(Kisi_Unvan, Unvan);
                    cv.put(Kisi_Mobil, Mobil);
                    cv.put(Kisi_Daire, Daire);
                    cv.put(Kisi_Sube, Sube);
                    cv.put(Kisi_DID, DID);
                    db.insert(TABLE_NAME, null, cv);

                } catch (Exception e) {
                }
            case 2:
                try {
                    ContentValues cv = new ContentValues();
                    cv.put(Parametre_Ver, Ver);
                    db.update("parametre",cv, null,null);
                } catch (Exception e) {
                }
        }
        db.close();
    }
    public Cursor getListContents(String Kriter) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data;
        if (Kriter != null) {
            data = db.rawQuery("SELECT id, AdSoyad, Unvan, Mobil, Daire, DID, Sube FROM " + TABLE_NAME + " WHERE (AdSoyad like '%" + Kriter + "%' OR Mobil like '%" + Kriter + "%' OR Daire like '%" + Kriter + "%') order by AdSoyad", null);
        } else {
            data = db.rawQuery("SELECT id, AdSoyad, Unvan, Mobil, Daire, DID, Sube FROM " + TABLE_NAME + " order by AdSoyad", null);
        }

        return data;
    }
    public Cursor getListDairelerinKisileri(String DaireAdi) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.i("dikkat","SELECT AdSoyad, Mobil, Daire FROM " + TABLE_NAME + " WHERE Daire='" + DaireAdi + "' GROUP BY AdSoyad, Mobil, Daire");
        Cursor data2 = db.rawQuery("SELECT AdSoyad, Unvan, Mobil, Daire, DID, Sube FROM " + TABLE_NAME + " WHERE Daire='" + DaireAdi + "' GROUP BY AdSoyad, Mobil, Daire ORDER BY AdSoyad", null);
        data2.moveToFirst();
        return data2;
    }
    public Cursor getListDaireler() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT Daire, count(id) FROM " + TABLE_NAME + " GROUP BY Daire HAVING count(id)>0 ORDER BY Daire", null);
        data.moveToFirst();
        return data;
    }
    public Integer getVersiyonDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        int empName;
        try {
            Cursor cursor = db.rawQuery("SELECT versiyon FROM parametre", null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                empName = cursor.getInt(cursor.getColumnIndex("versiyon"));
            }else{
                empName=0;
            }
            return empName;
        }finally {
            empName=0;
        }
    }

}
