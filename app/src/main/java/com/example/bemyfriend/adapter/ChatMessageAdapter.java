package com.example.bemyfriend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bemyfriend.R;
import com.example.bemyfriend.model.ChatMessage;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private String myMail;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages, String myMail) {
        this.messages = messages;
        this.myMail = myMail;
    }

    public ChatMessageAdapter() {
    }

    public void setMyMail(String myMail) {
        this.myMail = myMail;
    }

    public String getMyMail() {
        return myMail;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;

        switch (viewType) {
            case 0:
                layout = R.layout.message;
                break;

            default:
                layout = R.layout.message_other;
                break;
        }

        View chatView = LayoutInflater.from(parent.getContext()).
                inflate(layout, parent, false);

        return new ChatMessageViewHolder(chatView);
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getMessageUser().equals(myMail))
            return 0;
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage currentMessage = messages.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(messages.get(position).getMessageTime());

        holder.messageText.setText(currentMessage.getMessageText());
        int min = calendar.get(Calendar.MINUTE), hours = calendar.get(Calendar.HOUR_OF_DAY);
        String Smin = "" + min, Shours = "" + hours;
        if (min < 9)
            Smin = 0 + Smin;
        if (hours < 9)
            Shours = 0 + Shours;

        holder.time.setText(Shours + ":" + Smin);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText, time;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            time = itemView.findViewById(R.id.message_time);
        }
    }
}
