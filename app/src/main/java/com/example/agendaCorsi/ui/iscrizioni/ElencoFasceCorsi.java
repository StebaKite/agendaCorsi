package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;
import java.util.List;

public class ElencoFasceCorsi extends FunctionBase {

    TableLayout tabellaFasceCorsi;
    TextView corso, giorno, fascia, totaleFascia, idFascia, idCorso, sport, statoCorso, tipoCorso;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_fasce_corsi);
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
        listenerEsci(ElencoFasceCorsi.this, MainActivity.class, null);
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(true);
        /**
         * Cella 0
         */
        corso = new TextView(this);
        corso.setTextSize(14);
        corso.setPadding(10,20,10,20);
        corso.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_heading));
        corso.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        corso.setTypeface(null, Typeface.BOLD);
        corso.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        corso.setGravity(Gravity.CENTER);
        corso.setText("Nome corso");
        corso.setWidth(larghezzaColonna1);
        tableRow.addView(corso);
        /**
         * Cella 1
         */
        giorno = new TextView(this);
        giorno.setTextSize(14);
        giorno.setPadding(10,20,10,20);
        giorno.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_heading));
        giorno.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        giorno.setTypeface(null, Typeface.BOLD);
        giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        giorno.setGravity(Gravity.CENTER);
        giorno.setText("Giorno");
        giorno.setWidth(larghezzaColonna2);
        tableRow.addView(giorno);
        /**
         * Cella 2
         */
        fascia = new TextView(this);
        fascia.setTextSize(14);
        fascia.setPadding(10,20,10,20);
        fascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_heading));
        fascia.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        fascia.setTypeface(null, Typeface.BOLD);
        fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        fascia.setGravity(Gravity.CENTER);
        fascia.setText("Fascia");
        fascia.setWidth(larghezzaColonna3);
        tableRow.addView(fascia);
        /**
         * Cella 3
         */
        totaleFascia = new TextView(this);
        totaleFascia.setTextSize(14);
        totaleFascia.setPadding(10,20,10,20);
        totaleFascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_heading));
        totaleFascia.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        totaleFascia.setTypeface(null, Typeface.BOLD);
        totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        totaleFascia.setGravity(Gravity.CENTER);
        totaleFascia.setText("Totale");
        totaleFascia.setWidth(larghezzaColonna4);
        tableRow.addView(totaleFascia);

        tabellaFasceCorsi.addView(tableRow);
    }

    private void loadFasceCorsi() {
        List<Object> fasceCorsiList = FasciaDAO.getInstance().getAllFasceCorsi(QueryComposer.getInstance().getQuery(QUERY_GETALL_FASCE_CORSI));

        String descrizione_corso_save = "";

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = (FasciaCorso) entity;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            /**
             * Cella 0
             */
            corso = new TextView(this);
            corso.setTextSize(14);
            corso.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                corso.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            } else {
                corso.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_closed));
            }
            corso.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            corso.setGravity(Gravity.CENTER);
            if (fasciaCorso.getDescrizioneCorso().equals(descrizione_corso_save)) {
                corso.setVisibility(View.INVISIBLE);
            }
            else {
                descrizione_corso_save = fasciaCorso.getDescrizioneCorso();
            }
            corso.setText(fasciaCorso.getDescrizioneCorso());
            corso.setWidth(larghezzaColonna1);
            tableRow.addView(corso);
            /**
             * Cella 1
             */
            giorno = new TextView(this);
            giorno.setTextSize(14);
            giorno.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                giorno.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            } else {
                giorno.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_closed));
            }
            giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno.setGravity(Gravity.CENTER);
            giorno.setText(fasciaCorso.getGiornoSettimana());
            giorno.setWidth(larghezzaColonna2);
            tableRow.addView(giorno);
            /**
             * Cella 2
             */
            fascia = new TextView(this);
            fascia.setTextSize(14);
            fascia.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                fascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            } else {
                fascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_closed));
            }
            fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia.setGravity(Gravity.CENTER);
            fascia.setText(fasciaCorso.getDescrizioneFascia());
            fascia.setWidth(larghezzaColonna3);
            tableRow.addView(fascia);
            /**
             * Cella 3
             */
            totaleFascia = new TextView(this);
            totaleFascia.setTextSize(14);
            totaleFascia.setPadding(10,20,10,20);
            if (isFasciaCapiente(fasciaCorso.getTotaleFascia(), fasciaCorso.getCapienza())) {
                totaleFascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            } else {
                totaleFascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border_closed));
            }
            totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            totaleFascia.setGravity(Gravity.CENTER);
            totaleFascia.setText(fasciaCorso.getTotaleFascia());
            totaleFascia.setWidth(larghezzaColonna4);
            tableRow.addView(totaleFascia);
            /**
             * Cella 4
             */
            idFascia = new TextView(this);
            idFascia.setVisibility(View.INVISIBLE);
            idFascia.setText(fasciaCorso.getIdFascia());
            tableRow.addView(idFascia);
            /**
             * Cella 5
             */
            sport = new TextView(this);
            sport.setVisibility(View.INVISIBLE);
            sport.setText(fasciaCorso.getSSport());
            tableRow.addView(sport);
            /**
             * Cella 6
             */
            idCorso = new TextView(this);
            idCorso.setVisibility(View.INVISIBLE);
            idCorso.setText(fasciaCorso.getIdCorso());
            tableRow.addView(idCorso);
            /**
             * Cella 7
             */
            statoCorso = new TextView(this);
            statoCorso.setVisibility(View.INVISIBLE);
            statoCorso.setText(fasciaCorso.getStato());
            tableRow.addView(statoCorso);
            /**
             * Cella 8
             */
            tipoCorso = new TextView(this);
            tipoCorso.setVisibility(View.INVISIBLE);
            tipoCorso.setText(fasciaCorso.getTipoCorso());
            tableRow.addView(tipoCorso);

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

                        /**
                         * Passo alla classe destinazione l'id della riga selezionata più tutti gli item inseriti nella intentMap
                         */
                        if (totaleFascia == 0) {
                            Intent intent = new Intent(AgendaCorsiApp.getContext(), NuovaIscrizione.class);
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
                        else {
                            Intent intent = new Intent(AgendaCorsiApp.getContext(), ElencoIscrizioni.class);
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
                    }
                });
            }
            tabellaFasceCorsi.addView(tableRow);
        }
    }
}
