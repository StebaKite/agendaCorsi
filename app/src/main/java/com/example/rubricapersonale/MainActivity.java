package com.example.rubricapersonale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rubricapersonale.database.ContattiDAO;
import com.example.rubricapersonale.database.Contatto;
import com.example.rubricapersonale.database.DatabaseHelper;

import java.util.List;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            // codice
            return true;
        } else if (item.getTitle().equals("Dashboard")) {
            // codice
            return true;
        } else if (item.getTitle().equals("Notifications")) {
            // codice
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayElencoContatti() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.8);

        List<Contatto> contattiList = new ContattiDAO(this).getAll();

        int riga = 0;
        for (Contatto contatto : contattiList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            // Caricamento ID contatto sul textView

            idContatto = new TextView(this);
            idContatto.setTextSize(18);
            idContatto.setPadding(0,10,0,10);
            idContatto.setGravity(Gravity.CENTER);
            idContatto.setText(String.valueOf(contatto.getId()));
            idContatto.setWidth(larghezzaColonna1);
            if (riga % 2 == 1) idContatto.setBackgroundColor(Color.parseColor("#F0F0F0"));
            tableRow.addView(idContatto);

            // Caricamento del nome sul textView

            nomeContatto = new TextView(this);
            nomeContatto.setTextSize(18);
            nomeContatto.setPadding(0,10,0,10);
            nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(contatto.getNome()));
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
    }
}