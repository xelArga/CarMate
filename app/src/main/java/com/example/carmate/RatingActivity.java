package com.example.carmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    List<User> users;
    boolean[] selected;
    TextView currentRate;
    Button doneButton;
    ImageButton star1;
    ImageButton star2;
    ImageButton star3;
    ImageButton star4;
    ImageButton star5;
    int rating;
    int count = 0;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        users =  (List<User>)intent.getSerializableExtra("users");
        selected = intent.getBooleanArrayExtra("selected");
        doneButton = findViewById(R.id.doneButton);
        currentRate = findViewById(R.id.currentRate);
        boolean a = false;
        for(int j = 0; j < selected.length; ++j){
            if(selected[j]){
                if(!a) {
                    a = true;
                    currentRate.setText("Please rate " + users.get(j).getFullName());
                }
                ++count;
            }
        }
        if(count == 0){
            finish();
        } else if(count > 1){
            doneButton.setText("Next");
        }

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        View.OnClickListener buttonClickListener = v -> {
            String buttonTag = (String) v.getTag();
            ratingClick(v, buttonTag);
        };

        star1.setOnClickListener(buttonClickListener);
        star2.setOnClickListener(buttonClickListener);
        star3.setOnClickListener(buttonClickListener);
        star4.setOnClickListener(buttonClickListener);
        star5.setOnClickListener(buttonClickListener);
    }



    public void ratingClick(View v, String tag){
        switch(tag){
            case "star1":
                rating = 1;
                star1.setImageResource(R.drawable.filledstar);
                star2.setImageResource(R.drawable.unfilledstar);
                star3.setImageResource(R.drawable.unfilledstar);
                star4.setImageResource(R.drawable.unfilledstar);
                star5.setImageResource(R.drawable.unfilledstar);
                break;
            case "star2":
                rating = 2;
                star1.setImageResource(R.drawable.filledstar);
                star2.setImageResource(R.drawable.filledstar);
                star3.setImageResource(R.drawable.unfilledstar);
                star4.setImageResource(R.drawable.unfilledstar);
                star5.setImageResource(R.drawable.unfilledstar);
                break;
            case "star3":
                rating = 3;
                star1.setImageResource(R.drawable.filledstar);
                star2.setImageResource(R.drawable.filledstar);
                star3.setImageResource(R.drawable.filledstar);
                star4.setImageResource(R.drawable.unfilledstar);
                star5.setImageResource(R.drawable.unfilledstar);
                break;
            case "star4":
                rating = 4;
                star1.setImageResource(R.drawable.filledstar);
                star2.setImageResource(R.drawable.filledstar);
                star3.setImageResource(R.drawable.filledstar);
                star4.setImageResource(R.drawable.filledstar);
                star5.setImageResource(R.drawable.unfilledstar);
                break;
            case "star5":
                rating = 5;
                star1.setImageResource(R.drawable.filledstar);
                star2.setImageResource(R.drawable.filledstar);
                star3.setImageResource(R.drawable.filledstar);
                star4.setImageResource(R.drawable.filledstar);
                star5.setImageResource(R.drawable.filledstar);
                break;
        }
    }

    public void doneClick(View view){
        while(i < selected.length && !selected[i]){
            ++i;
        }
        if(i < selected.length && selected[i]) {
            users.get(i).setRating(rating);
            --count;
            ++i;
        }
            if (count == 0) {
                saveUsers(users);
                finish();
            } else if (count > 1) {
                doneButton.setText("Next");
                currentRate.setText("Please rate " + users.get(i).getFullName());
            } else {
                doneButton.setText("Finish");
                currentRate.setText("Please rate " + users.get(i).getFullName());
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

}