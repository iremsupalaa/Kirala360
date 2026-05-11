package services;

import models.Arac;

import java.util.ArrayList;

public class AracService {

    private ArrayList<Arac> aracListesi;

    public AracService() {
        aracListesi = new ArrayList<>();
    }

    // Araç ekleme
    public void aracEkle(Arac arac) {

        if (aracVarMi(arac.getId())) {

            System.out.println("Bu ID'ye sahip araç zaten var!");
            return;
        }

        aracListesi.add(arac);

        System.out.println("Araç başarıyla eklendi.");
    }

    // Araç silme
    public void aracSil(int id) {

        Arac silinecekArac = null;

        for (Arac arac : aracListesi) {

            if (arac.getId() == id) {
                silinecekArac = arac;
                break;
            }
        }

        if (silinecekArac != null) {

            aracListesi.remove(silinecekArac);

            System.out.println("Araç silindi.");
        }
        else {

            System.out.println("Araç bulunamadı.");
        }
    }

    // Araç listeleme
    public void araclariListele() {

        if (aracListesi.isEmpty()) {

            System.out.println("Araç listesi boş.");
            return;
        }

        for (Arac arac : aracListesi) {

            System.out.println(
                    arac.getId() + " - " +
                            arac.getMarka() + " " +
                            arac.getModel() + " - " +
                            arac.getGunlukFiyat() + " TL"
            );
        }
    }

    // ID ile araç getirme
    public Arac aracGetir(int id) {

        for (Arac arac : aracListesi) {

            if (arac.getId() == id) {
                return arac;
            }
        }

        return null;
    }

    // Aynı ID'ye sahip araç var mı kontrolü
    private boolean aracVarMi(int id) {

        for (Arac arac : aracListesi) {

            if (arac.getId() == id) {
                return true;
            }
        }

        return false;
    }

    // Araç listesini döndürme
    public ArrayList<Arac> getAracListesi() {
        return aracListesi;
    }
}