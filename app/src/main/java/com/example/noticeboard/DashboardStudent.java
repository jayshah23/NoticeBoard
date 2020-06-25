package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.noticeboard.ui.files.FilesFragment;
import com.example.noticeboard.ui.list.ListFragment;
import com.example.noticeboard.ui.about.AboutFragment;
import com.example.noticeboard.ui.home.HomeFragment;
import com.example.noticeboard.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardStudent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_st);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        if (!user.isEmailVerified()) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayoutStudent), "Email not verified", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("Verify now!!!", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DashboardStudent.this, "Email verification link sent to "+user, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DashboardStudent.this, "Email verification failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_signout:
                firebaseAuth.getInstance().signOut();
                Intent i = new Intent(this,IntroActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_svv:
                Intent k = new Intent(Intent.ACTION_VIEW);
                k.setData(Uri.parse("http://"+"myaccount.somaiya.edu/#/login"));
                startActivity(k);
                break;

            case R.id.nav_website:
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse("http://"+"kjsieit.somaiya.edu/en"));
                startActivity(j);
                break;

            case R.id.nav_about :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AboutFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_list_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ListFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_files:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new FilesFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}