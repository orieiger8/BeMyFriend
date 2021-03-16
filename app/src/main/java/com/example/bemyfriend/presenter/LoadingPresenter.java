package com.example.bemyfriend.presenter;

import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.screens.Loading;
import com.example.bemyfriend.utils.Observer;
import com.google.firebase.auth.FirebaseAuth;

public class LoadingPresenter implements Observer {
    private Loading activity;
    private boolean isLoggedIn;

    public LoadingPresenter(Loading activity) {
        this.activity = activity;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        isLoggedIn = mAuth.getCurrentUser() != null;

        //connect to database with DB
        Repository.InitializeConnection();
        Repository.attach(this);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void update() {
        activity.updateProgressBar(100);
    }
}
