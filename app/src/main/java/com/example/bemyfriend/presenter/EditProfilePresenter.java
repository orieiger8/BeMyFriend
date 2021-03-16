package com.example.bemyfriend.presenter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.Hobbies;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.EditProfile;
import com.example.bemyfriend.utils.Observer;

public class EditProfilePresenter implements Observer {
    private EditProfile activity;
    private User thisUser;
    private Repository repository;

    private String parentNameText, cities, childNameText, moreD, mails, passwords;
    private int ages;

    //
    private EditText parentName, city, childName, age, moreDetails, otherHobby;
    private RadioGroup mOption;
    private RadioButton male, female;
    //

    //תחביבים
    private CheckBox boardGamesCheckBox, artCheckBox, gamingCheckBox, musicCheckBox, natureCheckBox, scienceCheckBox, sportCheckBox;
    private boolean boardGames, art, gaming, music, nature, science, sport;
    private String otherHobbyText;
    //
    private Button picButton;

    public EditProfilePresenter(EditProfile activity) {
        this.activity = activity;
        //connect to database with DB
        repository = Repository.getInstance();
        //get my user
        thisUser = repository.getMyUser();

        findItems();

        RenderUserInfoToView(thisUser);
    }

    @Override
    public void update() {
        if (repository.userChanged(thisUser)) {
            thisUser = repository.getMyUser();
            RenderUserInfoToView(thisUser);
        }
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
        gaming = val.getHobby().getGaming();
        nature = val.getHobby().getNature();
        science = val.getHobby().getScience();
        music = val.getHobby().getMusic();
        sport = val.getHobby().getSports();


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
        picButton.setBackgroundResource(parentName.getResources().getIdentifier(thisUser.getPicId(),
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

    public void findItems() {
        // get layout components
        parentName = activity.findViewById(R.id.editTextEditName);
        city = activity.findViewById(R.id.editTextEditCity);
        childName = activity.findViewById(R.id.editTextEditChildName);
        age = activity.findViewById(R.id.editTextEditAge);
        male = activity.findViewById(R.id.editUser_radioButtonEditMale);
        female = activity.findViewById(R.id.editUser_radioButtonEditMale);
        moreDetails = activity.findViewById(R.id.editTextEditDetails);
        picButton = (Button) activity.findViewById(R.id.picbutton);
        mOption = (RadioGroup) activity.findViewById(R.id.radioEditGroup);
        otherHobby = activity.findViewById(R.id.editTextEditOtherHobby);

        //
        boardGamesCheckBox = activity.findViewById(R.id.checkBoxEditBoardGames);
        artCheckBox = activity.findViewById(R.id.checkBoxEditArt);
        gamingCheckBox = activity.findViewById(R.id.checkBoxEditGaming);
        musicCheckBox = activity.findViewById(R.id.checkBoxEditMusic);
        natureCheckBox = activity.findViewById(R.id.checkBoxEditNature);
        scienceCheckBox = activity.findViewById(R.id.checkBoxEditSience);
        sportCheckBox = activity.findViewById(R.id.checkBoxEditSport);
        //

    }

    public void saveChanges() {
        // update all values
        String pName = this.parentName.getText().toString(),
                cName = this.childName.getText().toString(),
                address = this.city.getText().toString(),
                age = this.age.getText().toString(),
                details = this.moreDetails.getText().toString(),
                other = this.otherHobby.getText().toString();
        Boolean isMale = ((RadioButton) activity.findViewById(R.id.editUser_radioButtonEditMale)).isChecked(),
                isFemale = ((RadioButton) activity.findViewById(R.id.editUser_radioButtonEditFemale)).isChecked();
        boolean pBoardgames = (this.boardGamesCheckBox).isChecked(),
                pArt = (this.artCheckBox).isChecked(),
                pGaming = (this.gamingCheckBox).isChecked(),
                pNature = (this.natureCheckBox).isChecked(),
                pScience = (this.scienceCheckBox).isChecked(),
                pMusic = (this.musicCheckBox).isChecked(),
                pSport = (this.sportCheckBox).isChecked();


        if (!pName.isEmpty())
            thisUser.setParentName(pName);
        if (!cName.isEmpty())
            thisUser.setChildName(cName);
        if (!address.isEmpty())
            thisUser.setAddress(address);
        if (!age.isEmpty())
            thisUser.setAge(Integer.parseInt(age));
        if (!details.isEmpty())
            thisUser.setDetails(details);

        if (isMale) thisUser.setGender("male");
        else if (isFemale) thisUser.setGender("female");
        else thisUser.setGender("non");

        Hobbies h = new Hobbies(pBoardgames, pScience, pNature, pSport, pArt, pGaming, pMusic, other);
        thisUser.setHobby(h);

        // save to db
        repository.updateUserInDB(thisUser);
    }

    public void setPicture(int i, AlertDialog dlg) {
        thisUser.setPicId("profile" + i);
        picButton.setBackgroundResource(parentName.getResources().getIdentifier(thisUser.getPicId(),
                "drawable", parentName.getContext().getPackageName()));
        dlg.dismiss();
    }
}
