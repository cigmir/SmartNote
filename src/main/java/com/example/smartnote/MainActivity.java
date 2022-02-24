package com.example.smartnote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Modello> ListaNote;
    DatabaseClasse databaseClasse;
    CoordinatorLayout coordinatorLayout;
    BottomNavigationView bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar=findViewById(R.id.bottomBar);

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getItemId() == R.id.impostazioni) {
                    Intent i = new Intent(MainActivity.this, Preferenze.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.esci)
                {
                    finish();
                    System.exit(0);
                }

                if (item.getItemId() == R.id.condividi) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MyNotes");
                        String shareMessage= "\nLasciami raccomandarti questa applicazione:\n\n";
                        shareMessage = shareMessage + "https://gitfront.io/r/user-6046848/d36a98886caf70f208cc57daf21d91c8cf4191b8/ProgettoAndroidStudio/" +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "scegli una"));
                    } catch(Exception e) {
                        //e.toString();
                    }

                }



                return true;
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.layout_main);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AggiungiNoteActivity.class);
                startActivity(intent);




            }
        });



        ListaNote = new ArrayList<>();
        databaseClasse = new DatabaseClasse(this);
        LeggiTutteLeNoteDalDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, MainActivity.this, ListaNote);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }


    void LeggiTutteLeNoteDalDatabase() {
        Cursor cursor = databaseClasse.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Non ci sono note da mostrare", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                ListaNote.add(new Modello(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opzioni_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.barra_di_ricerca);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Ricerca nota/e");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.cancella_tutte_le_note) {
            deleteAllNotes();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        DatabaseClasse db = new DatabaseClasse(MainActivity.this);
        db.deleteAllNotes();
        recreate();
    }


    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Modello item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Nota cancellata", Snackbar.LENGTH_LONG)
                    .setAction("ANNULLA", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseClasse db = new DatabaseClasse(MainActivity.this);
                                db.deleteSingleItem(item.getId());
                            }


                        }
                    });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };

}

