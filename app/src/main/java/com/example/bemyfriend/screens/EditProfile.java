package com.example.bemyfriend.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bemyfriend.R;
import com.example.bemyfriend.presenter.EditProfilePresenter;

public class EditProfile extends AppCompatActivity {

    //
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button[] buttonPics = new Button[16];
    //

    private Toolbar toolbar;
    private EditProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.edit_profile);

        presenter = new EditProfilePresenter(this);

    }

    public void saveChanges(View view) {
        presenter.saveChanges();
        // end activity
        startActivity(new Intent(EditProfile.this, MyProfile.class));
        finish();
    }

    public void cancelChanges(View view) {
        startActivity(new Intent(EditProfile.this, MyProfile.class));
        finish();
    }

    //popup window
    public void createNewContactDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_choose_pic, null);

        //find pic button
        for (int i = 0; i < buttonPics.length; i++) {
            final int picNum = i + 1;
            buttonPics[i] = (Button) contactPopupView.findViewById(getResources().getIdentifier("pic" + picNum, "id", getPackageName()));

            //onclick
            buttonPics[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.setPicture(picNum, dialog);
                }
            });
        }

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void openPicChoosing(View view) {
        createNewContactDialog();
    }
}