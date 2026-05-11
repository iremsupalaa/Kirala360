package services;
import models.Arac;
import java.io.*;
import java.util.ArrayList;
public class DosyaService {
    private final String DOSYA_YOLU = "data/araclar.txt";
    public void araclariKaydet(ArrayList<Arac> aracListesi) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_YOLU))) {
            for (Arac arac : aracListesi) {
                writer.write(
                        arac.getId() + "," +
                                arac.getMarka() + "," +
                                arac.getModel() + "," +
                                arac.getGunlukFiyat() + "," +
                                arac.isMusaitMi()
                );
                writer.newLine();
            }
            System.out.println("Araçlar dosyaya kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosyaya yazma hatası!");
            e.printStackTrace();
        }
    }
    public ArrayList<Arac> araclariYukle() {

        ArrayList<Arac> aracListesi = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_YOLU))) {

            String satir;
            while ((satir = reader.readLine()) != null) {

                String[] bilgiler = satir.split(",");

                int id = Integer.parseInt(bilgiler[0]);
                String marka = bilgiler[1];
                String model = bilgiler[2];
                double fiyat = Double.parseDouble(bilgiler[3]);
                boolean musaitMi = Boolean.parseBoolean(bilgiler[4]);
                Arac arac = new Arac(id, marka, model, fiyat, musaitMi);
                aracListesi.add(arac);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası!");
            e.printStackTrace();
        }
        return aracListesi;
    }
}
