package com.project.eldalell.user.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.eldalell.user.Activity.MainActivity;
import com.project.eldalell.user.Classes.Connection;
import com.project.eldalell.user.R;

import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    TextView tvRegisterNav;
    Button btnLogin;
    EditText etEmailLogin, etPasswordLogin;
    RequestQueue requestQueue;
    public static final String MY_PREFS_NAME = "tokenPref";
    ProgressBar loginFragmentProgress;
    LinearLayout loginFragmentLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        tvRegisterNav = v.findViewById(R.id.tvRegisterNav);
        btnLogin = v.findViewById(R.id.btnLogin);
        etEmailLogin = v.findViewById(R.id.etEmailLogin);
        etPasswordLogin = v.findViewById(R.id.etPasswordLogin);
        loginFragmentLayout = v.findViewById(R.id.loginFragmentLayout);
        loginFragmentProgress = v.findViewById(R.id.loginFragmentProgress);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                final String email = etEmailLogin.getText().toString().trim();
                final String password = etPasswordLogin.getText().toString().trim();

                loginFragmentProgress.setVisibility(View.VISIBLE);
                loginFragmentLayout.setVisibility(View.GONE);
                if ((!email.isEmpty() && !password.isEmpty())) {
                Connection connection = new Connection();
                StringRequest request = new StringRequest(Request.Method.POST, connection.getLoginUrl(), new Response.Listener<String>() {
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
                        loginFragmentProgress.setVisibility(View.GONE);
                        loginFragmentLayout.setVisibility(View.VISIBLE);

                        Navigation.findNavController(view).popBackStack(R.id.loginFragment, true);
                        Navigation.findNavController(view).navigate(R.id.orderSetupFragment);
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
                                Toast.makeText(getContext(), "Email or password is Wrong", Toast.LENGTH_LONG).show();
                            }
                        }

                        loginFragmentProgress.setVisibility(View.GONE);
                        loginFragmentLayout.setVisibility(View.VISIBLE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> parameter = new HashMap<String, String>();

                        parameter.put("email", email);
                        parameter.put("password", password);

                        return parameter;
                    }
                };
                request.setShouldCache(false);
                requestQueue.add(request);
                } else {
                    Toast.makeText(getContext(), "Please Fill all data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegisterNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.loginFragment, true);
                Navigation.findNavController(v).navigate(R.id.registerFragment);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
