package com.example.animapedia;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private FloatingActionButton btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatModel> chatList;

    private ApiService apiService; // NEW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Back button
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Initialize backend service
        apiService = new ApiService();  // NEW

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);

        chatList = new ArrayList<>();
        chatList.add(new ChatModel("Hello! I am Animapedia AI 🐾\nAsk me anything about animals!", true));

        chatAdapter = new ChatAdapter(chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMessage(message);
        });
    }

    private void sendMessage(String message) {
        chatList.add(new ChatModel(message, false));
        chatAdapter.notifyItemInserted(chatList.size() - 1);
        recyclerView.scrollToPosition(chatList.size() - 1);

        editTextMessage.setText("");

        sendToBackend(message); // NEW
    }

    // NEW — real RAG backend call
    private void sendToBackend(String userMessage) {
        apiService.sendMessage(userMessage, new ApiService.Callback() {
            @Override
            public void onSuccess(String answer) {
                runOnUiThread(() -> {
                    chatList.add(new ChatModel(answer, true));
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    chatList.add(new ChatModel("⚠️ Error: " + error, true));
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                });
            }
        });
    }
}
