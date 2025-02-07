package com.example.carmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    List<User> users;
    User user;
    int selectedUser;
    Spinner preferences;
    Button saveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveProfile = findViewById(R.id.saveProfile);
        Intent intent = getIntent();
        users = (List<User>)intent.getSerializableExtra("users");
        preferences = findViewById(R.id.preferencesSpinner);
        if(users == null){
            user = loadUser();
        }else{
            selectedUser = intent.getIntExtra("selectedUser", 0);
            user = users.get(selectedUser);
            saveProfile.setVisibility(View.GONE);
            preferences.setVisibility(View.GONE);
        }

        TextView userNameProfile = findViewById(R.id.userNameProfile);
        ImageView profilePicture = findViewById(R.id.profilePicture);
        TextView rating = findViewById(R.id.userProfileRating);
        TextView mainStatus = findViewById(R.id.mainProfileStatus);

        userNameProfile.setText(user.getFullName());
        profilePicture.setImageResource(user.getImgId());
        rating.setText("Rating: " + user.getRating());
        mainStatus.setText(user.getMainStatus());

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.preferences, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferences.setAdapter(adapter);
        preferences.setSelection(user.getPreference());

    }

    public void saveProfile(View view){
        String value = preferences.getSelectedItem().toString();
        switch(value){
            case "None": user.setPreferences(0);
                    break;
            case "Pay For Ride": user.setPreferences(1);
                break;
            case "No Pay For Ride": user.setPreferences(2);
                break;
            case "Music": user.setPreferences(3);
                break;
            case "No Music": user.setPreferences(4);
                break;
        }
        saveUser(user);
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
        setBottomNavigationSelectedItem(R.id.navigation_profile);
    }

    public void saveUser(User user) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("user_profile.ser", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.close();
            fileOutputStream.close();
            Log.d("UserSave", "User saved successfully.");
        } catch (IOException e) {
            Log.e("UserSave", "Failed to save user: " + e.getMessage());
        }
    }

    public User loadUser() {
        User user = null;
        try {
            FileInputStream fileInputStream = openFileInput("user_profile.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            Log.d("UserLoad", "User loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            Log.e("UserLoad", "Failed to load user: " + e.getMessage());
        }
        return user;
    }
}