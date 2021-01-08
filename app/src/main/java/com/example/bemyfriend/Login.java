package com.example.bemyfriend;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private String mail="";
    private EditText editTextMail;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.forgotpasswordbutton);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        db= DB.getInstance();
    }

    public void login(View view) {
        EditText passwordText= findViewById(R.id.editTextpassword);
        EditText emailText= findViewById(R.id.editTextMail);

        //check if the parameters are correct
        if (!emailText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {
            mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // notify DB
                                DB.getInstance().LoggedIn();

                                // move to main page
                                startActivity(new Intent(Login.this, Chats.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else
            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();

    }

    public void moveToRegister(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }

    //popup window forgot password
    public void createNewContactDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_forgot_password, null);
        editTextMail = (EditText) contactPopupView.findViewById(R.id.editTextmailforgotpassword);
        EditText temp= findViewById(R.id.editTextMail);
        mail=temp.getText().toString();
        Button b = contactPopupView.findViewById(R.id.resetpasswordbutton);

        if (!mail.equals(""))
            editTextMail.setText(mail);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mail = editTextMail.getText().toString().trim();
                if (mail.equals(""))
                    Toast.makeText(Login.this, "הכנס מייל", Toast.LENGTH_LONG).show();
                else {
                    if (db.mailExist(mail)) {
                        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(Login.this, "מייל לאיפוס סיסמה נשלח", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                    else
                        Toast.makeText(Login.this, "מייל זה אינו קיים במערכת", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void forgotPassword(View view) {
        createNewContactDialog();
    }
}
