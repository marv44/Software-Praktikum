package de.thkoeln.swp.wawi.adminsteuerung.services;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;

import java.util.List;

public interface IKategorieVerwaltung {

    boolean kategorieAnlegen(KategorieGrenz kategorie);

    boolean kategorieBearbeiten(KategorieGrenz kategorie);

    boolean kategorieLoeschen(int kategorieId);

    List<KategorieGrenz> getKategorieListe();

}
