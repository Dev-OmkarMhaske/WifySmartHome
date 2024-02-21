package com.wify.smart.home.room;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddRoomActivity extends AppCompatActivity {

    TextView cancel, save, room_name;

    RoomObject current_roomObject = null;

    Button remove_room, choose_photo, remove_photo;

    boolean isAccessoryExist = false;

    private static final int SELECT_PICTURE = 1;

    private ImageView imageView;

    public boolean removeFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_room_activity);

            removeFlag = false;

            current_roomObject = (RoomObject) getIntent().getSerializableExtra(UtilityConstants.ROOM_OBJ);

            String action = getIntent().getStringExtra(UtilityConstants.ACTION);

            cancel = findViewById(R.id.cancel_text);

            room_name = findViewById(R.id.add_room_textview);

            choose_photo = findViewById(R.id.choose_photo);

            remove_room = findViewById(R.id.remove_room);

            remove_photo = findViewById(R.id.remove_photo);

            if (current_roomObject != null) {

                room_name.setText(current_roomObject.getName());
            }

            save = findViewById(R.id.save_room);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();
                }
            });

            imageView = (ImageView) this.findViewById(R.id.imageView1);

            if (!Utility.isHost) {

                remove_room.setVisibility(View.GONE);

                room_name.setRawInputType(InputType.TYPE_NULL);
            }

            if (current_roomObject != null && current_roomObject.getFile() != null && Utility.RoomImages.containsKey(current_roomObject.getFile())) {

                imageView.setVisibility(View.VISIBLE);

                remove_photo.setVisibility(View.VISIBLE);

                imageView.setImageBitmap(Utility.ConvertBase64TOBitmap(Utility.RoomImages.get(current_roomObject.getFile())));
            }

            choose_photo.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {

                    Intent intent = new Intent();

                    intent.setType("image/*");

                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,

                            "Select Picture"), SELECT_PICTURE);
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (room_name.getText().toString().length() > 0) {

                        if (!isRoomNameUnique(room_name.getText().toString()) && action != null && action.equalsIgnoreCase(UtilityConstants.ADD)) {

                            Toast.makeText(AddRoomActivity.this, R.string.room_exist_err, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (current_roomObject.getFile().length() == 0) {

                            current_roomObject.setFile(UtilityConstants.ROOM + System.currentTimeMillis());

                        }

                        setImage();

                        current_roomObject.setName(room_name.getText().toString());

                        current_roomObject.setLast("" + System.currentTimeMillis());

                        Utility.ROOMMap.put(current_roomObject.getFile(), current_roomObject);

                        if (Utility.isHost) {
                            MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ROOM, current_roomObject.getFile(), new Gson().toJson(current_roomObject));
                        }

                        ConcurrentHashMap<String, RoomObject> roomObjectHashMap = SharedPreference.getRooms(getApplicationContext());

                        roomObjectHashMap.put(current_roomObject.getFile(), current_roomObject);

                        SharedPreference.setRooms(getApplicationContext(), roomObjectHashMap);

                        startActivity(new Intent(getApplicationContext(), RoomSettingActivity.class));

                    } else {

                        Toast.makeText(AddRoomActivity.this, R.string.valid_room_name_err, Toast.LENGTH_SHORT).show();

                    }

                }
            });

            if (current_roomObject != null && current_roomObject.getMac() != null && current_roomObject.getMac().length() > 0) {

                for (String s : current_roomObject.getMac().split(",")) {

                    for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                        if (current_roomObject.getMac().contains(genericObject.getMac())) {

                            isAccessoryExist = true;

                            break;
                        }
                    }

                    if (Utility.curtainObjectHashMap.containsKey(s) || Utility.rgbObjectHashMap.containsKey(s) || Utility.powerObjectHashMap.containsKey(s) || Utility.motionObjectHashMap.containsKey(s) || Utility.fanObjectHashMap.containsKey(s)) {

                        isAccessoryExist = true;
                    }
                }

            }

            if (Utility.isHost && !isAccessoryExist) {

                remove_room.setVisibility(View.VISIBLE);

            }

            remove_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        final EditText input = new EditText(AddRoomActivity.this);

                        input.setHint(getString(R.string.confirm_password_txt));

                        input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

                        input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this, R.style.AlertDialogCustom);

                        builder.setMessage(getString(R.string.confirm_change_wallpaper))
                                .setTitle(getString(R.string.remove_wallpaper_txt))
                                .setView(input)
                                .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {

                                        if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                            imageView.setVisibility(View.GONE);

                                            remove_photo.setVisibility(View.GONE);

                                            removeFlag = true;

                                            if (current_roomObject != null && current_roomObject.getFile() != null && Utility.RoomImages.containsKey(current_roomObject.getFile())) {

                                                Utility.RoomImages.remove(current_roomObject.getFile());

                                                SharedPreference.setRoomImages(getApplicationContext(), Utility.RoomImages);

                                            }

                                        } else {

                                            Toast.makeText(AddRoomActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });

            remove_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final EditText input = new EditText(AddRoomActivity.this);
                    input.setHint(getString(R.string.confirm_password_txt));
                    input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));
                    input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this, R.style.AlertDialogCustom);
                    builder.setMessage(getString(R.string.confirm_delete_room))
                            .setTitle(getString(R.string.delete_room_txt))
                            .setView(input)
                            .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                        if (current_roomObject.getFile().length() > 0) {

                                            MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.ROOM, current_roomObject.getFile(), "");

                                            ConcurrentHashMap<String, RoomObject> roomObjectHashMap = SharedPreference.getRooms(getApplicationContext());

                                            RoomObject roomObject = null;

                                            for (Map.Entry<String, RoomObject> entry : roomObjectHashMap.entrySet()) {

                                                roomObject = entry.getValue();

                                                if (roomObject != null && roomObject.getFile() != null && current_roomObject.getFile() != null && roomObject.getFile().equalsIgnoreCase(current_roomObject.getFile())) {

                                                    roomObjectHashMap.remove(roomObject.getFile());

                                                }
                                            }

                                            if (Utility.RoomImages.containsKey(current_roomObject.getFile())) {

                                                Utility.RoomImages.remove(current_roomObject.getFile());

                                                SharedPreference.setRoomImages(getApplicationContext(), Utility.RoomImages);
                                            }

                                            SharedPreference.setRooms(getApplicationContext(), roomObjectHashMap);

                                            Toast.makeText(AddRoomActivity.this, "Remove room", Toast.LENGTH_SHORT).show();

                                            finish();

                                            startActivity(new Intent(getApplicationContext(), RoomSettingActivity.class));
                                        }
                                    } else {

                                        Toast.makeText(AddRoomActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == SELECT_PICTURE) {

                    Uri selectedImageUri = data.getData();

                    imageView.setImageURI(selectedImageUri);

                    imageView.setVisibility(View.VISIBLE);

                    remove_photo.setVisibility(View.VISIBLE);

                    removeFlag = false;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public boolean isRoomNameUnique(String roomName) {

        try {
            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                if (roomName.length() > 0 && roomObject.getName().trim().equalsIgnoreCase(roomName.trim())) {

                    return false;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return true;
    }

    public void setImage() {

        Bitmap bmap = null;

        String imageData = null;

        try {

            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            imageView.buildDrawingCache();

            bmap = imageView.getDrawingCache();

            if (bmap != null && !removeFlag) {

                imageData = Utility.getEncoded64ImageStringFromBitmap(bmap);

                Utility.RoomImages.put(current_roomObject.getFile(), imageData);

                SharedPreference.setRoomImages(getApplicationContext(), Utility.RoomImages);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}