package com.project.eldalell.user.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.Classes.User;
import com.project.eldalell.user.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AccountInfoFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public AccountInfoFragment() {
  }

  public static AccountInfoFragment newInstance(String param1, String param2) {
    AccountInfoFragment fragment = new AccountInfoFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  TextView tvTitle;
  private TextView tvDateOfBirthInfo;
  private DatePickerDialog.OnDateSetListener dateDialog;
  private EditText etFirstNameInfo, etLastNameInfo, etEmailInfo, etPasswordInfo, etPhoneInfo;
  private RadioButton rdFemaleInfo, rdMaleInfo, radioButton;
  private Button btnEditInfo;
  RequestQueue requestQueue;
  RadioGroup rvGroupInfo;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View v = inflater.inflate(R.layout.fragment_account_info, container, false);
    tvTitle = getActivity().findViewById(R.id.tvTitleMain);
    tvDateOfBirthInfo = v.findViewById(R.id.tvDateOfBirthInfo);
    etFirstNameInfo = v.findViewById(R.id.etFirstNameInfo);
    etLastNameInfo = v.findViewById(R.id.etLastNameInfo);
    etEmailInfo = v.findViewById(R.id.etEmailInfo);
    etPasswordInfo = v.findViewById(R.id.etPasswordInfo);
    etPhoneInfo = v.findViewById(R.id.etPhoneInfo);
    rdFemaleInfo = v.findViewById(R.id.rdFemaleInfo);
    rdMaleInfo = v.findViewById(R.id.rdMaleInfo);
    btnEditInfo = v.findViewById(R.id.btnEditInfo);
    rvGroupInfo = v.findViewById(R.id.rvGroupInfo);
    requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

    if (MainActivity.user != null) {

      etFirstNameInfo.setText(MainActivity.user.getFirst_name());
      etLastNameInfo.setText(MainActivity.user.getLast_name());
      etEmailInfo.setText(MainActivity.user.getEmail());
      etPhoneInfo.setText(MainActivity.user.getPhone_number());

      if (MainActivity.user.getGender().equalsIgnoreCase("male")) {
        rdMaleInfo.setChecked(true);
      } else if (MainActivity.user.getGender().equalsIgnoreCase("Female")) {
        rdFemaleInfo.setChecked(true);
      }
      tvDateOfBirthInfo.setText(MainActivity.user.getBirthday());

    }
    etEmailInfo.setKeyListener(null);
    etPhoneInfo.setKeyListener(null);

    tvDateOfBirthInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateDialog, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
      }
    });
    dateDialog = new DatePickerDialog.OnDateSetListener() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        tvDateOfBirthInfo.setText(dayOfMonth + "/" + month + "/" + year);
      }
    };
    tvTitle.setText("Account Info");

    btnEditInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (MainActivity.user != null) {
          if ((!etFirstNameInfo.getText().toString().isEmpty() &&
                  !etLastNameInfo.getText().toString().isEmpty() &&
                  !etPhoneInfo.getText().toString().isEmpty() &&
                  !tvDateOfBirthInfo.getText().toString().isEmpty()) &&
                  (etPasswordInfo.getText().toString().length() >= 6 || etPasswordInfo.getText().toString().isEmpty())) {

            editAccount(requestQueue, getActivity(), v);
          } else {
            if (etPasswordInfo.getText().toString().length() < 6 && !etPasswordInfo.getText().toString().isEmpty()) {
              Toast.makeText(getActivity(), "Password Should be More than 6 char or number", Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(getActivity(), "Please Fill All Data", Toast.LENGTH_SHORT).show();
            }
          }
        } else {
          Toast.makeText(getActivity(), "Please Check if you are Login First", Toast.LENGTH_SHORT).show();
          etPhoneInfo.setText("");
          etFirstNameInfo.setText("");
          etLastNameInfo.setText("");
          etEmailInfo.setText("");
          etPasswordInfo.setText("");
          tvDateOfBirthInfo.setText("DD-MM-YYYY");
        }

      }
    });

    return v;
  }

  private void editAccount(RequestQueue requestQueue, final Context activity, View v) {
    final String firstName = etFirstNameInfo.getText().toString();
    final String lastName = etLastNameInfo.getText().toString();
    final String email = etEmailInfo.getText().toString();
    final String password = etPasswordInfo.getText().toString();
    final String phone = etPhoneInfo.getText().toString();
    final String birthday = tvDateOfBirthInfo.getText().toString();
    String gend = "";
    int SelectedID = rvGroupInfo.getCheckedRadioButtonId();
    radioButton = v.findViewById(SelectedID);
    try {
      gend = radioButton.getText().toString().trim();
    } catch (Exception e) {
    }
    final String gender = gend;
    final String userId = MainActivity.user.getId();
    Connection connection = new Connection();

    StringRequest request = new StringRequest(Request.Method.POST, connection.getEditUser() + userId,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  JSONObject jsonObject = new JSONObject(response);
                  User user = new User();
                  user.setId(jsonObject.getString("id"));
                  user.setFirst_name(jsonObject.getString("first_name"));
                  user.setLast_name(jsonObject.getString("last_name"));
                  user.setEmail(jsonObject.getString("email"));
                  user.setPhone_number(jsonObject.getString("phone_number"));
                  user.setBirthday(jsonObject.getString("birthday"));
                  user.setGender(jsonObject.getString("gender"));
                  user.setBlock(jsonObject.getString("block"));
                  MainActivity.user = user;
                  MainActivity.changeInfo();
                  Toast.makeText(activity, "Edit Done", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String, String> param = new HashMap<>();
        param.put("first_name", firstName);
        param.put("last_name", lastName);
        param.put("email", email);
        param.put("phone_number", phone);
        param.put("birthday", birthday);
        param.put("gender", gender);
        if (password.isEmpty()) {
          return param;
        } else {
          param.put("password", password);
        }
        return param;
      }
    };
    requestQueue.add(request);

  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }
}
