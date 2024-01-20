package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.ArrayMap;
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
import java.util.Map;

public class NuovaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TextView nome_contatto, id_elemento, emailContatto, eta;
    TableLayout _tabellaContattiIscrivibili;
    Context nuovaIscrizione;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_iscrizione);
        nuovaIscrizione = this;

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

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("tipoCorso", tipoCorso);

        listenerEsci(nuovaIscrizione, ElencoIscrizioni.class, intentMap);
    }

    private void loadContattiIscrvibili(String queryName) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);

        List<Object> contattiIscrivibiliList = ContattiDAO.getInstance().getIscrivibili(idCorso, idFascia, sport, QueryComposer.getInstance().getQuery(queryName));

        makeIntestazioneTabella(larghezzaColonna1, larghezzaColonna2);

        for (Object object : contattiIscrivibiliList) {
            ContattoIscrivibile contattoIscrivibile = (ContattoIscrivibile) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            /**
             * Cella 0
             */
            nome_contatto = new TextView(this);
            nome_contatto.setTextSize(14);
            nome_contatto.setPadding(10,20,10,20);
            nome_contatto.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border));
            nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nome_contatto.setGravity(Gravity.CENTER);
            nome_contatto.setText(String.valueOf(contattoIscrivibile.getNomeContatto()));
            nome_contatto.setWidth(larghezzaColonna1);
            tableRow.addView(nome_contatto);
            /**
             * Cella 1
             */
            eta = new TextView(this);
            eta.setTextSize(14);
            eta.setPadding(10,20,10,20);
            eta.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border));
            eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            eta.setGravity(Gravity.CENTER);
            eta.setText(String.valueOf(computeAge(contattoIscrivibile.getDataNascita())));
            eta.setWidth(larghezzaColonna2);
            tableRow.addView(eta);
            /**
             * Cella 2
             */
            id_elemento = new TextView(this);
            id_elemento.setText(String.valueOf(contattoIscrivibile.getIdElemento()));
            id_elemento.setVisibility(View.INVISIBLE);
            tableRow.addView(id_elemento);
            /**
             * Cella 3
             */
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

                    textView = (TextView) tableRow.getChildAt(2);
                    String idSelezionato = textView.getText().toString();

                    textView = (TextView) tableRow.getChildAt(3);
                    String emailContatto = textView.getText().toString();

                    Iscrizione iscrizione = new Iscrizione(null, idFascia, idSelezionato, "Attiva", null, null);
                    if (IscrizioneDAO.getInstance().insert(iscrizione, QueryComposer.getInstance().getQuery(QUERY_INS_ISCRIZIONE))) {
                        if (!statoCorso.equals(STATO_ATTIVO)) {
                            Corso corso = new Corso(idCorso,null,null, STATO_ATTIVO, null, null, null, null, null);
                            if (!CorsoDAO.getInstance().updateStato(corso, QueryComposer.getInstance().getQuery(QUERY_MOD_STATO_CORSO))) {
                                displayAlertDialog(nuovaIscrizione, "Attenzione!", "Cambio stato corso fallito, contatta il supporto tecnico");
                            }
                        }
                        Toast.makeText(nuovaIscrizione, "Iscrizione creata con successo.", Toast.LENGTH_LONG).show();
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(nuovaIscrizione, "Attenzione!", "Inserimento fallita, contatta il supporto tecnico");
                    }
                }
            });
            _tabellaContattiIscrivibili.addView(tableRow);
        }
    }

    private void makeIntestazioneTabella(int larghezzaColonna1, int larghezzaColonna2) {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);

        nome_contatto = new TextView(this);
        nome_contatto.setTextSize(16);
        nome_contatto.setPadding(10,20,10,20);
        nome_contatto.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border_heading));
        nome_contatto.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        nome_contatto.setTypeface(null, Typeface.BOLD);
        nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        nome_contatto.setGravity(Gravity.CENTER);
        nome_contatto.setText("Nome");
        nome_contatto.setWidth(larghezzaColonna1);
        tableRow.addView(nome_contatto);

        eta = new TextView(this);
        eta.setTextSize(16);
        eta.setPadding(10,20,10,20);
        eta.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border_heading));
        eta.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        eta.setTypeface(null, Typeface.BOLD);
        eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        eta.setGravity(Gravity.CENTER);
        eta.setText("Età");
        eta.setWidth(larghezzaColonna2);
        tableRow.addView(eta);

        _tabellaContattiIscrivibili.addView(tableRow);
    }
}
