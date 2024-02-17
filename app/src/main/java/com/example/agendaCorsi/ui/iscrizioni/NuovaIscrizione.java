package com.example.agendaCorsi.ui.iscrizioni;

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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

;

public class NuovaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout tabellaContattiIscrivibili, headerTabellaContattiIscrivibili;
    Context nuovaIscrizione;

    int larghezzaColonna1, larghezzaColonna2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_iscrizione);

        nuovaIscrizione = this;
        makeToolBar(nuovaIscrizione);

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

        tabellaContattiIscrivibili = findViewById(R.id.tabellaContattiIscrivibili);
        headerTabellaContattiIscrivibili = findViewById(R.id.headerTabellaContattiIscrivibili);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);

        makeIntestazioneTabella();
        loadContattiIscrvibili(tipoCorso);

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


    private void loadContattiIscrvibili(String tipoCorso) {
        try {
            Row seletionColumn = new Row();
            seletionColumn.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.SPORT), sport);

            if (tipoCorso.equals(Produzione)) {
                seletionColumn.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), STATO_CARICO);
            }
            List<Row> contattiIscrivibiliList = getContattiIsrivibili(seletionColumn);

            for (Row contattoIscrivibile : contattiIscrivibiliList) {
                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this,new TextView(this),
                        DETAIL_SIMPLE,
                        larghezzaColonna1,
                        contattoIscrivibile.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        DETAIL_SIMPLE,
                        larghezzaColonna2,
                        String.valueOf(computeAge(contattoIscrivibile.getColumnValue(Contatto.contattoColumns.get(Contatto.DATA_NASCITA)).toString())),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        DETAIL_SIMPLE,
                        0,
                        contattoIscrivibile.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO)).toString(),
                        0,
                        View.GONE));

                tableRow.addView(makeCell(this,new TextView(this),
                        DETAIL_SIMPLE,
                        0,
                        contattoIscrivibile.getColumnValue(Contatto.contattoColumns.get(Contatto.EMAIL)).toString(),
                        0,
                        View.GONE));

                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            TableRow tableRow = (TableRow) view;

                            TextView textView = (TextView) tableRow.getChildAt(0);
                            String nomeContatto = textView.getText().toString();

                            textView = (TextView) tableRow.getChildAt(2);
                            String idSelezionato = textView.getText().toString();

                            textView = (TextView) tableRow.getChildAt(3);
                            String emailContatto = textView.getText().toString();

                            tableRow.setBackground(ContextCompat.getDrawable(nuovaIscrizione, R.drawable.cell_bg_gradient));

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();

                            Row insertColumn = new Row();
                            insertColumn.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_FASCIA), idFascia);
                            insertColumn.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ELEMENTO), idSelezionato);
                            insertColumn.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_ATTIVA);
                            insertColumn.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_CREAZIONE), dateFormat.format(date));
                            insertColumn.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO), null);

                            List<Row> rowsToInsert = new LinkedList<>();
                            rowsToInsert.add(insertColumn);
                            ConcreteDataAccessor.getInstance().insert(Iscrizione.TABLE_NAME, rowsToInsert);

                            if (!statoCorso.equals(STATO_ATTIVO)) {
                                ConcreteDataAccessor.getInstance().update(Corso.TABLE_NAME,
                                        new Row(Corso.corsoColumns.get(Corso.ID_CORSO), idCorso),
                                        new Row(Corso.corsoColumns.get(Corso.STATO), STATO_ATTIVO));
                            }
                            makeToastMessage(nuovaIscrizione, "Iscrizione creata con successo.").show();
                            esci.callOnClick();
                        }
                        catch (Exception e) {
                            displayAlertDialog(nuovaIscrizione, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                        }
                    }
                });
                tabellaContattiIscrivibili.addView(tableRow);
            }
        }
        catch (Exception e) {
            displayAlertDialog(nuovaIscrizione, "Attenzione!", "Lettura contatti iscrivibili fallito, contatta il supporto tecnico");
        }
    }


    @NonNull
    private List<Row> getContattiIsrivibili(Row seletionColumn) throws Exception {
        List<Row> contattiPortfolioRows = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.VIEW_CONTATTI_PORTFOLIO,
                null,
                seletionColumn,
                new String[]{Contatto.contattoColumns.get(Contatto.NOME).toString()});

        List<Row> contattiIscrivibiliList = new LinkedList<>();

        for (Row contatto : contattiPortfolioRows) {
            List<Row> corsiIscrizioniRow = ConcreteDataAccessor.getInstance().read(Corso.VIEW_CORSI_ISCRIZIONI,
                    null,
                    new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO),
                            contatto.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO))),
                            null);
            if (corsiIscrizioniRow.size() == 0) {
                contattiIscrivibiliList.add(contatto);
            }
        }
        return contattiIscrivibiliList;
    }


    private void makeIntestazioneTabella() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        headerTabellaContattiIscrivibili.addView(tableRow);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(NuovaIscrizione.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
