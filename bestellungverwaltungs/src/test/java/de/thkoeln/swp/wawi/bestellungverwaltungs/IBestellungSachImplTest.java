package de.thkoeln.swp.wawi.bestellungverwaltungs;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungSach;
import de.thkoeln.swp.wawi.wawidbmodel.entities.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class IBestellungSachImplTest {

    private IBestellungSach classUnderTest;
    private EntityManager em;

    /* Erstellt einen Dummy Kunden und gibt diesesn zurück*/
    private Kunde createDummyKunde(){
        Kunde k = new Kunde();
        k.setKid(null);
        k.setName("Mustermann");
        k.setVorname("Max");
        k.setAdresse("Musteradresse");
        k.setCreated(LocalDate.of(2020, 10, 2));
        return k;
    }

    /*Erstellt eine Dummy Bestellung und gibt diese zurück*/
    private Bestellung createDummyBestellung(){
        Bestellung b = new Bestellung();
        b.setBid(null);
        Kunde k = createDummyKunde();
        em.persist(k);
        b.setKunde(k);
        b.setLieferadresse("Musterlieferadresse");
        b.setRechnungsadresse("Musterrechnungsadresse");
        b.setCreated(LocalDate.of(2020, 12, 21));
        b.setStatus("n");
        b.setGesamtbrutto(BigDecimal.valueOf(2345.2));
        b.setGesamtnetto(BigDecimal.valueOf(1900.45));
        return b;
    }

    /*Erstellt eine Dummy Bestellpostion und gibt diese zurück*/
    private Bestellungsposition createDummyBestellposition(){
        Bestellungsposition b = new Bestellungsposition();
        b.setBpid(null);
        b.setAnzahl(5);
        Bestellung bestellung = createDummyBestellung();
        em.persist(bestellung);
        b.setBestellung(bestellung);
        Produkt produkt = createDummyProdukt();
        em.persist(produkt);
        b.setProdukt(produkt);
        return b;
    }

    /*Erstellt ein Dummy Produkt und gibt dieses zurück:*/
    private Produkt createDummyProdukt(){
        Produkt p = new Produkt();
        p.setProdid(null);
        p.setName("Mustername");
        p.setBeschreibung("Musterbeschreibung");
        p.setAngelegt(LocalDate.of(2020, 12, 21));
        p.setStueckzahl(34);
        p.setNettopreis(BigDecimal.valueOf(234.6));
        p.setMwstsatz(16);
        p.setAktiv(true);
        Lagerort lagerort = createDummyLagerort();
        em.persist(lagerort);
        p.setLagerort(lagerort);
        Kategorie kategorie = createDummyKategorie();
        em.persist(kategorie);
        p.setKategorie(kategorie);
        return p;
    }

    /*Erstellt einen Lagerort und gibt diesen zurück*/
    private Lagerort createDummyLagerort(){
        Lagerort lagerort = new Lagerort();
        lagerort.setLgortid(null);
        lagerort.setBezeichnung("Musterbezeichnung");
        lagerort.setKapazitaet(348);
        Lager lager = createDummyLager();
        em.persist(lager);
        lagerort.setLager(lager);
        return lagerort;
    }

    /*Erstellt ein Lager und gibt dieses zurück*/
    private Lager createDummyLager(){
        Lager lager = new Lager();
        lager.setLagerid(null);
        lager.setName("Mustername");
        lager.setAddresse("Musteradresse");
        return lager;
    }

    /*Erstellt eine Kategorie und gibt diese zurück*/
    private Kategorie createDummyKategorie(){
        Kategorie k = new Kategorie();
        k.setKatid(null);
        k.setBeschreibung("Musterbeschreibung");
        k.setName("Mustername");
        return k;
    }

     /*
        @Before: angenommen()
        ANGENOMMEN der EntityManager wird korrekt geholt,
        UND die Implementierung der IBestellungSach Schnittstelle wird als classUnderTest instanziiert,
        UND der EntityManager wird per setEntityManager Methode der classUnderTest gesetzt,
        UND die Transaktion von em wird gestartet,
        UND die Daten der betreffenden Entitäten wurden in der DB gelöscht.
     */
    @Before
    public void angenommen() {
        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();

        classUnderTest = new IBestellungSachImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Bestellungsposition bp").executeUpdate();
        em.createQuery("DELETE FROM Lagerverkehr l").executeUpdate();
        em.createQuery("DELETE FROM Nachricht n").executeUpdate();
        em.createQuery("DELETE FROM Bestellung b").executeUpdate();
        em.createQuery("DELETE FROM Kunde k").executeUpdate();

    }

    /*
        @After: amEnde()
        AM ENDE wird die Transaktion zurück gesetzt.
     */
    @After
    public void amEnde(){
        em.getTransaction().rollback();
    }

    /*
        @Test: getAlleNeuenBestellungen_00()
        WENN x (x>0) Bestellungen in der DB existieren,
        UND y (y<x) neue Bestellungen in der DB existieren,
        UND die Methode getAlleNeuenBestellungen aufgerufen wird,
        DANN sollte sie eine Liste mit y Bestellungen zurückliefern.
     */
    @Test               /** Carina */
    public void getAlleNeuenBestellungen_00(){
        Bestellung b1 = createDummyBestellung();
        b1.setStatus("n");
        em.persist(b1);
        Bestellung b2 = createDummyBestellung();
        b2.setStatus("n");
        em.persist(b2);
        Bestellung b3 = createDummyBestellung();
        b3.setStatus("r");
        em.persist(b3);

        int anzNeueBestellungen = classUnderTest.getAlleNeuenBestellungen().size();
        assertEquals(2, anzNeueBestellungen);
    }

    /*
        @Test: getAlleNeuenBestellungen_01()
        WENN x (x>0) Bestellungen in der DB existieren,
        UND keine neuen Bestellungen in der DB existieren,
        UND die Methode getAlleNeuenBestellungen aufgerufen wird,
        DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test               /** Carina */
    public void getAlleNeuenBestellungen_01(){
        Bestellung b = createDummyBestellung();
        b.setStatus("r");
        em.persist(b);

        int anzNeueBestellungen = classUnderTest.getAlleNeuenBestellungen().size();
        assertEquals(0, anzNeueBestellungen);
    }

    /*
        @Test: getAlleAbgerechnetenBestellungen_00()
        WENN x (x>0) Bestellungen in der DB existieren,
        UND y (y<x) abgerechnete Bestellungen in der DB existieren,
        UND die Methode getAlleAbgerechnetenBestellungen aufgerufen wird,
        DANN sollte sie eine Liste mit y Bestellungen zurückliefern.
     */
    @Test               /** David */
    public void getAlleAbgerechnetenBestellungen_00(){
        Bestellung bNeu = createDummyBestellung();
        bNeu.setStatus("n");
        em.persist(bNeu);

        Bestellung bAbgerechnet = createDummyBestellung();
        bAbgerechnet.setStatus("r");
        em.persist(bAbgerechnet);

        assertEquals(1, classUnderTest.getAlleAbgerechnetenBestellungen().size());
    }

    /*
        @Test: getAlleAbgerechnetenBestellungen_01()
        WENN x (x>0) Bestellungen in der DB existieren,
        UND keine abgerechneten Bestellungen in der DB existieren,
        UND die Methode getAlleAbgerechnetenBestellungen aufgerufen wird,
        DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test               /** David */
    public void getAlleAbgerechnetenBestellungen_01(){
        Bestellung bNeu = createDummyBestellung();
        bNeu.setStatus("n");
        em.persist(bNeu);

        assertTrue(classUnderTest.getAlleAbgerechnetenBestellungen().isEmpty());
    }

    /*
        @Test: getProduktById_00()
        WENN ein Testprodukt bereits in der DB existiert,
        UND die Methode getProduktById mit der Id des Testprodukts aufgerufen wird,
        DANN sollte sie das Testprodukt zurückliefern.
     */
    @Test               /** David */
    public void getProduktById_00(){
        Produkt p = createDummyProdukt();
        em.persist(p);

        int prodId = p.getProdid();
        assertEquals(p, (Produkt) classUnderTest.getProduktById(prodId));
    }

    /*
        @Test: getProduktById_01()
        WENN ein Testprodukt nicht in der DB existiert,
        UND die Methode getProduktById mit der Id des Testprodukts aufgerufen wird,
        DANN sollte sie NULL zurückliefern.
     */
    @Test               /** David */
    public void getProduktById_01(){
        Produkt p = createDummyProdukt();
        em.persist(p);

        int prodId = p.getProdid();
        assertEquals(null, (Produkt) classUnderTest.getProduktById(prodId+1));
    }

    /*
        @Test: setBestellungRechnungGesendet_00()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungRechnungGesendet aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung den Status neu hat,
        DANN sollte sie TRUE zurückliefern,
        UND die Bestellung in der DB den Status abgerechnet haben.
     */
    @Test               /** David */
    public void setBestellungRechnungGesendet_00(){
        Bestellung bNeu = createDummyBestellung();
        bNeu.setStatus("n");
        em.persist(bNeu);

        int bId = bNeu.getBid();
        assertTrue(classUnderTest.setBestellungRechnungGesendet(bId));

        bNeu = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("r", bNeu.getStatus());
    }

    /*
        @Test: setBestellungRechnungGesendet_01()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungRechnungGesendet aufgerufen wird,
        UND die ID einer nicht existierenden Bestellung übergeben wird,
        DANN sollte sie FALSE zurückliefern.
     */
    @Test               /** David */
    public void setBestellungRechnungGesendet_01(){
        Bestellung b = createDummyBestellung();
        em.persist(b);

        int bId = b.getBid();
        assertFalse(classUnderTest.setBestellungRechnungGesendet(bId+1));
    }

    /*
        @Test: setBestellungRechnungGesendet_02()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungRechnungGesendet aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung einen anderen Status als neu hat,
        DANN sollte sie FALSE zurückliefern,
        UND der Status der Bestellung in der DB nicht verändert sein.
     */
    @Test               /** David */
    public void setBestellungRechnungGesendet_02(){
        Bestellung bNichtNeu = createDummyBestellung();
        bNichtNeu.setStatus("b");
        em.persist(bNichtNeu);

        int bId = bNichtNeu.getBid();
        assertFalse(classUnderTest.setBestellungRechnungGesendet(bId));

        bNichtNeu = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("b", bNichtNeu.getStatus());
    }

    /*
        @Test: setBestellungBezahlt_00()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungBezahlt aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung den Status abgerechnet hat,
        DANN sollte sie TRUE zurückliefern,
        UND die Bestellung in der DB den Status bezahlt haben.
     */
    @Test               /** David */
    public void setBestellungBezahlt_00(){
        Bestellung bAbgerechnet = createDummyBestellung();
        bAbgerechnet.setStatus("r");
        em.persist(bAbgerechnet);

        int bId = bAbgerechnet.getBid();
        assertTrue(classUnderTest.setBestellungBezahlt(bId));

        bAbgerechnet = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("b", bAbgerechnet.getStatus());
    }

    /*
        @Test: setBestellungBezahlt_01()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungBezahlt aufgerufen wird,
        UND die ID einer nicht existierenden Bestellung übergeben wird,
        DANN sollte sie FALSE zurückliefern.
     */
    @Test               /** David */
    public void setBestellungBezahlt_01(){
        Bestellung b = createDummyBestellung();
        em.persist(b);

        int bId = b.getBid();
        assertFalse(classUnderTest.setBestellungBezahlt(bId+1));
    }

    /*
        @Test: setBestellungBezahlt_02()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungBezahlt aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung einen anderen Status als abgerechnet hat,
        DANN sollte sie FALSE zurückliefern,
        UND der Status der Bestellung in der DB nicht verändert sein.
     */
    @Test               /** David */
    public void setBestellungBezahlt_02(){
        Bestellung bNichtAbgerechnet = createDummyBestellung();
        bNichtAbgerechnet.setStatus("n");
        em.persist(bNichtAbgerechnet);

        int bId = bNichtAbgerechnet.getBid();
        assertFalse(classUnderTest.setBestellungBezahlt(bId));

        bNichtAbgerechnet = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("n", bNichtAbgerechnet.getStatus());
    }

    /*
        @Test: setBestellungStorniert_00()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungStorniert aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung den Status abgerechnet hat,
        DANN sollte sie TRUE zurückliefern,
        UND die Bestellung in der DB den Status storniert haben.
     */
    @Test               /** David */
    public void setBestellungStorniert_00(){
        Bestellung bAbgerechnet = createDummyBestellung();
        bAbgerechnet.setStatus("r");
        em.persist(bAbgerechnet);

        int bId = bAbgerechnet.getBid();
        assertTrue(classUnderTest.setBestellungStorniert(bId));

        bAbgerechnet = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("s", bAbgerechnet.getStatus());
    }

    /*
        @Test: setBestellungStorniert_01()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungStorniert aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung den Status neu hat,
        DANN sollte sie TRUE zurückliefern,
        UND die Bestellung in der DB den Status storniert haben.
      */
    @Test               /** David */
    public void setBestellungStorniert_01(){
        Bestellung bNeu = createDummyBestellung();
        bNeu.setStatus("n");
        em.persist(bNeu);

        int bId = bNeu.getBid();
        assertTrue(classUnderTest.setBestellungStorniert(bId));

        bNeu = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("s", bNeu.getStatus());
    }

     /*
        @Test: setBestellungStorniert_02()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungStorniert aufgerufen wird,
        UND die ID einer nicht existierenden Bestellung übergeben wird,
        DANN sollte sie FALSE zurückliefern.
     */
    @Test               /** David */
    public void setBestellungStorniert_02(){
        Bestellung b = createDummyBestellung();
        em.persist(b);

        int bId = b.getBid();
        assertFalse(classUnderTest.setBestellungStorniert(bId+1));
    }

    /*
        @Test: setBestellungStorniert_03()
        WENN Bestellungen in der DB existieren,
        UND die Methode setBestellungStorniert aufgerufen wird,
        UND die ID einer existierenden Bestellung übergeben wird,
        UND die Bestellung nicht den Status abgerechnet oder nicht den Status neu hat,
        DANN sollte sie FALSE zurückliefern,
        UND der Status der Bestellung in der DB nicht verändert sein.
     */
    @Test               /** David */
    public void setBestellungStorniert_03(){
        Bestellung bNichtAbgerechnetOderNeu = createDummyBestellung();
        bNichtAbgerechnetOderNeu.setStatus("b");
        em.persist(bNichtAbgerechnetOderNeu);

        int bId = bNichtAbgerechnetOderNeu.getBid();
        assertFalse(classUnderTest.setBestellungStorniert(bId));

        bNichtAbgerechnetOderNeu = (Bestellung) classUnderTest.getBestellungById(bId);
        assertEquals("b", bNichtAbgerechnetOderNeu.getStatus());
    }

    /*
        @Test: updateBestellung_00()
        WENN eine TestBestellung in der DB existiert,
        UND die Methode updateBestellung mit einer veränderten TestBestellung (aber gleicher ID) aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die TestBestellung sollte in der DB verändert sein.
     */
    @Test               /** David */
    public void updateBestellung_00(){
        Bestellung b = createDummyBestellung();
        b.setStatus("n");
        em.persist(b);

        b.setStatus("r");
        assertTrue(classUnderTest.updateBestellung(b));
        assertEquals("r", classUnderTest.getBestellungById(b.getBid()).getStatus());
    }

    /*
        @Test: updateBestellung_01()
        WENN eine TestBestellung nicht in der DB existiert,
        UND die Methode updateBestellung mit der TestBestellung aufgerufen wird,
        DANN sollte sie FALSE zurückliefern,
        UND die TestBestellung sollte nicht in der DB existieren.
     */
    @Test               /** David */
    public void updateBestellung_01(){
        Bestellung b = createDummyBestellung();
        b.setStatus("r");
        b.setBid(1);

        assertFalse(classUnderTest.updateBestellung(b));
        assertTrue(classUnderTest.getAlleAbgerechnetenBestellungen().isEmpty());
    }

    /*
        @Test: deleteBestellung_00()
        WENN eine TestBestellung in der DB existiert,
        UND die Methode deleteBestellung mit der ID der TestBestellung aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die TestBestellung sollte nicht mehr in der DB existieren.
     */
    @Test               /** David */
    public void deleteBestellung_00(){
        Bestellung b = createDummyBestellung();
        b.setStatus("r");
        em.persist(b);

        int bId = b.getBid();
        assertTrue(classUnderTest.deleteBestellung(bId));
        assertTrue(classUnderTest.getAlleAbgerechnetenBestellungen().isEmpty());
    }

    /*
        @Test: deleteBestellung_01()
        WENN eine TestBestellung nicht in der DB existiert,
        UND die Methode deleteBestellung mit der ID der TestBestellung aufgerufen wird,
        DANN sollte sie FALSE zurückliefern.
     */
    @Test               /** David */
    public void deleteBestellung_01(){
        Bestellung b = createDummyBestellung();
        b.setStatus("r");
        em.persist(b);

        int bId = b.getBid();
        assertFalse(classUnderTest.deleteBestellung(bId+1));
    }

    /*
        @Test: getBestellungById_00()
        WENN eine TestBestellung bereits in der DB existiert,
        UND die Methode getBestellungById mit der Id der TestBestellung aufgerufen
        wird,
        DANN sollte sie die TestBestellung zurückliefern.
     */
     @Test               /** David */
     public void getBestellungById_00(){
         Bestellung b = createDummyBestellung();
         em.persist(b);

         int bId = b.getBid();
         assertEquals(b, (Bestellung) classUnderTest.getBestellungById(bId));
     }

    /*
        @Test: getBestellungById_01()
        WENN eine TestBestellung nicht in der DB existiert,
        UND die Methode getBestellungById mit der Id der TestBestellung aufgerufen
        wird,
        DANN sollte sie NULL zurückliefern.
     */
     @Test               /** David */
    public void getBestellungById_01(){
         Bestellung b = createDummyBestellung();
         em.persist(b);

         int bId = b.getBid();
         assertEquals(null, (Bestellung) classUnderTest.getBestellungById(bId+1));
    }

    /*
        @Test: insertBestellposition_00()
        WENN die Methode insertBestellposition mit einer TestBestellposition aufgerufen wird,
        UND die ID der TestBestellposition gleich null ist,
        DANN sollte sie TRUE zurückliefern,
        UND die TestBestellposition sollte in der DB existieren.
     */
    @Test               /** Carina */
    public void insertBestellposition_00(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        boolean erfolgreich = classUnderTest.insertBestellposition(bestellungsposition);
        assertTrue(erfolgreich);
        Bestellungsposition enthalten = em.find(Bestellungsposition.class, bestellungsposition.getBpid());
        assertEquals(bestellungsposition, enthalten);

    }

    /*
        @Test: insertBestellposition_01()
        WENN die Methode insertBestellposition mit einer TestBestellposition aufgerufen wird,
        UND die ID der TestlBestellposition ungleich null ist,
        DANN sollte sie FALSE zurückliefern,
        UND die DB wurde nicht verändert.
     */
    @Test               /** Carina */
    public void insertBestellposition_01(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        bestellungsposition.setBpid(2345);
        assertFalse(classUnderTest.insertBestellposition(bestellungsposition));
        assertEquals(0, em.createNamedQuery("Bestellungsposition.findAll").getResultList().size());

    }

    /*
        @Test: updateBestellposition_00()
        WENN eine TestBestellposition in der DB existiert,
        UND die Methode updateBestellposition mit einer veränderten TestBestellposition (aber gleicher ID) aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die TestBestellposition sollte in der DB verändert sein.
     */
    @Test               /** Carina */
    public void updateBestellposition_00(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        classUnderTest.insertBestellposition(bestellungsposition);
        int id = bestellungsposition.getBpid();
        Bestellungsposition veraendert = createDummyBestellposition();
        veraendert.setBpid(id);
        assertTrue(classUnderTest.updateBestellposition(veraendert));
        assertEquals(veraendert, em.find(Bestellungsposition.class, id));
    }

    /*
        @Test: updateBestellposition_01()
        WENN eine TestBestellposition nicht in der DB existiert,
        UND die Methode updateBestellposition mit der TestBestellposition aufgerufen
        wird,
        DANN sollte sie FALSE zurückliefern,
        UND die TestBestellposition sollte nicht in der DB existieren.
     */
    @Test               /** Carina */
    public void updateBestellposition_01(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        bestellungsposition.setBpid(566);
        assertFalse(classUnderTest.updateBestellposition(bestellungsposition));
        assertNull(em.find(Bestellungsposition.class, bestellungsposition.getBpid()));
    }

    /*
        @Test: deleteBestellposition_00()
        WENN eine TestBestellposition in der DB existiert,
        UND die Methode deleteBestellposition mit der ID der TestBestellposition aufgerufen wird,
        DANN sollte sie TRUE zurückliefern,
        UND die TestBestellposition sollte nicht mehr in der DB existieren.
     */
    @Test               /** Carina */
    public void deleteBestellpositon_00(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        classUnderTest.insertBestellposition(bestellungsposition);
        assertTrue(classUnderTest.deleteBestellposition(bestellungsposition.getBpid()));
        assertNull(em.find(Bestellungsposition.class, bestellungsposition.getBpid()));
    }

    /*
        @Test: deleteBestellposition_01()
        WENN eine TestBestellposition nicht in der DB existiert,
        UND die Methode deleteBestellposition mit der ID der TestBestellposition aufgerufen wird,
        DANN sollte sie FALSE zurückliefern.
     */
    @Test               /** Carina */
    public void deleteBestellpositon_01(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        bestellungsposition.setBpid(3285);
        assertFalse(classUnderTest.deleteBestellposition(bestellungsposition.getBpid()));
    }

    /*
        @Test: getBestellpositionById_00()
        WENN eine TestBestellposition bereits in der DB existiert,
        UND die Methode getBestellpositionById mit der Id der TestBestellposition aufgerufen wird,
        DANN sollte sie die TestBestellposition zurückliefern.
     */
    @Test               /** Carina */
    public void getBestellpostionById_00(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        em.persist(bestellungsposition);
        int id = bestellungsposition.getBpid();
        assertEquals(bestellungsposition, classUnderTest.getBestellpositionById(id));
    }

    /*
        @Test: getBestellpositionById_01()
        WENN eine TestBestellposition nicht in der DB existiert,
        UND die Methode getBestellpositionById mit der Id der TestBestellposition aufgerufen wird,
        DANN sollte sie NULL zurückliefern.
     */
    @Test               /** Carina */
    public void getBestellpostionById_01(){
        Bestellungsposition bestellungsposition = createDummyBestellposition();
        bestellungsposition.setBpid(384);
        int id = bestellungsposition.getBpid();
        assertNull(classUnderTest.getBestellpositionById(id));
    }
}
