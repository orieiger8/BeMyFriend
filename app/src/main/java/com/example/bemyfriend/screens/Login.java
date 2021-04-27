package com.example.bemyfriend.screens;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bemyfriend.R;
import com.example.bemyfriend.presenter.LoginPresenter;

public class Login extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LoginPresenter presenter;
    private EditText passwordText, emailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = findViewById(R.id.forgotpasswordbutton);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        presenter = new LoginPresenter(this);

        passwordText = findViewById(R.id.editTextpassword);
        emailText = findViewById(R.id.editTextMail);

    }

    public void login(View view) {
        presenter.login(passwordText.getText().toString(),
                emailText.getText().toString());
    }

    public void loggedIn(boolean successful) {
        if (successful) {
            // move to main page
            startActivity(new Intent(Login.this, Chats.class));
            finish();
        } else {
            Toast.makeText(Login.this, R.string.login_failed, Toast.LENGTH_LONG).show();
            passwordText.setText("");
        }

    }

    public void moveToRegister(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }

    //popup window forgot password
    public void createNewContactDialog() {
        String mail = "";
        EditText popupEditTextMail;

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_forgot_password, null);
        popupEditTextMail = contactPopupView.findViewById(R.id.editTextmailforgotpassword);
        EditText temp = findViewById(R.id.editTextMail);
        mail = temp.getText().toString();
        Button b = contactPopupView.findViewById(R.id.resetpasswordbutton);

        if (!mail.equals(""))
            popupEditTextMail.setText(mail);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String finalMail = popupEditTextMail.getText().toString().trim();
                if (finalMail.equals(""))
                    Toast.makeText(Login.this, R.string.enter_mail, Toast.LENGTH_LONG).show();
                else {
                    if (presenter.forgotPassword(finalMail))
                        dialog.dismiss();
                }
            }
        });
    }

    public void forgotPassword(View view) {
        createNewContactDialog();
    }
}
