package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoFasceCorsi extends FunctionBase {

    TableLayout tabellaFasceCorsi;
    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;
    Context elencoFasceCorsi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_fasce_corsi);

        elencoFasceCorsi = this;
        makeToolBar(elencoFasceCorsi);

        esci = findViewById(R.id.bExit);
        tabellaFasceCorsi = findViewById(R.id.tabellaFasceCorsi);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);
        larghezzaColonna4 = (int) (displayMetrics.widthPixels * 0.2);

        testataElenco();
        loadFasceCorsi();
        listenerEsci(ElencoFasceCorsi.this, MainActivity.class, null);
    }


    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(true);
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna1,"Corso", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna2,"Giorno", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna3,"Fascia", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this, new TextView(this), HEADER, larghezzaColonna4,"Totale", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tabellaFasceCorsi.addView(tableRow);
    }

    private void loadFasceCorsi() {
        List<Row> fasceCorsiList = null;
        try {
            fasceCorsiList = ConcreteDataAccessor.getInstance().read(Fascia.VIEW_ALL_FASCE_CORSI,
                    null,
                    null,
                    new String[]{
                            Corso.corsoColumns.get(Corso.DESCRIZIONE_CORSO),
                            Fascia.fasciaColumns.get(Fascia.NUMERO_GIORNO),
                            Fascia.fasciaColumns.get(Fascia.DESCRIZIONE_FASCIA)});

            String descrizione_corso_save = "";

            for (Row row : fasceCorsiList) {
                String detailType = (isFasciaCapiente(
                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.TOTALE_FASCIA)).toString(),
                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString())) ? DETAIL_SIMPLE : DETAIL_CLOSED;

                int cellVisibility = (row.getColumnValue(Corso.corsoColumns.get(Corso.DESCRIZIONE_CORSO)).toString().equals(descrizione_corso_save)) ? View.INVISIBLE : View.VISIBLE;
                if (cellVisibility == View.VISIBLE) {
                    descrizione_corso_save = row.getColumnValue(Corso.corsoColumns.get(Corso.DESCRIZIONE_CORSO)).toString();
                }

                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType,
                        larghezzaColonna1,
                        row.getColumnValue(Corso.corsoColumns.get(Corso.DESCRIZIONE_CORSO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        cellVisibility));

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType,
                        larghezzaColonna2,
                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType, larghezzaColonna3,
                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.DESCRIZIONE_FASCIA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this, new TextView(this),
                        detailType, larghezzaColonna4,
                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.TOTALE_FASCIA)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START,
                        View.VISIBLE));

                tableRow.addView(makeCell(this, new TextView(this), detailType, 0, row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_FASCIA)).toString(), 0, View.GONE));
                tableRow.addView(makeCell(this, new TextView(this), detailType, 0, row.getColumnValue(Corso.corsoColumns.get(Corso.SPORT)).toString(), 0, View.GONE));
                tableRow.addView(makeCell(this, new TextView(this), detailType, 0, row.getColumnValue(Corso.corsoColumns.get(Corso.ID_CORSO)).toString(), 0, View.GONE));
                tableRow.addView(makeCell(this, new TextView(this), detailType, 0, row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString(), 0, View.GONE));
                tableRow.addView(makeCell(this, new TextView(this), detailType, 0, row.getColumnValue(Corso.corsoColumns.get(Corso.TIPO)).toString(), 0, View.GONE));

                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TableRow tableRow = (TableRow) view;
                        TextView textView = (TextView) tableRow.getChildAt(4);
                        String idSelezionato = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(3);
                        int totaleFascia = Integer.parseInt(textView.getText().toString());

                        textView = (TextView) tableRow.getChildAt(0);
                        String descrizioneCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(1);
                        String giornoSettimana = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(2);
                        String descrizioneFascia = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(5);
                        String sport = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(6);
                        String idCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(7);
                        String statoCorso = textView.getText().toString();

                        textView = (TextView) tableRow.getChildAt(8);
                        String tipoCorso = textView.getText().toString();

                        tableRow.setBackground(ContextCompat.getDrawable(elencoFasceCorsi, R.drawable.cell_bg_gradient));

                        /**
                         * Passo alla classe destinazione l'id della riga selezionata più tutti gli item inseriti nella intentMap
                         */
                        if (totaleFascia == 0) {
                            Intent intent = new Intent(AgendaCorsiApp.getContext(), NuovaIscrizione.class);
                            intent.putExtra("idFascia", idSelezionato);
                            intent.putExtra("descrizioneCorso", descrizioneCorso);
                            intent.putExtra("giornoSettimana", giornoSettimana);
                            intent.putExtra("descrizioneFascia", descrizioneFascia);
                            intent.putExtra("sport", sport);
                            intent.putExtra("idCorso", idCorso);
                            intent.putExtra("statoCorso", statoCorso);
                            intent.putExtra("tipoCorso", tipoCorso);

                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(AgendaCorsiApp.getContext(), ElencoIscrizioni.class);
                            intent.putExtra("idFascia", idSelezionato);
                            intent.putExtra("descrizioneCorso", descrizioneCorso);
                            intent.putExtra("giornoSettimana", giornoSettimana);
                            intent.putExtra("descrizioneFascia", descrizioneFascia);
                            intent.putExtra("sport", sport);
                            intent.putExtra("idCorso", idCorso);
                            intent.putExtra("statoCorso", statoCorso);
                            intent.putExtra("tipoCorso", tipoCorso);
                            intent.putExtra("totaleFascia", String.valueOf(totaleFascia));
                            intent.putExtra("capienza", row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString());

                            startActivity(intent);
                            finish();
                        }
                    }
                });
                tabellaFasceCorsi.addView(tableRow);
            }
        }
        catch (Exception e) {
            displayAlertDialog(this, "Attenzione!", "Lettura fasce corsi fallita, contatta il supporto tecnico");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoFasceCorsi.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
