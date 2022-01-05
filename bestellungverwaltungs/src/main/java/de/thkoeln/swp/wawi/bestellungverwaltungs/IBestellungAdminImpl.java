package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class IBestellungAdminImpl implements IBestellungAdmin {
    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public List<Bestellung> getAlleBestellungen() {
        if(em==null){
            throw new NoEntityManagerException();
        }else{
            TypedQuery<Bestellung> typedQuery = (TypedQuery<Bestellung>) em.createNamedQuery("Bestellung.findAll");
            List<Bestellung> bestellungList = typedQuery.getResultList();
            return bestellungList;

        }
    }

    @Override
    public List<Bestellung> getBestellungenByDatum(LocalDate von, LocalDate bis) {
        if(em==null){
            throw new NoEntityManagerException();
        }else{
            TypedQuery<Bestellung> typedQuery = em.createQuery("SELECT b FROM Bestellung b WHERE b.created >= :von AND b.created <= :bis", Bestellung.class);
            typedQuery.setParameter("von", von);
            typedQuery.setParameter("bis", bis);
            List<Bestellung> ergebnis = typedQuery.getResultList () ;
            return ergebnis;

        }
    }

    @Override
    public Bestellung getBestellungById(int i) {
        if(em==null){
            throw new NoEntityManagerException();
        }else{
            Bestellung find = em.find(Bestellung.class, i);
            return find;
        }
    }
}
