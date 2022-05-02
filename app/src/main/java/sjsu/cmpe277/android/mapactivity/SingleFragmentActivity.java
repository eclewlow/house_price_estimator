package sjsu.cmpe277.android.mapactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private static final String TAG="MapActivity";

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.menu_item_information) {
//            Intent intent = new Intent(SingleFragmentActivity.this, InformationActivity.class);
//            startActivity(intent);
//        } else if(item.getItemId() == R.id.menu_item_uninstall) {
//            Intent intent = new Intent(Intent.ACTION_DELETE);
//            intent.setData(Uri.parse("package:"+getApplication().getPackageName()));
//            startActivity(intent);
//        }else {
//            return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }
}