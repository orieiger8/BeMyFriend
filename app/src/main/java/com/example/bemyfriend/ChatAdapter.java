package com.example.bemyfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

class ChatAdapter extends RecyclerView.Adapter<com.example.bemyfriend.ChatAdapter.ChatViewHolder> {


    private ArrayList<Chat> chats;
    private RecyclerViewClickListener listener;

    public ChatAdapter(ArrayList<Chat> chats){
        this.chats = chats;
    }

    public ChatAdapter(ArrayList<Chat> chats, RecyclerViewClickListener listener){
        this.chats = chats;
        this.listener = listener;
    }

    public  static  class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titleTextView;
        public TextView chatTextView;
        public TextView timeTextView;
        public ImageView personImageView;
        public TextView unReadMessagesView;

        RecyclerViewClickListener rListener;

        public ChatViewHolder(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textname);
            timeTextView = itemView.findViewById(R.id.texttime);
            chatTextView = itemView.findViewById(R.id.textlastchat);
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
        View chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_cht,parent,false);
        return  new ChatViewHolder(chatView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat currentChat = chats.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentChat.getTime());
        holder.titleTextView.setText(currentChat.getTitle());
        int min = calendar.get(Calendar.MINUTE),hours=calendar.get(Calendar.HOUR_OF_DAY);
        String sMin = ""+ min, sHours = "" + hours;
        if (min < 9)
            sMin = 0 + sMin;
        if (hours<9)
            sHours = 0 + sHours;

        holder.timeTextView.setText(sHours + ":" + sMin);
        if (currentChat.getText().length() > 25) {
            holder.chatTextView.setText(currentChat.getText().substring(0, 25) + "...");
        } else {
            holder.chatTextView.setText(currentChat.getText());
        }

        if (currentChat.getUnreadMessages() == 0){
            holder.unReadMessagesView.setText("");
        }
        else {
            holder.unReadMessagesView.setText("(" + currentChat.getUnreadMessages() + ")");
        }

        holder.personImageView.setImageResource(holder.titleTextView.getResources().getIdentifier(currentChat.getImageId(),
                "drawable", holder.titleTextView.getContext().getPackageName()));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface RecyclerViewClickListener{
        void OnClick(int position);
    }
}
