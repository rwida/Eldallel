package com.project.eldalell.user.CustomComponent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.project.eldalell.user.R;


public class RateDialog extends AppCompatDialogFragment {
    RatingBar rateShop;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_dialog, null);

        rateShop = view.findViewById(R.id.rateShop);

        builder.setView(view);
        return builder.create();
    }
}
