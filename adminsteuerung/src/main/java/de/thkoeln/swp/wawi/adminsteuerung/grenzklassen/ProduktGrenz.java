package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Kategorie;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lager;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerort;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduktGrenz {

    private Integer prodId;
    private String name;
    private String beschreibung;
    private LocalDate angelegt;
    private int stueckzahl;
    private BigDecimal nettopreis;
    private int mwstsatz;
    private boolean aktiv;
    private int kategorie;
    private int lagerort;

    public ProduktGrenz() {
    }

    public ProduktGrenz(Integer prodId, String name, String beschreibung, LocalDate angelegt, int stueckzahl, BigDecimal nettopreis, int mwstsatz, boolean aktiv, int kategorie, int lagerort) {
        this.prodId = prodId;
        this.name = name;
        this.beschreibung = beschreibung;
        this.angelegt = angelegt;
        this.stueckzahl = stueckzahl;
        this.nettopreis = nettopreis;
        this.mwstsatz = mwstsatz;
        this.aktiv = aktiv;
        this.kategorie = kategorie;
        this.lagerort = lagerort;
    }

    public ProduktGrenz(Produkt produkt) {
        this.prodId = produkt.getProdid();
        this.name = produkt.getName();
        this.beschreibung = produkt.getBeschreibung();
        this.angelegt = produkt.getAngelegt();
        this.stueckzahl = produkt.getStueckzahl();
        this.nettopreis = produkt.getNettopreis();
        this.mwstsatz = produkt.getMwstsatz();
        this.aktiv = produkt.getAktiv();
        this.kategorie = produkt.getKategorie().getKatid();
        this.lagerort = produkt.getLagerort().getLgortid();
    }


    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDate getAngelegt() {
        return angelegt;
    }

    public void setAngelegt(LocalDate angelegt) {
        this.angelegt = angelegt;
    }

    public int getStueckzahl() {
        return stueckzahl;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

    public BigDecimal getNettopreis() {
        return nettopreis;
    }

    public void setNettopreis(BigDecimal nettopreis) {
        this.nettopreis = nettopreis;
    }

    public int getMwstsatz() {
        return mwstsatz;
    }

    public void setMwstsatz(int mwstsatz) {
        this.mwstsatz = mwstsatz;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public int getKategorie() {
        return kategorie;
    }

    public void setKategorie(int kategorie) {
        this.kategorie = kategorie;
    }

    public int getLagerort() {
        return lagerort;
    }

    public void setLagerort(int lagerort) {
        this.lagerort = lagerort;
    }

    public Produkt toProdukt() {
        Produkt produkt = new Produkt(prodId, name, beschreibung, angelegt, stueckzahl, nettopreis, mwstsatz, aktiv);
        produkt.setKategorie(new Kategorie(kategorie));
        produkt.setLagerort(new Lagerort(lagerort));
        produkt.setBestellungspositionList(new ArrayList<>());
        return produkt;
    }

}
