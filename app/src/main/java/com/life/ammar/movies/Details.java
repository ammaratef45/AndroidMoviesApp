package com.life.ammar.movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment detailFragment=new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("idAsInt", getIntent().getExtras().getInt("idAsInt"));
        detailFragment.setArguments(args);
        transaction.add(R.id.container2, detailFragment);
        transaction.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
