package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ModificaElementoPortfolio extends FunctionBase {

    int idContatto, idElemento;
    String nomeContatto;
    TextView labelScheda, dataUltimaricarica;
    String descrizione, numeroLezioni;
    EditText _descrizione, _numeroLezioni;

    Context modificaElementoPortfolio, modificaContatto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_elemento_portfolio);

        Intent intent = getIntent();
        idElemento = intent.getIntExtra("idElemento", 0);
        idContatto = intent.getIntExtra("id", 0);
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

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        new ElementoPortfolioDAO(this).select(elementoPortfolio, properties.getProperty(QUERY_GET_ELEMENTO));

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
        intentMap.put("id", String.valueOf(idContatto));

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

                propertyReader = new PropertyReader(modificaElementoPortfolio);
                properties = propertyReader.getMyProperties("config.properties");

                if (new ElementoPortfolioDAO(modificaElementoPortfolio).delete(elementoPortfolio, properties.getProperty(QUERY_DEL_ELEMENTO))) {
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

        String stato = "Aperto";
        if (Integer.parseInt(_numeroLezioni.getText().toString()) > 0) {
            stato = "Carico";
        }

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
            propertyReader = new PropertyReader(this);
            properties = propertyReader.getMyProperties("config.properties");

            if (new ElementoPortfolioDAO(this).update(elementoPortfolio, properties.getProperty(QUERY_MOD_ELEMENTS))) {
                esci.callOnClick();
            } else {
                displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    public void makeAnnulla() {
        _descrizione.setText(descrizione);
        _numeroLezioni.setText(numeroLezioni);
    }
}