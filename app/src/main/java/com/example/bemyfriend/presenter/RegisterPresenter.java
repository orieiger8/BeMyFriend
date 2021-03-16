package com.example.bemyfriend.presenter;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.Hobbies;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class RegisterPresenter {
    private Register activity;
    private FirebaseAuth mAuth;
    private Repository repository;
    private boolean alreadyExist = false;
    private boolean allGood = true;
    private EditText emailText, passwordText, parentNameText, phoneNumberText, addressText, ageText, childNameText, detailText, otherHobby;
    private boolean male, female;
    private RadioButton maleButton, femaleButton;
    //hobbies
    private CheckBox boardGamesCheckBox, artCheckBox, gamingCheckBox, musicCheckBox, natureCheckBox, scienceCheckBox, sportCheckBox;
    private boolean boardgames, art, gaming, music, nature, science, sport;
    Random rg = new Random();
    private int toast;


    public RegisterPresenter(Register activity) {
        this.activity = activity;
        repository = Repository.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //get info
        emailText = activity.findViewById(R.id.editTextMailR);
        passwordText = activity.findViewById(R.id.editTextPasswordR);
        parentNameText = activity.findViewById(R.id.editTextName);
        phoneNumberText = activity.findViewById(R.id.editTextPhone);
        addressText = activity.findViewById(R.id.editTextCity);
        ageText = activity.findViewById(R.id.editTextAge);
        childNameText = activity.findViewById(R.id.editTextChildName);
        detailText = activity.findViewById(R.id.editTextDetails);
        maleButton = activity.findViewById(R.id.radioButtonMale);
        femaleButton = activity.findViewById(R.id.radioButtonFemale);

        //hobbies
        boardGamesCheckBox = activity.findViewById(R.id.checkBoxBoardGames);
        artCheckBox = activity.findViewById(R.id.checkBoxArt);
        gamingCheckBox = activity.findViewById(R.id.checkBoxGaming);
        musicCheckBox = activity.findViewById(R.id.checkBoxMusic);
        natureCheckBox = activity.findViewById(R.id.checkBoxNature);
        scienceCheckBox = activity.findViewById(R.id.checkBoxSience);
        sportCheckBox = activity.findViewById(R.id.checkBoxSport);
        otherHobby = activity.findViewById(R.id.editTextOtherHobby);

    }

    public void Register() {
        toast = -1;
        male = maleButton.isChecked();
        female = femaleButton.isChecked();
        boardgames = boardGamesCheckBox.isChecked();
        art = artCheckBox.isChecked();
        gaming = gamingCheckBox.isChecked();
        music = musicCheckBox.isChecked();
        nature = natureCheckBox.isChecked();
        science = scienceCheckBox.isChecked();
        sport = sportCheckBox.isChecked();

        //check the details
        if (!emailText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {
            allGood = true;
            if (allGood && alreadyExist) {
                toast = R.string.mail_already_exist;
                allGood = false;
            }
            if (allGood && passwordText.getText().toString().length() < 6) {
                toast = R.string.password_must_have_6_chars;
                allGood = false;
            }
            if (allGood && parentNameText.getText().toString().equals("")) {
                toast = R.string.enter_parent_name;
                allGood = false;
            }
            if (allGood && addressText.getText().toString().equals("")) {
                toast = R.string.enter_city;
                allGood = false;
            }
            if (allGood && childNameText.getText().toString().equals("")) {
                toast = R.string.enter_child_name;
                allGood = false;
            }
            if (allGood && ageText.getText().toString().equals("")) {
                toast = R.string.enter_age;
                allGood = false;
            }
            if (allGood && !male && !female) {
                toast = R.string.enter_gender;
                allGood = false;
            }

            //create the new user
            if (allGood) {
                //   createNewContact();
                mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //create user
                                    String gender;
                                    if (male) gender = "male";
                                    else if (female) gender = "female";
                                    else gender = "non";

                                    Hobbies hobbies = new Hobbies(boardgames, science, nature, sport,
                                            art, gaming, music, otherHobby.getText().toString());
                                    int age = Integer.parseInt(ageText.getText().toString());

                                    User user = new User(parentNameText.getText().toString(),
                                            childNameText.getText().toString(),
                                            emailText.getText().toString(),
                                            addressText.getText().toString(),
                                            age,
                                            detailText.getText().toString(),
                                            gender, "profile" + Math.abs(rg.nextInt() % 16 + 1),
                                            hobbies);

                                    repository.registerNewUserToFireBase(user);

                                    // notify DB
                                    Repository.getInstance().LoggedIn();

                                    toast = -1;
                                } else {
                                    toast = R.string.mail_or_password_wrong_or_already_taken;
                                }
                                activity.registerComplete(toast);
                            }
                        });
            } else {
                activity.registerComplete(toast);
            }
        } else {
            toast = R.string.male_or_password_empty;
            activity.registerComplete(toast);
        }
    }
}
