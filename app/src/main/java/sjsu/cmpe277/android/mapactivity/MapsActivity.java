package sjsu.cmpe277.android.mapactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends AppCompatActivity {

//    private GoogleMap mMap;
//    private ActivityMapsBinding binding;
    // creating a variable
    // for search view.
//    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_fragment);

        Button mapButton = (Button) findViewById(R.id.map_button);
        mapButton.setOnClickListener(v->{
            FragmentManager fm = getSupportFragmentManager();
//            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            Fragment fragment = MapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        });

        Button estimateButton = (Button) findViewById(R.id.estimate_button);
        estimateButton.setOnClickListener(v->{
            FragmentManager fm = getSupportFragmentManager();
//            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            Fragment fragment = EstimateFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        });

        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(v->{
            FragmentManager fm = getSupportFragmentManager();
//            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            Fragment fragment = AboutFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = MapFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        // initializing our search view.
//        searchView = findViewById(R.id.search_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);

        // adding on query listener for our search view.
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // on below line we are getting the
//                // location name from search view.
//                String location = searchView.getQuery().toString();
//
//                // below line is to create a list of address
//                // where we will store the list of all address.
//                List<Address> addressList = null;
//
//                // checking if the entered location is null or not.
//                if (location != null || location.equals("")) {
//                    // on below line we are creating and initializing a geo coder.
//                    Geocoder geocoder = new Geocoder(MapsActivity.this);
//                    try {
//                        // on below line we are getting location from the
//                        // location name and adding that location to address list.
//                        addressList = geocoder.getFromLocationName(location, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    // on below line we are getting the location
//                    // from our list a first position.
//                    Address address = addressList.get(0);
//
//                    // on below line we are creating a variable for our location
//                    // where we will add our locations latitude and longitude.
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//                    // on below line we are adding marker to that position.
//                    MarkerOptions mo = new MarkerOptions().position(latLng).title(location).snippet("hi");
//                    mMap.addMarker(mo);
//
//                    // below line is to animate camera to that position.
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        mapFragment.getMapAsync(this);
    }
    public void goToEstimate() {
        FragmentManager fm = getSupportFragmentManager();
//            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        Fragment fragment = EstimateFragment.newInstance();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}