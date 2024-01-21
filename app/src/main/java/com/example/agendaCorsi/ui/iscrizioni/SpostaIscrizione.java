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

    TextView _corso, _giorno, _fascia, _totaleFascia, _idFascia, _idCorso, _sport, _statoCorso, _tipoCorso;
    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    TableLayout tabellaFasce;
    Context spostaIscrizione;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;

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

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna4 = (int) (displayMetrics.widthPixels * 0.2);

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
        /**
         * Cella 0
         */
        _giorno = new TextView(this);
        _giorno.setTextSize(14);
        _giorno.setPadding(10,20,10,20);
        _giorno.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_heading));
        _giorno.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        _giorno.setTypeface(null, Typeface.BOLD);
        _giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        _giorno.setGravity(Gravity.CENTER);
        _giorno.setText("Giorno");
        _giorno.setWidth(larghezzaColonna2);
        tableRow.addView(_giorno);
        /**
         * Cella 1
         */
        _fascia = new TextView(this);
        _fascia.setTextSize(14);
        _fascia.setPadding(10,20,10,20);
        _fascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_heading));
        _fascia.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        _fascia.setTypeface(null, Typeface.BOLD);
        _fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        _fascia.setGravity(Gravity.CENTER);
        _fascia.setText("Fascia");
        _fascia.setWidth(larghezzaColonna3);
        tableRow.addView(_fascia);
        /**
         * Cella 2
         */
        _totaleFascia = new TextView(this);
        _totaleFascia.setTextSize(14);
        _totaleFascia.setPadding(10,20,10,20);
        _totaleFascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_heading));
        _totaleFascia.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        _totaleFascia.setTypeface(null, Typeface.BOLD);
        _totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        _totaleFascia.setGravity(Gravity.CENTER);
        _totaleFascia.setText("Totale");
        _totaleFascia.setWidth(larghezzaColonna4);
        tableRow.addView(_totaleFascia);

        tabellaFasce.addView(tableRow);
    }

    private void loadFasceCorso() {
        List<Object> fasceCorsiList = FasciaDAO.getInstance().getAllFasceDisponibili(idCorso, idFascia, QueryComposer.getInstance().getQuery(QUERY_GETALL_FASCE_DISPONIBILI));

        tableRow = new TableRow(this);
        tableRow.setClickable(true);

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = (FasciaCorso) entity;
            /**
             * Cella 0
             */
            _giorno = new TextView(this);
            _giorno.setTextSize(14);
            _giorno.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                _giorno.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border));
            } else {
                _giorno.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_closed));
            }
            _giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            _giorno.setGravity(Gravity.CENTER);
            _giorno.setText(fasciaCorso.getGiornoSettimana());
            _giorno.setWidth(larghezzaColonna2);
            tableRow.addView(_giorno);
            /**
             * Cella 1
             */
            _fascia = new TextView(this);
            _fascia.setTextSize(14);
            _fascia.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                _fascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border));
            } else {
                _fascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_closed));
            }
            _fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            _fascia.setGravity(Gravity.CENTER);
            _fascia.setText(fasciaCorso.getDescrizioneFascia());
            _fascia.setWidth(larghezzaColonna3);
            tableRow.addView(_fascia);
            /**
             * Cella 2
             */
            _totaleFascia = new TextView(this);
            _totaleFascia.setTextSize(14);
            _totaleFascia.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                _totaleFascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border));
            } else {
                _totaleFascia.setBackground(ContextCompat.getDrawable(SpostaIscrizione.this, R.drawable.cell_border_closed));
            }
            _totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            _totaleFascia.setGravity(Gravity.CENTER);
            _totaleFascia.setText(fasciaCorso.getTotaleFascia());
            _totaleFascia.setWidth(larghezzaColonna4);
            tableRow.addView(_totaleFascia);
            /**
             * Cella 3
             */
            _idFascia = new TextView(this);
            _idFascia.setVisibility(View.INVISIBLE);
            _idFascia.setText(fasciaCorso.getIdFascia());
            tableRow.addView(_idFascia);

            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {

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
