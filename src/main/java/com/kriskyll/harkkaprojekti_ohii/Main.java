package com.kriskyll.harkkaprojekti_ohii;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Painonseuranta-sovellus.
 * @author kriskyll
 * @version 1.0 (MVP)
 *
 * Sovelluksessa pystyt:
 *  -   Lisäämään uusia käyttäjiä
 *  -   Lisäämään käyttäjille mittauksia, joihin tulee
 *      a)   mitattu paino
 *      b)   mittauspäivämäärä
 *  -   Tarkastelemaan käyttäjien tallentamia mittauksia
 *
 * Sovellus tallentaa kaikki uudet mittaukset ja käyttäjät automaattisesti tiedostoon, ja näyttää ne
 * ohjelman uudestaan avattaessa.
 *
 * Sovelluksessa EI VOI tällä hetkellä (v1.0) tehdä seuraavia asioita:
 *  -   Poistaa mittauksia
 *  -   Poistaa käyttäjiä
 *  -   Valita tarkasteltavaa aikajanaa
 *  -   Valita käyttäjiä, joita haluaa tarkastella kuvaajassa
 *  -   Tarkastella yksittäisen käyttäjän tietoja, kuten
 *      a)  Pituus
 *      b)  BMI-tiedot
 *      c)  Kaikki käyttäjän mittaukset
 *
 * Have fun and enjoy! Niin minäkin tein. :upsidedownsmileyface
 */
public class Main extends Application {

    // Tietorakenne, johon tiedostosta luettavat (ja uudet luotavat) henkilöt tallennetaan.
    protected ArrayList<Henkilo> henkilot = new ArrayList<>();
    // Tietorakenne, johon tulee henkilöiden nimet
    protected ArrayList<String> nimet = new ArrayList<>();
    // Käytetään nappien toiminnallisuuksissa.
    private Henkilo henkiloUusiMittaus;
    // LineChart jota käytetään kuvaajana ja sen tarvitsemat x- ja y-akselit
    CategoryAxis xAkseli = new CategoryAxis(Paivamaarat.pvmLista(henkilot));
    NumberAxis yAkseli = new NumberAxis();
    LineChart<String, Number> kuvaaja = new LineChart<>(xAkseli, yAkseli);
    // ListView, jota käytetään käyttäjien valinnassa
    ListView<String> lvPaneeli = new ListView<>(FXCollections.observableArrayList(nimet));

    // Luodaan muuttumattomat tekstikentät, napit ymv. valmiiksi nappipaneelia varten
    private final TextField tfPaino = new TextField();
    private final TextField tfPaivamaara = new TextField();
    private final Button btTallennaUusiMittaus = new Button("Tallenna");
    private final TextField tfNimi = new TextField();
    private final TextField tfPituus = new TextField();
    private final Button btTallennaKayttaja = new Button("Tallenna");

    /**
     * Aloittaa graafisten ikkunoiden luomisen
     * @param aloitus   aloitusikkuna
     */
    @Override
    public void start(Stage aloitus) {
        // Luetaan tiedosto (tai yritetään sitä)
        TiedostonKaytto.lueTiedostosta(henkilot);
        // Lisätään tiedostosta löytyneiden henkilöiden nimet nimet-listaan
        nimetListaan();
        // Päivitetään kuvaajat
        paivitaKuvaaja();
        // Päivitetään lvPaneeli
        paivitaLVpaneeli();

        // Siirrytään tekemään graafisia elementtejä

        // Tässä tehdään GridPane, johon tulee painikkeet uusien tietojen tallentamiselle.
        GridPane nappipaneeli = new GridPane();
        // Tilaa solujen välille
        nappipaneeli.setHgap(10);
        nappipaneeli.setVgap(20);
        // Lyödään lvPaneeli omaan paneeliinsa vielä
        StackPane paikkaLVpaneelille = new StackPane(lvPaneeli);
        // Lisätään solmut
        nappipaneeli.add(new Label("Valitse henkilö:"), 0, 0);
        nappipaneeli.add(paikkaLVpaneelille, 1, 0);
        nappipaneeli.add(new Label("Paino \n(muodossa 0.0)"), 0, 1);
        nappipaneeli.add(tfPaino, 1, 1);
        nappipaneeli.add(new Label("Päivämäärä \n(muodossa VVVV-KK-PP)"), 0, 2);
        nappipaneeli.add(tfPaivamaara, 1,2);

        // Tehdään lvPaneelin toiminnallisuudet
        lvPaneeli.getSelectionModel().selectedIndexProperty().addListener(ov -> {
            String nimi = lvPaneeli.getSelectionModel().getSelectedItem();
            henkiloUusiMittaus = etsiHenkilo(nimi);
        });

        // Tässä tehdään GridPane, johon tulee painikkeet uuden käyttäjän tallentamiselle
        GridPane uusiKayttajaPaneeli = new GridPane();
        // Tilaa solujen välille
        uusiKayttajaPaneeli.setHgap(10);
        uusiKayttajaPaneeli.setVgap(20);
        // Lisätään solmut
        uusiKayttajaPaneeli.add(new Label("Nimi:"), 0, 0);
        uusiKayttajaPaneeli.add(tfNimi,1,0);
        // Okei, tää on jo niin törkyinen tapa saada kentät samalle kohdalle kuin ylempänä että itsekin nauran :D
        uusiKayttajaPaneeli.add(new Label("Pituus:                             "), 0,1);
        uusiKayttajaPaneeli.add(tfPituus, 1,1);

        // Tehdään pohja paneeleille ikkunan oikeaan laitaan
        VBox pohjapaneeli = new VBox(10);
        // Tehdään otsikko ja virheilmoitus labelit uuden mittauksen lisäämiselle
        Label otsikko1 = new Label("Lisää uusi mittaus");
        StackPane paneeliOtsikko1 = new StackPane(otsikko1);
        Label virheilmoitus1 = new Label("");
        // Tehdään otsikko ja virheilmoitus labelit uden käyttäjän lisäämiselle
        Label otsikko2 = new Label("Lisää uusi käyttäjä");
        StackPane paneeliOtsikko2 = new StackPane(otsikko2);
        Label virheilmoitus2 = new Label("");
        // Lisätään kaikki elementit pohjapaneeliin
        pohjapaneeli.getChildren().addAll(
                paneeliOtsikko1,
                nappipaneeli,
                virheilmoitus1,
                new StackPane(btTallennaUusiMittaus),
                paneeliOtsikko2,
                uusiKayttajaPaneeli,
                virheilmoitus2,
                new StackPane(btTallennaKayttaja));

        // Tehdään pohja johon lisätään kaikki muut elementit.
        BorderPane root = new BorderPane();
        root.setCenter(kuvaaja);
        root.setRight(pohjapaneeli);

        /*
        Tehdään eri napeille toiminnallisuudet. Katsotaan jos nämä ehtii
            a)  heittää omiksi metodeikseen tähän Main-luokkaan tai
            b)  heittää ihan omaksi luokakseen, mistä niitä kutsuisi (enempi työtä tässä)
         */

        // Tehdään tälle napille toiminnallisuudet. Voisi olla myös oma metodinsa (omassa luokassaan).
        btTallennaUusiMittaus.setOnAction(e -> {
            try {
                double paino = Double.parseDouble(tfPaino.getText());
                LocalDate pvm = LocalDate.parse(tfPaivamaara.getText());
                henkiloUusiMittaus.lisaaMittaus(pvm, paino);
                TiedostonKaytto.kirjoitaTiedostoon(henkilot);
                // Tyhjennetään kentät tekstistä, jos clear() on nyt oikea metodi siihen
                tfPaino.clear();
                tfPaivamaara.clear();
                // Tarkistapa tarvitseeko myös päivittää tässä jotain
                paivitaKuvaaja();
                root.setCenter(kuvaaja);
                virheilmoitus1.setText("");
            }
            catch (Exception ex) {
                virheilmoitus1.setText("Hups! Jotain meni vikaan. Kokeile uudestaan!");
            }
        });

        btTallennaKayttaja.setOnAction(e -> {
            try {
                String nimi = String.valueOf(tfNimi.getText());
                double pituus = Double.parseDouble(tfPituus.getText());
                Henkilo uusiHenkilo = new Henkilo(nimi, pituus);
                henkilot.add(uusiHenkilo);
                TiedostonKaytto.kirjoitaTiedostoon(henkilot);
                tfNimi.clear();
                tfPituus.clear();
                paivitaLVpaneeli();
                paikkaLVpaneelille.getChildren().clear();
                paikkaLVpaneelille.getChildren().add(lvPaneeli);
                virheilmoitus2.setText("");
            }
            catch (Exception exx) {
                virheilmoitus2.setText("Hups! Jotain meni vikaan. Kokeile uudestaan!");
            }
        });

        aloitus.setScene(new Scene(root));
        aloitus.setTitle("Painonseuranta v1.0");
        aloitus.show();
    }

    /**
     * Tallentaa henkilot-listalta löytyvät nimet nimet-listaan.
     */
    private void nimetListaan() {
        nimet.clear();
        for (Henkilo henkilo : henkilot) {
            nimet.add(henkilo.getNimi());
        }
    }

    /**
     * Etsii halutun Henkilo-olion annetulla nimellä.
     * @param nimi  etsittävän henkilön nimi
     * @return  Henkilo-olio, jota etsittiin
     */
    private Henkilo etsiHenkilo(String nimi) {
        for (Henkilo henkilo : henkilot) {
            if (henkilo.getNimi().equals(nimi))
                return henkilo;
        }
        // Jos ei löydä mitään, niin ei palauta mitään.
        return null;
    }

    /**
     * Piirtää LineChart-kuvaajan uudelleen aina kun tarve.
     * Käytetään lähinnä silloin, kun sinne lisätään tietoja.
     */
    private void paivitaKuvaaja() {
        xAkseli = new CategoryAxis(Paivamaarat.pvmLista(henkilot));
        yAkseli = new NumberAxis();
        kuvaaja = new LineChart<>(xAkseli, yAkseli);
        for (Henkilo henkilo : henkilot) {
            kuvaaja.getData().add(henkilo.getDatasarja());
        }
        kuvaaja.setCreateSymbols(false);
    }

    /**
     * Päivittää ListViewin käyttäjistä aina kun tarve.
     * Käytetään lähinnä silloin, kun lisätään uusia käyttäjiä.
     */
    private void paivitaLVpaneeli() {
        nimetListaan();
        lvPaneeli = new ListView<>(FXCollections.observableArrayList(nimet));
        lvPaneeli.setPrefSize(100,100);
        lvPaneeli.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvPaneeli.getSelectionModel().selectedIndexProperty().addListener(ov -> {
            String nimi = lvPaneeli.getSelectionModel().getSelectedItem();
            henkiloUusiMittaus = etsiHenkilo(nimi);
        });
    }

    /**
     * Käynnistää ohjelman.
     * @param args  komentoriviargumentit
     */
    public static void main(String[] args) {
        launch(args);
    }
}
