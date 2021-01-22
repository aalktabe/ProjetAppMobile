package com.example.app2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

public class RatingPopUp extends DialogFragment {
    BDLinks bdLinks;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bdLinks = new BDLinks(getContext());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_link, null);
        builder.setTitle("Rate the website").setView(view)
                .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int listenerId) {
                        int LinkId = (int) getArguments().getLong("id");
                        RatingBar ratingbar = view.findViewById(R.id.rating);
                        int rate = (int) ratingbar.getRating();
                        bdLinks.open();
                        bdLinks.updateRate(LinkId, rate);
                        bdLinks.close();
                        Intent refresh = new Intent(getContext(), MainActivity.class);
                        startActivity(refresh);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
