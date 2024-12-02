package com.example.carmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        users = (List<User>)intent.getSerializableExtra("users");

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        List<Message> messages = new LinkedList<>();
        ChatAdapter chatAdapter = new ChatAdapter(messages);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        Button sendButton = findViewById(R.id.sendButton);
        EditText messageInput = findViewById(R.id.messageInput);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                messages.add(new Message("You: " + messageText, true));
                chatAdapter.notifyItemInserted(messages.size() - 1);
                chatRecyclerView.scrollToPosition(messages.size() - 1);
                messageInput.setText("");

                       new Handler().postDelayed(() -> {
                    messages.add(new Message("Received: " + messageText, false));
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    chatRecyclerView.scrollToPosition(messages.size() - 1);
                }, 500);
            }
        });

    }
    private void setBottomNavigationSelectedItem(int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.bottom_navigation_fragment);

        if (fragment instanceof BottomNavigationFragment) {
            ((BottomNavigationFragment) fragment).setSelectedMenuItem(itemId);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationSelectedItem(R.id.navigation_chat);
    }
}