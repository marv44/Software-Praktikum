module de.thkoeln.swp.bestellungverwaltungs {
    requires de.thkoeln.swp.wawi.datenhaltungapi;
    requires de.thkoeln.swp.wawi.wawidbmodel;
    requires java.logging;


    provides de.thkoeln.swp.wawi.datenhaltungapi.INachrichtSach with de.thkoeln.swp.wawi.bestellungverwaltungs.INachrichtSachImpl;
    provides de.thkoeln.swp.wawi.datenhaltungapi.IKundeService with de.thkoeln.swp.wawi.bestellungverwaltungs.IKundeServiceImpl;

    provides de.thkoeln.swp.wawi.datenhaltungapi.IBestellungSach with de.thkoeln.swp.wawi.bestellungverwaltungs.IBestellungSachImpl;
    provides de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin with de.thkoeln.swp.wawi.bestellungverwaltungs.IBestellungAdminImpl;

}