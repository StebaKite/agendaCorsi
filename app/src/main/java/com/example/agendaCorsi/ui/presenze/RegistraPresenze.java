package com.example.agendaCorsi.ui.presenze;

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

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.ContattoIscritto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.contatti.ElencoContatti;
import com.example.agendaCorsi.ui.iscrizioni.ModificaIscrizione;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class RegistraPresenze extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout _tabellaContattiIscritti;
    Context registraPresenze;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_presenze);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        registraPresenze = this;

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
        _tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        makeIntestazioneTabella();
        loadContattiIscritti();

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsiRunning.class, null);
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

    private void loadContattiIscritti() {

        List<Object> contattiIscrittiList = ContattiDAO.getInstance().getIscrittiRunning(idFascia, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTI_ISCRITTI_RUNNING));

        for (Object object : contattiIscrittiList) {
            ContattoIscritto contattoIscritto = (ContattoIscritto) object;

            String detailType = (contattoIscritto.getIdPresenza().equals("")) ? DETAIL_SIMPLE : DETAIL_CONFIRMED;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna1, contattoIscritto.getNomeContatto(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna2, String.valueOf(computeAge(contattoIscritto.getDataNascita())), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna3, contattoIscritto.getStato(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdElemento(), 0, View.GONE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdPresenza(), 0, View.GONE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdIscrizione(), 0, View.GONE));

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("descrizioneCorso", descrizioneCorso);
            intentMap.put("descrizioneFascia", descrizioneFascia);
            intentMap.put("giornoSettimana", giornoSettimana);
            intentMap.put("sport", sport);
            intentMap.put("idCorso", idCorso);
            intentMap.put("idFascia", idFascia);
            intentMap.put("idIscrizione", contattoIscritto.getIdIscrizione());
            intentMap.put("statoCorso", statoCorso);
            intentMap.put("nomeIscritto", contattoIscritto.getNomeContatto());
            intentMap.put("statoIscrizione", contattoIscritto.getStato());
            intentMap.put("tipoCorso", tipoCorso);
            intentMap.put("idElemento", contattoIscritto.getIdElemento());

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;
                    TextView textView = (TextView) tableRow.getChildAt(3);
                    String idElementoSelezionato = textView.getText().toString();

                    textView = (TextView) tableRow.getChildAt(4);
                    String idPresenzaSelezionato = textView.getText().toString();

                    textView = (TextView) tableRow.getChildAt(5);
                    String idIscrizioneSelezionato = textView.getText().toString();

                    if (idPresenzaSelezionato.equals("")) {
                        if (CreaPresenzaContattoIscritto.getInstance().make(idIscrizioneSelezionato,idElementoSelezionato)) {
                            Toast.makeText(registraPresenze, "Presenza creata con successo.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (RimuoviPresenzaContattoIscritto.getInstance().make(idPresenzaSelezionato,idElementoSelezionato)) {
                            Toast.makeText(registraPresenze, "Presenza rimossa con successo.", Toast.LENGTH_LONG).show();
                        }
                    }

                    Intent intent = getIntent();

                    intentMap.put("idElemento", idElementoSelezionato);
                    intentMap.put("idPresenza", idPresenzaSelezionato);
                    intentMap.put("idIscrizione", idIscrizioneSelezionato);

                    if (intentMap != null) {
                        for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                            intent.putExtra(entry.getKey(), entry.getValue());
                        }
                    }
                    startActivity(intent);
                }
            });
            _tabellaContattiIscritti.addView(tableRow);
        }
    }

    private void makeIntestazioneTabella() {
        tableRow = new TableRow(registraPresenze);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        _tabellaContattiIscritti.addView(tableRow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(RegistraPresenze.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
