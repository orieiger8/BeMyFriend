package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mAuth = FirebaseAuth.getInstance();

        //set the toolbar
        toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("הפרופיל שלי");

        final TextView parentName = (TextView) findViewById(R.id.textViewParentNamemored);
        final TextView mail = (TextView) findViewById(R.id.textViewMail);
        final TextView city = (TextView) findViewById(R.id.textViewCitymored);
        final TextView childName = (TextView) findViewById(R.id.textViewChildNamemored);
        final TextView age = (TextView) findViewById(R.id.textViewAgemored);
        final TextView gender = (TextView) findViewById(R.id.textViewGendermored);
        final TextView moreDetails = (TextView) findViewById(R.id.textViewDetailsmored);
        final ImageView profilePic = findViewById(R.id.imageView71);
        final TextView hobbies = (TextView) findViewById(R.id.textViewHobbies);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //find current user and get the information from firebase to activity
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    User val = currentUser.getValue(User.class);
                    String currentUserMail = mAuth.getCurrentUser().getEmail();
                    String mailLoop = val.getMail();
                    if (currentUserMail.equals(mailLoop)) {
                        parentName.setText("שם ההורה: " + val.getParentName());
                        mail.setText( "מייל: "+ val.getMail());
                        city.setText("עיר מגורים: " + val.getAddress());
                        childName.setText("שם הילד: " + val.getChildName());
                        age.setText( "גיל: " + val.getAgeString());
                        if (val.getGender().equals("male"))
                            gender.setText("מין: זכר");
                        else if (val.getGender().equals("female"))
                            gender.setText("מין: נקבה");
                        moreDetails.setText("עוד על הילד: " + val.getDetails());
                        profilePic.setImageResource(mail.getResources().getIdentifier(val.getPicId(),
                                "drawable", mail.getContext().getPackageName()));
                        String h = "תחומי עניין: ";
                        if (val.getHobby().getBoardGames())
                            h += "משחקי קופסא, ";
                        if (val.getHobby().getScience())
                            h += "מדעים, ";
                        if (val.getHobby().getArt())
                            h += "אומנות, ";
                        if (val.getHobby().getGaming())
                            h += "משחקי מחשב, ";
                        if (val.getHobby().getMusic())
                            h += "מוזיקה, ";
                        if (val.getHobby().getNature())
                            h += "טבע, ";
                        if (val.getHobby().getSports())
                            h += "ספורט, ";
                        if (val.getHobby().getOther().length() != 0) {
                            h += (val.getHobby().getOther()) + ", ";
                        }
                        h = h.substring(0, h.length() - 2);
                        h += ".";
                        hobbies.setText(h);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void moveToEditProfile(View view) {
        startActivity(new Intent(this, EditProfile.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myprofile, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_myprofile_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile_chats){
            startActivity(new Intent(this, Chats.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile_findnewfriends){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}