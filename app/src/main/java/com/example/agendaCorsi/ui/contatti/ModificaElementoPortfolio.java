package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModificaElementoPortfolio extends FunctionBase {

    String idContatto;
    String idElemento;
    String nomeContatto;
    TextView labelScheda, dataUltimaricarica;
    String descrizione, numeroLezioni;
    EditText _descrizione, _numeroLezioni;

    Context modificaElementoPortfolio, modificaContatto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_elemento_portfolio);

        makeToolBar(this);

        Intent intent = getIntent();
        idElemento = intent.getStringExtra("idElemento");
        idContatto = intent.getStringExtra("idContatto");
        nomeContatto = intent.getStringExtra("nomeContatto");

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);
        elimina = findViewById(R.id.bElimina);
        ricarica5 = findViewById(R.id.bRicarica5);
        ricarica10 = findViewById(R.id.bRicarica10);

        _descrizione = findViewById(R.id.editDescrizione);
        _numeroLezioni = findViewById(R.id.editNumeroLezioni);
        dataUltimaricarica = findViewById(R.id.dataUltRicarica);

        labelScheda = findViewById(R.id.lScheda);
        labelScheda.setText(nomeContatto);

        modificaElementoPortfolio = this;
        modificaContatto = (Context) intent.getSerializableExtra("modificaContattoContext");
        /*
          Caricamento dati elemento portfolio selezionato
         */
        try {
            List<Row> rows = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME, null,
                    new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento), null);

            for (Row row : rows) {
                _descrizione.setText(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DESCRIZIONE)).toString());
                _numeroLezioni.setText(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI)).toString());
                dataUltimaricarica.setText(coalesceValue(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMA_RICARICA))));
                descrizione = _descrizione.getText().toString();
                numeroLezioni = _numeroLezioni.getText().toString();
                esci.requestFocus();

                /*
                 * Listener sui bottoni
                 */
                Map<String, String> intentMap = new ArrayMap<>();
                intentMap.put("idContatto", String.valueOf(idContatto));

                listenerAnnulla();
                listenerEsci(modificaElementoPortfolio, ModificaContatto.class, intentMap);
                listenerSalva();

                if (Integer.parseInt(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI)).toString()) > 0) {
                    elimina.setVisibility(View.GONE);
                } else {
                    listenerElimina();
                }
                listenerRicarica5();
                listenerRicarica10();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void makeRicarica(int ricarica) {
        int numLezioni = Integer.parseInt(_numeroLezioni.getText().toString());
        int newNumLezioni = numLezioni + ricarica;
        _numeroLezioni.setText(String.valueOf(newNumLezioni));
        makeToastMessage(AgendaCorsiApp.getContext(), "Ricarica eseguita, fai click su Salva.").show();
    }

    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaElementoPortfolio.this, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione!");
        messaggio.setMessage("Stai eliminando l'elemento " + String.valueOf(descrizione) + "\n\nConfermi?");
        messaggio.setCancelable(false);

        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    ConcreteDataAccessor.getInstance().delete(ElementoPortfolio.TABLE_NAME, new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento));
                    makeToastMessage(AgendaCorsiApp.getContext(), "Elemento portfolio eliminato con successo.").show();
                    esci.callOnClick();
                }
                catch (Exception e) {
                    displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
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

    public void makeSalva() {
        if (_descrizione.getText().toString().equals("") || _numeroLezioni.getText().toString().equals("")) {
            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
        } else {
            Row updateRow = new Row();
            boolean anyChange = false;

            if (!_descrizione.getText().toString().equals(descrizione)) {
                updateRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DESCRIZIONE), _descrizione.getText().toString());
                anyChange = true;
            }
            if (!_numeroLezioni.getText().toString().equals(numeroLezioni)) {
                updateRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI), _numeroLezioni.getText().toString());
                String stato = (Integer.parseInt(_numeroLezioni.getText().toString()) > 0) ? STATO_CARICO : STATO_ESAURITO;
                updateRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), stato);
                anyChange = true;
            }

            if (anyChange) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                updateRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMO_AGGIORNAMENTO), dateFormat.format(date));
                updateRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMA_RICARICA), dateFormat.format(date));
                try {
                    ConcreteDataAccessor.getInstance().update(ElementoPortfolio.TABLE_NAME,
                            new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento), updateRow);

                    if (Integer.parseInt(numeroLezioni) == 0) {
                        if (Integer.parseInt(String.valueOf(_numeroLezioni.getText())) > 0 ) {

                            /**
                             * Aggiorno lo stato di tutte le iscrizioni di questo elemento che si trovano in stato "Disattiva"
                             */
                            Row selectionRow = new Row();
                            selectionRow.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO), idElemento);
                            selectionRow.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_DISATTIVA);

                            Row updRow = new Row();
                            updRow.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_ATTIVA);

                            ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME,selectionRow, updRow);
                        }
                    }
                    makeToastMessage(AgendaCorsiApp.getContext(), "Elemento portfolio aggiornato con successo.").show();
                }
                catch (Exception e) {
                    displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                }
            }
        }
        esci.callOnClick();
    }

    public void makeAnnulla() {
        _descrizione.setText(descrizione);
        _numeroLezioni.setText(numeroLezioni);
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ModificaElementoPortfolio.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}