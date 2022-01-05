package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.componentcontroller.services.CompType;
import de.thkoeln.swp.wawi.componentcontroller.services.IActivateComponent;

/**
 * Die Klasse IActivateComponentImpl implementiert die IActivateComponent,
 * die für den Login und Logout als Admin Benutzer zuständig ist.
 * Mit korrekter id (14) kann der Admin sich in die Admin GUI einloggen.
 *
 * @author Janina Schroeder
 */
public class IActivateComponentImpl implements IActivateComponent {

    private boolean activated = false;

    /**
     * Gibt den Enum CompType vom Typ ADMIN zurück.
     *
     * @return Enum Typ ADMIN
     */
    @Override
    public CompType getCompType() {
        return CompType.ADMIN;
    }

    /**
     * Diese Methode aktiviert die Componente, die den Login im
     * Bootloader steuert falls sie deaktiviert ist.
     *
     * @param userid sollte 14 sein, um sich als Admin einzuloggen
     * @return zeigt an ob die Aktivierung erfolgreich war
     */
    @Override
    public boolean activateComponent(int userid) {
        if (!this.activated) {
            if (userid == 14) {
                this.activated = true;
                return true;
            }
        }

        return false;
    }

    /**
     * Diese Methode deaktiviert die Componente, die den Login im
     * Bootloader steuert falls sie aktiviert ist.
     *
     * @return zeigt an ob die Deaktivierung erfolgreich war
     */
    @Override
    public boolean deactivateComponent() {
        if (this.activated) {
            this.activated = false;
            return true;
        }

        return false;
    }

    /**
     * Diese Methode gibt den Aktivierungsstatus der Komponente zurück.
     *
     * @return Aktivierungsstatus der Komponente
     */
    @Override
    public boolean isActivated() {
        return this.activated;
    }

}
