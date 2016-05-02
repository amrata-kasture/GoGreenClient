package com.example.amanleenpuri.gogreen.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import adapter.ProxyUser;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GoGreenREST;
import ws.remote.GreenRESTInterface;

/**
 * Created by amanleenpuri on 4/29/16.
 */
public class SignUpActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_signup);

        Resources res = getResources();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sign_up);
        setSupportActionBar(toolbar);

        final Spinner roleTypeSp = (Spinner)findViewById(R.id.sp_roleSpinner);
        String[] roleTypeArr = res.getStringArray(R.array.roleType_array);
        ArrayAdapter<String> adapterRoleType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, roleTypeArr);
        roleTypeSp.setAdapter(adapterRoleType);

        Spinner interestAreaSp = (Spinner)findViewById(R.id.sp_interestAreaSpinner);
        String[] interestAreaArr = res.getStringArray(R.array.interestArea_array);
        ArrayAdapter<String> adapterInterestArea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, interestAreaArr);
        interestAreaSp.setAdapter(adapterInterestArea);

        final EditText firstNameEt = (EditText)findViewById(R.id.et_firstNameSignUp);
        firstNameEt.setTag("First Name");
        final EditText lastNameEt = (EditText)findViewById(R.id.et_lastNameSignUp);
        lastNameEt.setTag("Last Name");
        final EditText userNameEt = (EditText)findViewById(R.id.et_userNameSignUp);
        userNameEt.setTag("Username");
        final EditText passWordEt = (EditText)findViewById(R.id.et_passwordSignUp);
        passWordEt.setTag("Password");
        final EditText reEnterPasswordEt = (EditText)findViewById(R.id.et_reEnterPasswordSignUp);
        reEnterPasswordEt.setTag("Re-enter Password");
        final EditText cityEt = (EditText)findViewById(R.id.et_citySignUp);
        cityEt.setTag("City");
        final EditText stateEt = (EditText)findViewById(R.id.et_stateSignUp);
        stateEt.setTag("State");

        final String roleSelection = roleTypeSp.getSelectedItem().toString();
        final String interestAreaSelection = interestAreaSp.getSelectedItem().toString();



        Button signUpBtn = (Button) findViewById(R.id.btn_signUp);
        signUpBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText invalidEditText = checkIfEntered(firstNameEt, lastNameEt, userNameEt, passWordEt, reEnterPasswordEt, cityEt, stateEt);
                if (invalidEditText != null) {
//                    Toast.makeText(getApplicationContext(), invalidEditText.getTag() + " field cannot be empty.", Toast.LENGTH_LONG).show();
                    Toast toast= Toast.makeText(getApplicationContext(), invalidEditText.getTag() + " field cannot be empty.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }else if(roleSelection.isEmpty()){
                    Toast toast= Toast.makeText(getApplicationContext(), " Select a role Type!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }else if(interestAreaSelection.isEmpty()){
                    Toast toast= Toast.makeText(getApplicationContext(), " Select an area of Interest!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }else if(!(passWordEt.getText().toString()).equals(reEnterPasswordEt.getText().toString())){
                    Toast toast= Toast.makeText(getApplicationContext(), " Passwords don't match! Make sure you re-enter the same password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }else{

                    final User user = new User();
                    user.setFirstName(firstNameEt.getText().toString());
                    user.setLastName(lastNameEt.getText().toString());
                    user.setUsername(userNameEt.getText().toString());
                    user.setPassword(passWordEt.getText().toString());
                    user.setCity(cityEt.getText().toString());
                    user.setState(stateEt.getText().toString());
                    user.setRoleType(roleSelection);
                    user.setInterestArea(interestAreaSelection);

                    GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
                    Call<User> createUserCall = greenRESTInterface.createUser(user);
                    createUserCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User responseUser = response.body();
                                int userId = responseUser.getUserId();
                                ProxyUser pUser = ProxyUser.getInstance();
                                pUser.addUser(user.getUsername(), userId, getApplicationContext());
                                Intent i = new Intent(SignUpActivity.this, TimelineActivity.class);
                                startActivity(i);
                            } else {
                                Log.e("Signup", "Error in response " + response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Signup", "Failure to create user", t);
                        }
                    });

                }
            }
        });
    }

    EditText checkIfEntered(EditText... allInputFields) {
        for (EditText editText : allInputFields) {
            if (TextUtils.isEmpty(editText.getText())) {
                return editText;
            }
        }
        return null;
    }

 }