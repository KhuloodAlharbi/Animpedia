package com.example.animapedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatModel> chatList;
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;

    public ChatAdapter(List<ChatModel> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        // Check who sent the message to decide which layout to use
        if (chatList.get(position).isBot()) {
            return VIEW_TYPE_BOT;
        } else {
            return VIEW_TYPE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BOT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot, parent, false);
            return new BotViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel chat = chatList.get(position);
        if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).textMessage.setText(chat.getMessage());
        } else if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).textMessage.setText(chat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // ViewHolder for Bot Messages
    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }

    // ViewHolder for User Messages
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }
}
