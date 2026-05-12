package services;
import models.Kiralama;
import java.util.ArrayList;

public class KiralamaService {
    private ArrayList<Kiralama> kiralamaListesi;

    public KiralamaService() {
        kiralamaListesi = new ArrayList<>();
    }

    public void aracKirala(Kiralama kiralama) {
        if (!kiralama.getArac().isMusaitMi()) {
            System.out.println("Araç müsait değil!");
            return;
        }
        kiralamaListesi.add(kiralama);
        kiralama.getArac().setMusaitMi(false);
        System.out.println("Araç başarıyla kiralandı.");
    }

    public void kiralamalariListele(){
        if (kiralamaListesi.isEmpty()) {
            System.out.println("Kiralama bulunamadı.");
            return;
        }
        for (Kiralama kiralama : kiralamaListesi) {
            System.out.println(kiralama.getMusteri().getAdSoyad() + " -> " + kiralama.getArac().getMarka() + " " + kiralama.getArac().getModel());
        }
    }
}
