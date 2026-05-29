package services;

import models.Arac;

import java.util.ArrayList;

public class AracService {

    private ArrayList<Arac> aracListesi;

    // ── Boş constructor: dosyadan yükle ──────────────────────────────────────
    public AracService() {
        this.aracListesi = new DosyaService().araclariYukle();
    }

    // ── Dışarıdan liste verilirse kullan (LoginFrame'den geçiş) ──────────────
    public AracService(ArrayList<Arac> aracListesi) {
        this.aracListesi = aracListesi;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    public void aracEkle(Arac arac) {
        for (Arac a : aracListesi) {
            if (a.getId() == arac.getId()) {
                throw new IllegalArgumentException("Bu ID zaten mevcut: " + arac.getId());
            }
        }
        aracListesi.add(arac);
        kaydet();
    }

    public void aracSil(int id) {
        aracListesi.removeIf(a -> a.getId() == id);
        kaydet();
    }

    public Arac aracGetir(int id) {
        for (Arac a : aracListesi) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    public ArrayList<Arac> getAracListesi() {
        return aracListesi;
    }

    public ArrayList<Arac> musaitAraclariGetir() {
        ArrayList<Arac> musait = new ArrayList<>();
        for (Arac a : aracListesi) {
            if (a.isMusaitMi()) musait.add(a);
        }
        return musait;
    }

    // ── Her değişiklikte otomatik kaydet ─────────────────────────────────────
    private void kaydet() {
        new DosyaService().araclariKaydet(aracListesi);
    }
}