package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    DataSnapshot User;
    User userVal;

    String parentNameText, cities, childNameText, moreD, mails, passwords;
    int ages;

    //
    EditText parentName;
    EditText city;
    EditText childName;
    EditText age;
    RadioButton male;
    RadioButton female;
    EditText moreDetails;
    RadioGroup mOption;
    EditText otherHobby;
    //

    //תחביבים
    CheckBox boardGamesCheckBox, artCheckBox, gamingCheckBox, musicCheckBox, natureCheckBox, scienceCheckBox, sportCheckBox;
    boolean boardGames, art, gaming, music, nature, science, sport;
    String otherHobbyText;
    //

    //
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button[] buttonPics = new Button[16];
    private Button picButton;
    //

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("עריכת פרופיל");

        // get layout components
        parentName = findViewById(R.id.editTextEditName);
        city = findViewById(R.id.editTextEditCity);
        childName = findViewById(R.id.editTextEditChildName);
        age = findViewById(R.id.editTextEditAge);
        male = findViewById(R.id.editUser_radioButtonEditMale);
        female = findViewById(R.id.editUser_radioButtonEditMale);
        moreDetails = findViewById(R.id.editTextEditDetails);
        picButton = (Button) findViewById(R.id.picbutton);
        mOption = (RadioGroup) findViewById(R.id.radioEditGroup);
        otherHobby = findViewById(R.id.editTextEditOtherHobby);

        //
        boardGamesCheckBox = findViewById(R.id.checkBoxEditBoardGames);
        artCheckBox = findViewById(R.id.checkBoxEditArt);
        gamingCheckBox = findViewById(R.id.checkBoxEditGaming);
        musicCheckBox = findViewById(R.id.checkBoxEditMusic);
        natureCheckBox = findViewById(R.id.checkBoxEditNature);
        scienceCheckBox = findViewById(R.id.checkBoxEditSience);
        sportCheckBox = findViewById(R.id.checkBoxEditSport);
        //

        // get user from firebase
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference myRef= database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    User val = currentUser.getValue(User.class);
                    String currentUserMail = mAuth.getCurrentUser().getEmail();
                    String mailLoop = val.getMail();

                    if (currentUserMail.equals(mailLoop)) {
                        User = currentUser;
                        userVal = val;
                        RenderUserInfoToView(val);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void RenderUserInfoToView(User val) {
        //take information from firebase
        parentNameText = val.getParentName();
        cities = val.getAddress();
        childNameText = val.getChildName();
        moreD = val.getDetails();
        ages = val.getAgeInt();
        mails = val.getMail();
        otherHobbyText = val.getHobby().getOther();
        boardGames = val.getHobby().getBoardGames();
        art = val.getHobby().getArt();
        gaming= val.getHobby().getGaming();
        nature= val.getHobby().getNature();
        science = val.getHobby().getScience();
        music= val.getHobby().getMusic();
        sport= val.getHobby().getSports();


        //put the information in the activity
        parentName.setText(val.getParentName());
        city.setText(val.getAddress());
        childName.setText(val.getChildName());
        age.setText(val.getAgeString());
        moreDetails.setText(val.getDetails());
        otherHobby.setText(val.getHobby().getOther());
        if (val.getGender().equals("male"))
            mOption.check(R.id.editUser_radioButtonEditMale);
        else if (val.getGender().equals("female"))
            mOption.check(R.id.editUser_radioButtonEditFemale);
        picButton.setBackgroundResource(parentName.getResources().getIdentifier(userVal.getPicId(),
                "drawable", parentName.getContext().getPackageName()));
        if (boardGames)
            boardGamesCheckBox.setChecked(true);
        if (art)
            artCheckBox.setChecked(true);
        if (gaming)
            gamingCheckBox.setChecked(true);
        if (nature)
            natureCheckBox.setChecked(true);
        if (science)
            scienceCheckBox.setChecked(true);
        if (music)
            musicCheckBox.setChecked(true);
        if (sport)
            sportCheckBox.setChecked(true);
    }
    public void saveChanges(View view) {

        // update all values
        String pName = this.parentName.getText().toString(),
                cName = this.childName.getText().toString(),
                address = this.city.getText().toString(),
                age = this.age.getText().toString(),
                details = this.moreDetails.getText().toString(),
                other = this.otherHobby.getText().toString();
        Boolean isMale = ((RadioButton) findViewById(R.id.editUser_radioButtonEditMale)).isChecked(),
                isFemale = ((RadioButton) findViewById(R.id.editUser_radioButtonEditFemale)).isChecked();
        boolean pBoardgames= (this.boardGamesCheckBox).isChecked(),
                pArt = (this.artCheckBox).isChecked(),
                pGaming = (this.gamingCheckBox).isChecked(),
                pNature = (this.natureCheckBox).isChecked(),
                pScience = (this.scienceCheckBox).isChecked(),
                pMusic = (this.musicCheckBox).isChecked(),
                pSport = (this.sportCheckBox).isChecked();


        if (!pName.isEmpty())
            userVal.setParentName(pName);
        if (!cName.isEmpty())
            userVal.setChildName(cName);
        if (!address.isEmpty())
            userVal.setAddress(address);
        if (!age.isEmpty())
            userVal.setAge(Integer.parseInt(age));
        if (!details.isEmpty())
            userVal.setDetails(details);

        if (isMale) userVal.setGender("male");
        else if (isFemale) userVal.setGender("female");
        else userVal.setGender("non");

        Hobbies h = new Hobbies(pBoardgames,pScience,pNature,pSport,pArt,pGaming,pMusic,other);
        userVal.setHobby(h);

        // save to db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users/" + User.getKey());
        userRef.setValue(userVal);

        // end activity
        startActivity(new Intent(EditProfile.this, MyProfile.class));
        finish();
    }

    public void cancelChanges(View view) {
        startActivity(new Intent(EditProfile.this, MyProfile.class));
        finish();
    }
    //popup window
    public void createNewContactDialog1() {
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
                    setPicture(picNum, dialog);
                }
            });
        }

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void setPicture(int i, AlertDialog dlg) {
        userVal.setPicId("profile" + i);
        picButton.setBackgroundResource(parentName.getResources().getIdentifier(userVal.getPicId(),
                "drawable", parentName.getContext().getPackageName()));
        dlg.dismiss();
    }

    public void openPicChoosing(View view) {
        createNewContactDialog1();
    }
}