package com.example.carmate;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView currentAction;
    SearchView searchView;
    GoogleMap mMap;
    LatLng origin;
    LatLng destination;
    Polyline routePolyline;
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    ImageButton person1;
    ImageButton person2;
    ImageButton person3;
    String action;
    boolean showConnections = false;
    PopupWindow popupWindow;
    List<User> users;
    boolean[] selected = new boolean[3];
    Button startButton;
    Button groupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        users = new ArrayList<>();
//        users.add(new User("Jason", "Smith", R.drawable.jason, "passenger"));
//        users.add(new User("Jenny", "Green", R.drawable.jenny, "passenger")); //code for loading in test users
//        users.add(new User("Chris", "Crank", R.drawable.chris, "driver"));
//
//        saveUsers(users);
        users = loadUsers();
        Log.d("Users", users.get(0).getFirstName());

        Intent intent = getIntent();
        currentAction = findViewById(R.id.currentTextView);
        action = intent.getStringExtra("selection");
        currentAction.setText("Find " + action);
        person1 = findViewById(R.id.person1);
        person2 = findViewById(R.id.person2);
        person3 = findViewById(R.id.person3);
        person1.setVisibility(View.GONE);
        person2.setVisibility(View.GONE);
        person3.setVisibility(View.GONE);
        startButton = findViewById(R.id.startButton);
        startButton.setVisibility(View.GONE);
        groupButton = findViewById(R.id.viewButton);
        groupButton.setVisibility(View.GONE);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        View.OnClickListener buttonClickListener = v -> {
            String buttonTag = (String) v.getTag();
            showPopup(v, buttonTag);
        };
        person1.setOnClickListener(buttonClickListener);
        person2.setOnClickListener(buttonClickListener);
        person3.setOnClickListener(buttonClickListener);

    }

    private void showPopup(View anchorView, String buttonTag) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(anchorView.getRootView(), Gravity.CENTER, 0, 0);
        TextView title = popupView.findViewById(R.id.titleText);
        TextView userName = popupView.findViewById(R.id.userName);
        TextView rating = popupView.findViewById(R.id.userRating);
        TextView driver = popupView.findViewById(R.id.carDescription);

        Button addToGroup = popupView.findViewById(R.id.addToGroup);
        Button dismissButton = popupView.findViewById(R.id.dismissButton);

        if(buttonTag.equals("jason")){
            title.setText("Passenger");
            userName.setText(users.get(0).getFullName());
            rating.setText(String.format("%.1f", users.get(0).getRating()));
            driver.setVisibility(View.GONE);
            addToGroup.setText("Invite passenger");
        } else if(buttonTag.equals("jenny")){
            title.setText("Passenger");
            userName.setText(users.get(1).getFullName());
            rating.setText(String.format("%.1f", users.get(1).getRating()));
            addToGroup.setText("Invite passenger");
            driver.setVisibility(View.GONE);
        } else if(buttonTag.equals("chris")){
            title.setText("Driver");
            userName.setText(users.get(2).getFullName());
            rating.setText("Rating " + String.format("%.1f", users.get(2).getRating()));
            driver.setText("Red Toyota Corolla");
        }

        addToGroup.setOnClickListener(v -> {
            if(buttonTag.equals("jason")){
                person1.setVisibility(View.GONE);
                selected[0] = true;
            } else if(buttonTag.equals("jenny")){
                selected[1] = true;
                person2.setVisibility(View.GONE);
            } else if(buttonTag.equals("chris")){
                selected[2] = true;
                person3.setVisibility(View.GONE);
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            startButton.setVisibility(View.VISIBLE);
            groupButton.setVisibility(View.VISIBLE);
        });

        dismissButton.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
    }

    public void checkButtonVisibility(){
        if(showConnections && action.equals("passenger")){
            person1.setVisibility(View.VISIBLE);
            person2.setVisibility(View.VISIBLE);
        }else if(showConnections){
            person3.setVisibility(View.VISIBLE);
        }
    }

    public void startRide(View view){
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("selected", selected);
        intent.putExtra("users", (Serializable) users);
        startActivity(intent);
        finish();
    }

    public void viewGroupClick(View view){
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
        TextView title = popupView.findViewById(R.id.titleText);
        TextView group = popupView.findViewById(R.id.userName);
        TextView rating = popupView.findViewById(R.id.userRating);
        TextView driver = popupView.findViewById(R.id.carDescription);
        rating.setVisibility(View.GONE);
        driver.setVisibility(View.GONE);
        title.setText("Current Group");
        String current = "";
        for(int i = 0; i < selected.length; ++i){
            if(selected[i]){
                current += users.get(i).getFullName() + "\n";
            }
        }
        group.setText(current);
        Button addToGroup = popupView.findViewById(R.id.addToGroup);
        addToGroup.setVisibility(View.GONE);
        Button dismissButton = popupView.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        mMap = googleMap;
        LatLng kelowna = new LatLng(49.8801, -119.4436);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna, 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));

                if(origin == null){
                    origin = latLng;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 10));
                }else if (!latLng.equals(origin)){
                    destination = latLng;
                    fetchAndDrawRoute(origin, destination);
                }
            } else {
                Log.e("MapsAPI", "Location not found " + location);
            }
        } catch (IOException e) {
            Log.e("MapsAPI", "Error fetching results" + e.getMessage());
        }
    }

    private void fetchAndDrawRoute(LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=" + BuildConfig.DIRECTIONS_API_KEY;

         executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(() -> parseAndDrawRoute(responseData));
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    Log.e("DirectionsAPI", "Request failed. Response: " + errorBody);
                }
            } catch (IOException e) {
                Log.e("DirectionsAPI", "Error fetching results" + e.getMessage());
            }
        });
    }

    private void parseAndDrawRoute(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray routes = jsonObject.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String points = overviewPolyline.getString("points");
                List<LatLng> decodedPath = decodePolyline(points);

                if (routePolyline != null) {
                    routePolyline.remove();
                }

                routePolyline = mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng point : decodedPath) {
                    builder.include(point);
                }
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                showConnections = true;
                checkButtonVisibility();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        List<LatLng> poly = new java.util.ArrayList<>();
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new LatLng(
                    lat / 1E5, lng / 1E5
            ));
        }
        return poly;
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

}