package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.*;
import de.thkoeln.swp.wawi.adminsteuerung.services.ILagerVerwaltung;
import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;
import de.thkoeln.swp.wawi.datenhaltungapi.ILagerService;
import de.thkoeln.swp.wawi.wawidbmodel.entities.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Die Klasse ILagerVerwaltungImpl bietet Schnittstellen zur Verwaltung von Bestellungen und Einlieferungen.
 * LF140
 *
 * @author Pascal Sthamer, Marvin Vogel
 */
public class ILagerVerwaltungImpl implements ILagerVerwaltung {

    EntityManager em;
    ILagerService lagerService;
    IBestellungAdmin bestellungAdmin;

    public ILagerVerwaltungImpl() {
        // Implementierungen der Schnittstellen aus der Schicht Datenhaltung via loose Kopplung bekommen.
        ServiceLoader<ILagerService> lagerLoader = ServiceLoader.load(ILagerService.class);
        lagerService = lagerLoader.findFirst().orElseThrow();

        ServiceLoader<IBestellungAdmin> bestellungLoader = ServiceLoader.load(IBestellungAdmin.class);
        bestellungAdmin = bestellungLoader.findFirst().orElseThrow();

        // EntityManager setzen.
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        lagerService.setEntityManager(em);
        bestellungAdmin.setEntityManager(em);
    }

    /**
     * Gibt eine Liste aller Bestellungen zurück.
     * LF120/140
     *
     * @return die Liste aller Bestellungen
     */
    @Override
    public List<BestellungGrenz> getBestellungsListe() {
        List<BestellungGrenz> bestellungen = new ArrayList<>();

        for (Bestellung bestellung : bestellungAdmin.getAlleBestellungen()) {
            bestellungen.add(new BestellungGrenz(bestellung));
        }

        return bestellungen;
    }

    /**
     * Gibt eine Liste aller Bestellungen, die innerhalb des übergebenen Zeit-Intervals liegen, zurück.
     * LF120/140
     *
     * @param von das Startdatum des Zeitintervalls
     * @param bis das Enddatum des Zeitintervalls
     * @return die gefilterte Liste aller Bestellungen
     */
    @Override
    public List<BestellungGrenz> getBestellungsListe(LocalDate von, LocalDate bis) {
        List<BestellungGrenz> bestellungen = new ArrayList<>();

        for (Bestellung bestellung : bestellungAdmin.getBestellungenByDatum(von, bis)) {
            bestellungen.add(new BestellungGrenz(bestellung));
        }

        return bestellungen;
    }

    /**
     * Gibt eine Liste aller Bestellungspositionen, die zu einer Bestellung gehören, zurück.
     * LF140
     *
     * @param bestellId die ID der Bestellung, von der die Bestellungspositionen zurückgegeben werden sollen
     * @return die Liste der Bestellungspositionen oder null, wenn keine Bestellung mit der ID existiert
     */
    @Override
    public List<BestellungspositionGrenz> getBestellungspositionen(int bestellId) {
        List<BestellungspositionGrenz> bestellungspositionen = new ArrayList<>();
        Bestellung bestellung = bestellungAdmin.getBestellungById(bestellId);

        if (bestellung == null) {
            return null;
        }

        for (Bestellungsposition bp : bestellung.getBestellungspositionList()) {
            bestellungspositionen.add(new BestellungspositionGrenz(bp));
        }

        return bestellungspositionen;
    }

    /**
     * Gibt eine Bestellung anhand ihrer ID zurück.
     * LF120/140
     *
     * @param bestellId die ID der Bestellung
     * @return die Bestellung mit der angegebenen ID oder null, wenn keine Bestellung mit der ID existiert
     */
    @Override
    public BestellungGrenz getBestellungById(int bestellId) {
        Bestellung bestellung = bestellungAdmin.getBestellungById(bestellId);

        if (bestellung == null) {
            return null;
        }

        return new BestellungGrenz(bestellung);
    }


    /**
     * Gibt eine Liste aller Einlieferungen zurück.
     * LF130
     *
     * @return die Liste allerEinlieferungen
     */
    @Override
    public List<EinlieferungGrenz> getEinlieferungsListe() {
        List<EinlieferungGrenz> einlieferungen = new ArrayList<>();

        for (Einlieferung einlieferung : lagerService.getAlleEinlieferungen()) {
            einlieferungen.add(new EinlieferungGrenz(einlieferung));
        }

        return einlieferungen;
    }

    /**
     * Gibt eine Liste aller Einlieferungen, die innerhalb des übergebenen Zeit-Intervals liegen, zurück.
     * L130
     *
     * @param von das Startdatum des Zeitintervalls
     * @param bis das Enddatum des Zeitintervalls
     * @return die gefilterte Liste aller Einlieferungen
     */
    @Override
    public List<EinlieferungGrenz> getEinlieferungsListe(LocalDate von, LocalDate bis) {
        List<EinlieferungGrenz> einlieferungen = new ArrayList<>();

        for (Einlieferung einlieferung : lagerService.getEinlieferungenByDatum(von, bis)) {
            einlieferungen.add(new EinlieferungGrenz(einlieferung));
        }

        return einlieferungen;
    }

    /**
     * Gibt eine Liste aller Lieferpositionen, die zu einer Einlieferung gehören, zurück.
     * LF130
     *
     * @param einlieferungId die ID der Einlieferung, von der die Lieferpositionen zurückgegeben werden sollen
     * @return die Liste der Lieferpositionen oder null, wenn keine Einlieferung mit der ID existiert
     */
    @Override
    public List<LieferpositionGrenz> getLieferpositionen(int einlieferungId) {
        List<LieferpositionGrenz> lieferpositionen = new ArrayList<>();
        Einlieferung einlieferung = lagerService.getEinlieferungById(einlieferungId);

        if (einlieferung == null) {
            return null;
        }

        for (Lieferposition lp : einlieferung.getLieferpositionList()) {
            lieferpositionen.add(new LieferpositionGrenz(lp));
        }

        return lieferpositionen;
    }

    /**
     * Gibt eine Einlieferung anhand ihrer ID zurück.
     * LF120/130
     *
     * @param einlieferungsId die ID der Einlieferung
     * @return die Einlieferung mit der angegebenen ID oder null, wenn keine Einlieferung mit der ID existiert
     */
    @Override
    public EinlieferungGrenz getEinlieferungById(int einlieferungsId) {
        Einlieferung einlieferung = lagerService.getEinlieferungById(einlieferungsId);

        if (einlieferung == null) {
            return null;
        }

        return new EinlieferungGrenz(einlieferung);
    }

    /**
     * Gibt eine Liste aller Einlieferungen und Bestellungen zurück.
     * LF120
     *
     * @return Liste aller Einlieferungen und Bestellungen
     */
    @Override
    public List<LagerverkehrGrenz> getLagerverkehrListe() {
        List<LagerverkehrGrenz> gesamtLagerverkehr = new ArrayList<>();

        for (Lagerverkehr lagerverkehr : lagerService.getGesamtLagerverkehr()) {
            gesamtLagerverkehr.add(new LagerverkehrGrenz(lagerverkehr));
        }

        return gesamtLagerverkehr;
    }

    /**
     * Gibt eine Liste aller Einlieferungen und Bestellungen, die innerhalb des übergebenen Zeit-Intervals liegen, zurück.
     * LF120
     *
     * @param von das Startdatum des Zeitintervalls
     * @param bis das Enddatum des Zeitintervalls
     * @return die gefilterte Liste aller Einlieferungen und Bestellungen
     */
    @Override
    public List<LagerverkehrGrenz> getLagerverkehrListe(LocalDate von, LocalDate bis) {
        List<LagerverkehrGrenz> gesamtLagerverkehr = new ArrayList<>();

        for (Lagerverkehr lagerverkehr : lagerService.getLagerverkehrByDatum(von, bis)) {
            gesamtLagerverkehr.add(new LagerverkehrGrenz(lagerverkehr));
        }

        return gesamtLagerverkehr;
    }

    /**
     * Gibt eine Liste aller Lagerorte zurück.
     * LF100, LF110
     *
     * @return die Liste aller Lagerorte
     */
    @Override
    public List<LagerortGrenz> getLagerortListe() {
        List<LagerortGrenz> lagerortList = new ArrayList<LagerortGrenz>();
        for (Lagerort lagerort : lagerService.getLagerortListe()) {
            lagerortList.add(new LagerortGrenz(lagerort));
        }
        return lagerortList;
    }

}
