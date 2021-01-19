package com.example.app2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MaBaseSQLite extends SQLiteOpenHelper {
    private final static String CREATE_TABLE =
            "create table Links(" +
                    "id integer primary key autoincrement," +
                    "link varchar(50) unique, " +
                    "date varchar(20), " +
                    "rate integer);";

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //On crée la base en exécutant la requête de création
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //On peut faire ce qu'on veut ici mais on va juste supprimer la table et la recréer
        System.out.println("DROPPING THE TABLE");
        db.execSQL("drop table Links;");
        System.out.println("TABLE DROPPED");
    }
}