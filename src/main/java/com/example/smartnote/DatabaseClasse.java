package com.example.smartnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseClasse extends SQLiteOpenHelper {


    Context context;
    private static final String NomeDatabase = "MyNotes";
    private static final int VersioneDatabase = 1;
    private static final String NomeTabella = "mynotes";
    private static final String ColonnaId = "id";
    private static final String ColonnaTitolo = "title";
    private static final String ColonnaDescrizione = "description";



    public DatabaseClasse(@Nullable Context context) {
        super(context, NomeDatabase, null, VersioneDatabase);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + NomeTabella +
                " (" + ColonnaId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColonnaTitolo + " TEXT, " +
                ColonnaDescrizione + " TEXT);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NomeTabella);
        onCreate(db);
    }

    void addNotes(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColonnaTitolo, title);
        cv.put(ColonnaDescrizione, description);

        long resultValue = db.insert(NomeTabella, null, cv);

        if (resultValue == -1) {
            Toast.makeText(context, "Nota non inserita", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Nota aggiunta con successo", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + NomeTabella;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }


    void deleteAllNotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + NomeTabella;
        database.execSQL(query);
    }

    void updateNotes(String title, String description, String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColonnaTitolo, title);
        cv.put(ColonnaDescrizione, description);

        long result = database.update(NomeTabella, cv, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Fallito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successo", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteSingleItem(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(NomeTabella, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Item Non cancellato", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, "Item cancellato con successo", Toast.LENGTH_SHORT).show();
        }
    }


}
