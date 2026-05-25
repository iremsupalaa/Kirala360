package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Kiralama {
    private int kiralamaId;
    private Arac arac;
    private Musteri musteri;
    private int gunSayisi;
    private double toplamUcret;
    private LocalDate kiralamaTarihi;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Kiralama(int kiralamaId, Arac arac, Musteri musteri, int gunSayisi) {
        this.kiralamaId   = kiralamaId;
        this.arac         = arac;
        this.musteri      = musteri;
        this.gunSayisi    = gunSayisi;
        this.toplamUcret  = gunSayisi * arac.getGunlukFiyat();
        this.kiralamaTarihi = LocalDate.now();
    }

    public int getKiralamaId()              { return kiralamaId; }
    public void setKiralamaId(int id)       { this.kiralamaId = id; }

    public Arac getArac()                   { return arac; }
    public void setArac(Arac arac)          { this.arac = arac; }

    public Musteri getMusteri()             { return musteri; }
    public void setMusteri(Musteri m)       { this.musteri = m; }

    public int getGunSayisi()               { return gunSayisi; }
    public void setGunSayisi(int g)         { this.gunSayisi = g; }

    public double getToplamUcret()          { return toplamUcret; }

    public LocalDate getKiralamaTarihi()    { return kiralamaTarihi; }
    public void setKiralamaTarihi(LocalDate t) { this.kiralamaTarihi = t; }

    public long getKalanGun() {
        long gecenGun = ChronoUnit.DAYS.between(kiralamaTarihi, LocalDate.now());
        return Math.max(gunSayisi - gecenGun, 0);
    }

    // ── Tabloda gösterim için formatlı tarihler ──────────────────────────────

    /** Kiralandığı tarihi "dd.MM.yyyy" formatında döner */
    public String getKiralamaTarihiStr() {
        return kiralamaTarihi.format(FMT);
    }

    /** Müsait olacağı tarihi (kiralamaTarihi + gunSayisi) "dd.MM.yyyy" formatında döner */
    public String getMusaitOlacakTarihStr() {
        return kiralamaTarihi.plusDays(gunSayisi).format(FMT);
    }

    @Override
    public String toString() {
        return musteri.getAdSoyad() + " - " + arac.toString();
    }
}