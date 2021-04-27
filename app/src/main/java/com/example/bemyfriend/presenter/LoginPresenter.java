package com.example.bemyfriend.presenter;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.screens.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {
    private Login activity;
    private FirebaseAuth mAuth;
    private boolean ans1, ans2;
    private Repository repository;

    public LoginPresenter(Login activity) {
        this.activity = activity;
        repository = Repository.getInstance();
        mAuth= FirebaseAuth.getInstance();
    }

    public void login(String pass, String email) {
        //check if the parameters are correct
        if (!email.equals("") && !pass.equals("")) {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // notify DB
                                Repository.getInstance().LoggedIn();
                            }
                            activity.loggedIn(task.isSuccessful());
                        }
                    });
        }
    }

    public boolean forgotPassword(String mail) {
        mAuth = FirebaseAuth.getInstance();
        ans2 = false;
        if (repository.mailExist(mail)) {
            mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(activity, R.string.mail_sent, Toast.LENGTH_LONG).show();
                        ans2 = true;
                    }
                }
            });
        }
        else
            Toast.makeText(activity, R.string.mail_does_not_exist, Toast.LENGTH_LONG).show();
        return ans2;
    }
}
