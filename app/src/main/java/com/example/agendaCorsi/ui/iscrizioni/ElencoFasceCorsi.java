package com.example.agendaCorsi.ui.iscrizioni;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.List;
import java.util.Map;

public class ElencoFasceCorsi extends FunctionBase {

    TableLayout tabSettimana;
    TextView corso, giorno, fascia, totaleFascia, idFascia;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_fasce_corsi);
        esci = findViewById(R.id.bExit);

        displayElencoFasceCorsi();
        listenerEsci(ElencoFasceCorsi.this, MainActivity.class, null);
    }

    private void displayElencoFasceCorsi() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonnaCorso = (int) (displayMetrics.widthPixels * 0.3);
        int larghezzaColonnaGiorno = (int) (displayMetrics.widthPixels * 0.1);
        int larghezzaColonnaFascia = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonnaTotale = (int) (displayMetrics.widthPixels * 0.1);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> fasceCorsiList = new FasciaDAO(this).getAllFasceCorsi(properties.getProperty(QUERY_TOTALS_CORSI));

        String descrizione_corso_save = "";

        for (Object entity : fasceCorsiList) {
            FasciaCorso fasciaCorso = FasciaCorso.class.cast(entity);

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            corso = new TextView(this);
            corso.setTextSize(16);
            corso.setPadding(10,20,10,20);
            corso.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            corso.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            corso.setGravity(Gravity.CENTER);
            corso.setText(fasciaCorso.getDescrizioneCorso());
            corso.setWidth(larghezzaColonnaCorso);
            tableRow.addView(corso);

            giorno = new TextView(this);
            giorno.setTextSize(16);
            giorno.setPadding(10,20,10,20);
            giorno.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            giorno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno.setGravity(Gravity.CENTER);
            giorno.setText(fasciaCorso.getGiornoSettimana());
            giorno.setWidth(larghezzaColonnaGiorno);
            tableRow.addView(giorno);

            fascia = new TextView(this);
            fascia.setTextSize(16);
            fascia.setPadding(10,20,10,20);
            fascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            fascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia.setGravity(Gravity.CENTER);
            fascia.setText(fasciaCorso.getDescrizioneFascia());
            fascia.setWidth(larghezzaColonnaFascia);
            tableRow.addView(fascia);

            totaleFascia = new TextView(this);
            totaleFascia.setTextSize(16);
            totaleFascia.setPadding(10,20,10,20);
            totaleFascia.setBackground(ContextCompat.getDrawable(ElencoFasceCorsi.this, R.drawable.cell_border));
            totaleFascia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            totaleFascia.setGravity(Gravity.CENTER);
            totaleFascia.setText(fasciaCorso.getTotaleFascia());
            totaleFascia.setWidth(larghezzaColonnaTotale);
            tableRow.addView(totaleFascia);

            idFascia = new TextView(this);
            idFascia.setVisibility(View.INVISIBLE);
            idFascia.setText(fasciaCorso.getIdFascia());
            tableRow.addView(idFascia);

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("descrizioneCorso", fasciaCorso.getDescrizioneCorso());
            intentMap.put("descrizioneFascia", fasciaCorso.getDescrizioneFascia());
            intentMap.put("giornoSettimana", fasciaCorso.getGiornoSettimana());

            listenerTableRow(ElencoFasceCorsi.this, ElencoIscrizioni.class, "idFascia", intentMap, 4);
            tabSettimana.addView(tableRow);
        }
    }
}
