package sjsu.cmpe277.android.mapactivity;

import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.net.PlacesClient;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_SYNTH_ID = "synth_id";
        private GoogleMap mMap;
    private static final String TAG = MapFragment.class.getSimpleName();
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
//    private ActivityMapsBinding binding;
    // creating a variable
    // for search view.
    SearchView searchView;
    public static MapFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_SYNTH_ID, synthId);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID synthId = (UUID) getArguments().getSerializable(ARG_SYNTH_ID);
//        mSynth = SynthLab.get(getActivity()).getSynth(synthId);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        SearchView searchView = (SearchView) v.findViewById(R.id.search_view);

        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.location_details_box);
        cl.setVisibility(View.GONE);

        Button chooseLocationButton = (Button) v.findViewById(R.id.choose_your_location);
        chooseLocationButton.setEnabled(false);

        chooseLocationButton.setOnClickListener(view->{
            ((MapsActivity)getActivity()).goToEstimate();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        UiSettings.setZoomGesturesEnabled(boolean);

        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address;

                    try {
                      address = addressList.get(0);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), R.string.map_not_found,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    MarkerOptions mo = new MarkerOptions().position(latLng);
                    mMap.addMarker(mo);

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));

                    TextView tv = v.findViewById(R.id.location_details);

                    tv.setText("Latitude: " + address.getLatitude() +
                            ", Longitude" + address.getLongitude() +
                            "\n" + address.getAddressLine(0));

                    User.get(getContext()).setAddress(address);
                    cl.setVisibility(View.VISIBLE);
                    chooseLocationButton.setEnabled(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                chooseLocationButton.setEnabled(false);
                cl.setVisibility(View.GONE);
                return false;
            }
        });

//        mTitle = (TextView) v.findViewById(R.id.synth_title_label);
//        mTitle.setText(mSynth.getTitleResId());
//
//        mDesc = (TextView) v.findViewById(R.id.synth_desc_label);
//        mDesc.setText(mSynth.getDescResId());
//
//        mImage = (ImageView) v.findViewById(R.id.synth_image_label);
//        mImage.setImageResource(mSynth.getImageResId());

        return v;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }
}
