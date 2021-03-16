package com.example.bemyfriend.presenter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.FindNewFriends;
import com.example.bemyfriend.utils.Helper;
import com.example.bemyfriend.utils.Observer;

import java.util.ArrayList;

public class FindNewFriendsPresenter implements Observer {
    private FindNewFriends activity;
    private Repository repository;
    private User thisUser;
    private ArrayList<User> usersList = new ArrayList<>();

    public FindNewFriendsPresenter(FindNewFriends activity) {
        this.activity = activity;
        //connect to database with DB
        repository = Repository.getInstance();
        //get my user
        thisUser = repository.getMyUser();
        //get list of all users
        usersList = repository.getAllUsersExceptMe();
        //change the list to random order
        usersList = Helper.randomOrder(usersList,thisUser);

    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    @Override
    public void update() {

    }

    public boolean chatsExist() {
        //if there is no chats for the user move to find new friends
        if (thisUser.getChats().size() == 0)
            return false;
        return true;
    }

    public ArrayList<User> filter(View contactPopupView) {
        //
        EditText namePopup = contactPopupView.findViewById(R.id.editTextFilterName),
                agePopup = contactPopupView.findViewById(R.id.editTextFilterAge),
                cityPopup = contactPopupView.findViewById(R.id.editTextFilterCity),
                otherHobby = contactPopupView.findViewById(R.id.editTextFilterOtherHobie);
        //

        //
        final CheckBox boardGamesCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterBoardGames);
        final CheckBox artCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterArt);
        final CheckBox gamingCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterGaming);
        final CheckBox musicCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterMusic);
        final CheckBox natureCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterNature);
        final CheckBox scienceCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterSience);
        final CheckBox sportCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterSport);
        //

        boolean male = ((RadioButton) contactPopupView.findViewById(R.id.radioButtonfiltermale)).isChecked();
        boolean female = ((RadioButton) contactPopupView.findViewById(R.id.radioButtonfilterfemale)).isChecked();

        final ArrayList<User> friendList2 = new ArrayList<>();


        for (int i = 0; i < usersList.size(); i++) {
            User val = usersList.get(i);

            boolean filterOut = false;
            //check by name
            String cName = namePopup.getText().toString();
            if (!cName.equals("")) {
                if (!val.getChildName().equals(cName))
                    filterOut = true;
            }
            //check by age
            String ageStr = agePopup.getText().toString();
            if (!filterOut && !ageStr.equals("")) {
                if (val.getAge() != Integer.parseInt(ageStr))
                    filterOut = true;
            }
            //check by city
            String cityStr = cityPopup.getText().toString();
            if (!filterOut && !cityStr.equals("")) {
                if (!val.getAddress().equals(cityStr))
                    filterOut = true;
            }
            //check by other hobby
            String otherStr = otherHobby.getText().toString();
            if (!filterOut && !otherStr.equals("")) {
                if (!val.getHobby().getOther().equals(otherStr))
                    filterOut = true;
            }
            //check by gender
            if (!filterOut && male) {
                if (!val.getGender().equals("male"))
                    filterOut = true;
            }
            if (!filterOut && female) {
                if (!val.getGender().equals("female"))
                    filterOut = true;
            }
            //

            //check by hobbies
            boolean boardgames = boardGamesCheckBox.isChecked();
            if (!filterOut && boardgames) {
                if (!val.getHobby().getBoardGames())
                    filterOut = true;
            }
            boolean art = artCheckBox.isChecked();
            if (!filterOut && art) {
                if (!val.getHobby().getArt())
                    filterOut = true;
            }
            boolean sport = sportCheckBox.isChecked();
            if (!filterOut && sport) {
                if (!val.getHobby().getSports())
                    filterOut = true;
            }
            boolean nature = natureCheckBox.isChecked();
            if (!filterOut && nature) {
                if (!val.getHobby().getNature())
                    filterOut = true;
            }
            boolean music = musicCheckBox.isChecked();
            if (!filterOut && music) {
                if (!val.getHobby().getMusic())
                    filterOut = true;
            }
            boolean gaming = gamingCheckBox.isChecked();
            if (!filterOut && gaming) {
                if (!val.getHobby().getGaming())
                    filterOut = true;
            }
            boolean science = scienceCheckBox.isChecked();
            if (!filterOut && science) {
                if (!val.getHobby().getScience())
                    filterOut = true;
            }
            //
            if (!filterOut) {
                friendList2.add(val);
            }
        }
        if(friendList2.size()>0){
            usersList.clear();
            for (int i = 0; i < friendList2.size(); i++) {
                usersList.add(friendList2.get(i));
            }
            usersList = Helper.randomOrder(usersList, thisUser);
        }
        return usersList;
    }
    public boolean createNewChat(User user1){
        //check if chat already exist
        boolean alreadyExist = false;
        for (int i = 0; i < thisUser.getChats().size(); i++) {
            if (thisUser.getChats().get(i).getMail().equals(user1.getMail()))
                alreadyExist = true;
        }

        //create new chat
        if (!alreadyExist) {
            String flushName= Helper.FlushName(user1,thisUser);
            repository.createChat(flushName);
            thisUser.addChat(user1.getMail());
            thisUser.addMyMessage(user1.getMail(), "שלום");
            repository.updateUserInDB(thisUser);
            user1.addChat(thisUser.getMail());
            user1.addMessage(thisUser.getMail(), "שלום");
            repository.updateOtherUserInDB(user1);
        }
        return alreadyExist;
    }
}
