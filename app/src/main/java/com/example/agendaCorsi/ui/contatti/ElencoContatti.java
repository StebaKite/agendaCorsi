package com.example.agendaCorsi.ui.contatti;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import java.util.List;

public class ElencoContatti extends FunctionBase {

    TableLayout tabContatti;
    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_contatti);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.4);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        testataElenco();
        loadContatti();

        listenerEsci(ElencoContatti.this , MainActivity.class, null);
        listenerInserisci(ElencoContatti.this, NuovoContatto.class, null);
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tabContatti.addView(tableRow);
    }

    private void loadContatti() {

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> contattiList = ContattiDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_CONTATTI));

        for (Object entity : contattiList) {
            Contatto contatto = Contatto.class.cast(entity);
            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), contatto.getStatoElemento(), larghezzaColonna1, contatto.getNome(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), contatto.getStatoElemento(), larghezzaColonna2, String.valueOf(computeAge(contatto.getDataNascita())), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), contatto.getStatoElemento(), larghezzaColonna3, contatto.getStatoElemento(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contatto.getId(), View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

            listenerTableRow(ElencoContatti.this, ModificaContatto.class, "idContatto", null, 3);
            tabContatti.addView(tableRow);
        }
    }
}
