package com.example.carmate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;
import java.util.List;

public class BottomNavigationFragment extends Fragment {
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int itemId = item.getItemId();
                Class<?> targetActivity = null;
                Context context = getActivity();

                if (itemId == R.id.navigation_connect && !(context instanceof MainActivity)) {
                    targetActivity = MainActivity.class;
                } else if (itemId == R.id.navigation_friends && !(context instanceof FriendsActivity)) {
                    targetActivity = FriendsActivity.class;
                } else if (itemId == R.id.navigation_chat && !(context instanceof ChatListActivity || context instanceof ChatActivity)) {
                    targetActivity = ChatListActivity.class;
                } else if (itemId == R.id.navigation_profile && !(context instanceof ProfileActivity)) {
                    targetActivity = ProfileActivity.class;
                } else if (itemId == R.id.navigation_settings && !(context instanceof SettingsActivity)) {
                    targetActivity = SettingsActivity.class;
                }

                if (targetActivity != null) {
                    Intent intent = new Intent(context, targetActivity);
                    startActivity(intent);
                    requireActivity().finish();
                }
                return true;
            }
        });

        return view;
    }
    public void setSelectedMenuItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}
