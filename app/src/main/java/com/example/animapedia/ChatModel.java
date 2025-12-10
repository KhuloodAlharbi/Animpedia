package com.example.animapedia;

public class ChatModel {
    private String message;
    private boolean isBot; // true if message is from bot, false if from user

    // Constructor
    public ChatModel(String message, boolean isBot) {
        this.message = message;
        this.isBot = isBot;
    }

    // Getter for the message text
    public String getMessage() {
        return message;
    }

    // Getter to check if it's a bot message
    public boolean isBot() {
        return isBot;
    }
}
