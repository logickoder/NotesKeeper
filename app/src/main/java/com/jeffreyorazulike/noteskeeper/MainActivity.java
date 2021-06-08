package com.jeffreyorazulike.noteskeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jeffreyorazulike.noteskeeper.databinding.ActivityMainBinding;
import com.jeffreyorazulike.noteskeeper.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;
    private ActivityMainBinding mBinding;
    private final MutableLiveData<HomeFragment.SHOW_VALUES> mHomeFragmentCommunication = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.ab.toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_note_list, R.id.nav_note)
                .setDrawerLayout(mBinding.drawerLayout)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(mBinding.navView, mNavController);
        mBinding.navView.setNavigationItemSelectedListener(this);
        selectMenu(R.id.nav_notes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_FROM_BACKGROUND);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId();

        if(id == R.id.nav_notes) 
            mHomeFragmentCommunication.setValue(HomeFragment.SHOW_VALUES.NOTES);
        else if(id == R.id.nav_courses) 
            mHomeFragmentCommunication.setValue(HomeFragment.SHOW_VALUES.COURSES);
        else if (id == R.id.nav_share) 
            showSnackbar("Share to - " + PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(getString(R.string.pref_key_favourite_social),""));
        else showSnackbar(item.getTitle().toString());

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public LiveData<HomeFragment.SHOW_VALUES> getHomeFragmentLiveData() {
        return mHomeFragmentCommunication;
    }

    private void selectMenu(final int id){
        mBinding.navView.getMenu().findItem(id).setChecked(true);
    }

    private void showSnackbar(final String message){
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateNavHeader() {
        View header = mBinding.navView.getHeaderView(0);
        TextView userName = header.findViewById(R.id.tv_user_name);
        TextView email = header.findViewById(R.id.tv_email_address);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        userName.setText(pref.getString(getString(R.string.pref_key_display_name), ""));
        email.setText(pref.getString(getString(R.string.pref_key_email_address), ""));
    }
}