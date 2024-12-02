package com.example.carmate;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
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

public class ChatListActivity extends AppCompatActivity {
    SearchView friendSearch;
    ListView friendList;
    List<User> users;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        users = loadUsers();
        adapter = new UserAdapter(this, users);
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