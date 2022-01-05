module de.thkoeln.swp.wawi.admingui {

    requires de.thkoeln.swp.wawi.adminsteuerung;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports de.thkoeln.swp.wawi.admingui.application;

    opens de.thkoeln.swp.wawi.admingui.controllers to javafx.fxml;
}
