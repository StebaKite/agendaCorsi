package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.ContattoIscrivibile;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class NuovaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout _tabellaContattiIscrivibili;
    Context nuovaIscrizione;

    int larghezzaColonna1, larghezzaColonna2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_iscrizione);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);

        makeIntestazioneTabella();

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

        listenerEsci(nuovaIscrizione, ElencoFasceCorsi.class, intentMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem contattiItem = menu.findItem(R.id.navigation_contatti);
        contattiItem.setVisible(false);

        MenuItem corsiItem = menu.findItem(R.id.navigation_corsi);
        corsiItem.setVisible(false);

        MenuItem iscrizioniItem = menu.findItem(R.id.navigation_iscrizioni);
        iscrizioniItem.setVisible(false);

        MenuItem presenzeItem = menu.findItem(R.id.navigation_presenze);
        presenzeItem.setVisible(false);

        MenuItem exitItem = menu.findItem(R.id.navigation_esci);
        exitItem.setVisible(false);

        return true;
    }

    private void loadContattiIscrvibili(String queryName) {
        List<Object> contattiIscrivibiliList = ContattiDAO.getInstance().getIscrivibili(idCorso, idFascia, sport, QueryComposer.getInstance().getQuery(queryName));

        for (Object object : contattiIscrivibiliList) {
            ContattoIscrivibile contattoIscrivibile = (ContattoIscrivibile) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, larghezzaColonna1, contattoIscrivibile.getNomeContatto(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, larghezzaColonna2, String.valueOf(computeAge(contattoIscrivibile.getDataNascita())), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscrivibile.getIdElemento(), 0, View.GONE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscrivibile.getEmailContatto(), 0, View.GONE));

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

    private void makeIntestazioneTabella() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        _tabellaContattiIscrivibili.addView(tableRow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(NuovaIscrizione.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
