package com.example.bemyfriend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bemyfriend.R;
import com.example.bemyfriend.model.Chat;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {


    private ArrayList<Chat> chats;
    private RecyclerViewClickListener listener;

    public ChatAdapter(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public ChatAdapter(ArrayList<Chat> chats, RecyclerViewClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView, lastMessageTextView, timeTextView;
        public ImageView personImageView;
        public TextView unReadMessagesView;

        RecyclerViewClickListener rListener;

        public ChatViewHolder(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textname);
            timeTextView = itemView.findViewById(R.id.texttime);
            lastMessageTextView = itemView.findViewById(R.id.textlastchat);
            unReadMessagesView = itemView.findViewById(R.id.textunreadmessages);
            personImageView = itemView.findViewById(R.id.imageperson);

            rListener = recyclerViewClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            rListener.OnClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_cht, parent, false);
        return new ChatViewHolder(chatView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat currentChat = chats.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentChat.getTime());
        holder.nameTextView.setText(currentChat.getName());
        int min = calendar.get(Calendar.MINUTE), hours = calendar.get(Calendar.HOUR_OF_DAY);
        String sMin = "" + min, sHours = "" + hours;
        if (min < 9)
            sMin = 0 + sMin;
        if (hours < 9)
            sHours = 0 + sHours;

        holder.timeTextView.setText(sHours + ":" + sMin);
        if (currentChat.getLastMessage().length() > 25) {
            holder.lastMessageTextView.setText(currentChat.getLastMessage().substring(0, 25) + "...");
        } else {
            holder.lastMessageTextView.setText(currentChat.getLastMessage());
        }

        if (currentChat.getUnreadMessages() == 0) {
            holder.unReadMessagesView.setText("");
        } else {
            holder.unReadMessagesView.setText("(" + currentChat.getUnreadMessages() + ")");
        }

        holder.personImageView.setImageResource(holder.nameTextView.getResources().getIdentifier(currentChat.getImageId(),
                "drawable", holder.nameTextView.getContext().getPackageName()));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface RecyclerViewClickListener {
        void OnClick(int position);
    }
}
