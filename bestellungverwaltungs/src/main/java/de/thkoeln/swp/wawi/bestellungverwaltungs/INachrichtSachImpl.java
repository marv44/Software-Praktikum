package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.datenhaltungapi.INachrichtSach;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kunde;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Nachricht;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class INachrichtSachImpl implements INachrichtSach {

    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public boolean sendNachrichtAnKunde(Nachricht nachricht) {
        if(em==null) throw new NoEntityManagerException();

        if(nachricht.getTyp() == "ankunde" && nachricht.getStatus() == "ungelesen"){
            Kunde kunde = em.find(Kunde.class, nachricht.getKunde().getKid());
            if(kunde != null) {
                em.persist(nachricht);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Nachricht> getAlleNachrichten() {
        if(em==null) throw new NoEntityManagerException();

        TypedQuery<Nachricht> typedQuery = em.createQuery("" +
            "SELECT n FROM Nachricht n WHERE n.typ = 'anwawi' ",Nachricht.class);
        return typedQuery.getResultList();
    }

    @Override
    public boolean setNachrichtGelesen(int i) {
        if(em==null) throw new NoEntityManagerException();

        Nachricht nachricht = em.find(Nachricht.class, i);
        if (nachricht != null) {
            nachricht.setStatus("gelesen");
            em.merge(nachricht);
            return true;
        }
        return false;
    }

    @Override
    public boolean rechnungAnKundeSenden(Nachricht nachricht) {
        if(em==null) throw new NoEntityManagerException();
        if(nachricht.getTyp() == "ankunde" && nachricht.getStatus() == "ungelesen"){
            Kunde kunde = em.find(Kunde.class, nachricht.getKunde().getKid());
            if(kunde != null) {
                em.persist(nachricht);
                return true;
            }
        }
        return false;
    }

}
