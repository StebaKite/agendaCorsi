package com.example.agendaCorsi.ui.presenze;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;

import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.List;

public class ElencoFasceCorsiRunning extends FunctionBase {

    TableLayout tabellaFasceCorsi;
    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;
    Context elencoFasceCorsiRunning;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_fasce_corsi_running);
        elencoFasceCorsiRunning = this;

        esci = findViewById(R.id.bExit);
        tabellaFasceCorsi = findViewById(R.id.tabellaFasceCorsi);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna4 = (int) (displayMetrics.widthPixels * 0.2);

        testataElenco();
        loadFasceCorsi();
        listenerEsci(ElencoFasceCorsiRunning.this, MainActivity.class, null);
    }

    private void loadFasceCorsi() {
        List<Object> fasceCorsiList = FasciaDAO.getInstance().getAllFasceCorsiRunning(QueryComposer.getInstance().getQuery(QUERY_GETALL_FASCE_CORSI_RUNNING));

        String descrizione_corso_save = "";

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = (FasciaCorso) entity;

            String detailType = (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) ? DETAIL : DETAIL_CLOSED;
            int cellVisibility = (fasciaCorso.getDescrizioneCorso().equals(descrizione_corso_save)) ? View.INVISIBLE : View.VISIBLE;
            if (cellVisibility == View.VISIBLE) {
                descrizione_corso_save = fasciaCorso.getDescrizioneCorso();
            }

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna1, fasciaCorso.getDescrizioneCorso(), View.TEXT_ALIGNMENT_TEXT_START, cellVisibility));
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna2, fasciaCorso.getGiornoSettimana(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna3, fasciaCorso.getDescrizioneFascia(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna4, fasciaCorso.getTotaleFascia(), View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, 0, fasciaCorso.getIdFascia(), 0, View.GONE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, 0, fasciaCorso.getSport(), 0, View.GONE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, 0, fasciaCorso.getIdCorso(), 0, View.GONE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, 0, fasciaCorso.getStato(), 0, View.GONE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, 0, fasciaCorso.getTipoCorso(), 0, View.GONE));

            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {

                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TableRow tableRow = (TableRow) view;
                        TextView textView = (TextView) tableRow.getChildAt(4);
                        String idSelezionato = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(3);
                        int totaleFascia = Integer.parseInt(textView.getText().toString());

                        textView = (TextView) tableRow.getChildAt(0);
                        String descrizioneCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(1);
                        String giornoSettimana = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(2);
                        String descrizioneFascia = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(5);
                        String sport = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(6);
                        String idCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(7);
                        String statoCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(8);
                        String tipoCorso = textView.getText().toString();


                        Intent intent = new Intent(elencoFasceCorsiRunning, RegistraPresenze.class);
                        intent.putExtra("idFascia", idSelezionato);
                        intent.putExtra("descrizioneCorso", descrizioneCorso);
                        intent.putExtra("giornoSettimana", giornoSettimana);
                        intent.putExtra("descrizioneFascia", descrizioneFascia);
                        intent.putExtra("sport", sport);
                        intent.putExtra("idCorso", idCorso);
                        intent.putExtra("statoCorso", statoCorso);
                        intent.putExtra("tipoCorso", tipoCorso);

                        startActivity(intent);
                    }
                });
            }
            tabellaFasceCorsi.addView(tableRow);
        }
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(true);
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna1,"Nome corso", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna2,"Giorno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna3,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna4,"Totale", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
        tabellaFasceCorsi.addView(tableRow);
    }
}