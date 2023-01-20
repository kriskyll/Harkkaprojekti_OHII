module com.kriskyll.harkkaprojekti_ohii {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kriskyll.harkkaprojekti_ohii to javafx.fxml;
    exports com.kriskyll.harkkaprojekti_ohii;
}