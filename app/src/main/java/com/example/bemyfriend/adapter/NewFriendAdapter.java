package com.example.bemyfriend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bemyfriend.R;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.utils.Helper;

import java.util.ArrayList;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.newFriendViewHolder> {

    private ArrayList<User> nf;
    private OnListListener onListListener;

    public NewFriendAdapter(ArrayList<User> nf, OnListListener onListListener) {
        this.onListListener = onListListener;
        this.nf = nf;
    }

    public void setNf(ArrayList<User> nf) {
        this.nf = nf;
    }

    @NonNull
    @Override
    public newFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newFriendView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_findfriends, parent, false);
        return new NewFriendAdapter.newFriendViewHolder(newFriendView, onListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull newFriendViewHolder holder, int position) {
        User currentNewFriend = nf.get(position);
        String h = Helper.produceHobbyString(currentNewFriend);
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
