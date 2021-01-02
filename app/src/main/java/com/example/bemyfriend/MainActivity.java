package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements newFriendAdapter.OnListListener {

    private FirebaseAuth mAuth;
    private newFriendAdapter.OnListListener listener;
    private AlertDialog.Builder dialogBuilder, dialogBuilder2;
    private AlertDialog dialog,dialog2;
    private EditText namePopup, agePopup, cityPopup, otherHobby;
    private Button filterPopup, cancelMoreD, startChatMoreD;
    Random rg = new Random();
    private ArrayList<User> users2 = new ArrayList<>();
    private TextView parentName, childName,age,city,gender, moreD, hobbies;
    private ImageView moreDIcon;
    private newFriendAdapter newFriendAdapter;
    private User thisUser;

    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //set the toolbar
        toolBar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolBar);
        toolBar.setTitle("מצא חברים חדשים");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        final RecyclerView recyclerView1 = findViewById(R.id.recyclerViewFindFriends);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //create list of all users and find the current user
                users2.clear();
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    User val = currentUser.getValue(User.class);

                    if (!(val.getMail().equals(mAuth.getCurrentUser().getEmail()))) {
                        users2.add(val);
                    } else {
                        thisUser = val;
                    }
                }
                users2 = randomOrder(users2);
                //gives more info about the user you clicked on
                listener = new newFriendAdapter.OnListListener() {
                    @Override
                    public void onListClick(int position) {
                        createNewContactDialog2(users2.get(position));
                    }
                };

                newFriendAdapter = new newFriendAdapter(users2, listener);
                recyclerView1.setAdapter(newFriendAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void moveToChats(View view) {
        if (thisUser.getChats().size() == 0)
            Toast.makeText(MainActivity.this, "אין לך צ'אטים זמינים", Toast.LENGTH_LONG).show();
        else {
            startActivity(new Intent(this, Chats.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile) {
            startActivity(new Intent(this, MyProfile.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //popup window filter
    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView =getLayoutInflater().inflate(R.layout.popup,null);
        namePopup = (EditText) contactPopupView.findViewById(R.id.editTextFilterName);
        agePopup = (EditText) contactPopupView.findViewById(R.id.editTextFilterAge);
        cityPopup = (EditText) contactPopupView.findViewById(R.id.editTextFilterCity);
        otherHobby = (EditText) contactPopupView.findViewById(R.id.editTextFilterOtherHobie);
        filterPopup = (Button) contactPopupView.findViewById(R.id.filterButton2);
        String other = otherHobby.getText().toString();

        final CheckBox boardGamesCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterBoardGames);
        final CheckBox artCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterArt);
        final CheckBox gamingCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterGaming);
        final CheckBox musicCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterMusic);
        final CheckBox natureCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterNature);
        final CheckBox scienceCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterSience);
        final CheckBox sportCheckBox = contactPopupView.findViewById(R.id.checkBoxFilterSport);
        final EditText otherHobby = contactPopupView.findViewById(R.id.editTextFilterOtherHobie);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //check the filter the user chose
        filterPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean male = ((RadioButton) contactPopupView.findViewById(R.id.radioButtonfiltermale)).isChecked();
                boolean female = ((RadioButton) contactPopupView.findViewById(R.id.radioButtonfilterfemale)).isChecked();

                final ArrayList<User> friendList2 = new ArrayList<>();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");

                final RecyclerView recyclerView1 = findViewById(R.id.recyclerViewFindFriends);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView1.setLayoutManager(layoutManager);

                myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot currentUser : dataSnapshot.getChildren()) {
                            User val = currentUser.getValue(User.class);

                            boolean filterOut = false;
                            if (mAuth.getCurrentUser().getEmail().equals(val.getMail()))
                                filterOut = true;

                            String cName = namePopup.getText().toString();
                            if (!cName.equals(""))
                            {
                                if (!val.getChildName().equals(cName))
                                    filterOut = true;
                            }
                            String ageStr = agePopup.getText().toString();
                            if (!filterOut && !ageStr.equals("")) {
                                if (val.getAge() != Integer.parseInt(ageStr))
                                    filterOut = true;
                            }
                            String cityStr = cityPopup.getText().toString();
                            if (!filterOut && !cityStr.equals("")) {
                                if (!val.getAddress().equals(cityStr))
                                    filterOut = true;
                            }
                            String otherStr = otherHobby.getText().toString();
                            if (!filterOut && !otherStr.equals("")) {
                                if (!val.getHobby().getOther().equals(otherStr))
                                    filterOut = true;
                            }
                            if (!filterOut && male) {
                                if(!val.getGender().equals("male"))
                                    filterOut=true;
                            }
                            if (!filterOut && female) {
                                if (!val.getGender().equals("female"))
                                    filterOut = true;
                            }
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
                            if (!filterOut) {
                                friendList2.add(val);
                            }


                        }
                        newFriendAdapter.OnListListener listener2;
                        listener2= new newFriendAdapter.OnListListener() {
                            @Override
                            public void onListClick(int position) {
                                createNewContactDialog2(friendList2.get(position));
                            }
                        };
                        if (friendList2.size() == 0) {
                            Toast.makeText(MainActivity.this, "לא נמצאו אנשים", Toast.LENGTH_LONG).show();
                            newFriendAdapter.setNf(users2);
                        }
                        else {
                            ArrayList<User> friendList22= randomOrder(friendList2);
                            newFriendAdapter.setNf(friendList22);
                            newFriendAdapter.setOnListListener(listener2);
                        }
                        newFriendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                dialog.dismiss();
            }
        });
    }
    public void filter(View view) {
        createNewContactDialog();
    }

    public void createNewContactDialog2(final User user1) {
        dialogBuilder2 = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_more_details, null);

        dialogBuilder2.setView(contactPopupView);
        dialog2 = dialogBuilder2.create();
        dialog2.show();

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

        parentName.setText("שם ההורה: "+ user1.getParentName());
        childName.setText("שם הילד: " + user1.getChildName());
        age.setText("גיל: " + user1.getAgeString());
        city.setText("עיר מגורים: " + user1.getAddress());
        moreD.setText("עוד על הילד: " + user1.getDetails());
        if (user1.getGender().equals("male"))
            gender.setText("מין: זכר");
        else if (user1.getGender().equals("female"))
            gender.setText("מין: נקבה");
        moreDIcon.setImageResource(parentName.getResources().getIdentifier(user1.getPicId(),
                "drawable", parentName.getContext().getPackageName()));
        String h = "תחומי עניין: ";
        if (user1.getHobby().getBoardGames())
            h += "משחקי קופסא";
        if (user1.getHobby().getScience())
            h += "מדעים, ";
        if (user1.getHobby().getArt())
            h += "אומנות, ";
        if (user1.getHobby().getGaming())
            h += "משחקי מחשב, ";
        if (user1.getHobby().getMusic())
            h += "מוזיקה, ";
        if (user1.getHobby().getNature())
            h += "טבע, ";
        if (user1.getHobby().getSports())
            h += "ספורט, ";
        if (user1.getHobby().getOther().length() != 0) {
            h += (user1.getHobby().getOther()) + ", ";
        }
        h = h.substring(0, h.length() - 2);
        h += ".";
        hobbies.setText(h);

        cancelMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog2.dismiss();
            }
        });
        startChatMoreD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent intent = new Intent(MainActivity.this, ChatRoom.class);
                intent.putExtra("mail", user1.getMail());

                boolean alreadyexsist= false;
                for (int i = 0; i < thisUser.getChats().size(); i++){
                    if(thisUser.getChats().get(i).getMail().equals(user1.getMail()))
                        alreadyexsist = true;
                }

                if (!alreadyexsist) {
                    String flushName = FlushName(user1, thisUser);
                    FirebaseDatabase.getInstance().getReference("chats/" + flushName).push()
                            .setValue(new ChatMessage("שלום", FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                    thisUser.addChat(user1.getMail());
                    thisUser.addMyMessage(user1.getMail(), "שלום");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userRef = database.getReference("users/" + thisUser.getDataSnapshot());
                    userRef.setValue(thisUser);
                    user1.addChat(thisUser.getMail());
                    user1.addMessage(thisUser.getMail(), "שלום");


                    database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");

                    userRef = database.getReference("users/" + user1.getDataSnapshot());
                    userRef.setValue(user1);
                }

                startActivity(intent);
                finish();
            }
        });

    }

    //create chat name for firebase
    private String FlushName(User user1, User thisUser) {
        String name= AlphabetOrder(user1.getParentName(), thisUser.getParentName());
        String childName = AlphabetOrder(user1.getChildName(), thisUser.getChildName());
        String s = name + childName + (user1.getAge() + thisUser.getAge());
        return s;

    }

    //sort the names by alphabetic order
    private String AlphabetOrder(String parentName1, String parentName2) {
        int i;
        for(i = 0; i < parentName1.length() && i < parentName2.length(); i++){
            if (parentName1.charAt(i) != parentName2.charAt(i)){
                if (parentName1.charAt(i) < parentName2.charAt(i))
                    return parentName1 + parentName2;
                else
                    return parentName2 + parentName1;
            }
        }
        if (i == parentName1.length()) return parentName1 + parentName2;
        else return parentName2 + parentName1;
    }

    @Override
    public void onListClick(int position) {
        createNewContactDialog2(users2.get(position));
    }

    //change the list to random order
    public ArrayList<User> randomOrder(ArrayList<User> list) {
        ArrayList<User> ans = new ArrayList<>();
        while (list.size() != 0) {
            int n = Math.abs(rg.nextInt() % list.size());
            ans.add(list.get(n));
            list.remove(n);
        }
        if (thisUser.getChats().size() != 0) {
            for (int i = 0; i < ans.size(); i++) {
                for (int j = 0; j < thisUser.getChats().size(); j++) {
                    if (thisUser.getChats().get(j).getMail().equals(ans.get(i).getMail())) {
                        ans.remove(i);
                        i--;
                        j = thisUser.getChats().size();
                    }
                }
            }
        }
        return ans;
    }
}

