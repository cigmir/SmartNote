package com.example.smartnote;

public class Modello {

        String titolo;
        String descrizione;
        String id;

    public Modello(String id, String titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
