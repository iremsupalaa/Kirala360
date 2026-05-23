package services;

import models.Kiralama;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class KiralamaService {

    private ArrayList<Kiralama> kiralamaListesi;

    public KiralamaService() {
        kiralamaListesi = new ArrayList<>();
    }

    // Araç kiralama
    public void aracKirala(Kiralama kiralama) {

        kiralamaListesi.add(kiralama);
        kiralama.getArac().setMusaitMi(false);
    }

    // Kiralamaları döndür
    public ArrayList<Kiralama> getKiralamaListesi() {
        return kiralamaListesi;
    }
}