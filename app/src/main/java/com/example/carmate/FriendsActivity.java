package com.example.carmate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    SearchView friendSearch;
    ListView friendList;
    List<User> users;
    UserAdapter adapter;
    Button friendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friends);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        users = loadUsers();
        adapter = new UserAdapter(this, users, user ->{
            Toast.makeText(this, "Chat with " + user.getFullName(), Toast.LENGTH_SHORT).show();
        });
        friendSearch = findViewById(R.id.friendSearchView);
        friendSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        friendList = findViewById(R.id.friendListView);
        friendList.setAdapter(adapter);
        friendList.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = users.get(position);
            showUserDetailsPopup(selectedUser);
        });
        friendButton = findViewById(R.id.friendButton);
        friendButton.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(this).inflate(R.layout.add_friend_popup, null);

            AlertDialog addFriendDialog = new AlertDialog.Builder(this)
                    .setView(popupView)
                    .setCancelable(false)
                    .create();
            addFriendDialog.show();

            EditText friendCodeInput = popupView.findViewById(R.id.friendCodeInput);
            Button confirmAddButton = popupView.findViewById(R.id.addFriendButton);
            Button cancelButton = popupView.findViewById(R.id.cancelButton);

            confirmAddButton.setOnClickListener(v1 -> {
                String friendCode = friendCodeInput.getText().toString().trim();

                if (!friendCode.isEmpty()) {
                    addFriendByCode(friendCode);

                    addFriendDialog.dismiss();
                } else {
                    Toast.makeText(this, "Please enter a valid friend code", Toast.LENGTH_SHORT).show();
                }
            });

            // Handle "Cancel" button click
            cancelButton.setOnClickListener(v2 -> addFriendDialog.dismiss());
        });


    }
    private void showUserDetailsPopup(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user_details, null);

        ImageView profileImage = dialogView.findViewById(R.id.userDetailProfileImage);
        TextView nameText = dialogView.findViewById(R.id.userDetailName);
        TextView rating = dialogView.findViewById(R.id.userRating);
        Button messageButton = dialogView.findViewById(R.id.blockButton);
        Button blockButton = dialogView.findViewById(R.id.blockButton);

        profileImage.setImageResource(user.getImgId());
        nameText.setText(user.getFullName());
        rating.setText("Rating " + String.format("%.1f",user.getRating()));

        messageButton.setOnClickListener(v -> {
            Toast.makeText(this, "Messaging " + user.getFullName(), Toast.LENGTH_SHORT).show();
        });

        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        blockButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Block")
                    .setMessage("Are you sure you want to block this user?")
                    .setPositiveButton("Yes", (otherDialog, which) -> {
                        users.remove(user);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (otherDialog, which) -> otherDialog.dismiss())
                    .show();
        });
        dialog.show();
    }

    private void addFriendByCode(String friendCode) {
        User newUser = new User("Sarah", "Roon", R.drawable.sarah, "passenger");
        newUser.setRating(4);
        users.add(newUser);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Friend added successfully!", Toast.LENGTH_SHORT).show();
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
        setBottomNavigationSelectedItem(R.id.navigation_friends);
    }

    public List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = openFileInput("users_data.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            userList = (List<User>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            Log.d("UserLoad", "User list loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            Log.e("UserLoad", "Failed to load user list: " + e.getMessage());
        }
        return userList;
    }
}