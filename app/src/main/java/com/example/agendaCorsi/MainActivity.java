package com.example.agendaCorsi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.agendaCorsi.ui.corsi.ElencoCorsi;
import com.example.agendacorsi.R;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Contatti")) {
            Intent intent = new Intent(MainActivity.this, ElencoContatti.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("Corsi")) {
            Intent intent = new Intent(MainActivity.this, ElencoCorsi.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("Notifications")) {
            // codice
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}