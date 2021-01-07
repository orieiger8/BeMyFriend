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

public class MyProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        DB db = DB.getInstance();
        thisUser = db.getMyUser();


        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.my_profile);

        final TextView parentName = (TextView) findViewById(R.id.textViewParentNamemored);
        final TextView mail = (TextView) findViewById(R.id.textViewMail);
        final TextView city = (TextView) findViewById(R.id.textViewCitymored);
        final TextView childName = (TextView) findViewById(R.id.textViewChildNamemored);
        final TextView age = (TextView) findViewById(R.id.textViewAgemored);
        final TextView gender = (TextView) findViewById(R.id.textViewGendermored);
        final TextView moreDetails = (TextView) findViewById(R.id.textViewDetailsmored);
        final ImageView profilePic = findViewById(R.id.imageView71);
        final TextView hobbies = (TextView) findViewById(R.id.textViewHobbies);


        //get the information from firebase to activity

        parentName.setText("שם ההורה: " + thisUser.getParentName());
        mail.setText("מייל: " + thisUser.getMail());
        city.setText("עיר מגורים: " + thisUser.getAddress());
        childName.setText("שם הילד: " + thisUser.getChildName());
        age.setText("גיל: " + thisUser.getAgeString());
        if (thisUser.getGender().equals("male"))
            gender.setText("מין: זכר");
        else if (thisUser.getGender().equals("female"))
            gender.setText("מין: נקבה");
        moreDetails.setText("עוד על הילד: " + thisUser.getDetails());
        profilePic.setImageResource(mail.getResources().getIdentifier(thisUser.getPicId(),
                "drawable", mail.getContext().getPackageName()));
        String h = "תחומי עניין: ";
        if (thisUser.getHobby().getBoardGames())
            h += "משחקי קופסא, ";
        if (thisUser.getHobby().getScience())
            h += "מדעים, ";
        if (thisUser.getHobby().getArt())
            h += "אומנות, ";
        if (thisUser.getHobby().getGaming())
            h += "משחקי מחשב, ";
        if (thisUser.getHobby().getMusic())
            h += "מוזיקה, ";
        if (thisUser.getHobby().getNature())
            h += "טבע, ";
        if (thisUser.getHobby().getSports())
            h += "ספורט, ";
        if (thisUser.getHobby().getOther().length() != 0) {
            h += (thisUser.getHobby().getOther()) + ", ";
        }
        h = h.substring(0, h.length() - 2);
        h += ".";
        hobbies.setText(h);
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