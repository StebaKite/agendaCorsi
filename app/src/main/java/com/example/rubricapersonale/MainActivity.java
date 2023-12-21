package com.example.rubricapersonale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rubricapersonale.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SQLiteDatabase database;
    TableLayout tabContatti;
    TableRow tableRow;
    TextView nomeContatto, idContatto;
    Button inserisci; String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);

        // apro o creo il database esclusivo per questa app
        database = openOrCreateDatabase("contattiPersonali.db",MODE_PRIVATE,null);

        // creo la tabella contatti se non esiste
        query = "create table if not exists contatti(id integer primary key autoincrement, nome text, indirizzo text, telefono text, email text)";
        database.execSQL(query);

        displayElencoContatti();

        // listener per il bottone di inserimento nuovo contatto


    }

    private void displayElencoContatti() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.8);

        query = "select id, nome FROM contatti order by nome";
        Cursor resultset = database.rawQuery(query, null);

        int riga = 0;
        while (resultset.moveToNext()) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            // Caricamento ID contatto sul textView

            idContatto = new TextView(this);
            idContatto.setTextSize(18);
            idContatto.setPadding(0,10,0,10);
            idContatto.setGravity(Gravity.CENTER);
            idContatto.setText(String.valueOf(resultset.getInt(0)));
            idContatto.setWidth(larghezzaColonna1);
            if (riga % 2 == 1) idContatto.setBackgroundColor(Color.parseColor("#F0F0F0"));
            tableRow.addView(idContatto);

            // Caricamento del nome sul textView

            nomeContatto = new TextView(this);
            nomeContatto.setTextSize(18);
            nomeContatto.setPadding(0,10,0,10);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(resultset.getInt(1)));
            nomeContatto.setWidth(larghezzaColonna2);
            if (riga % 2 == 1) idContatto.setBackgroundColor(Color.parseColor("#F0F0F0"));
            tableRow.addView(nomeContatto);

            // qui dovrò settare il listener sul click della riga per vedere la scheda del contatto



            // aggiungo la riga alla tabella dei contatti

            tabContatti.addView(tableRow);
            riga++;
        }
    }




}