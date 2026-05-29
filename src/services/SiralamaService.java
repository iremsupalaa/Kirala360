package services;

import models.Arac;

import java.util.ArrayList;

/**
 * Sıralama algoritmaları servisi.
 *
 * Algoritmalar:
 *   1. Bubble Sort    → fiyata göre artan/azalan
 *   2. Insertion Sort → markaya göre A-Z / Z-A  ← YENİ (ek puan)
 */
public class SiralamaService {

    // ══════════════════════════════════════════════════════════════════════════
    // 1. BUBBLE SORT — fiyata göre
    // ══════════════════════════════════════════════════════════════════════════

    public ArrayList<Arac> fiyataGoreBubbleSort(ArrayList<Arac> liste, boolean artan) {
        ArrayList<Arac> kopya = new ArrayList<>(liste);
        int n = kopya.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean degistir = artan
                        ? kopya.get(j).getGunlukFiyat() > kopya.get(j + 1).getGunlukFiyat()
                        : kopya.get(j).getGunlukFiyat() < kopya.get(j + 1).getGunlukFiyat();
                if (degistir) {
                    Arac tmp = kopya.get(j);
                    kopya.set(j, kopya.get(j + 1));
                    kopya.set(j + 1, tmp);
                }
            }
        }
        return kopya;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. INSERTION SORT — markaya göre alfabetik  ← YENİ
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Insertion Sort algoritması ile araçları markaya göre sıralar.
     * @param liste  kaynak liste (değiştirilmez, kopya döner)
     * @param azdan  true → A-Z,  false → Z-A
     */
    public ArrayList<Arac> markaGoreInsertionSort(ArrayList<Arac> liste, boolean azdan) {
        ArrayList<Arac> kopya = new ArrayList<>(liste);
        int n = kopya.size();

        for (int i = 1; i < n; i++) {
            Arac anahtar = kopya.get(i);
            int j = i - 1;

            while (j >= 0 && karsilastir(kopya.get(j), anahtar, azdan) > 0) {
                kopya.set(j + 1, kopya.get(j));
                j--;
            }
            kopya.set(j + 1, anahtar);
        }
        return kopya;
    }

    /**
     * Önce markaya göre, eşitse modele göre karşılaştırır.
     */
    private int karsilastir(Arac a, Arac b, boolean azdan) {
        int sonuc = a.getMarka().compareToIgnoreCase(b.getMarka());
        if (sonuc == 0) sonuc = a.getModel().compareToIgnoreCase(b.getModel());
        return azdan ? sonuc : -sonuc;
    }
}