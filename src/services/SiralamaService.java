package services;

import models.Arac;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Araç listesi için sıralama algoritmaları.
 * Ek puan kriteri: veri sıralama algoritması (Bubble Sort implementasyonu).
 */
public class SiralamaService {

    /**
     * Bubble Sort — fiyata göre artan sıralama.
     * Orijinal listeyi değiştirmez; yeni sıralı liste döner.
     */
    public ArrayList<Arac> fiyataGoreBubbleSort(ArrayList<Arac> liste, boolean artan) {
        ArrayList<Arac> kopya = new ArrayList<>(liste);
        int n = kopya.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double fiyat1 = kopya.get(j).getGunlukFiyat();
                double fiyat2 = kopya.get(j + 1).getGunlukFiyat();

                boolean takas = artan ? fiyat1 > fiyat2 : fiyat1 < fiyat2;
                if (takas) {
                    Arac temp = kopya.get(j);
                    kopya.set(j, kopya.get(j + 1));
                    kopya.set(j + 1, temp);
                }
            }
        }
        return kopya;
    }

    /**
     * Bubble Sort — ID'ye göre artan sıralama.
     */
    public ArrayList<Arac> idyeGoreBubbleSort(ArrayList<Arac> liste, boolean artan) {
        ArrayList<Arac> kopya = new ArrayList<>(liste);
        int n = kopya.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int id1 = kopya.get(j).getId();
                int id2 = kopya.get(j + 1).getId();

                boolean takas = artan ? id1 > id2 : id1 < id2;
                if (takas) {
                    Arac temp = kopya.get(j);
                    kopya.set(j, kopya.get(j + 1));
                    kopya.set(j + 1, temp);
                }
            }
        }
        return kopya;
    }

    /**
     * Bubble Sort — marka adına göre alfabetik sıralama.
     */
    public ArrayList<Arac> markaGoreBubbleSort(ArrayList<Arac> liste, boolean artan) {
        ArrayList<Arac> kopya = new ArrayList<>(liste);
        int n = kopya.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int cmp = kopya.get(j).getMarka()
                        .compareToIgnoreCase(kopya.get(j + 1).getMarka());

                boolean takas = artan ? cmp > 0 : cmp < 0;
                if (takas) {
                    Arac temp = kopya.get(j);
                    kopya.set(j, kopya.get(j + 1));
                    kopya.set(j + 1, temp);
                }
            }
        }
        return kopya;
    }
}