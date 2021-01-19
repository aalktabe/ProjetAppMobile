package com.example.app2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class BDLinks {
    private final static int VERSION_BDD = 1;
    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    public BDLinks(Context context) {
        //On crée la BDD et sa table :
        maBaseSQLite = new MaBaseSQLite(context,"Links",null, VERSION_BDD);

    }

    public void open(){
        //On ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
        if (bdd == null) {
            System.out.println("La base n'existe pas");
        }
    }

    public void close(){
        //On ferme la BDD
        bdd.close();
    }

    public SQLiteDatabase getBdd(){
        return bdd;
    }

    public long insertLink(Links link){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        values.put("link", link.getLink());
        values.put("date", link.getDate());
        values.put("rate", link.getRate());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert("Links", null, values);
    }

    public long updateRate(int id, int rate) {
        ContentValues values = new ContentValues();
        values.put("rate", rate);
        return bdd.update("Links", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Links> getAllLinks() {
        ArrayList<Links> links = new ArrayList<>();
        Cursor cursor = bdd.query("Links", new String[] {"link", "date", "rate"}, null, null, null, null, null, null);
        if (cursor == null) {
            System.out.println("Cursor vide");
            return null;
        }
        while (cursor.moveToNext()) {
            String link = cursor.getString(cursor.getColumnIndex("link"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int  rate = cursor.getInt(cursor.getColumnIndex("rate"));
            Links newLink = new Links(link, date, rate);
            links.add(newLink);
        }
       return links;
    }
}
