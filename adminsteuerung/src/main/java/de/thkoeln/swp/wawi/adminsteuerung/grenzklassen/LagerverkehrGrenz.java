package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerverkehr;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LagerverkehrGrenz {

    private Integer lgvid;
    private Integer elfid;
    private Integer bid;
    private String typ;
    private LocalDate angelegt;
    private BigDecimal gesamtPreis;

    public LagerverkehrGrenz() {
    }

    public LagerverkehrGrenz(Integer lgvid, Integer elfid, Integer bid, LocalDate angelegt, BigDecimal gesamtPreis) {
        this.lgvid = lgvid;
        this.elfid = elfid;
        this.bid = bid;
        if (elfid != null && bid == null) {
            this.typ = "Einlieferung";
        } else if (elfid == null && bid != null) {
            this.typ = "Bestellung";
        }
        this.angelegt = angelegt;
        this.gesamtPreis = gesamtPreis;
    }

    public LagerverkehrGrenz(Lagerverkehr lagerverkehr) {
        this.lgvid = lagerverkehr.getLgvid();

        if (lagerverkehr.getEinlieferung() != null) {
            this.typ = "Einlieferung";
            this.elfid = lagerverkehr.getEinlieferung().getElfid();
            this.gesamtPreis = lagerverkehr.getEinlieferung().getGesamtpreis();
        }

        if (lagerverkehr.getBestellung() != null) {
            this.typ = "Bestellung";
            this.bid = lagerverkehr.getBestellung().getBid();
            this.gesamtPreis = lagerverkehr.getBestellung().getGesamtbrutto();
        }

        this.angelegt = lagerverkehr.getCreated();
    }

    public Integer getLgvid() {
        return lgvid;
    }

    public void setLgvid(Integer lgvid) {
        this.lgvid = lgvid;
    }

    public Integer getElfid() {
        return elfid;
    }

    public void setElfid(Integer elfid) {
        this.elfid = elfid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public LocalDate getAngelegt() {
        return angelegt;
    }

    public void setAngelegt(LocalDate angelegt) {
        this.angelegt = angelegt;
    }

    public BigDecimal getGesamtPreis() {
        return gesamtPreis;
    }

    public void setGesamtPreis(BigDecimal gesamtPreis) {
        this.gesamtPreis = gesamtPreis;
    }

    public String getTyp() {
        return typ;
    }

}
