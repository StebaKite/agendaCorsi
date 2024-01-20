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
    TextView nomeContatto, idContatto, eta;

    int larghezzaColonna1, larghezzaColonna2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_contatti);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);
        esci = findViewById(R.id.bExit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.6);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);

        testataElenco();
        loadContatti();

        listenerEsci(ElencoContatti.this , MainActivity.class, null);
        listenerInserisci(ElencoContatti.this, NuovoContatto.class, null);
    }

    private void testataElenco() {
        tableRow = new TableRow(this);
        tableRow.setClickable(false);
        /**
         * Cella 0
         */
        nomeContatto = new TextView(this);
        nomeContatto.setTextSize(14);
        nomeContatto.setPadding(10,20,10,20);
        nomeContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border_heading));
        nomeContatto.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        nomeContatto.setTypeface(null, Typeface.BOLD);
        nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        nomeContatto.setGravity(Gravity.CENTER);
        nomeContatto.setText("Nome");
        nomeContatto.setWidth(larghezzaColonna1);
        tableRow.addView(nomeContatto);
        /**
         * Cella 1
         */
        eta = new TextView(this);
        eta.setTextSize(14);
        eta.setPadding(10,20,10,20);
        eta.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border_heading));
        eta.setTextColor(getResources().getColor(R.color.table_border, getResources().newTheme()));
        eta.setTypeface(null, Typeface.BOLD);
        eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        eta.setGravity(Gravity.CENTER);
        eta.setText("Età");
        eta.setWidth(larghezzaColonna2);
        tableRow.addView(eta);

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
            /**
             * Cella 0
             */
            nomeContatto = new TextView(this);
            nomeContatto.setTextSize(14);
            nomeContatto.setPadding(10,20,10,20);
            nomeContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(contatto.getNome()));
            nomeContatto.setWidth(larghezzaColonna1);
            tableRow.addView(nomeContatto);
            /**
             * Cella 1
             */
            eta = new TextView(this);
            eta.setTextSize(14);
            eta.setPadding(10,20,10,20);
            eta.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            eta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            eta.setGravity(Gravity.CENTER);
            eta.setText(String.valueOf(computeAge(contatto.getDataNascita())));
            eta.setWidth(larghezzaColonna2);
            tableRow.addView(eta);
            /**
             * Cella 2
             */
            idContatto = new TextView(this);
            idContatto.setVisibility(View.INVISIBLE);
            idContatto.setText(String.valueOf(contatto.getId()));
            tableRow.addView(idContatto);

            listenerTableRow(ElencoContatti.this, ModificaContatto.class, "idContatto", null, 2);
            tabContatti.addView(tableRow);
        }
    }
}
