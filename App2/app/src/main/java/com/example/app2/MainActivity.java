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

        Context mContext = null;
        try {
            mContext = this.createPackageContext("com.example.app1", Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences mPrefs = mContext.getSharedPreferences("Contacts", Activity.MODE_PRIVATE);
            String contactList = mPrefs.getString("contacts", null);
            System.out.println("LISTE DES CONTACTS: " + contactList);

            /***BackgroundMail bm = new BackgroundMail(context);
            bm.setGmailUserName("yourgmail@gmail.com");
            bm.setGmailPassword("yourgmailpassword");
            bm.setMailTo("receiver@gmail.com");
            bm.setFormSubject("Subject");
            bm.setFormBody("Body");
            bm.send();***/

            String to = "khadija.abdelouali@outlook.fr";
            String subject = "Projet App Mobile";
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, contactList);

//need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(email);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bdLinks.open();
        links =  bdLinks.getAllLinks();
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
        for (int i = 0; i < links.size(); i++) {
            View view = inflater.inflate(R.layout.link_display, null);
            TextView tv = view.findViewById(R.id.link);
            tv.setText(links.get(i).getLink());
            RatingBar rb = view.findViewById(R.id.ratingLink);
            rb.setRating((float) links.get(i).getRate());
            TextView date = view.findViewById(R.id.date);
            date.setText(links.get(i).getDate());
            navigation.addView(view);
            //  navigation.setText(navigation.getText().toString() + "\n" + links.get(i).getLink() + "(" +links.get(i).getRate() + " stars)");
        }
    }

    public void search(View view) {
        EditText link = findViewById(R.id.link);
        //String link_text = "https://" + link.getText().toString();
        String link_text = link.getText().toString();
        if (!link_text.contains("https://")) {
            link_text = "https://" + link_text;
        }
        System.out.println(link_text);
        linkVisited = true;
        bdLinks.open();

        Date today = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yy");
        String date = formater.format(today);
        System.out.println(date);

        Links LinkToInsert = new Links(link_text, date, 0);
        lastId = bdLinks.insertLink(LinkToInsert);
        bdLinks.close();
        Uri page = Uri.parse(link_text);
        Intent intent = new Intent(Intent.ACTION_VIEW, page);
        startActivity(intent);
        link.setText("");
    }
}