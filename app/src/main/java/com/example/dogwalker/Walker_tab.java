package com.example.dogwalker;

import android.os.Bundle;

import com.example.dogwalker.databinding.TabWalkerBinding;
import com.example.dogwalker.ui.home.HomeFragment;
import com.example.dogwalker.ui.map.MapFragment;
import com.example.dogwalker.ui.mypage.WalkerMyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



public class Walker_tab extends AppCompatActivity {
    private TabWalkerBinding tabWalkerBinding;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    WalkerMyPageFragment walkerMyPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabWalkerBinding = TabWalkerBinding.inflate(getLayoutInflater());
        setContentView(tabWalkerBinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main3);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(tabWalkerBinding.navView, navController);

        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        walkerMyPageFragment = new WalkerMyPageFragment();



//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.nav_host_fragment_activity_main3, homeFragment).commitAllowingStateLoss();




    }

}