package com.example.bemyfriend.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bemyfriend.R;
import com.example.bemyfriend.adapter.NewFriendAdapter;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.presenter.FindNewFriendsPresenter;
import com.example.bemyfriend.utils.Helper;

import java.util.ArrayList;

public class FindNewFriends extends AppCompatActivity implements NewFriendAdapter.OnListListener {

    private AlertDialog.Builder dialogBuilderFilter, dialogBuilderMoreDetails;
    private AlertDialog dialogFilter, dialogMoreDetails;
    private Button filterPopup, cancelMoreD, startChatMoreD;
    private TextView parentName, childName, age, city, gender, moreD, hobbies;
    private ImageView moreDIcon;
    private NewFriendAdapter newFriendAdapter;

    private Toolbar toolBar;
    private FindNewFriendsPresenter presenter;
    private MenuItem startMusic, stopMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_friends);

        //set the toolbar
        toolBar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolBar);
        toolBar.setTitle(R.string.find_new_friends);

        presenter = new FindNewFriendsPresenter(this);

        //create recycler view layout
        final RecyclerView recyclerView1 = findViewById(R.id.recyclerViewFindFriends);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);

        //gives more info about the user you clicked on
        NewFriendAdapter.OnListListener listener = new NewFriendAdapter.OnListListener() {
            @Override
            public void onListClick(int position) {
                createNewContactDialogMoreDetails(presenter.getUsersList().get(position));
            }
        };

        //create recycler view
        newFriendAdapter = new NewFriendAdapter(presenter.getUsersList(), listener);
        recyclerView1.setAdapter(newFriendAdapter);
    }

    public void moveToChats(View view) {
        //if there is no chats for the user move to find new friends
        if (!presenter.chatsExist())
            Toast.makeText(FindNewFriends.this, R.string.no_available_chats, Toast.LENGTH_LONG).show();
        else {
            //move to chat activity
            startActivity(new Intent(this, Chats.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menu_chats).setVisible(true);
        menu.findItem(R.id.menu_delete_chat).setVisible(false);
        menu.findItem(R.id.menu_find_new_friends).setVisible(false);
        menu.findItem(R.id.menu_logout).setVisible(true);
        menu.findItem(R.id.menu_my_profile).setVisible(true);
        menu.findItem(R.id.app_bar_search).setVisible(false);
        startMusic = menu.findItem(R.id.menu_start_music);
        stopMusic = menu.findItem(R.id.menu_stop_music);
        if (Helper.IsMusicON()) {
            stopMusic.setVisible(true);
            startMusic.setVisible(false);
        } else {
            stopMusic.setVisible(false);
            startMusic.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = Helper.MenuSelect(item, this, startMusic, stopMusic);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //popup window filter
    public void createNewContactDialogFilter() {
        //get all activity objects
        dialogBuilderFilter = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_filter, null);

        //create dialog
        dialogBuilderFilter.setView(contactPopupView);
        dialogFilter = dialogBuilderFilter.create();
        dialogFilter.show();

        filterPopup = contactPopupView.findViewById(R.id.filterButton2);

        //check the filter the user chose
        filterPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final RecyclerView recyclerView1 = findViewById(R.id.recyclerViewFindFriends);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FindNewFriends.this);
                recyclerView1.setLayoutManager(layoutManager);

                ArrayList<User> friendList2 = presenter.filter(contactPopupView);

                // if there is not any user who is matching
                if (friendList2.size() == 0) {
                    Toast.makeText(FindNewFriends.this, R.string.have_not_found, Toast.LENGTH_LONG).show();
                }
                newFriendAdapter.setNf(friendList2);
                newFriendAdapter.notifyDataSetChanged();


                dialogFilter.dismiss();
            }
        });
    }

    public void filter(View view) {
        createNewContactDialogFilter();
    }

    public void createNewContactDialogMoreDetails(final User thisUser) {
        dialogBuilderMoreDetails = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_more_details, null);

        //build dialog
        dialogBuilderMoreDetails.setView(contactPopupView);
        dialogMoreDetails = dialogBuilderMoreDetails.create();
        dialogMoreDetails.show();

        //get all dialog objects
        parentName = (TextView) contactPopupView.findViewById(R.id.textViewParentNamemored);
        childName = (TextView) contactPopupView.findViewById(R.id.textViewChildNamemored);
        age = (TextView) contactPopupView.findViewById(R.id.textViewAgemored);
        city = (TextView) contactPopupView.findViewById(R.id.textViewCitymored);
        gender = (TextView) contactPopupView.findViewById(R.id.textViewGendermored);
        moreD = (TextView) contactPopupView.findViewById(R.id.textViewDetailsmored);
        moreDIcon = (ImageView) contactPopupView.findViewById(R.id.imageView71);
        hobbies = (TextView) contactPopupView.findViewById(R.id.textViewHobiesmored);
        cancelMoreD = (Button) contactPopupView.findViewById(R.id.cancelButtonmored);
        startChatMoreD = (Button) contactPopupView.findViewById(R.id.startchatbuttonmored);
        //

        //set all user's information
        parentName.setText("שם ההורה: " + thisUser.getParentName());
        childName.setText("שם הילד: " + thisUser.getChildName());
        age.setText("גיל: " + thisUser.getAgeString());
        city.setText("עיר מגורים: " + thisUser.getAddress());
        moreD.setText("עוד על הילד: " + thisUser.getDetails());
        if (thisUser.getGender().equals("male"))
            gender.setText("מין: זכר");
        else if (thisUser.getGender().equals("female"))
            gender.setText("מין: נקבה");
        moreDIcon.setImageResource(parentName.getResources().getIdentifier(thisUser.getPicId(),
                "drawable", parentName.getContext().getPackageName()));
        hobbies.setText(Helper.produceHobbyString(thisUser));
        //

        cancelMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogMoreDetails.dismiss();
            }
        });
        startChatMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent intent = new Intent(FindNewFriends.this, ChatRoom.class);
                intent.putExtra("mail", thisUser.getMail());

                if (presenter.createNewChat(thisUser)) {
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onListClick(int position) {
        //TODO: existential crisis - why the hell does this is exist?
        ;
    }


    public void setNetConnection(boolean b) {
        presenter.setNetConnected(b);
    }
}

