package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

import java.util.Map;
import java.util.Objects;

public class NuovoElementoPortfolio extends FunctionBase {

    int idContatto;
    String nomeContatto;
    TextView labelScheda, descrizione, numeroLezioni;
    Context nuovoElementoPortfolio;
    RadioButton radio_skate, radio_basket, radio_pallavolo, radio_pattini;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_elemento_portfolio);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        Intent intent = getIntent();
        idContatto = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("id")));
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
        intentMap.put("id", String.valueOf(idContatto));

        listenerEsci(nuovoElementoPortfolio, ModificaContatto.class, intentMap);
        listenerAnnulla();
        listenerSalva();
    }

    public void makeSalva() {
        String dataUltimaRicarica = "";
        String sport = "";

        if (radio_skate.isChecked()) { sport = Skate; }
        if (radio_basket.isChecked()) { sport = Basket; }
        if (radio_pallavolo.isChecked()) { sport = Pallavolo; }
        if (radio_pattini.isChecked()) { sport = Pattini; }

        ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, String.valueOf(idContatto),
                descrizione.getText().toString(), sport, numeroLezioni.getText().toString(), dataUltimaRicarica, STATO_APERTO);

        if (elementoPortfolio.getDescrizione().equals("") || elementoPortfolio.getNumeroLezioni().equals("")) {
            displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            elementoPortfolio.setSport(sport);

            propertyReader = new PropertyReader(this);
            properties = propertyReader.getMyProperties("config.properties");

            if (new ElementoPortfolioDAO(nuovoElementoPortfolio).isNew(elementoPortfolio, properties.getProperty(QUERY_ISNEW_ELEMENTO))) {
                propertyReader = new PropertyReader(this);
                properties = propertyReader.getMyProperties("config.properties");

                if (new ElementoPortfolioDAO(nuovoElementoPortfolio).insert(elementoPortfolio, properties.getProperty(QUERY_INS_ELEMENTS))) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                }
            }
            else {
                displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Elemento portfolio già presente, duplicazione non ammessa");
            }
        }
    }

    public void makeAnnulla() {
        descrizione.setText("");
        numeroLezioni.setText("0");
    }
}
