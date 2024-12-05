package com.example.carmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


public class MainActivity extends AppCompatActivity {
    Button button;
    List<User> users;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        users = loadUsers();
        if(users.isEmpty()){
            users = new ArrayList<>();
            users.add(new User("Jason", "Smith", R.drawable.jason, "passenger"));
            users.add(new User("Jenny", "Green", R.drawable.jenny, "passenger")); //code for loading in test users
            users.add(new User("Chris", "Crank", R.drawable.chris, "driver"));
            users.get(0).setPreferences(0);
            users.get(1).setPreferences(4);
            users.get(2).setPreferences(1);
            users.get(0).setRating(4);
            users.get(1).setRating(3);
            users.get(2).setRating(3);
            saveUsers(users);
        }
        user = loadUser();
        if(user == null){
            user = new User("John", "Jenkins", R.drawable.john, "passenger");
            user.setRating(4);
            user.setPreferences(0);
            saveUser(user);
        }
    }

    public void startDriver(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("selection",  "driver");
        startActivity(intent);
    }

    public void startPassenger(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("selection",  "passenger");
        startActivity(intent);
    }
    private void setBottomNavigationSelectedItem(int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.bottom_navigation_fragment);

        if (fragment instanceof BottomNavigationFragment) {
            ((BottomNavigationFragment) fragment).setSelectedMenuItem(itemId);
        }
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
