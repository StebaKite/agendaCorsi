package com.example.agendaCorsi.ui.corsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.util.List;

public class ElencoCorsi extends FunctionBase {

    TableLayout tabCorsi, headerTabCorsi;
    int larghezzaColonna1, larghezzaColonna2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_elenco_corsi));

        makeToolBar(this);

        headerTabCorsi = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabCorsi = findViewById(R.id.tabellaCorsi);
        inserisci = findViewById(R.id.bInserisciCorso);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.6);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);

        testataElenco();
        loadCorsi();

        listenerEsci(ElencoCorsi.this, MainActivity.class, null);
        listenerInserisci(ElencoCorsi.this, NuovoCorso.class, null);
    }


    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome corso", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        headerTabCorsi.addView(tableRow);
    }

    private void loadCorsi() {
        try {
            List<Row> corsiList = ConcreteDataAccessor.getInstance().read(Corso.TABLE_NAME, null, null, null);
            for (Row row : corsiList) {
                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString(),
                        larghezzaColonna1,
                        row.getColumnValue(Corso.corsoColumns.get(Corso.DESCRIZIONE)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString(),
                        larghezzaColonna2,
                        row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                tableRow.addView(makeCell(this,new TextView(this),
                        row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString(), 0,
                        row.getColumnValue(Corso.corsoColumns.get(Corso.ID_CORSO)).toString(),
                        View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

                if (!row.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).equals(STATO_CHIUSO)) {
                    listenerTableRow(ElencoCorsi.this, ModificaCorso.class, "idCorso", null, 2);
                }
                tabCorsi.addView(tableRow);
            }
        }
        catch (Exception e) {
            displayAlertDialog(this, "Attenzione!", "Lettura corsi fallita, contatta il supporto tecnico");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ElencoCorsi.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
