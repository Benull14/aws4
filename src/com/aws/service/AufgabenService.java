package com.aws.service;

import com.aws.model.Aufgabe;
import com.aws.repos.AufgabenRepo;

import java.util.List;

public class AufgabenService {
    private AufgabenRepo repository;

    public AufgabenService() {
        this.repository = new AufgabenRepo("aufgaben.db");
    }

    public void aufgabeHinzufuegen(Aufgabe aufgabe) {
        repository.aufgabeHinzufuegen(aufgabe);
        System.out.println("Aufgabe hinzugefügt: " + aufgabe.getTitel());
    }

    public List<Aufgabe> getAufgabenListe() {
        return repository.getAufgaben();
    }

    public void aufgabeAlsErledigtMarkieren(String titel) {
        repository.aufgabeAlsErledigtMarkieren(titel);
        System.out.println("Aufgabe als erledigt markiert: " + titel);
    }

    public void aufgabeLoeschen(String titel) {
        repository.aufgabeLoeschen(titel);
        System.out.println("Aufgabe gelöscht: " + titel);
    }


    public void close() {
        repository.close();
    }
}
