package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BDLinks bdLinks;
    ArrayList<Links> links;
    boolean linkVisited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bdLinks = new BDLinks(this);
        links = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bdLinks.open();
        links =  bdLinks.getAllLinks();
        bdLinks.close();
        TextView navigation = findViewById(R.id.navigation);
        navigation.setText("");
        if (linkVisited) {
            DialogFragment test = new RatingPopUp();
            test.show(getSupportFragmentManager(), "rates");
            linkVisited = false;
        }
        for (int i = 0; i < links.size(); i++) {
            navigation.setText(navigation.getText().toString() + "\n" + links.get(i).getLink());
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
        String date = "01/01/21";
        Links LinkToInsert = new Links(link_text, date, 2);
        bdLinks.insertLink(LinkToInsert);
        bdLinks.close();
        //Uri page = Uri.parse("https://google.com");
        Uri page = Uri.parse(link_text);
        Intent intent = new Intent(Intent.ACTION_VIEW, page);
        startActivity(intent);
        link.setText("");
    }
}