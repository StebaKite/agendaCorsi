package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ModificaContatto extends FunctionBase {

    String idContatto;
    String nome, indirizzo, telefono, email;
    EditText _nome, _indirizzo, _telefono, _email;
    //Button annulla, esci, salva, elimina, nuovoElemPortfolio;

    Context modificaContatto;
    TableLayout tabellaElePortfolio;
    TableRow tableRow;
    TextView descrizione, stato, id_elemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_contatto);
        tabellaElePortfolio = findViewById(R.id.tabellaElePortfolio);

        Intent intent = getIntent();
        idContatto = intent.getStringExtra("idContatto");

        modificaContatto = this;

        _nome = findViewById(R.id.editNomeMod);
        _indirizzo = findViewById(R.id.editIndirizzoMod);
        _telefono = findViewById(R.id.editTelefonoMod);
        _email = findViewById(R.id.editEmailMod);

        annulla = findViewById(R.id.AnnullaModButton);
        esci = findViewById(R.id.ExitModButton);
        salva = findViewById(R.id.SalvaModButton);
        elimina = findViewById(R.id.EliminaModButton);
        inserisci = findViewById(R.id.NuovoElemModButton);

        /**
         * Caricamento dati contatto selezionato
         */
        Contatto contatto = new Contatto(null, null, null, null, null);
        contatto.setId(idContatto);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        new ContattiDAO(this).select(contatto, properties.getProperty(QUERY_GET_CONTATTO));

        if (contatto.getId().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _nome.setText(contatto.getNome());
            _indirizzo.setText(contatto.getIndirizzo());
            _telefono.setText(contatto.getTelefono());
            _email.setText(contatto.getEmail());
            nome = _nome.getText().toString();
            indirizzo = _indirizzo.getText().toString();
            telefono = _telefono.getText().toString();
            email = _email.getText().toString();

            displayElencoElementiPortfolio(idContatto, contatto.getNome());
            esci.requestFocus();
        }
        /**
         * Listener dei bottoni
         */
        listenerAnnulla();
        listenerEsci(ModificaContatto.this, ElencoContatti.class, null);
        listenerSalva();
        listenerElimina();

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idContatto", idContatto);
        intentMap.put("nomeContatto", contatto.getNome());

        listenerInserisci(modificaContatto, NuovoElementoPortfolio.class, intentMap);
    }


    private void displayElencoElementiPortfolio(String idContatto, String nomeContatto) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<ElementoPortfolio> elementiPortfoList = new ElementoPortfolioDAO(this).getContattoElements(idContatto, properties.getProperty(QUERY_GET_ELEMENTS));

        for (ElementoPortfolio elementoPortfolio : elementiPortfoList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            descrizione = new TextView(this);
            descrizione.setTextSize(16);
            descrizione.setPadding(10, 20, 10, 20);
            descrizione.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(elementoPortfolio.getDescrizione()));
            descrizione.setWidth(larghezzaColonna1);
            tableRow.addView(descrizione);

            stato = new TextView(this);
            stato.setTextSize(16);
            stato.setPadding(10, 20, 10, 20);
            stato.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            stato.setGravity(Gravity.CENTER);
            stato.setText(String.valueOf(elementoPortfolio.getStato()));
            stato.setWidth(larghezzaColonna2);
            tableRow.addView(stato);

            id_elemento = new TextView(this);
            id_elemento.setText(String.valueOf(elementoPortfolio.getIdElemento()));
            id_elemento.setVisibility(View.INVISIBLE);
            tableRow.addView(id_elemento);

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("idContatto", idContatto);
            intentMap.put("nomeContatto", nomeContatto);

            listenerTableRow(ModificaContatto.this, ModificaElementoPortfolio.class, "idElemento", intentMap);
            tabellaElePortfolio.addView(tableRow);
        }
    }

    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando il contatto " + nome + "\n\nConfermi?");
        messaggio.setCancelable(false);

        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Contatto contatto = new Contatto(String.valueOf(idContatto), null, null, null, null);

                propertyReader = new PropertyReader(modificaContatto);
                properties = propertyReader.getMyProperties("config.properties");

                if (new ContattiDAO(modificaContatto).delete(contatto, properties.getProperty(QUERY_DEL_CONTATTO))) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(modificaContatto, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
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
        /**
         * dati immessi in un oggetto contatto
         */
        Contatto contatto = new Contatto(idContatto,
                _nome.getText().toString(),
                _indirizzo.getText().toString(),
                _telefono.getText().toString(),
                _email.getText().toString());

        if (contatto.getNome().equals("") || contatto.getIndirizzo().equals("") ||
            contatto.getTelefono().equals("") || contatto.getEmail().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            propertyReader = new PropertyReader(this);
            properties = propertyReader.getMyProperties("config.properties");

            if (new ContattiDAO(this).update(contatto, properties.getProperty(QUERY_MOD_CONTATTO))) {
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaContatto, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    public void makeAnnulla() {
        _nome.setText(nome);
        _indirizzo.setText(indirizzo);
        _telefono.setText(telefono);
        _email.setText(email);
    }
}
