package sjsu.cmpe277.android.mapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d(TAG, "onCreate(Bundle) called");


        setContentView(R.layout.activity_landing);

        Button signInButton = (Button) findViewById(R.id.signin_button);

        signInButton.setOnClickListener(v->{
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
        });

        Button registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(v->{
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
}
