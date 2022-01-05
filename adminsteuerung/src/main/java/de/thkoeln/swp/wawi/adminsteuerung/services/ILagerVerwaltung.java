package de.thkoeln.swp.wawi.adminsteuerung.services;

import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.*;

import java.time.LocalDate;
import java.util.List;

public interface ILagerVerwaltung {

    List<BestellungGrenz> getBestellungsListe();

    List<BestellungGrenz> getBestellungsListe(LocalDate von, LocalDate bis);

    List<BestellungspositionGrenz> getBestellungspositionen(int bestellId);

    BestellungGrenz getBestellungById(int bestellId);

    List<EinlieferungGrenz> getEinlieferungsListe();

    List<EinlieferungGrenz> getEinlieferungsListe(LocalDate von, LocalDate bis);

    List<LieferpositionGrenz> getLieferpositionen(int einlieferungId);

    EinlieferungGrenz getEinlieferungById(int einlieferungsId);

    List<LagerverkehrGrenz> getLagerverkehrListe();

    List<LagerverkehrGrenz> getLagerverkehrListe(LocalDate von, LocalDate bis);

    List<LagerortGrenz> getLagerortListe();
}
