package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.wawidbmodel.entities.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class IKundeServiceImplTest {

    private IKundeServiceImpl classUnderTest ;
    private EntityManager em;
    private Lager lager;
    private Lagerort lagerort;
    private Kategorie kat;

    @Before
    public void angenommen ( ) {
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        classUnderTest = new IKundeServiceImpl();
        classUnderTest.setEntityManager(em);

        kat = new Kategorie();
        kat.setName("Test");
        kat.setBeschreibung("Kategorie für Tests");

        lager = new Lager();
        lager.setName("Testlager");
        lager.setAddresse("Test Adresse");

        lagerort = new Lagerort();
        lagerort.setLager(lager);
        lagerort.setBezeichnung("Testlager Ort");
        lagerort.setKapazitaet(100000);

        em.getTransaction().begin();

        em.createQuery("DELETE FROM Lieferposition l").executeUpdate();
        em.createQuery("DELETE FROM Bestellungsposition b").executeUpdate();
        em.createQuery("DELETE FROM Produkt p").executeUpdate();
        em.createQuery("DELETE FROM Kategorie k").executeUpdate();
        em.createQuery("DELETE FROM Lagerort l").executeUpdate();
        em.createQuery("DELETE FROM Lagerverkehr l").executeUpdate();
        em.createQuery("DELETE FROM Lager l").executeUpdate();


        em.persist(kat);
        em.persist(lager);
        em.persist(lagerort);
    }

    @After
    public void amEnde ( ) {
        em.getTransaction().rollback();
    }

    /*  @Test: getAktiveProdukte_00()
        WENN x (x>2) Produkte in der Datenbank existieren,
        UND y (y>0 und y<x) aktive Produkte existieren,
        UND die Methode getAktiveProdukte aufgerufen wird,
        DANN sollte sie eine Liste mit y Produkten zurückliefern.
     */
    @Test
    public void getAktiveProdukte_00(){
        for(int i=0; i<10; i++){
            Produkt neu = new Produkt();
            neu.setName("TestProdukt");
            neu.setBeschreibung("Testbeschreibung");
            neu.setAngelegt(LocalDate.now());
            neu.setStueckzahl(4);
            neu.setNettopreis(BigDecimal.valueOf(404));
            neu.setMwstsatz(19);
            neu.setKategorie(kat);
            neu.setLagerort(lagerort);
            if(i<5) neu.setAktiv(true);
            else neu.setAktiv(false);
            em.persist(neu);
        }

        assertEquals(5,classUnderTest.getAktiveProdukte().size());
    }

    /*  @Test: getAktiveProdukte_01()
        WENN x (x>0) Produkte in der Datenbank existieren,
        UND keine aktiven Produkte existieren,
        UND die Methode getAktiveProdukte aufgerufen wird,
        DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAktiveProdukte_01(){
        for(int i=0; i<10; i++){
            Produkt neu = new Produkt();
            neu.setName("TestProdukt");
            neu.setBeschreibung("Testbeschreibung");
            neu.setAngelegt(LocalDate.now());
            neu.setStueckzahl(4);
            neu.setNettopreis(BigDecimal.valueOf(404));
            neu.setMwstsatz(19);
            neu.setKategorie(kat);
            neu.setLagerort(lagerort);
            neu.setAktiv(false);
            em.persist(neu);
        }
        assertEquals(0,classUnderTest.getAktiveProdukte().size());
    }
}
