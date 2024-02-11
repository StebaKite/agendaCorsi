package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NuovoElementoPortfolio extends FunctionBase {

    String idContatto;
    String nomeContatto;
    TextView labelScheda, descrizione, numeroLezioni;
    Context nuovoElementoPortfolio;
    RadioButton radio_skate, radio_basket, radio_pallavolo, radio_pattini;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_elemento_portfolio);

        makeToolBar(this);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        Intent intent = getIntent();
        idContatto = intent.getStringExtra("idContatto");
        nomeContatto = intent.getStringExtra("nomeContatto");

        descrizione = findViewById(R.id.editDescrizione);
        numeroLezioni = findViewById(R.id.editNumeroLezioni);
        labelScheda = findViewById(R.id.lScheda);
        radio_skate = findViewById(R.id.radio_skate);
        radio_basket = findViewById(R.id.radio_basket);
        radio_pallavolo = findViewById(R.id.radio_pallavolo);
        radio_pattini = findViewById(R.id.radio_pattini);

        labelScheda.setText(nomeContatto);

        nuovoElementoPortfolio = this;
        /*
         * listener sui bottoni
         */
        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idContatto", String.valueOf(idContatto));

        listenerEsci(nuovoElementoPortfolio, ModificaContatto.class, intentMap);
        listenerAnnulla();
        listenerSalva();
    }


    public void makeSalva() {
        if (descrizione.getText().toString().equals("") || numeroLezioni.getText().toString().equals("")) {
            displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            String sport = "";
            if (radio_skate.isChecked()) { sport = Skate; }
            if (radio_basket.isChecked()) { sport = Basket; }
            if (radio_pallavolo.isChecked()) { sport = Pallavolo; }
            if (radio_pattini.isChecked()) { sport = Pattini; }

            String statoElemento = (Integer.parseInt(String.valueOf(numeroLezioni.getText())) > 0) ? STATO_CARICO : STATO_APERTO;

            String[] columnToRead = {ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO)};
            Row selectionRow = new Row();
            selectionRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_CONTATTO), idContatto);
            selectionRow.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.SPORT), sport);

            try {
                List<Row> elementiList = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME, columnToRead, selectionRow, null);
                if (elementiList.size() == 0) {
                    Row row = new Row();
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_CONTATTO), idContatto);
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DESCRIZIONE), descrizione.getText().toString());
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.SPORT), sport);
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI), numeroLezioni.getText().toString());
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMA_RICARICA), null);
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), statoElemento);
                    row.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_CREAZIONE), dateFormat.format(date));

                    List<Row> rowsToInsert = new LinkedList<>();
                    rowsToInsert.add(row);
                    ConcreteDataAccessor.getInstance().insert(ElementoPortfolio.TABLE_NAME, rowsToInsert);

                    makeToastMessage(AgendaCorsiApp.getContext(), "Elemento portfolio creato con successo.").show();
                    esci.callOnClick();
                } else {
                    displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Elemento portfolio già presente, duplicazione non ammessa");
                }
            } catch (Exception e) {
                displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
            }
        }
    }

    public void makeAnnulla() {
        descrizione.setText("");
        numeroLezioni.setText("0");
        makeToastMessage(AgendaCorsiApp.getContext(), "Ripristino dati originali eseguito").show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(NuovoElementoPortfolio.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
