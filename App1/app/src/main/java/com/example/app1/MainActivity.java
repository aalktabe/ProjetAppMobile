package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Contact {
    private String name;
    private String number;
    private String color;

    public Contact(String name, String number, String color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

public class MainActivity extends AppCompatActivity {
    LinearLayout tv_phonebook;
    ArrayList<Contact> arrayList;

    // Initialize the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_phonebook = findViewById(R.id.tv_phonebook);
        arrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, 1);
        } else {
            getcontact();
        }

        SharedPreferences preferences = getSharedPreferences("Contacts", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("contacts", arrayList.toString());
        editor.apply();

        System.out.println("TEST PREFERENCES PARTAGEES: " + preferences.getString("contacts", null));
    }


     private String rgbToHex() {
          // Génération de r,g,b
          float r = (float) Math.floor(Math.random() * Math.floor(255));
          float g = (float) Math.floor(Math.random() * Math.floor(255));
          float b = (float)Math.floor(Math.random() * Math.floor(255));
          // Conversion rgbToHex
         System.out.println("R: " + r + " G: " + g + " B: " + b);
         /***ColorMatrix cm = new ColorMatrix(new float[] {
                 // Change red channel
                 r, 0, 0, 0, 0,
                 // Change green channel
                 0, g, 0, 0, 0,
                 // Change blue channel
                 0, 0, b, 0, 0,
                 // Keep alpha channel
                 0, 0, 0, 1, 0,
         });***/
          //return cm;
          //return ((1 << 24) + ((int) r << 16) + ((int) g << 8) + b);
         return String.format("#%02X%02X%02X", (int)r, (int)g, (int)b);
    }

    private void getcontact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        LayoutInflater inflater = this.getLayoutInflater();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String cm = rgbToHex();
            System.out.println(cm);
            arrayList.add(new Contact(name, mobile, cm));
            View view = inflater.inflate(R.layout.contact_display, null);
            TextView tv_name = view.findViewById(R.id.name);
            tv_name.setText(name);
            TextView tv_number = view.findViewById(R.id.number);
            TextView placeholder = view.findViewById(R.id.placeholder);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.square);
            ColorDrawable cd = new ColorDrawable(Color.parseColor(cm));
            drawable.setColorFilter(Color.parseColor(cm), PorterDuff.Mode.MULTIPLY);
            placeholder.setBackground(drawable);
            placeholder.setText("" + name.charAt(0) + "");
            tv_number.setText(mobile);
            tv_phonebook.addView(view);
            //tv_phonebook.setText(arrayList.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcontact();
            }
        }
    }
}

