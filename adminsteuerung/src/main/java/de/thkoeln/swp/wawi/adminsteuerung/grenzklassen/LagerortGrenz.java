package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerort;

public class LagerortGrenz {

    private Integer lgortid;
    private String bezeichnung;

    public LagerortGrenz() {
    }

    public LagerortGrenz(Integer lgortid, String bezeichnung) {
        this.lgortid = lgortid;
        this.bezeichnung = bezeichnung;
    }

    public LagerortGrenz(Lagerort lagerort) {
        this.lgortid = lagerort.getLgortid();
        this.bezeichnung = lagerort.getBezeichnung();
    }

    public Integer getLgortid() {
        return lgortid;
    }

    public void setLgortid(Integer lgortid) {
        this.lgortid = lgortid;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String toString() {
        return bezeichnung;
    }

}
