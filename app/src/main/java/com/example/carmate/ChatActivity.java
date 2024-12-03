package com.example.carmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    List<User> users;
    int currentUser;
    ImageButton backButton;

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
        currentUser = intent.getIntExtra("selectedUser", 0);

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        ChatAdapter chatAdapter = new ChatAdapter(users.get(currentUser).messages);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        Button sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        EditText messageInput = findViewById(R.id.messageInput);
        TextView currentChat = findViewById(R.id.currentChat);
        ImageView profilePicture = findViewById(R.id.profileImage);
        currentChat.setText(users.get(currentUser).getFullName());
        profilePicture.setImageResource(users.get(currentUser).getImgId());

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                users.get(currentUser).messages.add(new Message("You: " + messageText, true));
                chatAdapter.notifyItemInserted(users.get(currentUser).messages.size() - 1);
                chatRecyclerView.scrollToPosition(users.get(currentUser).messages.size() - 1);
                messageInput.setText("");

                       new Handler().postDelayed(() -> {
                           users.get(currentUser).messages.add(new Message
                                   (users.get(currentUser).getFirstName() +": "+ messageText, false));
                    chatAdapter.notifyItemInserted(users.get(currentUser).messages.size() - 1);
                    chatRecyclerView.scrollToPosition(users.get(currentUser).messages.size() - 1);
                }, 500);
            }
        });

    }

    public void backClick(View view){
        saveUsers(users);
        setResult(RESULT_OK);
        finish();
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
    public void saveUsers(List<User> userList) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("users_data.ser", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userList);
            objectOutputStream.close();
            fileOutputStream.close();
            Log.d("UserSave", "User list saved successfully.");
        } catch (IOException e) {
            Log.e("UserSave", "Failed to save user list: " + e.getMessage());
        }
    }
}