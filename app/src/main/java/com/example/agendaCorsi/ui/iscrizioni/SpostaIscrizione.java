package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ModificaFascia;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;


public class SpostaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    TableLayout tabellaFasce;
    Context spostaIscrizione;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sposta_iscrizione);
        spostaIscrizione = this;

        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        idIscrizione = intent.getStringExtra("idIscrizione");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");
        nomeIscritto = intent.getStringExtra("nomeIscritto");
        statoIscrizione = intent.getStringExtra("statoIscrizione");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _nomeIscritto = findViewById(R.id.editNomeIscritto);
        tabellaFasce = findViewById(R.id.tabellaFasce);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);
        _nomeIscritto.setText(nomeIscritto);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("idIscrizione", idIscrizione);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("nomeIscritto", nomeIscritto);
        intentMap.put("statoIscrizione", statoIscrizione);

        testataelenco();
        loadFasceCorso();

        listenerEsci(spostaIscrizione, ElencoIscrizioni.class, intentMap);
    }

    private void testataelenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna1,"Giorno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna2,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna3,"Totale", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
    }

    private void loadFasceCorso() {
        List<Object> fasceCorsiList = FasciaDAO.getInstance().getAllFasceDisponibili(idCorso, idFascia, QueryComposer.getInstance().getQuery(QUERY_GETALL_FASCE_DISPONIBILI));

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = (FasciaCorso) entity;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            String stato = (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) ? DETAIL : DETAIL_CLOSED;

            tableRow.addView(makeCell(this,new TextView(this), stato, larghezzaColonna1, fasciaCorso.getGiornoSettimana(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), stato, larghezzaColonna2, fasciaCorso.getDescrizioneFascia(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), stato, larghezzaColonna3, fasciaCorso.getTotaleFascia(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), stato, 0, fasciaCorso.getIdFascia(), 0, View.GONE));

            if (stato.equals(DETAIL)) {

                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TableRow tableRow = (TableRow) view;
                        TextView textView = (TextView) tableRow.getChildAt(3);
                        String idSelezionato = textView.getText().toString();

                        Iscrizione iscrizione = new Iscrizione(idIscrizione, idSelezionato, null, null, null, null);
                        if (IscrizioneDAO.getInstance().update(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_ISCRIZIONE))) {
                            Toast.makeText(spostaIscrizione, "Iscrizione spostata con successo.", Toast.LENGTH_LONG).show();
                            esci.callOnClick();
                        } else {
                            displayAlertDialog(spostaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                        }
                    }
                });
            }
            tabellaFasce.addView(tableRow);
        }
    }
}
