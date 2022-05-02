package sjsu.cmpe277.android.mapactivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class EstimateFragment extends Fragment {
    private static final String ARG_SYNTH_ID = "synth_id";
    private static final String TAG = EstimateFragment.class.getSimpleName();
    private static final int IO_BUFFER_SIZE = 4 * 1024;

    protected EditText mHousePrice;
    protected Button mEstimatePrice;

    public static EstimateFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_SYNTH_ID, synthId);

        EstimateFragment fragment = new EstimateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID synthId = (UUID) getArguments().getSerializable(ARG_SYNTH_ID);
//        mSynth = SynthLab.get(getActivity()).getSynth(synthId);

        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estimate, container, false);

        User user = User.get(getContext());
        Address address = user.getAddress();

        EditText latitude = (EditText) v.findViewById(R.id.latitude_edit);
        latitude.setEnabled(false);

        EditText longitude = (EditText) v.findViewById(R.id.longitude_edit);
        longitude.setEnabled(false);

        EditText city = (EditText) v.findViewById(R.id.city_edit);
        city.setEnabled(false);

        ImageView staticMap = (ImageView) v.findViewById(R.id.static_map_image);
        staticMap.setVisibility(View.INVISIBLE);

        ProgressBar pb = (ProgressBar) v.findViewById(R.id.progress_bar);

        EditText homeSizeEditText = (EditText) v.findViewById(R.id.home_size_edit);
        homeSizeEditText.setText(Double.toString(user.getHomeSize()));
        homeSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    user.setHomeSize(Double.parseDouble(s.toString()));
                } catch(NumberFormatException e) {
                    user.setHomeSize(0.0);
                }
            }
        });

        EditText lotSizeEditText = (EditText) v.findViewById(R.id.lot_size_edit);
        lotSizeEditText.setText(Double.toString(user.getLotSize()));
        lotSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    user.setLotSize(Double.parseDouble(s.toString()));
                } catch(NumberFormatException e) {
                    user.setLotSize(0.0);
                }
            }
        });

        EditText bedroomEditText = (EditText) v.findViewById(R.id.bedroom_edit);
        bedroomEditText.setEnabled(false);
        TextView bedroomValue = (TextView) v.findViewById(R.id.bedroom_value);
        bedroomValue.setText(Integer.toString(user.getBeds()));
        Button bedroomPlus = (Button) v.findViewById(R.id.bedroom_plus);
        bedroomPlus.setOnClickListener(view->{
            if(user.getBeds() == 9) return;
            user.setBeds(user.getBeds()+1);
            bedroomValue.setText(Integer.toString(user.getBeds()));
        });
        Button bedroomMinus = (Button) v.findViewById(R.id.bedroom_minus);
        bedroomMinus.setOnClickListener(view->{
            if(user.getBeds() == 0) return;
            user.setBeds(user.getBeds()-1);
            bedroomValue.setText(Integer.toString(user.getBeds()));
        });

        EditText bathroomEditText = (EditText) v.findViewById(R.id.bathroom_edit);
        bathroomEditText.setEnabled(false);
        TextView bathroomValue = (TextView) v.findViewById(R.id.bathroom_value);
        bathroomValue.setText(Integer.toString(user.getBathrooms()));
        Button bathroomPlus = (Button) v.findViewById(R.id.bathroom_plus);
        bathroomPlus.setOnClickListener(view->{
            if(user.getBathrooms() == 9) return;
            user.setBathrooms(user.getBathrooms()+1);
            bathroomValue.setText(Integer.toString(user.getBathrooms()));
        });
        Button bathroomMinus = (Button) v.findViewById(R.id.bathroom_minus);
        bathroomMinus.setOnClickListener(view->{
            if(user.getBathrooms() == 0) return;
            user.setBathrooms(user.getBathrooms()-1);
            bathroomValue.setText(Integer.toString(user.getBathrooms()));
        });

        mEstimatePrice = (Button) v.findViewById(R.id.estimate_button);
        mEstimatePrice.setOnClickListener(view->{
            // create object of MyAsyncTasks class and execute it
            HerokuRequest herokuRequest = new HerokuRequest();
            herokuRequest.execute();
        });

        mHousePrice = (EditText) v.findViewById(R.id.house_price);
        mHousePrice.setEnabled(false);

        if(address != null) {
            String lat =Double.toString(address.getLatitude());
            String lng = Double.toString(address.getLongitude());

            latitude.setText(lat);
            longitude.setText(lng);

            city.setText(address.getLocality() + ", "+address.getAdminArea());

            ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        v.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        try {
                            ApplicationInfo app = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(),PackageManager.GET_META_DATA);
                            Bundle bundle = app.metaData;

                            String API_KEY = bundle.getString("com.google.android.geo.API_KEY");
                            String url = "http://maps.google.com/maps/api/staticmap?center=" +
                                    lat + "," + lng +
                                    "&zoom=16&size=" +
                                    staticMap.getMeasuredWidth() +
                                    "x" +
                                    staticMap.getMeasuredHeight() +
                                    "&sensor=false&key="+API_KEY;

                            Thread thread = new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    try {
                                        //Your code goes here
                                        Bitmap bmp = loadBitmap(url);

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                staticMap.setVisibility(View.VISIBLE);
                                                staticMap.setImageBitmap(bmp);
                                                pb.setVisibility(View.INVISIBLE);

                                            }
                                        });

                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }
                            });
                            thread.start();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

//        SearchView searchView = (SearchView) v.findViewById(R.id.search_view);

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

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);

            int bytesRead;
            byte[] buffer = new byte[IO_BUFFER_SIZE];

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();


            final byte[] data = dataStream.toByteArray();

            BitmapFactory.Options options = new BitmapFactory.Options();

            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);

            in.close();
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url + ", " +e.toString()+  ", " + e.getMessage());
        } finally {
        }

        return bitmap;
    }

    public class HerokuRequest extends AsyncTask<String, String, String> {
        public HerokuRequest() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, s);

            // dismiss the progress dialog after receiving data from API
//            progressDialog.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(s);

                double prediction = Double.parseDouble(jsonObject.getString("prediction"));
                double roundedPrediction = Double.parseDouble(jsonObject.getString("rounded_prediction"));

                DecimalFormat df = new DecimalFormat("0.00");
                mHousePrice.setText("$ " + df.format(prediction));
//                JSONArray jsonArray1 = jsonObject.getJSONArray("users");

//                JSONObject jsonObject1 =jsonArray1.getJSONObject(index_no);
//                String id = jsonObject1.getString("id");
//                String name = jsonObject1.getString("name");
//                String my_users = "User ID: "+id+"\n"+"Name: "+name;

                //Show the Textview after fetching data
//                resultsTextView.setVisibility(View.VISIBLE);

                //Display data with the Textview
//                resultsTextView.setText(my_users);

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            mHousePrice.setText("hi");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("https://machine-learning-mobile-app.herokuapp.com/prediction");
                    //open a URL coonnection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");

                    urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setRequestProperty("Accept","*/*");

                    User user = User.get(getContext());

                    Map<String, String> m = new HashMap<>();
                    m.put("home_size", Double.toString(user.getHomeSize()));
                    m.put("lot_size", Double.toString(user.getLotSize()));
                    m.put("beds", Integer.toString(user.getBeds()));
                    m.put("baths", Double.toString(user.getBathrooms()));
                    m.put("latitude", Double.toString(user.getAddress().getLatitude()));
                    m.put("longitude", Double.toString(user.getAddress().getLongitude()));
                    m.put("city", user.getAddress().getLocality());

                    JSONObject json = new JSONObject(m);

//                    String jsonInputString = "{"name": "Upendra", "job": "Programmer"}";
                    Log.e(TAG, json.toString());
                    // Enable writing
                    urlConnection.setDoOutput(true);

                    // Write the data
                    urlConnection.connect();

                    OutputStream os = urlConnection.getOutputStream();
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
//                    urlConnection.connect();

                    InputStream in;
//                    in = urlConnection.getErrorStream();
                    in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }

                    Log.e(TAG, result);
                    // return the data to onPostExecute method
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }
    }
}
