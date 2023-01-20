package com.kriskyll.harkkaprojekti_ohii;

import javafx.scene.chart.XYChart;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Luokan ilmentyvät ovat henkilö-olioita, joihin tiedot tallennetaan:
 *  -   Henkilön eli käyttäjän nimi
 *  -   Merkinnät painosta kunakin päivämääränä
 * Luokan ilmentymät kirjoitetaan tiedostoon talteen.
 */
public class Henkilo implements Serializable {
    private String nimi;
    private TreeMap<LocalDate, Double> data = new TreeMap<>();
    private double pituus;

    /**
     * Konstruktori. Luo uuden henkilö-olion, jolle annetaan parametrina henkilön nimi.
     * @param nimi  henkilölle annettava nimi
     */
    public Henkilo(String nimi, double pituus) {
        this.nimi = nimi;
        this.pituus = pituus;
    }

    /**
     * Konstruktori. Luo uuden henkilö-olion, jolle annetaan parametrina henkilön nimi ja
     * samalla tallennetaan ensimmäinen merkintä painosta tiettynä päivämääränä.
     * Tätä ei v1.0 käytä ollenkaan, jatkokehitystä varten.
     * @param nimi  henkilölle annettava nimi
     * @param pvm   päivämäärä, jolle mitattu paino merkitään
     * @param paino mitattu paino
     */
    public Henkilo(String nimi, double pituus, LocalDate pvm, double paino) {
        this.nimi = nimi;
        this.pituus = pituus;
        data.put(pvm, paino);
    }

    /**
     * Palauttaa henkilö-olion nimen.
     * @return  henkilön nimi
     */
    public String getNimi() {
        return this.nimi;
    }

    /**
     * Metodi uuden mittauksen lisäämiseen, joka tallennetaan olion tietoihin (TreeMap).
     * @param pvm   mittauspäivämäärä
     * @param paino mitattu paino
     */
    public void lisaaMittaus(LocalDate pvm, double paino) {
        data.put(pvm, paino);
    }

    /**
     * Luo XYChart.Series -"datasarjan" LineChart-kuvaajaa varten.
     * @return  datasarja kuvaajaan henkilön tallennetuista painoista päivämäärittäin
     */
    public XYChart.Series<String, Number> getDatasarja() {
        XYChart.Series<String, Number> sarja = new XYChart.Series<>();
        sarja.setName(this.nimi);
        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            sarja.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        return sarja;
    }

    /**
     * Palauttaa TreeMapin pienimmän avaimen, eli vanhimman tallennetun mittauksen ajankohdan.
     * @return  vanhin mittauspäivämäärä
     */
    public LocalDate pieninPvm() {
        return data.firstKey();
    }

    /**
     * Palauttaa TreeMapin suurimman avaimen, eli uusimman tallennetun mittauksen ajankohdan.
     * @return  uusin mittauspäivämäärä
     */
    public LocalDate suurinPvm() {
        return data.lastKey();
    }
}
