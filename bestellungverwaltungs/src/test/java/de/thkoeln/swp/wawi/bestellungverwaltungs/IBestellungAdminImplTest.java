package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.bestellungverwaltungs.IBestellungAdminImpl;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kunde;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IBestellungAdminImplTest {
    private IBestellungAdminImpl classUnderTest;
    private EntityManager em;

    private Kunde createDummyKunde(){
        Kunde k = new Kunde();
        k.setKid(null);
        k.setName("Meier");
        k.setVorname("Yannik");
        k.setAdresse("lkjdfjkysjfslk naehe Bonn");
        k.setCreated(LocalDate.of(2020, 10, 2));
        return k;

    }
    private Bestellung createDummyBestellung(){
        Bestellung b = new Bestellung();
        b.setBid(null);
        Kunde k = createDummyKunde();
        em.persist(k);
        b.setKunde(k);
        b.setLieferadresse("blase alks 50823 Koeln");
        b.setRechnungsadresse("kkdlsfj Bergisch Gladbach");
        b.setCreated(LocalDate.of(2020, 12, 21));
        b.setStatus("n");
        b.setGesamtbrutto(BigDecimal.valueOf(2345.2));
        b.setGesamtnetto(BigDecimal.valueOf(1900.45));
        return b;

    }

    /* @Before: angenommen()
      ANGENOMMEN der EntityManager wird korrekt geholt,
      UND die Implementierung der IBestellungAdmin Schnittstelle wird als classUnderTest instanziiert,
      UND der EntityManager wird per setEntityManager Methode der classUnderTest gesetzt,
      UND die Transaktion von em wird gestartet,
      UND die Daten der betreffenden Entitäten wurden in der DB gelöscht.
    */
    @Before
    public void angenommen() {
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();

        classUnderTest = new IBestellungAdminImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Bestellungsposition bp").executeUpdate();
        em.createQuery("DELETE FROM Lagerverkehr l").executeUpdate();
        em.createQuery("DELETE FROM Nachricht n").executeUpdate();
        em.createQuery("DELETE FROM Bestellung b").executeUpdate();
        em.createQuery("DELETE FROM Kunde k").executeUpdate();

    }

    /* @After: amEnde()
       AM ENDE wird die Transaktion zurück gesetzt.
     */
    @After
    public void amEnde(){
        em.getTransaction().rollback();
    }

    /* @Test: getAlleBestellungen_00()
       WENNx (x>0) Bestellungen in der DB existieren,
       UND die MethodegetAlleBestellungen aufgerufen wird,
       DANN sollte sie eine Liste mit x Bestellungen zurückliefern.
     */
    @Test
    public void getAlleBestellungen_00(){
        Bestellung b1 = createDummyBestellung();
        em.persist(b1);
        Bestellung b2 = createDummyBestellung();
        em.persist(b2);
        int anzahlBestellungen = classUnderTest.getAlleBestellungen().size();
        assertEquals(2, anzahlBestellungen);
    }

    /*@Test: getAlleBestellungen_01()
      WENN keine Bestellungen in der DB existieren,
      UND die Methode getAlleBestellungen aufgerufen wird,
      DANN sollte sie eineleere Liste zurückliefern.
     */
    @Test
    public void getAlleBestellungen_01(){
        assertEquals(0, classUnderTest.getAlleBestellungen().size());

    }

    /*@Test: getBestellungenByDatum_00()
    WENN x (x>0) Bestellungen in der DB existieren,
    UND y (y<x) Bestellungen in der DB existieren im Zeitraum von a bis b,
    UND die Methode getBestellungenByDatum mit Zeitraum von a bis b aufgerufen
    wird,
    DANN sollte sie eine Liste mit y Bestellungen zurückliefern.
     */
    @Test
    public void getBestellungenByDatum_00(){
        Bestellung b1 = createDummyBestellung();
        b1.setCreated(LocalDate.of(2020, 12, 21));
        em.persist(b1);
        Bestellung b2 = createDummyBestellung();
        b2.setCreated(LocalDate.of(2020, 12, 17));
        em.persist(b2);
        Bestellung b3 = createDummyBestellung();
        b3.setCreated(LocalDate.of(2020, 10, 03));
        em.persist(b3);

        int bestellungenInZeitraum = classUnderTest.getBestellungenByDatum(LocalDate.of(2020, 12, 1), LocalDate.of(2020, 12, 31)).size();

        assertEquals(2, bestellungenInZeitraum);

    }

    /*@Test: getBestellungenByDatum_01()
     WENN x (x>0) Bestellunegn in der DB existieren,
     UND keine Bestellungen in der DB existieren im Zeitraum von a bis b,
     UND die Methode getBestellungenByDatum mit Zeitraum von a bis b aufgerufen
     wird,
     DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getBestellungBYDatum_01(){
        Bestellung b1 = createDummyBestellung();
        b1.setCreated(LocalDate.of(2020, 12, 21));
        em.persist(b1);
        Bestellung b2 = createDummyBestellung();
        b2.setCreated(LocalDate.of(2020, 12, 17));
        em.persist(b2);
        Bestellung b3 = createDummyBestellung();
        b3.setCreated(LocalDate.of(2020, 12, 03));
        em.persist(b3);

        int bestellungenInZeitraum = classUnderTest.getBestellungenByDatum(LocalDate.of(2020, 10, 1), LocalDate.of(2020, 10, 31)).size();

        assertEquals(0, bestellungenInZeitraum);

    }

    /*@Test: getBestellungById_00()
    WENN eine Testbestellung bereits in der DB existiert,
    UND die Methode getBestellungById mit der Id der Testbestellung aufgerufen wird,
    DANN sollte sie die Testbestellung zurückliefern.
     */
    @Test
    public void getBestellungById_00(){
        Bestellung b = createDummyBestellung();
        em.persist(b);
        int id = b.getBid();
        assertEquals(b, classUnderTest.getBestellungById(id));
    }

    /*@Test: getBestellungById_01()
    WENN eine Testbestellung nicht in der DB existiert,
    UND die Methode getBestellungById mit der Id der Testbestellung aufgerufen wird,
    DANN sollte sie NULL zurückliefern.
     */
    @Test
    public void getBestellungById_01(){
        Bestellung b = createDummyBestellung();
        b.setBid(12355);
        int id = b.getBid();
        assertNull(classUnderTest.getBestellungById(id));
    }


}
