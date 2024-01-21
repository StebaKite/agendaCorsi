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

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.ContattoIscritto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ElencoIscrizioni extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TextView nome_contatto, id_iscrizione, stato, eta;
    TableLayout _tabellaContattiIscritti;
    Context elencoIscrizioni;

    int larghezzaColonna1;
    int larghezzaColonna2;
    int larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_iscrizioni);
        elencoIscrizioni = this;

        esci = findViewById(R.id.bExit);
        inserisci = findViewById(R.id.bNuovaIscrizione);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        makeIntestazioneTabella(larghezzaColonna1, larghezzaColonna2, larghezzaColonna3);
        loadContattiIscritti();

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("tipoCorso", tipoCorso);

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsi.class, null);
        listenerInserisci(AgendaCorsiApp.getContext(), NuovaIscrizione.class, intentMap);
    }

    private void loadContattiIscritti() {


        List<Object> contattiIscrittiList = ContattiDAO.getInstance().getIscritti(idFascia, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTI_ISCRITTI));

        for (Object object : contattiIscrittiList) {
            ContattoIscritto contattoIscritto = (ContattoIscritto) object;

            tableRow = new TableRow(elencoIscrizioni);
            tableRow.setClickable(true);

            nome_contatto = new TextView(elencoIscrizioni);
            nome_contatto.setTextSize(14);
            nome_contatto.setPadding(10,20,10,20);
            if (contattoIscritto.getStato().equals(STATO_CHIUSO)) {
                nome_contatto.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_closed));
            } else {
                nome_contatto.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border));
            }
            nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nome_contatto.setGravity(Gravity.CENTER);
            nome_contatto.setText(contattoIscritto.getNomeContatto());
            nome_contatto.setWidth(larghezzaColonna1);
            tableRow.addView(nome_contatto);

            eta = new TextView(elencoIscrizioni);
            eta.setTextSize(14);
            eta.setPadding(10,20,10,20);
            if (contattoIscritto.getStato().equals(STATO_CHIUSO)) {
                eta.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_closed));
            } else {
                eta.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border));
            }
            eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            eta.setGravity(Gravity.CENTER);
            eta.setText(String.valueOf(computeAge(contattoIscritto.getDataNascita())));
            eta.setWidth(larghezzaColonna2);
            tableRow.addView(eta);

            stato = new TextView(elencoIscrizioni);
            stato.setTextSize(14);
            stato.setPadding(10,20,10,20);
            if (contattoIscritto.getStato().equals(STATO_CHIUSO)) {
                stato.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_closed));
            } else {
                stato.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border));
            }
            stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            stato.setGravity(Gravity.CENTER);
            stato.setText(contattoIscritto.getStato());
            stato.setWidth(larghezzaColonna3);
            tableRow.addView(stato);

            id_iscrizione = new TextView(elencoIscrizioni);
            id_iscrizione.setText(contattoIscritto.getIdIscrizione());
            id_iscrizione.setVisibility(View.INVISIBLE);
            tableRow.addView(id_iscrizione);

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("descrizioneCorso", descrizioneCorso);
            intentMap.put("descrizioneFascia", descrizioneFascia);
            intentMap.put("giornoSettimana", giornoSettimana);
            intentMap.put("sport", sport);
            intentMap.put("idCorso", idCorso);
            intentMap.put("idFascia", idFascia);
            intentMap.put("idIscrizione", contattoIscritto.getIdIscrizione());
            intentMap.put("statoCorso", statoCorso);
            intentMap.put("nomeIscritto", contattoIscritto.getNomeContatto());
            intentMap.put("statoIscrizione", contattoIscritto.getStato());
            intentMap.put("tipoCorso", tipoCorso);

            listenerTableRow(elencoIscrizioni, ModificaIscrizione.class, "idIscrizione", intentMap, 2);

            _tabellaContattiIscritti.addView(tableRow);
        }
    }

    private void makeIntestazioneTabella(int larghezzaColonna1, int larghezzaColonna2, int larghezzaColonna3) {
        tableRow = new TableRow(elencoIscrizioni);
        tableRow.setClickable(false);

        nome_contatto = new TextView(elencoIscrizioni);
        nome_contatto.setTextSize(16);
        nome_contatto.setPadding(10,20,10,20);
        nome_contatto.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_heading));
        nome_contatto.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        nome_contatto.setTypeface(null, Typeface.BOLD);
        nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        nome_contatto.setGravity(Gravity.CENTER);
        nome_contatto.setText("Nome");
        nome_contatto.setWidth(larghezzaColonna1);
        tableRow.addView(nome_contatto);

        eta = new TextView(elencoIscrizioni);
        eta.setTextSize(16);
        eta.setPadding(10,20,10,20);
        eta.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_heading));
        eta.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        eta.setTypeface(null, Typeface.BOLD);
        eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        eta.setGravity(Gravity.CENTER);
        eta.setText("Età");
        eta.setWidth(larghezzaColonna2);
        tableRow.addView(eta);

        stato = new TextView(elencoIscrizioni);
        stato.setTextSize(16);
        stato.setPadding(10,20,10,20);
        stato.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border_heading));
        stato.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        stato.setTypeface(null, Typeface.BOLD);
        stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        stato.setGravity(Gravity.CENTER);
        stato.setText("Stato");
        stato.setWidth(larghezzaColonna3);
        tableRow.addView(stato);

        _tabellaContattiIscritti.addView(tableRow);
    }
}
