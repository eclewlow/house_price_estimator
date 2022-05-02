package sjsu.cmpe277.android.mapactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {
    private static final String ARG_SYNTH_ID = "synth_id";

    private static final String TAG = AboutFragment.class.getSimpleName();


    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_SYNTH_ID, synthId);

        AboutFragment fragment = new AboutFragment();
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

    public Intent createEmailIntent() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "eugene.clewlow@sjsu.edu");

//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject.getText());

//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext.getText());

//        Email.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        return emailIntent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        TextView emailUs = (TextView) v.findViewById(R.id.email_us_text);
        emailUs.setOnClickListener(view->{
            Intent emailIntent = createEmailIntent();
            startActivity(emailIntent);
        });

        ImageView emailUsButton = (ImageView) v.findViewById(R.id.email_us_button);
        emailUsButton.setOnClickListener(view->{
            Intent emailIntent = createEmailIntent();
            startActivity(emailIntent);
        });

        TextView websiteText = (TextView) v.findViewById(R.id.website_text);
        websiteText.setOnClickListener(view->{
            String url = "http://www.google.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        ImageView websiteButton = (ImageView) v.findViewById(R.id.website_button);
        websiteButton.setOnClickListener(view->{
            String url = "http://www.google.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

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

}
