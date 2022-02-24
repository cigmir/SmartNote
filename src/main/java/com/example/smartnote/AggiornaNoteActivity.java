package com.example.smartnote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AggiornaNoteActivity extends AppCompatActivity {

    EditText titolo, descrizione;
    Button aggiornaNote;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiorna_note);

        titolo =findViewById(R.id.titolo);
        descrizione =findViewById(R.id.descrizione);
        aggiornaNote =findViewById(R.id.aggiornaNota);

        Intent i =getIntent();
        titolo.setText(i.getStringExtra("title"));
        descrizione.setText(i.getStringExtra("description"));
        id=i.getStringExtra("id");

        aggiornaNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(titolo.getText().toString()) && !TextUtils.isEmpty(descrizione.getText().toString()))
                {

                    DatabaseClasse db = new DatabaseClasse(AggiornaNoteActivity.this);
                    db.updateNotes(titolo.getText().toString(), descrizione.getText().toString(),id);

                    Intent i=new Intent(AggiornaNoteActivity.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();


                }
                else
                {
                    Toast.makeText(AggiornaNoteActivity.this, "Entrambi i campi sono richiesti", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}