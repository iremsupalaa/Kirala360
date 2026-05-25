package services;

import models.Arac;
import models.Kiralama;
import models.Musteri;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DosyaService {

    private static final String ARAC_DOSYA     = "data/araclar.txt";
    private static final String KIRALAMA_DOSYA = "data/kiralamalar.txt";

    // ══════════════════════════════════════════════════════════════════════════
    // ARAÇ DOSYASI  (mevcut — değişmedi)
    // ══════════════════════════════════════════════════════════════════════════

    public void araclariKaydet(ArrayList<Arac> aracListesi) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARAC_DOSYA))) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(ARAC_DOSYA))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] b = satir.split(",");
                int     id      = Integer.parseInt(b[0]);
                String  marka   = b[1];
                String  model   = b[2];
                double  fiyat   = Double.parseDouble(b[3]);
                boolean musait  = Boolean.parseBoolean(b[4]);
                aracListesi.add(new Arac(id, marka, model, fiyat, musait));
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası!");
            e.printStackTrace();
        }
        return aracListesi;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // KİRALAMA DOSYASI
    // Format: kiralamaId,aracId,musteriAdSoyad,musteriTelefon,gunSayisi,kiralamaTarihi
    // ══════════════════════════════════════════════════════════════════════════

    public void kiralamalariKaydet(ArrayList<Kiralama> kiralamaListesi) {
        // data/ klasörü yoksa oluştur
        new File("data").mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KIRALAMA_DOSYA))) {
            for (Kiralama k : kiralamaListesi) {
                writer.write(
                        k.getKiralamaId()               + "," +
                                k.getArac().getId()              + "," +
                                k.getMusteri().getAdSoyad()      + "," +
                                k.getMusteri().getTelefon()      + "," +
                                k.getGunSayisi()                 + "," +
                                k.getKiralamaTarihi().toString()   // ISO-8601: yyyy-MM-dd
                );
                writer.newLine();
            }
            System.out.println("Kiralamalar dosyaya kaydedildi.");
        } catch (IOException e) {
            System.out.println("Kiralama dosyasına yazma hatası!");
            e.printStackTrace();
        }
    }

    /**
     * Kiralamaları dosyadan yükler.
     * Aracı bulmak için mevcut aracListesi gerekli (ID ile eşleştirme).
     */
    public ArrayList<Kiralama> kiralamalariYukle(ArrayList<Arac> aracListesi) {
        ArrayList<Kiralama> kiralamaListesi = new ArrayList<>();
        File dosya = new File(KIRALAMA_DOSYA);
        if (!dosya.exists()) return kiralamaListesi;   // İlk çalıştırmada dosya olmayabilir

        try (BufferedReader reader = new BufferedReader(new FileReader(KIRALAMA_DOSYA))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] b = satir.split(",");

                int    kiralamaId = Integer.parseInt(b[0]);
                int    aracId     = Integer.parseInt(b[1]);
                String adSoyad   = b[2];
                String telefon   = b[3];
                int    gunSayisi  = Integer.parseInt(b[4]);
                LocalDate tarih  = LocalDate.parse(b[5]);   // yyyy-MM-dd

                // Araç listesinden ID ile eşleştir
                Arac arac = null;
                for (Arac a : aracListesi) {
                    if (a.getId() == aracId) { arac = a; break; }
                }
                if (arac == null) continue;   // Araç silinmişse atla

                Musteri musteri = new Musteri(kiralamaId, adSoyad, telefon);

                // Kiralama nesnesini oluştur ve tarihi dosyadaki tarihle override et
                Kiralama k = new Kiralama(kiralamaId, arac, musteri, gunSayisi);
                k.setKiralamaTarihi(tarih);

                kiralamaListesi.add(k);
            }
            System.out.println("Kiralamalar dosyadan yüklendi.");
        } catch (IOException e) {
            System.out.println("Kiralama dosyası okuma hatası!");
            e.printStackTrace();
        }
        return kiralamaListesi;
    }
}