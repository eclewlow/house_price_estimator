package sjsu.cmpe277.android.mapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG="MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d(TAG, "onCreate(Bundle) called");
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_signin);

        Button signInButton = (Button) findViewById(R.id.signin_button);

        signInButton.setOnClickListener(v->{
//            Intent i = new Intent(this, MapsActivity.class);
//            startActivity(i);
//            if(true)return;

            EditText emailEditText = (EditText) findViewById(R.id.email_address);
            String email = emailEditText.getText().toString();

            EditText passwordEditText = (EditText) findViewById(R.id.password);
            String password = passwordEditText.getText().toString();

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, R.string.form_complete_all_fields,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(SignInActivity.this, MapsActivity.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.\n"+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }
                        }
                    });
        });


        TextView tv = (TextView) findViewById(R.id.register_text);

        tv.setOnClickListener(v->{
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
}
