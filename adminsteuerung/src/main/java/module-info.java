module de.thkoeln.swp.wawi.adminsteuerung {
    exports de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;
    exports de.thkoeln.swp.wawi.adminsteuerung.services;
    exports de.thkoeln.swp.wawi.adminsteuerung.impl;

    requires de.thkoeln.swp.wawi.componentcontroller;
    requires de.thkoeln.swp.wawi.wawidbmodel;
    requires de.thkoeln.swp.wawi.datenhaltungapi;
    requires java.logging;

    uses de.thkoeln.swp.wawi.datenhaltungapi.ICRUDProdukt;
    uses de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKategorie;
    uses de.thkoeln.swp.wawi.datenhaltungapi.ILagerService;
    uses de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;

}
