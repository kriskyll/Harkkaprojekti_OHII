package com.kriskyll.harkkaprojekti_ohii;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Päivämääriin liittyvät staattiset metodit koottuna yhteen luokkaan:
 *  -   Vanhin ja uusin päivämäärä kaikista ohjelmaan tallennetuista mittaustuloksista
 *  -   Lista päivämääristä String-olioina näiden em. päivämäärien välillä
 */
public class Paivamaarat {
    /**
     * Palauttaa listan, jossa on String-olioina kerran kaikki päivämäärät pienimmästä suurimpaan Henkilo-olioihin
     * tallennetuista päivämääristä. Lista muotoa FXCollections.observableArrayList.
     * @param henkilot  taulukko, johon henkilöt on tallennettu
     * @return          listana kaikki päivämäärät pienimmästä suurimpaan, jotka Henkilo-olioilla käytössä
     */
    public static ObservableList<String> pvmLista(ArrayList<Henkilo> henkilot) {
        ObservableList<String> pvmLista = FXCollections.observableArrayList();
        LocalDate min = pieninPvmKaikista(henkilot);
        // Jos mitään päivämääriä ei ole tallennettu, palautetaan tyhjä lista.
        if (min == null)
            return pvmLista;
        while (!min.equals(suurinPvmKaikista(henkilot).plusDays(1))) {
            pvmLista.add(min.toString());
            min = min.plusDays(1);
        }
        return pvmLista;
    }

    /**
     * Palauttaa kaikista vanhimman päivämäärän LocalDate-oliona kaikista päivämääristä, jotka Henkilo-olioiden
     * tietorakenteisiin on tallennettu.
     * @param henkilot  taulukko, johon henkilöt on tallennettu
     * @return          kaikista vanhin käytetty päivämäärä
     */
    public static LocalDate pieninPvmKaikista(ArrayList<Henkilo> henkilot) {
        LocalDate min = LocalDate.MAX;
        for (Henkilo henkilo : henkilot) {
            try {
                if (henkilo.pieninPvm().isBefore(min)) {
                    min = henkilo.pieninPvm();
                }
            }
            catch (Exception e) {
                // Ei ole tarvetta tehdä tässä mitään.
            }
        }
        // Palauttaa null:n jos mitään päivämäärää ei ole tallennettu.
        if (min.equals(LocalDate.MAX))
            return null;
        return min;
    }

    /**
     * Palauttaa kaikista uusimman päivämäärän LocalDate-oliona kaikista päivämääristä, jotka Henkilo-olioiden
     * tietorakenteisiin on tallennettu.
     * @param henkilot  taulukko, johon henkilöt on tallennettu
     * @return          kaikista uusin käytetty päivämäärä
     */
    public static LocalDate suurinPvmKaikista(ArrayList<Henkilo> henkilot) {
        LocalDate max = LocalDate.MIN;
        for (Henkilo henkilo : henkilot) {
            try {
                if (henkilo.suurinPvm().isAfter(max)) {
                    max = henkilo.suurinPvm();
                }
            }
            catch (Exception e) {
                // Ei ole tarvetta tehdä tässä mitään.
            }
        }
        // Palauttaa null:n jos mitään päivämäärää ei ole tallennettu.
        if (max.equals(LocalDate.MIN))
            return null;
        return max;
    }
}
