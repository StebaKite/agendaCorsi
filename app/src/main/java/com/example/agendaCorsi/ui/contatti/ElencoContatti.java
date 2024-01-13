package com.example.agendaCorsi.ui.contatti;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    TextView nomeContatto, idContatto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_contatti);
        tabContatti = findViewById(R.id.tabellaContatti);
        inserisci = findViewById(R.id.bInserisciContatto);
        esci = findViewById(R.id.bExit);

        displayElencoContatti();
        listenerEsci(ElencoContatti.this , MainActivity.class, null);
        listenerInserisci(ElencoContatti.this, NuovoContatto.class, null);
    }

    private void displayElencoContatti() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.8);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> contattiList = ContattiDAO.getInstance().getAll(QueryComposer.getInstance().getQuery(QUERY_GETALL_CONTATTI));

        for (Object entity : contattiList) {
            Contatto contatto = Contatto.class.cast(entity);
            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            /**
             * Caricamento del nome sul textView
             */
            nomeContatto = new TextView(this);
            nomeContatto.setTextSize(16);
            nomeContatto.setPadding(10,20,10,20);
            nomeContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            nomeContatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nomeContatto.setGravity(Gravity.CENTER);
            nomeContatto.setText(String.valueOf(contatto.getNome()));
            nomeContatto.setWidth(larghezzaColonna1);
            tableRow.addView(nomeContatto);
            /**
             * Caricamento ID contatto sul textView
             */
            idContatto = new TextView(this);
            idContatto.setTextSize(16);
            idContatto.setVisibility(View.INVISIBLE);
            idContatto.setPadding(10,20,10,20);
            idContatto.setBackground(ContextCompat.getDrawable(ElencoContatti.this, R.drawable.cell_border));
            idContatto.setGravity(Gravity.CENTER);
            idContatto.setText(String.valueOf(contatto.getId()));
            idContatto.setWidth(larghezzaColonna2);
            tableRow.addView(idContatto);

            listenerTableRow(ElencoContatti.this, ModificaContatto.class, "idContatto", null, 1);
            tabContatti.addView(tableRow);
        }
    }
}
