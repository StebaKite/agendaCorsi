package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

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
        ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, null, null, null, null, null, null);
        elementoPortfolio.setIdElemento(String.valueOf(idElemento));

        ElementoPortfolioDAO.getInstance().select(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_GET_ELEMENTO));

        if (elementoPortfolio.getIdElemento().equals("")) {
            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Lettura fallita, contatto il supporto tecnico");
        } else {
            _descrizione.setText(elementoPortfolio.getDescrizione());
            _numeroLezioni.setText(elementoPortfolio.getNumeroLezioni());
            dataUltimaricarica.setText(elementoPortfolio.getDataUltimaRicarica());
            descrizione = _descrizione.getText().toString();
            numeroLezioni = _numeroLezioni.getText().toString();
            esci.requestFocus();
        }
        /*
         * Listener sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idContatto", String.valueOf(idContatto));

        listenerAnnulla();
        listenerEsci(modificaElementoPortfolio, ModificaContatto.class, intentMap);
        listenerSalva();

        if (Integer.parseInt(elementoPortfolio.getNumeroLezioni()) > 0) {
            elimina.setVisibility(View.GONE);
        } else {
            listenerElimina();
        }
        listenerRicarica5();
        listenerRicarica10();
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
                ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, null, null, null, null, null, null);
                elementoPortfolio.setIdElemento(String.valueOf(idElemento));

                if (ElementoPortfolioDAO.getInstance().delete(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_DEL_ELEMENTO))) {
                    makeToastMessage(AgendaCorsiApp.getContext(), "Elemento portfolio eliminato con successo.").show();
                    esci.callOnClick();
                }
                else {
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        String stato = (Integer.parseInt(_numeroLezioni.getText().toString()) > 0) ? STATO_CARICO : STATO_ESAURITO;

        ElementoPortfolio elementoPortfolio = new ElementoPortfolio(String.valueOf(idElemento),
                String.valueOf(idContatto),
                _descrizione.getText().toString(),
                null,
                _numeroLezioni.getText().toString(),
                dateFormat.format(date),
                stato);

        if (elementoPortfolio.getDescrizione().equals("") || elementoPortfolio.getNumeroLezioni().equals("")) {
            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
        } else {
            if (ElementoPortfolioDAO.getInstance().update(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_MOD_ELEMENTS))) {

                if (Integer.parseInt(numeroLezioni) == 0) {
                    if (Integer.parseInt(String.valueOf(_numeroLezioni.getText())) > 0 ) {

                        /**
                         * Aggiorno lo stato di tutte le iscrizioni di questo elemento che si trovano in stato "Disattiva"
                         */
                        Iscrizione iscrizione = new Iscrizione(null, null, elementoPortfolio.getIdElemento(), STATO_ATTIVA, null, null);
                        if (!IscrizioneDAO.getInstance().updateStatoIscrizioni(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_ISCRIZIONI_ELEMENTO))) {
                            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Aggiornamento iscrizioni fallito, contatta il supporto tecnico");
                        }
                    }
                }
                makeToastMessage(AgendaCorsiApp.getContext(), "Elemento portfolio aggiornato con successo.").show();
                esci.callOnClick();

            } else {
                displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
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