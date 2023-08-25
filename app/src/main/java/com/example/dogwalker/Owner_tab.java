package com.example.dogwalker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dogwalker.databinding.TabOwnerBinding;
import com.example.dogwalker.ui.home.HomeFragment;
import com.example.dogwalker.ui.map.MapFragment;
import com.example.dogwalker.ui.mypage.OwnerMyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Owner_tab extends AppCompatActivity {
    private TabOwnerBinding tabOwnerBinding;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    OwnerMyPageFragment ownerMyPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabOwnerBinding = TabOwnerBinding.inflate(getLayoutInflater());
        setContentView(tabOwnerBinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(tabOwnerBinding.navView, navController);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        ownerMyPageFragment = new OwnerMyPageFragment();



//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.nav_host_fragment_activity_main3, homeFragment).commitAllowingStateLoss();




    }

}