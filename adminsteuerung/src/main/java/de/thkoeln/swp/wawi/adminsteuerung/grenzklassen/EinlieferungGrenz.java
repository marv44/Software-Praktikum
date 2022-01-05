package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Einlieferung;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EinlieferungGrenz {

    private Integer elfid;
    private String lieferant;
    private LocalDate angelegt;
    private BigDecimal gesamtPreis;

    public EinlieferungGrenz() {
    }

    public EinlieferungGrenz(Integer elfid, String lieferant, LocalDate angelegt, BigDecimal gesamtPreis) {
        this.elfid = elfid;
        this.lieferant = lieferant;
        this.angelegt = angelegt;
        this.gesamtPreis = gesamtPreis;
    }

    public EinlieferungGrenz(Einlieferung einlieferung) {
        this.elfid = einlieferung.getElfid();
        this.lieferant = einlieferung.getLieferant().getVorname() + ", " + einlieferung.getLieferant().getName();
        this.angelegt = einlieferung.getCreated();
        this.gesamtPreis = einlieferung.getGesamtpreis();
    }

    public Integer getElfid() {
        return elfid;
    }

    public void setElfid(Integer elfid) {
        this.elfid = elfid;
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

    public String getLieferant() {
        return lieferant;
    }

    public void setLieferant(String lieferant) {
        this.lieferant = lieferant;
    }
}
