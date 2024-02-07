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

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.ContattoIscritto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ElencoIscrizioni extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, capienza, totaleFascia;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout _tabellaContattiIscritti;
    Context elencoIscrizioni;

    int larghezzaColonna1;
    int larghezzaColonna2;
    int larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_iscrizioni);

        elencoIscrizioni = this;
        makeToolBar(elencoIscrizioni);

        esci = findViewById(R.id.bExit);
        inserisci = findViewById(R.id.bNuovaIscrizione);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");
        capienza = intent.getStringExtra("capienza");
        totaleFascia = intent.getStringExtra("totaleFascia");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        makeIntestazioneTabella();
        loadContattiIscritti();

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("tipoCorso", tipoCorso);
        intentMap.put("totaleFascia", totaleFascia);
        intentMap.put("capienza", capienza);

        if (isFasciaCapiente(totaleFascia, capienza)) {
            listenerInserisci(AgendaCorsiApp.getContext(), NuovaIscrizione.class, intentMap);
        }
        else {
            inserisci.setVisibility(View.GONE);
        }

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsi.class, null);
    }


    private void loadContattiIscritti() {
        List<Object> contattiIscrittiList = ContattiDAO.getInstance().getIscritti(idFascia, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTI_ISCRITTI));

        for (Object object : contattiIscrittiList) {
            ContattoIscritto contattoIscritto = (ContattoIscritto) object;

            String detailType = "";
            if (contattoIscritto.getStato().equals(STATO_CHIUSA) || contattoIscritto.getStato().equals(STATO_DISATTIVA)) {
                detailType = DETAIL_CLOSED;
            } else {
                if (contattoIscritto.getStato().equals(STATO_SOSPESO)) {
                    detailType = DETAIL_SUSPENDED;
                } else {
                    detailType = DETAIL_SIMPLE;
                }
            }

            tableRow = new TableRow(elencoIscrizioni);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), detailType, larghezzaColonna1, contattoIscritto.getNomeContatto(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), detailType, larghezzaColonna2, String.valueOf(computeAge(contattoIscritto.getDataNascita())), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), detailType, larghezzaColonna2, contattoIscritto.getStato(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), detailType, 0, contattoIscritto.getIdIscrizione(), 0, View.GONE));

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
            intentMap.put("capienza", capienza);
            intentMap.put("totaleFascia", totaleFascia);

            listenerTableRow(elencoIscrizioni, ModificaIscrizione.class, "idIscrizione", intentMap, 2);

            _tabellaContattiIscritti.addView(tableRow);
        }
    }

    private void makeIntestazioneTabella() {
        tableRow = new TableRow(elencoIscrizioni);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        _tabellaContattiIscritti.addView(tableRow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoIscrizioni.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
