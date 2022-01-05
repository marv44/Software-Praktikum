package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellungsposition;

public class BestellungspositionGrenz {

    private Integer bpid;
    private int bestellung;
    private String produkt;
    private int anzahl;

    public BestellungspositionGrenz() {
    }

    public BestellungspositionGrenz(Integer bpid, int bestellung, String produkt, int anzahl) {
        this.bpid = bpid;
        this.bestellung = bestellung;
        this.produkt = produkt;
        this.anzahl = anzahl;
    }

    public BestellungspositionGrenz(Bestellungsposition bp) {
        bpid = bp.getBpid();
        bestellung = bp.getBestellung().getBid();
        produkt = bp.getProdukt().getName();
        anzahl = bp.getAnzahl();
    }

    public Integer getBpid() {
        return bpid;
    }

    public void setBpid(Integer bpid) {
        this.bpid = bpid;
    }

    public int getBestellung() {
        return bestellung;
    }

    public void setBestellung(int bestellung) {
        this.bestellung = bestellung;
    }

    public String getProdukt() {
        return produkt;
    }

    public void setProdukt(String produkt) {
        this.produkt = produkt;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

}
