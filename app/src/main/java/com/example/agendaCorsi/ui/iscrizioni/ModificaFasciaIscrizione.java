package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

public class ModificaFasciaIscrizione extends FunctionBase {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_iscrizione);
        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        String idFascia = intent.getStringExtra("idFascia");
        String idIscrizione = intent.getStringExtra("idIscrizione");

        Iscrizione iscrizione = new Iscrizione(idIscrizione, idFascia, null, null, null, null);
        if (IscrizioneDAO.getInstance().update(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_ISCRIZIONE))) {
            Toast.makeText(AgendaCorsiApp.getContext(), "Iscrizione spostata con successo.", Toast.LENGTH_LONG).show();
            esci.callOnClick();
        }
    }
}
