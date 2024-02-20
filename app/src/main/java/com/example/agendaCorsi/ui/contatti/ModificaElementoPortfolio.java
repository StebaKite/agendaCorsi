package com.example.agendaCorsi.ui.contatti;

import android.app.DownloadManager;
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
import com.example.agendaCorsi.database.access.AssenzaDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.access.PresenzaDAO;
import com.example.agendaCorsi.database.table.Assenza;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ModificaElementoPortfolio extends FunctionBase {

    String idContatto;
    String idElemento;
    String nomeContatto;
    TextView dataUltimaricarica;
    String descrizione, numeroLezioni;
    EditText _descrizione, _numeroLezioni, _numeroAssenzeRecuperabili;

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

        _descrizione = findViewById(R.id.editDescrizione);
        _numeroLezioni = findViewById(R.id.editNumeroLezioni);
        _numeroAssenzeRecuperabili = findViewById(R.id.editNumeroAssenzeRecuperabili);
        dataUltimaricarica = findViewById(R.id.dataUltRicarica);

        modificaElementoPortfolio = this;
        modificaContatto = (Context) intent.getSerializableExtra("modificaContattoContext");
        /*
          Caricamento dati elemento portfolio selezionato
         */
        ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, null, null, null, null, null, null, null);
        elementoPortfolio.setIdElemento(String.valueOf(idElemento));

        ElementoPortfolioDAO.getInstance().select(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_GET_ELEMENTO));

        if (elementoPortfolio.getIdElemento().equals("")) {
            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Lettura fallita, contatto il supporto tecnico");
        } else {
            _descrizione.setText(elementoPortfolio.getDescrizione());
            _numeroLezioni.setText(elementoPortfolio.getNumeroLezioni());
            _numeroAssenzeRecuperabili.setText(elementoPortfolio.getNumeroAssenzeRecuperabili());
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
                ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, null, null, null, null, null, null, null);
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
        String numLezioni;
        String numAssenzeRecuperabili;

        String stato = (Integer.parseInt(_numeroLezioni.getText().toString()) > 0) ? STATO_CARICO : STATO_ESAURITO;

        numLezioni = (_numeroLezioni.getText().toString().equals("")) ? "0" : _numeroLezioni.getText().toString();
        numAssenzeRecuperabili = (_numeroAssenzeRecuperabili.getText().toString().equals("")) ? "0" : _numeroAssenzeRecuperabili.getText().toString();

        ElementoPortfolio elementoPortfolio = new ElementoPortfolio(String.valueOf(idElemento),
                String.valueOf(idContatto),
                _descrizione.getText().toString(),
                null,
                numLezioni,
                numAssenzeRecuperabili,
                dateFormat.format(date),
                stato);

        if (elementoPortfolio.getDescrizione().equals("") ||
            elementoPortfolio.getNumeroLezioni().equals("") ||
            elementoPortfolio.getNumeroAssenzeRecuperabili().equals("")) {
            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
        } else {
            if (ElementoPortfolioDAO.getInstance().update(elementoPortfolio, QueryComposer.getInstance().getQuery(QUERY_MOD_ELEMENTS))) {

                if (Integer.parseInt(numeroLezioni) == 0) {
                    if (Integer.parseInt(numLezioni) > 0 ) {
                        /**
                         * Aggiorno lo stato di tutte le iscrizioni di questo elemento che si trovano in stato "Disattiva"
                         */
                        Iscrizione iscrizione = new Iscrizione(null, null, elementoPortfolio.getIdElemento(), STATO_ATTIVA, null, null);
                        if (!IscrizioneDAO.getInstance().updateStatoIscrizioni(iscrizione, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_ISCRIZIONI_ELEMENTO))) {
                            displayAlertDialog(modificaElementoPortfolio, "Attenzione!", "Aggiornamento iscrizioni fallito, contatta il supporto tecnico");
                        }
                        /**
                         * Prendo tutte le iscrizioni "Attive" o "Disattive" di questo elemento e, per ciascuna di queste,
                         * elimino tutte le presenze e assenze inserite salvo quelle inserite nella giornata odierna.
                         * Questa operazione è necessaria per ripristinare una situazione di partenza che permetta, ad ogni ricarica,
                         * di contare le assenze fatte per gestire correttamente il loro eventuale recupero.
                         */
                        iscrizione.setIdIscrizione(null);
                        iscrizione.setIdFascia(null);
                        iscrizione.setIdElemento(elementoPortfolio.getIdElemento());
                        iscrizione.setStato(null);
                        iscrizione.setDataCreazione(null);
                        iscrizione.setDataUltimoAggiornamento(null);
                        List<Object> objectList = IscrizioneDAO.getInstance().getAllIscrizioniElemento(iscrizione,
                                QueryComposer.getInstance().getQuery(FunctionBase.QUERY_GETALL_ISCRIZIONI_ELEMENTO));

                        for (Object object : objectList) {
                            Iscrizione iscr = (Iscrizione) object;
                            Presenza presenza = new Presenza(null, iscr.getIdIscrizione(), null);
                            PresenzaDAO.getInstance().deleteAllPresenzeIscrizione(presenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_DEL_PRESENZE_ISCRIZIONE));

                            Assenza assenza = new Assenza(null, iscr.getIdIscrizione(), null);
                            AssenzaDAO.getInstance().deleteAllAssenzeIscrizione(assenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_DEL_ASSENZE_ISCRIZIONE));
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