package com.example.carmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<User> userList;
    private List<User> filteredUserList;
    private OnChatClickListener onChatClickListener;

    public UserAdapter(Context context, List<User> userList, OnChatClickListener onChatClickListener) {
        this.context = context;
        this.userList = new ArrayList<>(userList);
        this.filteredUserList = userList;
        this.onChatClickListener = onChatClickListener;
    }

    @Override
    public int getCount() {
        return filteredUserList.size();
    }

    @Override
    public User getItem(int position) {
        return filteredUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        User user = getItem(position);
        TextView userNameTextView = convertView.findViewById(R.id.userName);
        ImageView profileImageView = convertView.findViewById(R.id.userProfileImage);
        ImageView chatIconView = convertView.findViewById(R.id.chatIcon);

        userNameTextView.setText(user.getFullName());
        profileImageView.setImageResource(user.getImgId());
        ImageView chatIcon = convertView.findViewById(R.id.chatIcon);
        chatIcon.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onChatClick(user);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = userList;
                    results.count = userList.size();
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    List<User> filteredList = new ArrayList<>();

                    for (User user : userList) {
                        if (user.getFullName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(user);
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredUserList = (List<User>) results.values;
                notifyDataSetChanged(); // Update the ListView
            }
        };
    }
    public interface OnChatClickListener {
        void onChatClick(User user);
    }
}

