package com.example.bemyfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class newFriendAdapter extends RecyclerView.Adapter<newFriendAdapter.newFriendViewHolder> {

    private ArrayList<User> nf;
    private OnListListener onListListener;

    public newFriendAdapter(ArrayList<User> nf, OnListListener onListListener) {
        this.onListListener = onListListener;
        this.nf = nf;
    }

    public void UpdateFriendsList(ArrayList<User> nf) {
        this.nf = nf;

        //notifyAll();
    }

    public newFriendAdapter() {
    }

    public void setNf(ArrayList<User> nf) {
        this.nf = nf;
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    public ArrayList<User> getNf() {
        return nf;
    }

    public OnListListener getOnListListener() {
        return onListListener;
    }

    @NonNull
    @Override
    public newFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newFriendView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_findfriends,parent,false);
        return new newFriendAdapter.newFriendViewHolder(newFriendView, onListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull newFriendViewHolder holder, int position) {
        User currentNewFriend = nf.get(position);
        String h = "";
        if (currentNewFriend.getHobby().getBoardGames())
            h += "משחקי קופסא";
        if (currentNewFriend.getHobby().getScience())
            h += "מדעים, ";
        if (currentNewFriend.getHobby().getArt())
            h += "אומנות, ";
        if (currentNewFriend.getHobby().getGaming())
            h += "משחקי מחשב, ";
        if (currentNewFriend.getHobby().getMusic())
            h += "מוזיקה, ";
        if (currentNewFriend.getHobby().getNature())
            h += "טבע, ";
        if (currentNewFriend.getHobby().getSports())
            h += "ספורט, ";
        if (currentNewFriend.getHobby().getOther().length() != 0) {
            h += (currentNewFriend.getHobby().getOther()) + ", ";
        }
        h = h.substring(0, h.length() - 2);
        h += ".";
        holder.nameTextView.setText(currentNewFriend.getChildName());
        holder.ageTextView.setText(currentNewFriend.getAgeString());
        holder.cityTextView.setText(currentNewFriend.getAddress());
        holder.hobbiesTextView.setText(h);
        holder.personImageView.setImageResource(holder.nameTextView.getResources().getIdentifier(currentNewFriend.getPicId(),
                "drawable", holder.ageTextView.getContext().getPackageName()));
    }

    @Override
    public int getItemCount() {
        return nf.size();
    }

    public static class newFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public TextView ageTextView;
        public TextView cityTextView;
        public ImageView personImageView;
        public TextView hobbiesTextView;

        OnListListener onListListener;

        public newFriendViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewReyclerKidName);
            ageTextView = itemView.findViewById(R.id.textViewRcyclerAge);
            cityTextView = itemView.findViewById(R.id.textViewRecyclerCity);
            personImageView = itemView.findViewById(R.id.imageView6);
            hobbiesTextView = itemView.findViewById(R.id.textViewHobbies);
            this.onListListener = onListListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
        }
    }
    //
    public interface OnListListener {
        void onListClick(int position);
    }
}
