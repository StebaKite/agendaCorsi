package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ModificaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione, capienza, totaleFascia;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    Context modificaIscrizione;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_iscrizione);

        modificaIscrizione = this;
        makeToolBar(modificaIscrizione);

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
        try {
            List<Row> iscrizioniList = ConcreteDataAccessor.getInstance().read(Iscrizione.TABLE_NAME,
                    new String[]{Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO)},
                    new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione), null
            );

            for (Row iscrizioneRow : iscrizioniList) {
                List<Row> elementiPortfolioList = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME,
                        new String[]{ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)},
                        new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO),
                                iscrizioneRow.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO))),
                        null
                );
                for (Row elementoPortfolioRow : elementiPortfolioList) {
                    Row updateColumns = new Row();
                    updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());
                    String toastMessage = "";

                    if (elementoPortfolioRow.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString().equals(STATO_ESAURITO)) {
                        updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_DISATTIVA);
                        toastMessage = "Iscrizione disattiva a causa del portfolio esaurito";
                    } else {
                        updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_ATTIVA);
                        toastMessage = "OK, iscrizione aperta";
                    }

                    try {
                        ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME,
                                new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione),
                                updateColumns
                        );
                        makeToastMessage(modificaIscrizione, toastMessage).show();
                        esci.callOnClick();
                    } catch (Exception e) {
                        displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                    }
                }
            }
        }
        catch (Exception e) {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Prelievo elemento portfolio fallito, contatta il supporto tecnico");
        }
    }


    public void makeChiudi() {
        try {
            Row updateColumns = new Row();
            updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_CHIUSO);
            updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());
            ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME,
                    new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione),
                    updateColumns
            );
            makeToastMessage(modificaIscrizione, "Iscrizione chiusa con successo").show();
            esci.callOnClick();
        }
        catch (Exception e) {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento stato fallito, contatta il supporto tecnico");
        }
    }

    public void makeSospendi() {
        try {
            Row updateColumns = new Row();
            updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_SOSPESO);
            updateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());
            ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME,
                    new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione),
                    updateColumns
            );
            makeToastMessage(modificaIscrizione, "Iscrizione sospesa con successo").show();
            esci.callOnClick();
        }
        catch (Exception e) {
            displayAlertDialog(modificaIscrizione, "Attenzione!", "Aggiornamento stato fallito, contatta il supporto tecnico");
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
                try {
                    ConcreteDataAccessor.getInstance().delete(Iscrizione.TABLE_NAME,
                            new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione)
                    );
                    makeToastMessage(modificaIscrizione, "Iscrizione eliminata con successo.").show();
                    esci.callOnClick();
                }
                catch (Exception e) {
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
        finish();
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

