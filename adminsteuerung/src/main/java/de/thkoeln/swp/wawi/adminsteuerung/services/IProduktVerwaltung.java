package de.thkoeln.swp.wawi.adminsteuerung.services;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.ProduktGrenz;

import java.util.List;

public interface IProduktVerwaltung {

    boolean produktAnlegen(ProduktGrenz produkt);

    boolean produktBearbeiten(ProduktGrenz produkt);

    byte produktLoeschen(int produktId);

    List<ProduktGrenz> getProduktListe();

    List<ProduktGrenz> getProduktListe(StueckzahlFilter filter, int stueckzahl);

    boolean produktAktivieren(int minStueckzahl);

    boolean produktDeaktivieren(int maxStueckzahl);

}
