package com.example.rubricapersonale;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rubricapersonale.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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

        // implemento il listener per il bottone di inserimento nuovo contatto

        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NuovoContatto.class);
                startActivity(intent);
            }
        });
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
            nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(resultset.getString(1)));
            nomeContatto.setWidth(larghezzaColonna2);
            if (riga % 2 == 1) nomeContatto.setBackgroundColor(Color.parseColor("#F0F0F0"));
            tableRow.addView(nomeContatto);

            // implemento il listener sul click della riga per vedere la scheda del contatto
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;        // in view c'è la riga selezionata
                    TextView textView = (TextView) tableRow.getChildAt(0);    // idcontatto
                    Integer idContattoSelezionato = Integer.parseInt(textView.getText().toString());

                    // Passo all'attività ModificaContatto l'id del contatto selezionato
                    Intent intent = new Intent(MainActivity.this, ModificaContatto.class);
                    intent.putExtra("id", idContattoSelezionato);
                    startActivity(intent);
                }
            });


            // aggiungo la riga alla tabella dei contatti
            tabContatti.addView(tableRow);
            riga++;
        }
        resultset.close();
    }




}