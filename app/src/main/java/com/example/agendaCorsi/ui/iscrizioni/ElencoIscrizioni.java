package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
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
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ElencoIscrizioni extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TextView nome_contatto, id_iscrizione;
    TableLayout _tabellaContattiIscritti;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_iscrizioni);

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

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        loadContattiIscritti();

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("statoCorso", statoCorso);

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsi.class, null);
        listenerInserisci(AgendaCorsiApp.getContext(), NuovaIscrizione.class, intentMap);
    }

    private void loadContattiIscritti() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);

        List<Object> contattiIscrittiList = ContattiDAO.getInstance().getIscritti(idFascia, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTI_ISCRITTI));

        for (Object object : contattiIscrittiList) {
            ContattoIscritto contattoIscritto = (ContattoIscritto) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            nome_contatto = new TextView(this);
            nome_contatto.setTextSize(16);
            nome_contatto.setPadding(10,20,10,20);
            nome_contatto.setBackground(ContextCompat.getDrawable(ElencoIscrizioni.this, R.drawable.cell_border));
            nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nome_contatto.setGravity(Gravity.CENTER);
            nome_contatto.setText(String.valueOf(contattoIscritto.getNomeContatto()));
            nome_contatto.setWidth(larghezzaColonna1);
            tableRow.addView(nome_contatto);

            id_iscrizione = new TextView(this);
            id_iscrizione.setText(String.valueOf(contattoIscritto.getIdIscrizione()));
            id_iscrizione.setVisibility(View.INVISIBLE);
            tableRow.addView(id_iscrizione);

            listenerTableRow(AgendaCorsiApp.getContext(), ModificaIscrizione.class, "idIscrizione", null, 1);
        }
        _tabellaContattiIscritti.addView(tableRow);
    }
}
