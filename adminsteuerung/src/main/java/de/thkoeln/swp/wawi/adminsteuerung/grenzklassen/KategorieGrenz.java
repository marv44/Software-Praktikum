package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Kategorie;

import java.util.ArrayList;

public class KategorieGrenz {

    private Integer katid;
    private int parentkatid;
    private String name;
    private String beschreibung;

    public KategorieGrenz() {
    }

    public KategorieGrenz(Integer katid, int parentkatid, String name, String beschreibung) {
        this.katid = katid;
        this.parentkatid = parentkatid;
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public KategorieGrenz(Kategorie kategorie) {
        katid = kategorie.getKatid();
        parentkatid = kategorie.getParentkatid();
        name = kategorie.getName();
        beschreibung = kategorie.getBeschreibung();
    }

    public Integer getKatid() {
        return katid;
    }

    public void setKatid(Integer katid) {
        this.katid = katid;
    }

    public int getParentkatid() {
        return parentkatid;
    }

    public void setParentkatid(int parentkatid) {
        this.parentkatid = parentkatid;
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

    public Kategorie toKategorie() {
        Kategorie kat = new Kategorie(katid, parentkatid, name, beschreibung);
        kat.setProduktList(new ArrayList<>());
        return kat;
    }

    public String toString() {
        return name;
    }

}
