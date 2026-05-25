package services;

import models.Arac;
import models.Kiralama;

import java.util.ArrayList;

public class KiralamaService {

    private ArrayList<Kiralama> kiralamaListesi;
    private DosyaService dosyaService;

    /**
     * Constructor — mevcut araç listesiyle birlikte kiralamalar dosyadan yüklenir.
     * AracService'den güncel aracListesi geçilmeli ki ID eşleştirmesi doğru çalışsın.
     */
    public KiralamaService(ArrayList<Arac> aracListesi) {
        this.dosyaService  = new DosyaService();
        this.kiralamaListesi = dosyaService.kiralamalariYukle(aracListesi);

        // Dosyadan yüklenen kiralamalar için araçları kiraya çıkar
        // (araclar.txt'de musaitMi=false zaten kayıtlı olduğundan genelde gerekmez,
        //  ama tutarlılık için garantiye alıyoruz)
        for (Kiralama k : kiralamaListesi) {
            k.getArac().setMusaitMi(false);
        }
    }

    /** Araç kirala ve hemen dosyaya kaydet */
    public void aracKirala(Kiralama kiralama) {
        kiralamaListesi.add(kiralama);
        kiralama.getArac().setMusaitMi(false);
        dosyaService.kiralamalariKaydet(kiralamaListesi);
    }

    /** Kiralama listesini döndür */
    public ArrayList<Kiralama> getKiralamaListesi() {
        return kiralamaListesi;
    }

    /**
     * Araç ID'sine göre kiralama kaydını iptal eder.
     * Aracı tekrar müsait olarak işaretler ve dosyayı günceller.
     */
    public void kiralamaIptalEt(int aracId) {
        kiralamaListesi.removeIf(k -> {
            if (k.getArac().getId() == aracId) {
                k.getArac().setMusaitMi(true);   // Aracı müsait yap
                return true;                      // Listeden çıkar
            }
            return false;
        });
        dosyaService.kiralamalariKaydet(kiralamaListesi);
    }
}