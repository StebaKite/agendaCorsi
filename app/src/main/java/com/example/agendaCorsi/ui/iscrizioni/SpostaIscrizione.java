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

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;


public class SpostaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso, nomeIscritto, idIscrizione, statoIscrizione, capienza, totaleFascia;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana, _nomeIscritto;
    TableLayout tabellaFasce;
    Context spostaIscrizione;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sposta_iscrizione);

        spostaIscrizione = this;
        makeToolBar(spostaIscrizione);

        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        idIscrizione = intent.getStringExtra("idIscrizione");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");
        nomeIscritto = intent.getStringExtra("nomeIscritto");
        statoIscrizione = intent.getStringExtra("statoIscrizione");
        capienza = intent.getStringExtra("capienza");
        totaleFascia = intent.getStringExtra("totaleFascia");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _nomeIscritto = findViewById(R.id.editNomeIscritto);
        tabellaFasce = findViewById(R.id.tabellaFasce);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);
        _nomeIscritto.setText(nomeIscritto);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("descrizioneCorso", descrizioneCorso);
        intentMap.put("descrizioneFascia", descrizioneFascia);
        intentMap.put("giornoSettimana", giornoSettimana);
        intentMap.put("sport", sport);
        intentMap.put("idCorso", idCorso);
        intentMap.put("idFascia", idFascia);
        intentMap.put("idIscrizione", idIscrizione);
        intentMap.put("statoCorso", statoCorso);
        intentMap.put("nomeIscritto", nomeIscritto);
        intentMap.put("statoIscrizione", statoIscrizione);
        intentMap.put("totaleFascia", totaleFascia);
        intentMap.put("capienza", capienza);

        testataelenco();
        loadFasceCorso();

        listenerEsci(spostaIscrizione, ElencoIscrizioni.class, intentMap);
    }


    private void testataelenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna1,"Giorno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna2,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna3,"Totale", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
    }

    private void loadFasceCorso() {
        try {
            String[] orderColumns = new String[]{
                    Corso.corsoColumns.get(Corso.DESCRIZIONE_CORSO),
                    Fascia.fasciaColumns.get(Fascia.NUMERO_GIORNO),
                    Fascia.fasciaColumns.get(Fascia.DESCRIZIONE_FASCIA)
            };

            List<Row> fasceCorsiList = ConcreteDataAccessor.getInstance().read(Fascia.VIEW_FASCE_DISPONIBILI,
                    null,
                    new Row(Fascia.fasciaColumns.get(Fascia.ID_CORSO), idCorso),
                    orderColumns);

            for (Row row : fasceCorsiList) {
                if (!row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_FASCIA).toString()).equals(idFascia)) {
                    tableRow = new TableRow(this);
                    tableRow.setClickable(true);
                    String stato = (isFasciaCapiente(
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.TOTALE_FASCIA)).toString(),
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString())) ? DETAIL_SIMPLE : DETAIL_CLOSED;

                    tableRow.addView(makeCell(this,new TextView(this),
                            stato,
                            larghezzaColonna1,
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA)).toString(),
                            View.TEXT_ALIGNMENT_TEXT_START,
                            View.VISIBLE));

                    tableRow.addView(makeCell(this,new TextView(this),
                            stato,
                            larghezzaColonna2,
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.DESCRIZIONE_FASCIA)).toString(),
                            View.TEXT_ALIGNMENT_TEXT_START,
                            View.VISIBLE));

                    tableRow.addView(makeCell(this,new TextView(this),
                            stato,
                            larghezzaColonna3,
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.TOTALE_FASCIA)).toString(),
                            View.TEXT_ALIGNMENT_TEXT_START,
                            View.VISIBLE));

                    tableRow.addView(makeCell(this,new TextView(this),
                            stato, 0,
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_FASCIA)).toString(), 0, View.GONE));

                    if (isFasciaCapiente(
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.TOTALE_FASCIA)).toString(),
                            row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString())) {

                        tableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TableRow tableRow = (TableRow) view;
                                TextView textView = (TextView) tableRow.getChildAt(3);
                                String idSelezionato = textView.getText().toString();

                                tableRow.setBackground(ContextCompat.getDrawable(spostaIscrizione, R.drawable.cell_bg_gradient));

                                try {
                                    ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME,
                                            new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione),
                                            new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_FASCIA), idSelezionato));

                                    makeToastMessage(spostaIscrizione, "Iscrizione spostata con successo.").show();
                                    esci.callOnClick();
                                }
                                catch (Exception e) {
                                    displayAlertDialog(spostaIscrizione, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                                }
                            }
                        });
                    }
                    tabellaFasce.addView(tableRow);
                }
            }
        }
        catch (Exception e) {
            displayAlertDialog(spostaIscrizione, "Attenzione!", "Lettura fasce disponibili fallito, contatta il supporto tecnico");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(SpostaIscrizione.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
