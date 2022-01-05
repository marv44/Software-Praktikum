package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.datenhaltungapi.IKundeService;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class IKundeServiceImpl implements IKundeService {

    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public List<Produkt> getAktiveProdukte() {

        if(em == null) throw new NoEntityManagerException();

        TypedQuery<Produkt> typedQuery = em.createQuery("" +
                "SELECT p FROM Produkt p WHERE p.aktiv = true ",Produkt.class);
        return typedQuery.getResultList();
    }
}
