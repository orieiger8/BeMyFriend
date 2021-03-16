package com.example.bemyfriend.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bemyfriend.R;
import com.example.bemyfriend.presenter.RegisterPresenter;

public class Register extends AppCompatActivity {

    //private AlertDialog.Builder dialogBuilder;
    //private AlertDialog dialog;
    RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter= new RegisterPresenter(this);
    }

    public void register(View view) {
        presenter.Register();
    }
    public void registerComplete(int toast){
        if(toast==-1){
            // move to main page
            startActivity(new Intent(Register.this, FindNewFriends.class));
            finish();
        }
        else
            Toast.makeText(Register.this, toast, Toast.LENGTH_LONG).show();
    }


    //verify phone number with sms (not working)
    /*
    public void createNewContact() {
        // Get SMS permission and send it
        GetSMSPermission();
    }
    public void endRegistrationProcess() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_verify_phone_number,
                null);

        // get view vars
        EditText number = contactPopupView.findViewById(R.id.editTextNumber);
        Button check = contactPopupView.findViewById(R.id.button);
        TextView phoneNumber = contactPopupView.findViewById(R.id.textView11);
        phoneNumber.setText("למספר: " + phoneNumberText.getText().toString());


        // send SMS
        boolean verified = verifyPhoneNumber();

        if (verified) {
            // register the motherfucker
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verifyPhone == Integer.parseInt(number.getText().toString())) {
                        String gender;
                        if (male) gender = "male";
                        else if (female) gender = "female";
                        else gender = "non";

                        Hobbies h = new Hobbies(boardgames, science, nature, sport, art, gaming, music,
                                otherHobby.getText().toString());
                        int n = Integer.parseInt(ageText.getText().toString());

                        User u = new User(parentNameText.getText().toString(),
                                childNameText.getText().toString(),
                                emailText.getText().toString(),
                                addressText.getText().toString(), n,
                                detailText.getText().toString(),
                                gender, "profile" + Math.abs(rg.nextInt() % 16 + 1), h);

                        db.registerNewUserToFireBase(u);

                        // notify DB
                        DB.getInstance().LoggedIn();

                        // move to main page
                        startActivity(new Intent(Register.this, FindNewFriends.class));
                        finish();

                    } else
                        Toast.makeText(Register.this, "הקוד שהוכנס שגוי, נסה שוב", Toast.LENGTH_LONG).show();
                }
            });

            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.show();
        }
        else {
            Toast.makeText(this, "WRONG CODE BITCH!!!", Toast.LENGTH_LONG);
        }
    }

    private boolean verifyPhoneNumber() {
        // generate random code
        for (int i = 0; i < 4; i++) {
            verifyPhone += rg.nextInt(10) * Math.pow(10, i);
        }

        // send SMS
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumberText.getText().toString(), null,
                "hello javatpoint", null, null);

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void GetSMSPermission() {
        String permission = Manifest.permission.SEND_SMS;
        int REQUEST_CODE = 1;

        int permissionGranted = ContextCompat.checkSelfPermission(this, permission);
        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                // show educational dialog explaining why we need the permission

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Directly ask for the permission.
                        requestPermissions(new String[] { permission }, REQUEST_CODE);
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
            }
            else {
                // Directly ask for the permission.
                requestPermissions(new String[] { permission }, REQUEST_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, end registration process
                    endRegistrationProcess();
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Fuck you MR. User, I need your permission" +
                            "you selfish son of a bitch", Toast.LENGTH_LONG);
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

     */
}

