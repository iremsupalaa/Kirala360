package services;

import models.Arac;
import models.Kiralama;

import java.util.ArrayList;

public class KiralamaService {

    private ArrayList<Kiralama> kiralamaListesi;
    private final ArrayList<Arac> aracListesi;

    // ── Dışarıdan yüklenmiş listelerle çalış ─────────────────────────────────
    public KiralamaService(ArrayList<Arac> aracListesi) {
        this.aracListesi    = aracListesi;
        this.kiralamaListesi = new DosyaService().kiralamalariYukle(aracListesi);
    }

    public KiralamaService(ArrayList<Arac> aracListesi,
                           ArrayList<Kiralama> kiralamaListesi) {
        this.aracListesi    = aracListesi;
        this.kiralamaListesi = kiralamaListesi;
    }

    // ── İşlemler ─────────────────────────────────────────────────────────────

    public void aracKirala(Kiralama kiralama) {
        kiralama.getArac().setMusaitMi(false);
        kiralamaListesi.add(kiralama);
        kaydet();
    }

    public void kiralamaIptalEt(int aracId) {
        Kiralama hedef = null;
        for (Kiralama k : kiralamaListesi) {
            if (k.getArac().getId() == aracId) { hedef = k; break; }
        }
        if (hedef != null) {
            hedef.getArac().setMusaitMi(true);
            kiralamaListesi.remove(hedef);
            kaydet();
            new DosyaService().araclariKaydet(aracListesi);
        }
    }

    public ArrayList<Kiralama> getKiralamaListesi() {
        return kiralamaListesi;
    }

    // ── Her değişiklikte otomatik kaydet ─────────────────────────────────────
    private void kaydet() {
        new DosyaService().kiralamalariKaydet(kiralamaListesi);
    }
}