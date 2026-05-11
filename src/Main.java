import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;

public class Main {

    public static void main(String[] args) {

        Arac arac = new Arac(1, "Toyota", "Corolla", 1500, true);
        Musteri musteri = new Musteri(1, "İremsu Pala", "5551234567");
        Kiralama kiralama = new Kiralama(1, arac, musteri, 3);

        System.out.println(arac);
        System.out.println(musteri);
        System.out.println(kiralama);
        System.out.println("Toplam Ücret: " + kiralama.getToplamUcret());

        //---------------------------------------------------------------------------------------------------------
        AracService aracService = new AracService();
        Arac arac1 = new Arac(1, "Toyota", "Corolla", 1500, true);
        Arac arac2 = new Arac(2, "BMW", "320i", 3000, true);

        aracService.aracEkle(arac1);
        aracService.aracEkle(arac2);
        System.out.println("\nARAÇ LİSTESİ:");
        aracService.araclariListele();
        System.out.println("\nARAÇ SİLME:");
        aracService.aracSil(1);
        System.out.println("\nGÜNCEL LİSTE:");
        aracService.araclariListele();
    }
}