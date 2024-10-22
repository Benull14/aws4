package com.aws.model;

public class Aufgabe {
    private String titel;
    private String beschreibung;
    private int prioritaet; // z.B. 1 = Hoch, 2 = Mittel, 3 = Niedrig
    private boolean erledigt;

    // Konstruktoren
    public Aufgabe(String titel, String beschreibung, int prioritaet) {
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prioritaet = prioritaet;
        this.erledigt = false;
    }

    // Getter und Setter
    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(int prioritaet) {
        this.prioritaet = prioritaet;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    // toString()-Methode
    @Override
    public String toString() {
        String status = erledigt ? "Erledigt" : "Offen";

        String prio;
        switch (prioritaet) {
            case 1:
                prio = "Hoch";
                break;
            case 2:
                prio = "Mittel";
                break;
            case 3:
                prio = "Niedrig";
                break;
            default:
                prio = "Unbekannt";
        }
        return String.format("%-20s | %-10s | %-8s | %s", titel, prio, status, beschreibung);
    }
}
