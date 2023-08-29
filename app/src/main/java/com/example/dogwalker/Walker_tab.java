package com.example.dogwalker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

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


        tabWalkerBinding.navView.findViewById(R.id.navigation_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.popup_item1) {
                    Intent intent = new Intent(getApplicationContext(), Owner_tab.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.popup_item2) {
//                    Intent intent2 = new Intent(getApplicationContext(), .class);
//                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}