package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.ProduktGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.services.IProduktVerwaltung;
import de.thkoeln.swp.wawi.adminsteuerung.services.StueckzahlFilter;
import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDProdukt;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellungsposition;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Die Klasse IProduktVerwaltungImpl bietet Schnittstellen zur Verwaltung von Produkten.
 *
 * @author Janina Schroeder, Fabian Tsirogiannis
 */
public class IProduktVerwaltungImpl implements IProduktVerwaltung {

    private EntityManager em;
    private ICRUDProdukt crudProdukt;

    public IProduktVerwaltungImpl() {
        // Implementierungen der Schnittstellen aus der Schicht Datenhaltung via loose Kopplung bekommen.
        ServiceLoader<ICRUDProdukt> loader = ServiceLoader.load(ICRUDProdukt.class);
        crudProdukt = loader.findFirst().orElseThrow();

        // EntityManager setzen.
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        crudProdukt.setEntityManager(em);
    }

    /**
     * Legt ein neues Produkt an.
     * LF100
     *
     * @param produkt als Produkt das angelegt werden soll mit id null
     * @return zeigt an ob das Anlegen erfolgreich war
     */
    @Override
    public boolean produktAnlegen(ProduktGrenz produkt) {
        em.getTransaction().begin();

        if (crudProdukt.insertProdukt(produkt.toProdukt())) {
            em.getTransaction().commit();
            return true;
        } else {
            em.getTransaction().rollback();
        }

        return false;
    }

    /**
     * Bearbeitet ein Produkt.
     * LF100
     *
     * @param produkt als Produkt das angelegt werden soll mit einer vorhandenen id
     * @return zeigt an ob das Bearbeiten erfolgreich war
     */
    @Override
    public boolean produktBearbeiten(ProduktGrenz produkt) {
        em.getTransaction().begin();

        if (crudProdukt.updateProdukt(produkt.toProdukt())) {
            em.getTransaction().commit();
            return true;
        } else {
            em.getTransaction().rollback();
        }

        return false;
    }

    /**
     * Löscht ein Produkt falls es noch nie bestellt wurde. Andernfalls wird
     * es auf inaktiv gesetzt.
     * LF100
     *
     * @param produktId als Produkt das geloescht werden soll mit vorhandener id.
     * @return 0: löschen war erfolgreich, 1: Produkt wurde inaktiv gesetzt, 2: Löschen fehlgeschlagen
     */
    @Override
    public byte produktLoeschen(int produktId) {
        byte response = 2;

        em.getTransaction().begin();
        Produkt loescheProdukt = crudProdukt.getProduktById(produktId);

        if (loescheProdukt == null) {
            em.getTransaction().rollback();
        } else {
            List<Bestellungsposition> bestellungspositionList = loescheProdukt.getBestellungspositionList();

            if (bestellungspositionList.isEmpty()) {
                if (crudProdukt.deleteProdukt(produktId)) {
                    em.getTransaction().commit();
                    response = 0;
                } else {
                    em.getTransaction().rollback();
                    response = 2;
                }
            } else {
                loescheProdukt.setAktiv(false);
                crudProdukt.updateProdukt(loescheProdukt);
                em.getTransaction().commit();
                response = 1;
            }
        }

        return response;
    }

    /**
     * Holt die Liste aller Produkte.
     * LF100, LF110
     *
     * @return gibt die Liste aller Produkte zurueck
     */
    @Override
    public List<ProduktGrenz> getProduktListe() {
        List<ProduktGrenz> produktGrenzList = new ArrayList<ProduktGrenz>();

        for (Produkt p : crudProdukt.getProduktListe()) {
            produktGrenzList.add(new ProduktGrenz(p));
        }

        return produktGrenzList;
    }

    /**
     * Holt eine Liste von Produkten, gefiltert nach Stückzahl.
     * LF110
     *
     * @param filter     Wie gefiltert werden soll (weniger als, mehr als, gleich)
     * @param stueckzahl Stueckzahl anhand der gefiltert wird
     * @return gefilterte Produktliste
     */
    @Override
    public List<ProduktGrenz> getProduktListe(StueckzahlFilter filter, int stueckzahl) {
        List<ProduktGrenz> produkte = new ArrayList<ProduktGrenz>();

        for (Produkt p : crudProdukt.getProduktListe()) {
            switch (filter) {
                case WENIGER_ALS:
                    if (p.getStueckzahl() < stueckzahl) produkte.add(new ProduktGrenz(p));
                    break;
                case MEHR_ALS:
                    if (p.getStueckzahl() > stueckzahl) produkte.add(new ProduktGrenz(p));
                    break;
                case GLEICH:
                    if (p.getStueckzahl() == stueckzahl) produkte.add((new ProduktGrenz(p)));
                    break;
            }
        }

        return produkte;
    }

    /**
     * Produkte die oberhalb einer eingegebenen Stueckzahl vorhanden sind, sollen aktiviert werden koennen
     * LF110
     *
     * @param minStueckzahl Stückzahl oberhalb welcher die Produkte aktiviert werden
     * @return ob der Vorgang erfolgreich war
     */
    @Override
    public boolean produktAktivieren(int minStueckzahl) {
        em.getTransaction().begin();

        List<Produkt> produkte = crudProdukt.getProduktListe();

        for (Produkt p : produkte) {
            if (p.getStueckzahl() > minStueckzahl) {
                p.setAktiv(true);
                crudProdukt.updateProdukt(p);
            }
        }
        em.getTransaction().commit();
        return true;
    }

    /**
     * Produkte die unterhalb einer eingegebenen Stueckzahl vorhanden sind sollen deaktiviert werden koennen.
     * LF110
     *
     * @param maxStueckzahl Stückzahl unterhalb welcher die Produkte deaktiviert werden
     * @return ob der Vorgang erfolgreich war
     */
    @Override
    public boolean produktDeaktivieren(int maxStueckzahl) {
        em.getTransaction().begin();

        List<Produkt> produkte = crudProdukt.getProduktListe();

        for (Produkt p : produkte) {
            if (p.getStueckzahl() < maxStueckzahl) {
                p.setAktiv(false);
                crudProdukt.updateProdukt(p);
            }
        }
        em.getTransaction().commit();
        return true;
    }

}
