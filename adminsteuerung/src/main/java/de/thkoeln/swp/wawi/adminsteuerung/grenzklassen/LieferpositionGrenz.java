package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Lieferposition;

import java.math.BigDecimal;

public class LieferpositionGrenz {

    private Integer lpid;
    private int einlieferung;
    private String produkt;
    private int anzahl;
    private BigDecimal kaufpreis;

    public LieferpositionGrenz() {
    }

    public LieferpositionGrenz(Integer lpid, Integer einlieferung, String produkt, int anzahl, BigDecimal kaufpreis) {
        this.lpid = lpid;
        this.einlieferung = einlieferung;
        this.produkt = produkt;
        this.anzahl = anzahl;
        this.kaufpreis = kaufpreis;
    }

    public LieferpositionGrenz(Lieferposition lieferposition) {
        this.lpid = lieferposition.getLpid();
        this.einlieferung = lieferposition.getEinlieferung().getElfid();
        this.produkt = lieferposition.getProdukt().getName();
        this.anzahl = lieferposition.getAnzahl();
        this.kaufpreis = lieferposition.getKaufpreis();
    }

    public Integer getLpid() {
        return lpid;
    }

    public void setLpid(Integer lpid) {
        this.lpid = lpid;
    }

    public int getEinlieferung() {
        return einlieferung;
    }

    public void setEinlieferung(int einlieferung) {
        this.einlieferung = einlieferung;
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

    public BigDecimal getKaufpreis() {
        return kaufpreis;
    }

    public void setKaufpreis(BigDecimal kaufpreis) {
        this.kaufpreis = kaufpreis;
    }

}
