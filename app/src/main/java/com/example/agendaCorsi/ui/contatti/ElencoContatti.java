package com.example.agendaCorsi.ui.contatti;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoContatti extends FunctionBase {

    TableLayout tabContatti, headerTabContatti;
    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_contatti);

        makeToolBar(this);

        headerTabContatti = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        try {
            testataElenco();
            loadContatti();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        listenerEsci(ElencoContatti.this , MainActivity.class, null);
        listenerInserisci(ElencoContatti.this, NuovoContatto.class, null);
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        headerTabContatti.addView(tableRow);
    }

    private void loadContatti() {
        List<Row> rows = null;
        try {
            rows = ConcreteDataAccessor.getInstance().read(Contatto.VIEW_ALL_CONTATTI, null, null, new String[]{Contatto.contattoColumns.get(Contatto.NOME)});
            for (Row row : rows) {
                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.STATO_ELEMENTO)).toString(),
                        larghezzaColonna1,
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.STATO_ELEMENTO)).toString(),
                        larghezzaColonna2,
                        String.valueOf(computeAge(row.getColumnValue(Contatto.contattoColumns.get(Contatto.DATA_NASCITA)).toString())),
                        View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.STATO_ELEMENTO)).toString(),
                        larghezzaColonna3,
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.STATO_ELEMENTO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0,
                        row.getColumnValue(Contatto.contattoColumns.get(Contatto.ID_CONTATTO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

                listenerTableRow(ElencoContatti.this, ModificaContatto.class, "idContatto", null, 3);
                tabContatti.addView(tableRow);
            }
        } catch (Exception e) {
            displayAlertDialog(this, "Attenzione!", "Lettura contatti fallita, contatta il supporto tecnico");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoContatti.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
