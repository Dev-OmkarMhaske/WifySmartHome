package com.wify.smart.home.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.DBObject;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ValidateOTPActivity extends AppCompatActivity {

    private EditText edtOTP;

    private Button verifyOTPBtn;

    private String verificationId, phoneNumber, username;

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            try {

                super.onCodeSent(s, forceResendingToken);

                verificationId = s;

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            try {

                final String code = phoneAuthCredential.getSmsCode();

                if (code != null) {

                    edtOTP.setText(code);

                    verifyCode(code);

                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.variefy_otp_activity);

            edtOTP = findViewById(R.id.idEdtOtp);

            verifyOTPBtn = findViewById(R.id.verifyOTPBtn);

            username = getIntent().getStringExtra(UtilityConstants.USER_NAME_STR);

            phoneNumber = getIntent().getStringExtra(UtilityConstants.USER_NUMBER_STR);

            sendVerificationCode(UtilityConstants.COUNTRY_CODE, phoneNumber);

            verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(edtOTP.getText().toString())) {

                        Toast.makeText(getApplicationContext(), R.string.enter_otp, Toast.LENGTH_SHORT).show();

                    } else {

                        verifyCode(edtOTP.getText().toString());

                    }
                }

            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // Phone number authentication methods - start
    private void signInWithCredential(PhoneAuthCredential credential) {

        try {

            Utility.firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                DBObject dbObject = new DBObject();

                                dbObject.setName(username);

                                SharedPreference.setPhone_number(getApplicationContext(), phoneNumber);

                                startActivity(new Intent(getApplicationContext(), OtpVerifiedSuccessActivity.class));

                                setDBData();

                            } else {

                                Toast.makeText(LoginActivity.loginActivity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }

                    });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void sendVerificationCode(String countryCode, String number) {

        try {

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(Utility.firebaseAuth)
                            .setPhoneNumber(countryCode + number)            // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) //
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                            .build();

            PhoneAuthProvider.verifyPhoneNumber(options);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void verifyCode(String code) {

        try {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            signInWithCredential(credential);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void setDBData() {

        try {

            String strDate = Utility.getDate_inDDMMYY();

            Utility.myRef_db.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Utility.dbObject = dataSnapshot.getValue(DBObject.class);

                    } else {

                        Utility.dbObject = new DBObject();

                        Utility.dbObject.setName(username);

                        Utility.dbObject.setDate(strDate);

                        Utility.dbObject.setHost(new ArrayList<>());

                        Utility.dbObject.setShared(new ArrayList<>());

                        Utility.myRef_db.child(phoneNumber).setValue(Utility.dbObject);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
