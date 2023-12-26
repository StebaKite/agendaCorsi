package com.example.agendaCorsi.ui.contatti;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaCorsi.MainActivity;
import com.example.agendacorsi.R;

public class ModificaContatto extends AppCompatActivity {
    TextView labelScheda;
    int idContatto;
    String nome, indirizzo, telefono, email;
    EditText _nome, _indirizzo, _telefono, _email;
    Button annulla, esci, salva, elimina;
    SQLiteDatabase database;
    String query;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_contatto);
        Intent intent = getIntent();

        idContatto = intent.getIntExtra("id", 0);
        _nome = findViewById(R.id.editNomeMod);
        _indirizzo = findViewById(R.id.editIndirizzoMod);
        _telefono = findViewById(R.id.editTelefonoMod);
        _email = findViewById(R.id.editEmailMod);

        annulla = findViewById(R.id.AnnullaModButton);
        esci = findViewById(R.id.ExitModButton);
        salva = findViewById(R.id.SalvaModButton);
        elimina = findViewById(R.id.EliminaModButton);

        labelScheda = findViewById(R.id.labelScheda);
        labelScheda.setText(getString(R.string.scheda_contatto) + String.valueOf(idContatto));

        // apro la connessione al db ed eseguo la query
        database = openOrCreateDatabase("contattiPersonali.db", MODE_PRIVATE, null);
        query = "select * from contatti where id = " + String.valueOf(idContatto);
        final Cursor resultSet = database.rawQuery(query, null);

        // caricamento dei dati in pagina
        while (resultSet.moveToNext()) {
            nome = resultSet.getString(1);
            indirizzo = resultSet.getString(2);
            telefono = resultSet.getString(3);
            email = resultSet.getString(4);

            _nome.setText(nome);
            _indirizzo.setText(indirizzo);
            _telefono.setText(telefono);
            _email.setText(email);

            esci.requestFocus();
        }
        resultSet.close();

        // implemento i listener dei bottoni

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _nome.setText(nome);
                _indirizzo.setText(indirizzo);
                _telefono.setText(telefono);
                _email.setText(email);
            }
        });

        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificaContatto.this, ElencoContatti.class);
                startActivity(intent);
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // dati modificati sulla pagina
                String nomeMod = _nome.getText().toString();
                String indirizzoMod = _indirizzo.getText().toString();
                String telefonoMod = _telefono.getText().toString();
                String emailMod = _email.getText().toString();

                // controllo di validità
                if (nomeMod.equals("") || indirizzoMod.equals("") || telefonoMod.equals("") || emailMod.equals("")) {
                    AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this);
                    messaggio.setTitle("Attenzione!");
                    messaggio.setMessage("Inserire tutti i campi");
                    messaggio.setCancelable(false);
                    messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                } else {
                    String query = "update contatti set nome = '" + nomeMod + "', indirizzo = '" + indirizzoMod +
                            "', telefono = '" + telefonoMod + "', email = '" + emailMod + "' where id = " +
                            String.valueOf(idContatto);
                    database.execSQL(query);
                    esci.callOnClick();
                }
            }
        });

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this);
                messaggio.setTitle("Attenzione");
                messaggio.setMessage("Stai eliminando il contatto " + String.valueOf(idContatto) + "\n\nConfermi?");
                messaggio.setCancelable(false);

                // implemento i listener sui bottoni della conferma eliminazione
                messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String query = "delete from contatti where id = " + String.valueOf(idContatto);
                        database.execSQL(query);
                        esci.callOnClick();
                    }
                });

                messaggio.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog ad = messaggio.create();
                ad.show();
            }
        });
    }
}
