package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.ui.found.FoundFragment;
import com.example.myapplication.ui.post.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d("FCM Token", token);
                        // Save the token to use later
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });
        toolbar=findViewById(R.id.toolbar1);
        Menu menu = toolbar.getMenu();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId=item.getItemId();
                if(ItemId==R.id.Home){
                    openFragment(new Home());
                    return true;
                } else if (ItemId==R.id.Post) {
                    openFragment(new PostFragment());
                    return true;
                } else if (ItemId==R.id.Found) {
                    openFragment(new FoundFragment());
                    return true;
                }else if (ItemId==R.id.AddPost) {
                    openFragment(new AddPost());
                    return true;
                }
                return false;
            }
        });


        fragmentManager= getSupportFragmentManager();
        openFragment(new Home());
    }


    private void sendNotification(String title, String messageBody) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        new NotificationSender().sendNotification(token, title, messageBody);
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId= menuItem.getItemId();
        if(itemId==R.id.account){
            startActivity(new Intent(this, AccountDetails.class));
        }
        else if (itemId==R.id.chat) {
            startActivity(new Intent(this, ChatM.class));
        }
        else if (itemId==R.id.help) {
            openFragment(new Help());
        }
        else if (itemId==R.id.about) {
            openFragment(new About());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


   private void openFragment(Fragment fragment)
   {
       FragmentTransaction transaction=fragmentManager.beginTransaction();
       transaction.replace(R.id.fragment_container,fragment);
       transaction.commit();
   }


}