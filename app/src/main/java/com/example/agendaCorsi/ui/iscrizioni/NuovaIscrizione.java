package com.example.agendaCorsi.ui.iscrizioni;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.ContattoIscrivibile;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;

public class NuovaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TextView nome_contatto, id_elemento, emailContatto;
    TableLayout _tabellaContattiIscrivibili;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_iscrizione);

        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _tabellaContattiIscrivibili = findViewById(R.id.tabellaContattiIscrivibili);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        if (tipoCorso.equals(Test)) {
            loadContattiIscrvibili(QUERY_GET_CONTATTI_ISCRIVIBILI_OPEN);
        }
        else {
            loadContattiIscrvibili(QUERY_GET_CONTATTI_ISCRIVIBILI);
        }

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsi.class, null);
    }

    private void loadContattiIscrvibili(String queryName) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);

        List<Object> contattiIscrivibiliList = ContattiDAO.getInstance().getIscrivibili(idCorso, idFascia, sport, QueryComposer.getInstance().getQuery(queryName));

        for (Object object : contattiIscrivibiliList) {
            ContattoIscrivibile contattoIscrivibile = (ContattoIscrivibile) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            nome_contatto = new TextView(this);
            nome_contatto.setTextSize(16);
            nome_contatto.setPadding(10,20,10,20);
            nome_contatto.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border));
            nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nome_contatto.setGravity(Gravity.CENTER);
            nome_contatto.setText(String.valueOf(contattoIscrivibile.getNomeContatto()));
            nome_contatto.setWidth(larghezzaColonna1);
            tableRow.addView(nome_contatto);

            id_elemento = new TextView(this);
            id_elemento.setText(String.valueOf(contattoIscrivibile.getIdElemento()));
            id_elemento.setVisibility(View.INVISIBLE);
            tableRow.addView(id_elemento);

            emailContatto = new TextView(this);
            emailContatto.setText(String.valueOf(contattoIscrivibile.getEmailContatto()));
            emailContatto.setVisibility(View.INVISIBLE);
            tableRow.addView(emailContatto);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;

                    TextView textView = (TextView) tableRow.getChildAt(0);
                    String nomeContatto = textView.getText().toString();

                    textView = (TextView) tableRow.getChildAt(1);
                    String idSelezionato = textView.getText().toString();

                    textView = (TextView) tableRow.getChildAt(2);
                    String emailContatto = textView.getText().toString();

                    Iscrizione iscrizione = new Iscrizione(null, idFascia, idSelezionato, "Attiva", null, null);
                    if (IscrizioneDAO.getInstance().insert(iscrizione, QueryComposer.getInstance().getQuery(QUERY_INS_ISCRIZIONE))) {
                        if (!statoCorso.equals(STATO_ATTIVO)) {
                            Corso corso = new Corso(idCorso,null,null, STATO_ATTIVO, null, null, null, null, null);
                            if (!CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
                                displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Cambio stato corso fallito, contatta il supporto tecnico");
                            }
                        }
                        makeInvioNotificaIscrizione(emailContatto, nomeContatto, descrizioneCorso);
                        Toast.makeText(AgendaCorsiApp.getContext(), "Iscrizione creata con successo.", Toast.LENGTH_LONG).show();
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Inserimento fallita, contatta il supporto tecnico");
                    }
                }
            });
            _tabellaContattiIscrivibili.addView(tableRow);
        }
    }

    private void makeInvioNotificaIscrizione(String email, String nome, String corso) {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(AgendaCorsiApp.getContext(), R.style.Theme_InfoDialog);
        messaggio.setTitle("Notifica");
        messaggio.setMessage("Contatto iscritto con successo.\n\nVuoi inviare una mail di notifica al contatto?");
        messaggio.setCancelable(false);
        /**
         * implemento i listener sui bottoni della conferma invio notifica
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String emailText = properties.getProperty("EMAIL_INSCRIZIONE_NOTIFICATION_BODY").replace("#NOME#", nome).replace("#CORSO#", corso);
                    if (sendEmail("Iscrizione corso " + descrizioneCorso, emailText, email)) {
                        Toast.makeText(AgendaCorsiApp.getContext(), "Notifica inviata con successo al contatto.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Invio notifica fallito, contatta il supporto tecnico");
                    }
                }
                catch (Exception e) {
                    displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Invio notifica fallito, contatta il supporto tecnico");
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
}
