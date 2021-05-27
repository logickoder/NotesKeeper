package com.jeffreyorazulike.noteskeeper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jeffreyorazulike.noteskeeper.databinding.ActivityMainBinding;
import com.jeffreyorazulike.noteskeeper.ui.note.NoteFragment;
import com.jeffreyorazulike.noteskeeper.ui.notelist.NoteListFragment;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;
    private ActivityMainBinding mBinding;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId();
        final NotesKeeperInterfaces.Function<NoteListFragment.ARGUMENTS.SHOW_VALUES, Void> show = value -> {
            Bundle bundle = new Bundle(1);
            bundle.putSerializable(NoteListFragment.ARGUMENTS.SHOW.name(), value);
            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.action_nav_note_list, bundle);
            return null;
        };
        if(id == R.id.nav_notes) show.fun(NoteListFragment.ARGUMENTS.SHOW_VALUES.NOTES);
        else if(id == R.id.nav_courses) show.fun(NoteListFragment.ARGUMENTS.SHOW_VALUES.COURSES);
        else{
            Snackbar.make(mBinding.getRoot(), item.getTitle(), Snackbar.LENGTH_SHORT).show();
        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}