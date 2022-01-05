package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.wawidbmodel.entities.Kunde;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Nachricht;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;

public class INachrichtSachImplTest {

    private INachrichtSachImpl classUnderTest ;
    private EntityManager em;
    private Kunde kunde;
    private Nachricht nachricht;

    @Before
    public void angenommen ( ) {
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        classUnderTest = new INachrichtSachImpl();
        classUnderTest.setEntityManager(em);

        kunde = new Kunde();
        kunde.setAdresse("spaceholder_address");
        kunde.setName("test_nachname");
        kunde.setVorname("test_vorname");
        kunde.setCreated(LocalDate.now());

        nachricht = new Nachricht();
        nachricht.setCreated(LocalDate.now());
        nachricht.setBetreff("test_Betreff");
        nachricht.setNachricht("test_Nachricht");
        nachricht.setKunde(kunde);

        em.getTransaction().begin();

        em.createQuery("DELETE FROM Bestellungsposition b").executeUpdate();
        em.createQuery("DELETE FROM Lagerverkehr l").executeUpdate();
        em.createQuery("DELETE FROM Bestellung b").executeUpdate();
        em.createQuery("DELETE FROM Nachricht n").executeUpdate();
        em.createQuery("DELETE FROM Kunde k").executeUpdate();
    }

    @After
    public void amEnde () {
        em.getTransaction().rollback();
    }


    /*  @Test: sendNachrichtAnKunde_00()
        WENN die zu sendende Nachricht vom Typ ankunde ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode sendNachrichtAnKunde mit der Nachricht aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die Nachricht in der DB vorhanden sein.
    */
    @Test
    public void sendNachrichtAnKunde_00(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");

        em.persist(kunde);

        assertTrue(classUnderTest.sendNachrichtAnKunde(nachricht));

        assertTrue(em.contains(nachricht));
    }


    /*  @Test: sendNachrichtAnKunde_01()
        WENN die zu sendende Nachricht vom Typ anwawi ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode sendNachrichtAnKunde mit der Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
    */
    @Test
    public void sendNachrichtAnKunde_01(){
        nachricht.setTyp("anwawi");
        nachricht.setStatus("ungelesen");

        em.persist(kunde);

        assertFalse(classUnderTest.sendNachrichtAnKunde(nachricht));
        assertFalse(em.contains(nachricht));
    }

    /*  @Test: sendNachrichtAnKunde_02()
        WENN die zu sendende Nachricht vom Typ ankunde ist,
        UND der Status der Nachricht gelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode sendNachrichtAnKunde mit der Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
    */
    @Test
    public void sendNachrichtAnKunde_02(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("gelesen");

        em.persist(kunde);

        assertFalse(classUnderTest.sendNachrichtAnKunde(nachricht));
        assertFalse(em.contains(nachricht));
    }

    /*  @Test: sendNachrichtAnKunde_03()
        WENN die zu sendende Nachricht vom Typ ankunde ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID nicht in der DB existiert,
        UND die Methode sendNachrichtAnKunde mit der Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
     */
    @Test
    public void sendNachrichtAnKunde_03(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");

        em.persist(kunde);
        em.remove(kunde);

        assertFalse(classUnderTest.sendNachrichtAnKunde(nachricht));
        assertFalse(em.contains(nachricht));
    }

    /*  @Test: getAlleNachrichten_00()
        WENN x (x>0) Nachrichten in der DB existieren,
        UND y (y<x) Nachrichten mit Status anwawi in der DB existieren,
        UND die Methode getAlleNachrichten aufgerufen wird,
        DANN sollte sie eine Liste mit y Nachrichten zurückliefern.
     */

    @Test
    public void getAlleNachrichten_00(){
        Nachricht nachricht1 = new Nachricht();
        nachricht1.setTyp("ankunde");
        nachricht1.setStatus("ungelesen");
        nachricht1.setCreated(LocalDate.now());
        nachricht1.setBetreff("test_Betreff");
        nachricht1.setNachricht("test_Nachricht");
        nachricht1.setKunde(kunde);

        nachricht.setTyp("anwawi");
        nachricht.setStatus("ungelesen");
        nachricht.setKunde(kunde);

        em.persist(kunde);

        em.persist(nachricht);
        em.persist(nachricht1);

        assertEquals(1,classUnderTest.getAlleNachrichten().size());
    }


    /*  @Test: getAlleNachrichten_01()
    WENN x (x>0) Nachrichten in der DB existieren,
    UND keine Nachrichten mit Status anwawi in der DB existieren,
    UND die Methode getAlleNachrichten aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getAlleNachrichten_01(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");
        nachricht.setKunde(kunde);

        em.persist(kunde);
        em.persist(nachricht);

        assertEquals(0,classUnderTest.getAlleNachrichten().size());
    }

    /*  @Test: setNachrichtGelesen_00()
        WENN Nachrichten in der DB existieren,
        UND die Methode setNachrichtGelesen aufgerufen wird,
        UND die ID einer existierenden Nachricht übergeben wird,
        DANN sollte sie TRUE zurückliefern,
        UND die Nachricht in der DB den Status gelesen haben.
    */
    @Test
    public void setNachrichtGelesen_00(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");
        nachricht.setKunde(kunde);

        em.persist(kunde);
        em.persist(nachricht);

        assertTrue(classUnderTest.setNachrichtGelesen(nachricht.getNid()));
        nachricht = em.find(Nachricht.class, nachricht.getNid());
        assertEquals("gelesen",nachricht.getStatus());
    }

    /*  @Test: setNachrichtGelesen_01()
        WENN Nachrichten in der DB existieren,
        UND die Methode setNachrichtGelesen aufgerufen wird,
        UND die ID einer nicht existierenden Nachricht übergeben wird,
        DANN sollte sie FALSE zurückliefern,
        UND der Status der Nachricht in der DB nicht verändert sein.
    */
    @Test
    public void setNachrichtGelesen_01(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");
        nachricht.setKunde(kunde);

        em.persist(kunde);
        em.persist(nachricht);

        Nachricht nachricht1 = new Nachricht();
        nachricht1.setTyp("ankunde");
        nachricht1.setStatus("ungelesen");
        nachricht1.setCreated(LocalDate.now());
        nachricht1.setBetreff("test_Betreff");
        nachricht1.setNachricht("test_Nachricht");
        nachricht1.setKunde(kunde);

        //Muss eingefügt werden, damit es eine ID erhält
        em.persist(nachricht1);
        em.remove(nachricht1);

        assertFalse(classUnderTest.setNachrichtGelesen(nachricht1.getNid()));
        assertEquals(nachricht,em.find(Nachricht.class, nachricht.getNid()));
    }

    /*  @Test: rechnungAnKundeSenden_00()
        WENN die zu sendende Nachricht (Rechnung) vom Typ ankunde ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode rechnungAnKundeSenden mit Kunden ID und Nachricht aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die Nachricht in der DB vorhanden sein.
    */
    @Test
    public void rechnungAnKundeSenden_00(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");

        nachricht.setKunde(kunde);
        em.persist(kunde);

        assertTrue(classUnderTest.rechnungAnKundeSenden(nachricht));
        assertTrue(em.contains(nachricht));
    }
    /*  @Test: rechnungAnKundeSenden_01()
        WENN die zu sendende Nachricht vom Typ anwawi ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode rechnungAnKundeSenden mit Kunden ID und Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
    */
    @Test
    public void rechnungAnKundeSenden_01(){
        nachricht.setTyp("anwawi");
        nachricht.setStatus("ungelesen");

        nachricht.setKunde(kunde);
        em.persist(kunde);

        assertFalse(classUnderTest.rechnungAnKundeSenden(nachricht));
        assertFalse(em.contains(nachricht));
    }

    /*  @Test: rechnungAnKundeSenden_02()
        WENN die zu sendende Nachricht vom Typ ankunde ist,
        UND der Status der Nachricht gelesen ist,
        UND die übergebene Kunden ID in der DB existiert,
        UND die Methode rechnungAnKundeSenden mit Kunden ID und Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
    */
    @Test
    public void rechnungAnKundeSenden_02(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("gelesen");

        nachricht.setKunde(kunde);
        em.persist(kunde);

        assertFalse(classUnderTest.rechnungAnKundeSenden(nachricht));
        assertFalse(em.contains(nachricht));
    }

    /*  @Test: rechnungAnKundeSenden_03()
        WENN die zu sendende Nachricht vom Typ ankunde ist,
        UND der Status der Nachricht ungelesen ist,
        UND die übergebene Kunden ID nichtin der DB existiert,
        UND die Methode rechnungAnKundeSenden mit Kunden ID und Nachricht aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die Nachricht nicht in der DB vorhanden sein.
     */
    @Test
    public void rechnungAnKundeSenden_03(){
        nachricht.setTyp("ankunde");
        nachricht.setStatus("ungelesen");

        nachricht.setKunde(kunde);
        em.persist(kunde);
        em.remove(kunde);

        assertFalse(classUnderTest.rechnungAnKundeSenden(nachricht));
        assertFalse(em.contains(nachricht));
    }
}
