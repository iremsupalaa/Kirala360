package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Kiralama {
    private int kiralamaId;
    private Arac arac;
    private Musteri musteri;
    private int gunSayisi;
    private double toplamUcret;
    private LocalDate kiralamaTarihi;

    public Kiralama(int kiralamaId, Arac arac, Musteri musteri, int gunSayisi) {
        this.kiralamaId = kiralamaId;
        this.arac = arac;
        this.musteri = musteri;
        this.gunSayisi = gunSayisi;
        this.toplamUcret = gunSayisi * arac.getGunlukFiyat();
        this.kiralamaTarihi = LocalDate.now();
    }

    public int getKiralamaId() {
        return kiralamaId;
    }

    public void setKiralamaId(int kiralamaId) {
        this.kiralamaId = kiralamaId;
    }

    public Arac getArac() {
        return arac;
    }

    public void setArac(Arac arac) {
        this.arac = arac;
    }

    public Musteri getMusteri() {
        return musteri;
    }

    public void setMusteri(Musteri musteri) {
        this.musteri = musteri;
    }

    public int getGunSayisi() {
        return gunSayisi;
    }

    public void setGunSayisi(int gunSayisi) {
        this.gunSayisi = gunSayisi;
    }

    public double getToplamUcret() {
        return toplamUcret;
    }

    public long getKalanGun() {
        long gecenGun = ChronoUnit.DAYS.between(kiralamaTarihi, LocalDate.now());
        long kalan = gunSayisi - gecenGun;
        return Math.max(kalan, 0);
    }
    public LocalDate getKiralamaTarihi() {
        return kiralamaTarihi;
    }

    @Override
    public String toString() {
        return musteri.getAdSoyad() + " - " + arac.toString();
    }


}
