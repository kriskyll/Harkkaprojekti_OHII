package com.kriskyll.harkkaprojekti_ohii;

import java.io.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Metodit tiedoston kirjoittamiseen ja lukemiseen, jolla luetaan aiemmin tallennetut henkilöt tietoineen
 * ohjelman käyttöön sekä tallennetaan ohjelman lopuksi (<-- HUOM!) kaikki henkilöt, joita ohjelman
 * henkilot-ArrayListissä sillä hetkellä on.
 */
public class TiedostonKaytto {
    // HUOM! OBS! Muokkaa tähän haluamasi tiedostosijainti (ja tiedoston nimi, jos tarve).
    // Tuossa on tuo sijainti nyt vähän hölmösti tuplana, kiireestä johtuen.
    protected final static String tiedostosijainti = "painonseuranta.dat";
    private final static File tiedosto = new File("painonseuranta.dat");

    /**
     * Kirjoittaa henkilot-listassa olevat Henkilo-oliot tiedostoon.
     * Palautettavaa boolean-arvoa ei versiossa v1.0 hyödynnä.
     * @param henkilot  käytettävien henkilöiden lista
     * @return          boolean-arvo sen mukaan, onnistuiko kirjoitus tiedostoon vai ei
     */
    public static boolean kirjoitaTiedostoon(ArrayList<Henkilo> henkilot) {
        try {
            FileOutputStream tiedosto = new FileOutputStream(tiedostosijainti);
            ObjectOutputStream tiedostoOlio = new ObjectOutputStream(tiedosto);
            for (Henkilo henkilo : henkilot) {
                tiedostoOlio.writeObject(henkilo);
            }
            tiedosto.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Lukee tiedostossa olevat Henkilö-oliot henkilot-listaan.
     * Palautettavaa boolean-arvoa ei versiossa v1.0 hyödynnä.
     * @param henkilot  käytettävien henkilöiden lista
     * @return          boolean-arvo sen mukaan, onnistuiko kirjoitus tiedostoon vai ei
     */
    public static boolean lueTiedostosta(ArrayList<Henkilo> henkilot) {
        if (tiedosto.exists()) {
            boolean tiedostoLoppu = false;
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(tiedosto));
                while (!tiedostoLoppu) {
                    try {
                        Henkilo lisattava = (Henkilo) input.readObject();
                        henkilot.add(lisattava);
                        System.out.println("Henkilö luettunna ja lisättynnä listaan");
                    }
                    catch (EOFException | ClassNotFoundException e) {
                        tiedostoLoppu = true;
                        input.close();
                    }
                }
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
