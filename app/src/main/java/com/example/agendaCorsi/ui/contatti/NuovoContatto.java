package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import com.example.agendacorsi.R;

public class NuovoContatto extends AppCompatActivity {

    Button cancella, esci, salva;
    TextView nome, indirizzo, telefono, email;
    SQLiteDatabase database;
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

        // apro la connessione al db
        database = openOrCreateDatabase("contattiPersonali.db", MODE_PRIVATE, null);

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

                    AlertDialog.Builder messaggio=  new AlertDialog.Builder(NuovoContatto.this);
                    messaggio.setTitle("Attenzione!");
                    messaggio.setMessage("Inserire tutti i campi");
                    messaggio.setCancelable(false);
                    messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog ad = messaggio.create();
                    ad.show();
                } else {
                    if (new ContattiDAO(nuovoContatto).insert(contatto)) {
                        esci.callOnClick();
                    }
                    else {
                        AlertDialog.Builder messaggio = new AlertDialog.Builder(NuovoContatto.this, R.style.Theme_AlertDialog);
                        messaggio.setTitle("Attenzione!");
                        messaggio.setMessage("Inserimento fallito, contatta il supporto tecnico");
                        messaggio.setCancelable(false);
                        messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog ad = messaggio.create();
                        ad.show();
                    }
                }
            }
        });
    }
}
