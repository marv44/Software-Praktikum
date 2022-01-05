package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;
import de.thkoeln.swp.wawi.adminsteuerung.services.IKategorieVerwaltung;
import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKategorie;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kategorie;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Die Klasse IKategorieVerwaltungImpl bietet Schnittstellen zur Verwaltung von Kategorien.
 * LF105, LF110
 *
 * @author Pascal Sthamer
 */
public class IKategorieVerwaltungImpl implements IKategorieVerwaltung {

    EntityManager em;
    ICRUDKategorie crudKategorie;

    public IKategorieVerwaltungImpl() {
        // Implementierungen der Schnittstellen aus der Schicht Datenhaltung via loose Kopplung bekommen.
        ServiceLoader<ICRUDKategorie> loader = ServiceLoader.load(ICRUDKategorie.class);
        crudKategorie = loader.findFirst().orElseThrow();

        // EntityManager setzen.
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        crudKategorie.setEntityManager(em);
    }

    /**
     * Legt eine neue Kategorie an.
     * LF105
     *
     * @param kategorie die Kategorie, die angelegt werden soll. Die ID muss null sein
     * @return ob die Kategorie erfolgreich angelegt wurde
     */
    @Override
    public boolean kategorieAnlegen(KategorieGrenz kategorie) {
        em.getTransaction().begin();

        if (!crudKategorie.insertKategorie(kategorie.toKategorie())) {
            em.getTransaction().rollback();
            return false;
        }

        em.getTransaction().commit();
        return true;
    }

    /**
     * Bearbeitet eine bereits vorhandene Kategorie.
     * LF105
     *
     * @param kategorie die Kategorie mit den neuen Werten. Die ID darf nicht null sein und muss zu einer bereits vorhandenen Kategorie gehören
     * @return ob die Kategorie erfolgreich bearbeitet wurde
     */
    @Override
    public boolean kategorieBearbeiten(KategorieGrenz kategorie) {
        em.getTransaction().begin();

        if (!crudKategorie.updateKategorie(kategorie.toKategorie())) {
            em.getTransaction().rollback();
            return false;
        }

        em.getTransaction().commit();
        return true;
    }

    /**
     * Löscht eine bereits vorhandene Kategorie.
     * Funktioniert nur, wenn der Kategorie keine Produkte mehr zugewiesen sind.
     * LF105
     *
     * @param kategorieId die ID der Kategorie, die gelöscht werden soll
     * @return ob die Kategorie erfolgreich gelöscht wurde
     */
    @Override
    public boolean kategorieLoeschen(int kategorieId) {
        em.getTransaction().begin();

        Kategorie kategorie = crudKategorie.getKategorieById(kategorieId);

        if (kategorie == null) {
            em.getTransaction().rollback();
            return false;
        }

        if (!kategorie.getProduktList().isEmpty()) {
            em.getTransaction().rollback();
            return false;
        }

        // Get all sub-categories of the category that should be deleted.
        List<Kategorie> kategorien = crudKategorie.getKategorieListe()
                .stream()
                .filter(k -> k.getParentkatid() == kategorie.getKatid() && k.getKatid() != kategorie.getKatid())
                .collect(Collectors.toList());

        // Add all sub-categories to the parent category of the category that should be deleted.
        for (Kategorie k : kategorien) {
            int newParentcategoryId;

            if (kategorie.getParentkatid() == kategorie.getKatid()) {
                newParentcategoryId = 0;
            } else {
                newParentcategoryId = kategorie.getParentkatid();
            }

            k.setParentkatid(newParentcategoryId);
            if (!crudKategorie.updateKategorie(k)) {
                em.getTransaction().rollback();
                return false;
            }
        }

        if (!crudKategorie.deleteKategorie(kategorie.getKatid())) {
            em.getTransaction().rollback();
            return false;
        }

        em.getTransaction().commit();
        return true;
    }

    /**
     * Gibt eine Liste aller vorhandenen Kategorien zurück.
     * LF105, LF110
     *
     * @return die Liste aller Kategorien
     */
    @Override
    public List<KategorieGrenz> getKategorieListe() {
        List<KategorieGrenz> kategorien = new ArrayList<>();

        for (Kategorie kategorie : crudKategorie.getKategorieListe()) {
            kategorien.add(new KategorieGrenz(kategorie));
        }

        return kategorien;
    }

}
