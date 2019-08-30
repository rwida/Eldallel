package com.project.eldalell.user.CustomComponent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.project.eldalell.user.R;


public class SearchDialog extends AppCompatDialogFragment {

  private CustomEditText etSearch;
  private RecyclerView rvSearchResult;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.search_dialog, null);

    etSearch = view.findViewById(R.id.etSearch);
    rvSearchResult = view.findViewById(R.id.rvSearchResult);

    builder.setView(view);
    return builder.create();
  }
}
