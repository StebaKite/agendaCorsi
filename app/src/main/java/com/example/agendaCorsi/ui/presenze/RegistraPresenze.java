package com.example.agendaCorsi.ui.presenze;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class RegistraPresenze extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout tabellaContattiIscritti, headerTabellaContattiIscritti;
    Context registraPresenze;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_presenze);

        registraPresenze = this;
        makeToolBar(registraPresenze);

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

        headerTabellaContattiIscritti = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

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


    private void loadContattiIscritti() {
        try {
            List<Row> contattiIscrittiList = ConcreteDataAccessor.getInstance().read(Contatto.VIEW_CONTATTI_ISCRITTI_RUNNING,
                    null,
                    new Row(Fascia.fasciaColumns.get(Fascia.ID_FASCIA), idFascia),
                    new String[]{Contatto.contattoColumns.get(Contatto.NOME)});

            for (Row row : contattiIscrittiList) {
                String detailType = "";
                if (row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO)).toString().equals(STATO_CHIUSO) ||
                    row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO)).toString().equals(STATO_ESAURITO)) {
                    detailType = DETAIL_CLOSED;
                } else {
                    detailType = (row.getColumnValue(Presenza.presenzaColumns.get(Presenza.ID_PRESENZA)).toString().equals("")) ? DETAIL_SIMPLE : DETAIL_CONFIRMED;
                }

                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType,
                        larghezzaColonna1,
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType,
                        larghezzaColonna2,
                        String.valueOf(computeAge(row.getColumnValue(Contatto.contattoColumns.get(Contatto.DATA_NASCITA)).toString())),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType,
                        larghezzaColonna3,
                        row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0,
                        row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO)).toString(), 0, View.GONE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0,
                        row.getColumnValue(Presenza.presenzaColumns.get(Presenza.ID_PRESENZA)).toString(), 0, View.GONE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0,
                        row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE)).toString(), 0, View.GONE));

                Map<String, String> intentMap = new ArrayMap<>();
                intentMap.put("descrizioneCorso", descrizioneCorso);
                intentMap.put("descrizioneFascia", descrizioneFascia);
                intentMap.put("giornoSettimana", giornoSettimana);
                intentMap.put("sport", sport);
                intentMap.put("idCorso", idCorso);
                intentMap.put("idFascia", idFascia);
                intentMap.put("idIscrizione", row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE)).toString());
                intentMap.put("statoCorso", statoCorso);
                intentMap.put("nomeIscritto", row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString());
                intentMap.put("statoIscrizione", row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO)).toString());
                intentMap.put("tipoCorso", tipoCorso);
                intentMap.put("idElemento", row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO)).toString());

                if (row.getColumnValue(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO)).toString().equals(STATO_ATTIVA) &&
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString().equals(STATO_CARICO)) {

                    createListener(intentMap, row);
                }
                tabellaContattiIscritti.addView(tableRow);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createListener(Map<String, String> intentMap, Row row) {
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tableRow = (TableRow) view;
                TextView textView = (TextView) tableRow.getChildAt(3);
                String idElementoSelezionato = textView.getText().toString();

                tableRow.setBackground(ContextCompat.getDrawable(registraPresenze, R.drawable.cell_bg_gradient));

                textView = (TextView) tableRow.getChildAt(4);
                String idPresenzaSelezionato = textView.getText().toString();

                textView = (TextView) tableRow.getChildAt(5);
                String idIscrizioneSelezionato = textView.getText().toString();

                int numeroLezioniRimanenti = 0;
                if (idPresenzaSelezionato.equals("")) {
                    numeroLezioniRimanenti = CreaPresenzaContattoIscritto.getInstance().make(idIscrizioneSelezionato, idElementoSelezionato);
                    if (numeroLezioniRimanenti == 0) {
                        makeToastMessage(registraPresenze, "Presenza confermata, " + row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString() + " ha terminato le lezioni").show();
                    }
                    else {
                        makeToastMessage(registraPresenze, "Presenza confermata, " + numeroLezioniRimanenti + " lezioni rimanenti").show();
                    }
                }
                else {
                    numeroLezioniRimanenti = RimuoviPresenzaContattoIscritto.getInstance().make(idPresenzaSelezionato,idElementoSelezionato);
                    makeToastMessage(registraPresenze, "Presenza rimossa, " + numeroLezioniRimanenti + " lezioni rimanenti").show();
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
                finish();
            }
        });
    }

    private void makeIntestazioneTabella() {
        tableRow = new TableRow(registraPresenze);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        headerTabellaContattiIscritti.addView(tableRow);
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
