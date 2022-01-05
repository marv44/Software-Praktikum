package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungSach;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellungsposition;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class IBestellungSachImpl implements IBestellungSach {

    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override               /** Carina */
    public List<Bestellung> getAlleNeuenBestellungen() {
        if(em == null){
            throw new NoEntityManagerException();
        }else{
            TypedQuery<Bestellung> typedQuery = em.createQuery("SELECT b FROM Bestellung b WHERE b.status = 'n'", Bestellung.class);
            List<Bestellung> alleBestellungenNeu = typedQuery.getResultList();
            return alleBestellungenNeu;

        }
    }

    @Override               /** David */
    public List<Bestellung> getAlleAbgerechnetenBestellungen() {
        if(em == null) throw new NoEntityManagerException();

        TypedQuery<Bestellung> query = em.createNamedQuery("Bestellung.findByStatus", Bestellung.class);
        query.setParameter("status", "r");
        List<Bestellung> abgerechneteBestellungenList = query.getResultList();

        return abgerechneteBestellungenList;
    }

    @Override               /** David */
    public Produkt getProduktById(int i) {
        if(em == null) throw new NoEntityManagerException();

        return em.find(Produkt.class, i);
    }

    @Override               /** David */
    public boolean setBestellungRechnungGesendet(int i) {
        if(em == null) throw new NoEntityManagerException();

        Bestellung b = em.find(Bestellung.class, i);

        if(b == null || !b.getStatus().equals("n")){
            return false;
        } else {
            b.setStatus("r");
            em.merge(b);
        }
        return true;
    }

    @Override               /** David */
    public boolean setBestellungBezahlt(int i) {
        if(em == null) throw new NoEntityManagerException();

        Bestellung b = em.find(Bestellung.class, i);

        if(b == null || !b.getStatus().equals("r")){
            return false;
        } else {
            b.setStatus("b");
            em.merge(b);
        }
        return true;
    }

    @Override               /** David */
    public boolean setBestellungStorniert(int i) {
        if(em == null) throw new NoEntityManagerException();

        Bestellung b = em.find(Bestellung.class, i);

        if(b == null) return false;
        if(b.getStatus().equals("n") || b.getStatus().equals("r")){
            b.setStatus("s");
            em.merge(b);
            return true;
        }
        return false;
    }

    @Override               /** David */
    public boolean updateBestellung(Bestellung bestellung) {
        if(em == null) throw new NoEntityManagerException();

        if(bestellung.getBid() == null) return false;

        Bestellung b = em.find(Bestellung.class, bestellung.getBid());

        if(b == null) return false;

        em.merge(bestellung);
        return true;
    }

    @Override               /** David */
    public boolean deleteBestellung(int i) {
        if(em == null) throw new NoEntityManagerException();

        Bestellung b = em.find(Bestellung.class, i);

        if(b == null) return false;

        em.remove(b);
        return true;
    }

    @Override               /** David */
    public Bestellung getBestellungById(int i) {
        if(em == null) throw new NoEntityManagerException();

        return em.find(Bestellung.class, i);
    }

    @Override               /** Carina */
    public boolean insertBestellposition(Bestellungsposition bestellungsposition) {
        if(em == null){
            throw new NoEntityManagerException();
        }else {
            if(bestellungsposition.getBpid() != null){
                return false;
            }else{
                em.persist(bestellungsposition);
                return true;
            }
        }
    }

    @Override               /** Carina */
    public boolean updateBestellposition(Bestellungsposition bestellungsposition) {
       if(em == null){
           throw new NoEntityManagerException();
       }else {
           Bestellungsposition vorhanden = em.find(Bestellungsposition.class, bestellungsposition.getBpid());
           if (vorhanden != null) {
               em.merge(bestellungsposition);
               return true;
           } else {
               return false;
           }
       }
    }

    @Override               /** Carina */
    public boolean deleteBestellposition(int i) {
        if(em == null){
            throw new NoEntityManagerException();
        }else {
            Bestellungsposition loesche = getBestellpositionById(i);
            if(loesche != null){
                em.remove(loesche);
                return true;
            }else{
                return false;
            }
        }
    }

    @Override               /** Carina */
    public Bestellungsposition getBestellpositionById(int i) {

            if(em == null){
                throw new NoEntityManagerException();
            }else {
                Bestellungsposition gefunden = em.find(Bestellungsposition.class, i);
                return gefunden;
            }
    }
}
