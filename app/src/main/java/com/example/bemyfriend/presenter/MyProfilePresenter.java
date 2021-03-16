package com.example.bemyfriend.presenter;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.MyProfile;
import com.example.bemyfriend.utils.Helper;
import com.example.bemyfriend.utils.Observer;

public class MyProfilePresenter implements Observer {
    private MyProfile activity;
    private Repository repository;
    private User thisUser;
    private TextView parentName, mail, city, childName, age, gender, moreDetails, hobbies;
    private ImageView profilePic;

    public MyProfilePresenter(MyProfile activity) {
        this.activity = activity;
        repository = Repository.getInstance();
        thisUser = repository.getMyUser();

        parentName = activity.findViewById(R.id.textViewParentNamemored);
        mail = activity.findViewById(R.id.textViewMail);
        city = activity.findViewById(R.id.textViewCitymored);
        childName = activity.findViewById(R.id.textViewChildNamemored);
        age = activity.findViewById(R.id.textViewAgemored);
        gender = activity.findViewById(R.id.textViewGendermored);
        moreDetails = activity.findViewById(R.id.textViewDetailsmored);
        profilePic = activity.findViewById(R.id.imageView71);
        hobbies = activity.findViewById(R.id.textViewHobbies);
        changeDetails();
    }

    @Override
    public void update() {
        if (!repository.userChanged(thisUser)) {
            thisUser = repository.getMyUser();
            changeDetails();
        }
    }

    public void changeDetails() {
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
        hobbies.setText(Helper.produceHobbyString(thisUser));
    }
}
