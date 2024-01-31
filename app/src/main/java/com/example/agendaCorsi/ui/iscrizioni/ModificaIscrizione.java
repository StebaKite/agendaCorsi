package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendacorsi.R;

import java.util.Map;

public class ModificaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione, capienza, totaleFascia;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    Context modificaIscrizione;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_iscrizione);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        modificaIscrizione = this;

        chiudi = findViewById(R.id.bChiudi);
        sospendi = findViewById(R.id.bSospendi);
        elimina = findViewById(R.id.bElimina);
        esci = findViewById(R.id.bExit);
        sposta = findViewById(R.id.bSposta);
        apri = findViewById(R.id.bApri);
        sposta = findViewById(R.id.bSposta);

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
        capienza = intent.getStringExtra("capienza");
        totaleFascia = intent.getStringExtra("totaleFascia");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _nomeIscritto = findViewById(R.id.editNomeIscritto);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);
        _nomeIscritto.setText(nomeIscritto);

        /*
         * La visibilità dei bottoni di cambio stato rispetta le regole
         * documentate dal diagramma degli stati del Corso
         */
        if (statoIscrizione.equals(STATO_ATTIVA)) {
            apri.setVisibility(View.GONE);
        } else if (statoIscrizione.equals(STATO_SOSPESO)) {
            sospendi.setVisibility(View.GONE);
            sposta.setVisibility(View.GONE);
        } else if (statoIscrizione.equals(STATO_CHIUSO)) {
            chiudi.setVisibility(View.GONE);
            sospendi.setVisibility(View.GONE);
            apri.setVisibility(View.GONE);
            sposta.setVisibility(View.GONE);
        }

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("tipoCorso", tipoCorso);
        intentMap.put("statoIscrizione", statoIscrizione);
        intentMap.put("totaleFascia", totaleFascia);
        intentMap.put("capienza", capienza);

        listenerSpostaIscrizione(idIscrizione);
        listenerChiudi();
        listenerSospendi();
        listenerApri();
        listenerElimina();
        listenerEsci(modificaIscrizione, ElencoIscrizioni.class, intentMap);
    }


    public void makeApri() {
        Iscrizione iscrizione = new Iscrizione(idIscrizione,null,null,STATO_ATTIVA,null,null);
        if (IscrizioneDAO.getInstance().updateStato(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_ISCRIZIONE))) {
            Toast.makeText(modificaIscrizione, "Iscrizione aperta con successo", Toast.LENGTH_LONG).show();
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    public void makeChiudi() {
        Iscrizione iscrizione = new Iscrizione(idIscrizione,null,null,STATO_CHIUSO,null,null);
        if (IscrizioneDAO.getInstance().updateStato(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_ISCRIZIONE))) {
            Toast.makeText(modificaIscrizione, "Iscrizione chiusa con successo", Toast.LENGTH_LONG).show();
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }
    }

    public void makeSospendi() {
        Iscrizione iscrizione = new Iscrizione(idIscrizione,null,null,STATO_SOSPESO,null,null);
        if (IscrizioneDAO.getInstance().updateStato(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_ISCRIZIONE))) {
            Toast.makeText(modificaIscrizione, "Iscrizione sospesa con successo", Toast.LENGTH_LONG).show();
            esci.callOnClick();
        }
        else {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
        }

    }

    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(modificaIscrizione, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando l'iscrizione di : " + nomeIscritto +
                "\nCancellerò anche tutte le presenze legate" + "\n\nConfermi?");
        messaggio.setCancelable(false);
        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Iscrizione iscrizione = new Iscrizione(idIscrizione, null, null, null, null, null);
                if (IscrizioneDAO.getInstance().delete(iscrizione, QueryComposer.getInstance().getQuery(QUERY_DEL_ISCRIZIONE))) {
                    Toast.makeText(modificaIscrizione, "Iscrizione eliminata con successo.", Toast.LENGTH_LONG).show();
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(modificaIscrizione, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
                }
            }
        }).getContext();

        messaggio.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog ad = messaggio.create();
        ad.show();
    }

    /**
     * Lo spostamento di un'iscrizione comporta il cambio della fascia oraria sulla quale
     * l'iscrizione insiste. Il bottone porta ad una pagina dedicata che elenca tutte le fascie
     * del corso ancora capienti. Se non ci sono fascie capienti lo spostamento non potrà essere
     * completato.
     * @param idIscrizione
     */
    public void makeSpostaIscrizione(String idIscrizione) {

        Intent intent = new Intent(modificaIscrizione, SpostaIscrizione.class);
        intent.putExtra("descrizioneCorso", descrizioneCorso);
        intent.putExtra("descrizioneFascia", descrizioneFascia);
        intent.putExtra("giornoSettimana", giornoSettimana);
        intent.putExtra("sport", sport);
        intent.putExtra("idCorso", idCorso);
        intent.putExtra("idFascia", idFascia);
        intent.putExtra("idIscrizione", idIscrizione);
        intent.putExtra("statoCorso", statoCorso);
        intent.putExtra("nomeIscritto", nomeIscritto);
        intent.putExtra("statoIscrizione", statoIscrizione);
        intent.putExtra("capienza", capienza);
        intent.putExtra("totaleFascia", totaleFascia);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ModificaIscrizione.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

