package com.project.eldalell.user.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.R;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    private TextView tvDateOfBirth;
    private DatePickerDialog.OnDateSetListener dateDialog;
    TextView tvLogin;
    Button btnSignUp;
    EditText etFirstNameReg, etLastNameReg, etEmailReg, etPasswordReg, etConfirmPasswordReg, etPhoneReg;
    RequestQueue requestQueue;
    RadioGroup rvGroup;
    RadioButton radioButton;
    public static final String MY_PREFS_NAME = "tokenPref";
    String gend = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_register, container, false);

        tvDateOfBirth = v.findViewById(R.id.tvDateOfBirth);
        tvLogin = v.findViewById(R.id.tvLogin);
        btnSignUp = v.findViewById(R.id.btnSignUp);
        etFirstNameReg = v.findViewById(R.id.etFirstNameReg);
        etLastNameReg = v.findViewById(R.id.etLastNameReg);
        etEmailReg = v.findViewById(R.id.etEmailReg);
        etPasswordReg = v.findViewById(R.id.etPasswordReg);
        etConfirmPasswordReg = v.findViewById(R.id.etConfirmPasswordReg);
        etPhoneReg = v.findViewById(R.id.etPhoneReg);
        rvGroup = v.findViewById(R.id.rvGroup);



        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        tvDateOfBirth.setOnClickListener(new View.OnClickListener() {
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
                tvDateOfBirth.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.registerFragment, true);
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int SelectedID = rvGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(SelectedID);
                try {
                    gend = radioButton.getText().toString().trim();
                } catch (Exception e) {

                }
                final String first_name = etFirstNameReg.getText().toString().trim();
                final String last_name = etLastNameReg.getText().toString().trim();
                final String email = etEmailReg.getText().toString().trim();
                final String phone_number = etPhoneReg.getText().toString().trim();
                final String birthday = tvDateOfBirth.getText().toString().trim();
                final String gender = gend;
                final String password = etPasswordReg.getText().toString().trim();
                final String password_confirmation = etConfirmPasswordReg.getText().toString().trim();
                final View viw = view;
                Connection connection = new Connection();

                StringRequest request = new StringRequest(Request.Method.POST, connection.getSignUpUrl(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject Success = object.getJSONObject("success");
                            String token = Success.getString("token");
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("token", token);
                            editor.apply();
                            MainActivity.user = MainActivity.getUserAPI(token, getContext(), requestQueue, MainActivity.nvView, MainActivity.tvHeaderUserName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Navigation.findNavController(viw).popBackStack(R.id.registerFragment, true);
                        Navigation.findNavController(viw).navigate(R.id.orderSetupFragment);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "NoConnectionError", Toast.LENGTH_SHORT).show();
                        } else if (networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            if (networkResponse.statusCode == 500) {
                                Toast.makeText(getContext(), "Email or phone already exist", Toast.LENGTH_LONG).show();
                            } else if (networkResponse.statusCode == 401) {
                                Toast.makeText(getContext(), "Please Make Sure you fill all data Correctly", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> parameter = new HashMap<String, String>();

                        parameter.put("first_name", first_name);
                        parameter.put("last_name", last_name);
                        parameter.put("email", email);
                        parameter.put("phone_number", phone_number);
                        parameter.put("birthday", birthday);
                        parameter.put("gender", gend);
                        parameter.put("password", password);
                        parameter.put("password_confirmation", password_confirmation);

                        return parameter;
                    }
                };
                request.setShouldCache(false);
                requestQueue.add(request);

            }
        });


        return v;
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}