package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendacorsi.R;

public class NuovoContatto extends FunctionBase {

    TextView nome, indirizzo, telefono, email;
    Context nuovoContatto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_contatto);

        annulla = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        nome = findViewById(R.id.editNome);
        indirizzo = findViewById(R.id.editIndirizzo);
        telefono = findViewById(R.id.editTelefono);
        email = findViewById(R.id.editEmail);

        nuovoContatto = this;
        /*
         * implemento i listener sui bottoni
         */
        listenerEsci(nuovoContatto, ElencoContatti.class, null);
        listenerAnnulla();
        listenerSalva();
    }

    public void makeSalva() {
        Contatto contatto = new Contatto(null, nome.getText().toString(), indirizzo.getText().toString(), telefono.getText().toString(), email.getText().toString());
        /**
         * controllo validità campi inseriti
         */
        if (contatto.getNome().equals("") ||
            contatto.getIndirizzo().equals("") ||
            contatto.getTelefono().equals("") ||
            contatto.getEmail().equals("")) {

            displayAlertDialog(nuovoContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            propertyReader = new PropertyReader(this);
            properties = propertyReader.getMyProperties("config.properties");

            if (new ContattiDAO(nuovoContatto).insert(contatto, properties.getProperty(QUERY_INS_CONTATTO))) {
                esci.callOnClick();
            }
            else {
                displayAlertDialog(nuovoContatto, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
            }
        }
    }
}
