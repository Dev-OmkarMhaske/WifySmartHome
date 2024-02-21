package com.wify.smart.home.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

public class WiFiSettingFragment extends Fragment {

    View view;

    Context mCtx;

    Button save_miniserver_setting;

    EditText networkNameEditText, passwordEditText, ipAddress_txt, gateway_txt;

    LinearLayout error_msg_layout, networKLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.miniserver_wifi_setting_fragment, null);

            mCtx = view.getContext();

            save_miniserver_setting = view.findViewById(R.id.save_miniserver_setting);

            networkNameEditText = view.findViewById(R.id.networkNameEditText);

            passwordEditText = view.findViewById(R.id.passwordEditText);

            ipAddress_txt = view.findViewById(R.id.ipAddress_txt);

            gateway_txt = view.findViewById(R.id.gateway_txt);

            error_msg_layout = view.findViewById(R.id.error_msg_layout);

            networKLayout = view.findViewById(R.id.networKLayout);

            save_miniserver_setting.setVisibility(View.GONE);

            if (Utility.COMMUNICATION_MODE == 3) {

                //getCurrentWifiSetting();

                networkNameEditText.setText(Utility.STASSID);

                passwordEditText.setText(Utility.STAPWD);

                gateway_txt.setText(Utility.GATEWAY);

                ipAddress_txt.setText(Utility.IP);

                save_miniserver_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (networkNameEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0) {

                            ConfirmSaveSetting();

                        } else {

                            Toast.makeText(getContext(), R.string.validate_setting_txt, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {

                error_msg_layout.setVisibility(View.VISIBLE);

                networKLayout.setVisibility(View.GONE);

            }

            networkNameEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    save_miniserver_setting.setVisibility(View.VISIBLE);
                }
            });

            passwordEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    save_miniserver_setting.setVisibility(View.VISIBLE);
                }
            });

            gateway_txt.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    save_miniserver_setting.setVisibility(View.VISIBLE);
                }
            });

            ipAddress_txt.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    save_miniserver_setting.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }

    public void setWifiCredentials(String STASSID, String STAPWD, String GATEWAY, String IP) {

        if (STASSID.trim().length() == 0 || STAPWD.trim().length() == 0 || GATEWAY.trim().length() == 0 || IP.trim().length() == 0) {

            Toast.makeText(mCtx, R.string.validate_setting_txt, Toast.LENGTH_SHORT).show();

            return;
        }

        MqttOperation.preferenceValueAction(UtilityConstants.WRITE, UtilityConstants.WiFi_STR, new StringBuilder(STASSID).append("#").append(STAPWD).append("#").append(IP).append("#").append(GATEWAY).toString());

        for (AccessoriesObject accessoriesObject : Utility.AccessoriesMap.values()) {

            if (accessoriesObject.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY)) {

                accessoriesObject.setIp(IP);

                MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ACCESSORY, UtilityConstants.ACCESSORY, new Gson().toJson(Utility.AccessoriesMap));

            }
        }

    }

    public void ConfirmSaveSetting() {

        final EditText input = new EditText(view.getContext());

        input.setHint(getString(R.string.confirm_password_txt));

        input.setHintTextColor(view.getContext().getResources().getColor(R.color.black_transparent));

        input.setTextColor(view.getContext().getResources().getColor(R.color.text_white));

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogCustom);

        builder.setMessage(getString(R.string.confirm_save_setting))
                .setTitle(getString(R.string.save_setting_str))
                .setView(input)
                .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                            save_miniserver_setting.setVisibility(View.GONE);

                            setWifiCredentials(networkNameEditText.getText().toString(), passwordEditText.getText().toString(), gateway_txt.getText().toString(), ipAddress_txt.getText().toString());

                            SharedPreference.setIP(mCtx, ipAddress_txt.getText().toString());

                            Toast.makeText(view.getContext(), getString(R.string.save_setting_str), Toast.LENGTH_SHORT).show();

                            getActivity().onBackPressed();

                        } else {

                            Toast.makeText(view.getContext(), R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                });

        builder.create();

        builder.show();
    }

}

