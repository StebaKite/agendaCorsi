package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_fasce_corsi);
        esci = findViewById(R.id.bExit);
        tabellaFasceCorsi = findViewById(R.id.tabellaFasceCorsi);

        displayElencoFasceCorsi();
        listenerEsci(ElencoFasceCorsi.this, MainActivity.class, null);
    }

    private void displayElencoFasceCorsi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaCorso = (int) (displayMetrics.widthPixels * 0.3);
        int larghezzaColonnaGiorno = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.3);
        int larghezzaColonnaTotale = (int) (displayMetrics.widthPixels * 0.1);

        List<Object> fasceCorsiList = FasciaDAO.getInstance().getAllFasceCorsi(QueryComposer.getInstance().getQuery(QUERY_GETALL_FASCE_CORSI));

        String descrizione_corso_save = "";

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = (FasciaCorso) entity;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            corso = new TextView(this);
            corso.setTextSize(16);
            corso.setPadding(10,20,10,20);
            corso.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            corso.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            corso.setGravity(Gravity.CENTER);
            corso.setText(fasciaCorso.getDescrizioneCorso());
            corso.setWidth(larghezzaColonnaCorso);
            tableRow.addView(corso);

            giorno = new TextView(this);
            giorno.setTextSize(16);
            giorno.setPadding(10,20,10,20);
            giorno.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno.setGravity(Gravity.CENTER);
            giorno.setText(fasciaCorso.getGiornoSettimana());
            giorno.setWidth(larghezzaColonnaGiorno);
            tableRow.addView(giorno);

            fascia = new TextView(this);
            fascia.setTextSize(16);
            fascia.setPadding(10,20,10,20);
            fascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia.setGravity(Gravity.CENTER);
            fascia.setText(fasciaCorso.getDescrizioneFascia());
            fascia.setWidth(larghezzaColonnaFascia);
            tableRow.addView(fascia);

            totaleFascia = new TextView(this);
            totaleFascia.setTextSize(16);
            totaleFascia.setPadding(10,20,10,20);
            totaleFascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            totaleFascia.setGravity(Gravity.CENTER);
            totaleFascia.setText(fasciaCorso.getTotaleFascia());
            totaleFascia.setWidth(larghezzaColonnaTotale);
            tableRow.addView(totaleFascia);

            idFascia = new TextView(this);
            idFascia.setVisibility(View.INVISIBLE);
            idFascia.setText(fasciaCorso.getIdFascia());
            tableRow.addView(idFascia);

            sport = new TextView(this);
            sport.setVisibility(View.INVISIBLE);
            sport.setText(fasciaCorso.getSSport());
            tableRow.addView(sport);

            idCorso = new TextView(this);
            idCorso.setVisibility(View.INVISIBLE);
            idCorso.setText(fasciaCorso.getIdCorso());
            tableRow.addView(idCorso);

            statoCorso = new TextView(this);
            statoCorso.setVisibility(View.INVISIBLE);
            statoCorso.setText(fasciaCorso.getStato());
            tableRow.addView(statoCorso);

            tipoCorso = new TextView(this);
            tipoCorso.setVisibility(View.INVISIBLE);
            tipoCorso.setText(fasciaCorso.getStato());
            tableRow.addView(tipoCorso);

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
            tabellaFasceCorsi.addView(tableRow);
        }
    }
}
