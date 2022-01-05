package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BestellungGrenz {

    private int bid;
    private String kunde;
    private String status;
    private BigDecimal gesamtnetto;
    private LocalDate angelegt;

    public BestellungGrenz() {
    }

    public BestellungGrenz(Integer bid, String kunde, String status, BigDecimal gesamtnetto, LocalDate angelegt) {
        this.bid = bid;
        this.kunde = kunde;
        this.status = status;
        this.gesamtnetto = gesamtnetto;
        this.angelegt = angelegt;
    }

    public BestellungGrenz(Bestellung bestellung) {
        bid = bestellung.getBid();
        gesamtnetto = bestellung.getGesamtbrutto();
        angelegt = bestellung.getCreated();
        this.kunde = bestellung.getKunde().getVorname() + ", " + bestellung.getKunde().getName();
        this.status = bestellung.getStatus();
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public BigDecimal getGesamtPreis() {
        return gesamtnetto;
    }

    public void setGesamtPreis(BigDecimal gesamtPreis) {
        this.gesamtnetto = gesamtPreis;
    }

    public LocalDate getAngelegt() {
        return angelegt;
    }

    public void setAngelegt(LocalDate angelegt) {
        this.angelegt = angelegt;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getKunde() {
        return kunde;
    }

    public void setKunde(String kunde) {
        this.kunde = kunde;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
