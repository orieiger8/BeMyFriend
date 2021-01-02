package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Random rg = new Random();
    private boolean alreadyExist = false;
    private boolean allGood = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        final EditText emailText = findViewById(R.id.editTextMailR);
        final EditText passwordText = findViewById(R.id.editTextPasswordR);
        final EditText parentNameText = findViewById(R.id.editTextName);
        final EditText addressText = findViewById(R.id.editTextCity);
        final EditText ageText = findViewById(R.id.editTextAge);
        final EditText childNameText = findViewById(R.id.editTextChildName);
        final EditText detailText = findViewById(R.id.editTextDetails);
        final boolean male = ((RadioButton) findViewById(R.id.radioButtonMale)).isChecked();
        final boolean female = ((RadioButton) findViewById(R.id.radioButtonFemale)).isChecked();
        //תחביבים
        final CheckBox boardGamesCheckBox = findViewById(R.id.checkBoxBoardGames);
        final boolean boardgames = boardGamesCheckBox.isChecked();
        final CheckBox artCheckBox = findViewById(R.id.checkBoxArt);
        final boolean art = artCheckBox.isChecked();
        final CheckBox gamingCheckBox = findViewById(R.id.checkBoxGaming);
        final boolean gaming = gamingCheckBox.isChecked();
        final CheckBox musicCheckBox = findViewById(R.id.checkBoxMusic);
        final boolean music = musicCheckBox.isChecked();
        final CheckBox natureCheckBox = findViewById(R.id.checkBoxNature);
        final boolean nature = natureCheckBox.isChecked();
        final CheckBox scienceCheckBox = findViewById(R.id.checkBoxSience);
        final boolean science = scienceCheckBox.isChecked();
        final CheckBox sportCheckBox = findViewById(R.id.checkBoxSport);
        final boolean sport = sportCheckBox.isChecked();
        final EditText otherHobby = findViewById(R.id.editTextOtherHobby);


        //check the details
        if (!emailText.getText().toString().equals("") && !passwordText.getText().toString().equals("")) {
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            DatabaseReference myRef= database.getReference("users");
            alreadyExist = false;

            myRef.addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot currentUser : dataSnapshot.getChildren()) {
                        User val = currentUser.getValue(User.class);

                        if (val.getMail().equals(emailText.getText().toString())) {
                            alreadyExist = true;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            allGood = true;
            if (allGood && alreadyExist){
                Toast.makeText(Register.this, "המייל כבר קיים במערכת", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && passwordText.getText().toString().length() < 6){
                Toast.makeText(Register.this, "הסיסמה צריכה להכיל מינימום 6 תווים", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && parentNameText.getText().toString().equals("")){
                Toast.makeText(Register.this, "הכנס/י את שם ההורה", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && addressText.getText().toString().equals("")){
                Toast.makeText(Register.this, "הכנס/י את עיר המגורים", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && childNameText.getText().toString().equals("")){
                Toast.makeText(Register.this, "הכנס/י את שם הילד", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && ageText.getText().toString().equals("")){
                Toast.makeText(Register.this, "הכנס/י את גיל הילד", Toast.LENGTH_LONG).show();
                allGood = false;
            }
            if (allGood && !male && !female){
                Toast.makeText(Register.this, "הכנס/י את מין הילד", Toast.LENGTH_LONG).show();
                allGood = false;
            }

            //create the new user
            if (allGood) {
                mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("users").push();

                                    String gender;
                                    if (male) gender = "male";
                                    else if (female) gender = "female";
                                    else gender = "non";


                                    Hobbies h = new Hobbies(boardgames, science, nature, sport, art, gaming, music, otherHobby.getText().toString());
                                    int n = Integer.parseInt(ageText.getText().toString());

                                    User u = new User(parentNameText.getText().toString(),
                                            childNameText.getText().toString(),
                                            emailText.getText().toString(),
                                            addressText.getText().toString(), n,
                                            detailText.getText().toString(),
                                            gender, "profile" + Math.abs(rg.nextInt() % 16 + 1), h, myRef.getKey());


                                    myRef.setValue(u);
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(Register.this, "המייל או הסיסמה אינם נכונים", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
        else
            Toast.makeText(Register.this, "המייל או הסיסמה ריקים", Toast.LENGTH_LONG).show();
    }

}
