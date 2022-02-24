package com.example.smartnote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AggiungiNoteActivity extends AppCompatActivity {

    EditText titolo, descrizione;
    Button aggiungiNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_notes);

        titolo = findViewById(R.id.titolo);
        descrizione = findViewById(R.id.descrizione);
        aggiungiNota = findViewById(R.id.aggiungiNota);

        aggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(titolo.getText().toString()) && !TextUtils.isEmpty(descrizione.getText().toString())) {
                    DatabaseClasse db = new DatabaseClasse(AggiungiNoteActivity.this);
                    db.addNotes(titolo.getText().toString(), descrizione.getText().toString());

                    Intent intent = new Intent(AggiungiNoteActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AggiungiNoteActivity.this, "Entrambi i due spazi sono richiesti", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}