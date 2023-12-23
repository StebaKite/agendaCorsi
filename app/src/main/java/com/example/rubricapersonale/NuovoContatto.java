package com.example.rubricapersonale;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NuovoContatto extends AppCompatActivity {

    Button cancella, esci, salva;
    TextView nome, indirizzo, telefono, email;
    SQLiteDatabase database;

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

        // apro la connessione al db
        database = openOrCreateDatabase("contattiPersonali.db", MODE_PRIVATE, null);

        // implemento i listener sui bottoni
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NuovoContatto.this, MainActivity.class);
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
                String _nome = nome.getText().toString();
                String _indirizzo = indirizzo.getText().toString();
                String _telefono = telefono.getText().toString();
                String _email = email.getText().toString();

                // controllo validità campi inseriti
                if (_nome.equals("") || _indirizzo.equals("") || _telefono.equals("") || _email.equals("")) {
                    AlertDialog.Builder messaggio=  new AlertDialog.Builder(NuovoContatto.this);
                    messaggio.setTitle("Attenzione!");
                    messaggio.setMessage("Iinserire tutti i campi");
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
                    String query = "insert into contatti (nome, indirizzo, telefono, email)" +
                            "values ('" + _nome + "', '" + _indirizzo + "', '" + _telefono + "', '" + _email + "')";
                    database.execSQL(query);
                    esci.callOnClick();
                }
            }
        });
    }
}
