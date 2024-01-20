package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.corsi.ModificaFascia;
import com.example.agendacorsi.R;

import java.util.Map;


public class SpostaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    TextView nome_contatto, id_elemento, emailContatto;
    TableLayout tabellaFasce;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sposta_iscrizione);

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
        statoIscrizione = intent.getStringExtra("stato");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _nomeIscritto = findViewById(R.id.editNomeIscritto);
        tabellaFasce = findViewById(R.id.tabellaFasce);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);
        _nomeIscritto.setText(nomeIscritto);

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
        intentMap.put("stato", statoIscrizione);

        loadFasceCorso();

        listenerEsci(AgendaCorsiApp.getContext(), ModificaIscrizione.class, null);

    }

    private void loadFasceCorso() {




    }
}
