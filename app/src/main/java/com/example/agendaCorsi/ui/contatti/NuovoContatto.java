package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

public class NuovoContatto extends FunctionBase {

    Button cancella, esci, salva;
    TextView nome, indirizzo, telefono, email;
    Context nuovoContatto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_contatto);

        cancella = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        nome = findViewById(R.id.editNome);
        indirizzo = findViewById(R.id.editIndirizzo);
        telefono = findViewById(R.id.editTelefono);
        email = findViewById(R.id.editEmail);

        nuovoContatto = this;

        // implemento i listener sui bottoni
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NuovoContatto.this, ElencoContatti.class);
                startActivity(intent);
            }
        });

        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome.setText("");
                indirizzo.setText("");
                telefono.setText("");
                email.setText("");
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contatto contatto = new Contatto(null, nome.getText().toString(), indirizzo.getText().toString(), telefono.getText().toString(), email.getText().toString());
                /**
                 * controllo validità campi inseriti
                 */
                if (contatto.getNome().equals("") ||
                    contatto.getIndirizzo().equals("") ||
                    contatto.getTelefono().equals("") ||
                    contatto.getEmail().equals("")) {

                    displayAlertDialog(nuovoContatto, "Attenzione!", "Inserire tutti i campi");
                } else {
                    if (new ContattiDAO(nuovoContatto).insert(contatto)) {
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(nuovoContatto, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                    }
                }
            }
        });
    }
}
