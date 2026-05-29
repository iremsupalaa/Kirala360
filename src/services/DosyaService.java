package services;

import models.Arac;
import models.Kiralama;
import models.Musteri;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DosyaService {

    private static final String ARAC_DOSYA       = "data/araclar.txt";
    private static final String KIRALAMA_DOSYA   = "data/kiralamalar.txt";
    private static final String KULLANICI_DOSYA  = "data/kullanicilar.txt";

    // ══════════════════════════════════════════════════════════════════════════
    // ARAÇ DOSYASI
    // ══════════════════════════════════════════════════════════════════════════

    public void araclariKaydet(ArrayList<Arac> aracListesi) {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARAC_DOSYA))) {
            for (Arac arac : aracListesi) {
                writer.write(
                        arac.getId()          + "," +
                                arac.getMarka()       + "," +
                                arac.getModel()       + "," +
                                arac.getGunlukFiyat() + "," +
                                arac.isMusaitMi()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Dosyaya yazma hatas\u0131: " + e.getMessage());
        }
    }

    public ArrayList<Arac> araclariYukle() {
        ArrayList<Arac> liste = new ArrayList<>();
        File dosya = new File(ARAC_DOSYA);
        if (!dosya.exists()) return liste;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARAC_DOSYA))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] b = satir.split(",");
                if (b.length < 5) continue;
                int     id     = Integer.parseInt(b[0].trim());
                String  marka  = b[1].trim();
                String  model  = b[2].trim();
                double  fiyat  = Double.parseDouble(b[3].trim());
                boolean musait = Boolean.parseBoolean(b[4].trim());
                liste.add(new Arac(id, marka, model, fiyat, musait));
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatas\u0131: " + e.getMessage());
        }
        return liste;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // KİRALAMA DOSYASI
    // Format: kiralamaId,aracId,musteriAdSoyad,musteriTelefon,gunSayisi,kiralamaTarihi
    // ══════════════════════════════════════════════════════════════════════════

    public void kiralamalariKaydet(ArrayList<Kiralama> kiralamaListesi) {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KIRALAMA_DOSYA))) {
            for (Kiralama k : kiralamaListesi) {
                writer.write(
                        k.getKiralamaId()              + "," +
                                k.getArac().getId()             + "," +
                                k.getMusteri().getAdSoyad()     + "," +
                                k.getMusteri().getTelefon()     + "," +
                                k.getGunSayisi()                + "," +
                                k.getKiralamaTarihi().toString()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Kiralama dosyas\u0131na yazma hatas\u0131: " + e.getMessage());
        }
    }

    public ArrayList<Kiralama> kiralamalariYukle(ArrayList<Arac> aracListesi) {
        ArrayList<Kiralama> liste = new ArrayList<>();
        File dosya = new File(KIRALAMA_DOSYA);
        if (!dosya.exists()) return liste;

        try (BufferedReader reader = new BufferedReader(new FileReader(KIRALAMA_DOSYA))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] b = satir.split(",");
                if (b.length < 6) continue;

                int       kiralamaId = Integer.parseInt(b[0].trim());
                int       aracId     = Integer.parseInt(b[1].trim());
                String    adSoyad    = b[2].trim();
                String    telefon    = b[3].trim();
                int       gunSayisi  = Integer.parseInt(b[4].trim());
                LocalDate tarih      = LocalDate.parse(b[5].trim());

                Arac arac = null;
                for (Arac a : aracListesi) {
                    if (a.getId() == aracId) { arac = a; break; }
                }
                if (arac == null) continue;

                Musteri musteri = new Musteri(kiralamaId, adSoyad, telefon);
                Kiralama k = new Kiralama(kiralamaId, arac, musteri, gunSayisi);
                k.setKiralamaTarihi(tarih);
                liste.add(k);
            }
        } catch (IOException e) {
            System.out.println("Kiralama dosyas\u0131 okuma hatas\u0131: " + e.getMessage());
        }
        return liste;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // KULLANICI DOSYASI  ← YENİ
    // Format: kullaniciAdi,sifre
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Kullanıcıları dosyadan yükler.
     * Dosya yoksa varsayılan admin/1234 döner ve dosyayı oluşturur.
     */
    public Map<String, String> kullanicilariYukle() {
        Map<String, String> kullanicilar = new HashMap<>();
        File dosya = new File(KULLANICI_DOSYA);

        if (!dosya.exists()) {
            // İlk çalıştırma: varsayılan kullanıcı oluştur
            kullanicilar.put("admin", "1234");
            kullanicilariKaydet(kullanicilar);
            return kullanicilar;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(KULLANICI_DOSYA))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] b = satir.split(",", 2);
                if (b.length < 2) continue;
                kullanicilar.put(b[0].trim(), b[1].trim());
            }
        } catch (IOException e) {
            System.out.println("Kullan\u0131c\u0131 dosyas\u0131 okuma hatas\u0131: " + e.getMessage());
            kullanicilar.put("admin", "1234"); // Hata durumunda varsayılan
        }

        // Dosya boşsa varsayılan ekle
        if (kullanicilar.isEmpty()) {
            kullanicilar.put("admin", "1234");
            kullanicilariKaydet(kullanicilar);
        }

        return kullanicilar;
    }

    public void kullanicilariKaydet(Map<String, String> kullanicilar) {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KULLANICI_DOSYA))) {
            for (Map.Entry<String, String> entry : kullanicilar.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Kullan\u0131c\u0131 dosyas\u0131na yazma hatas\u0131: " + e.getMessage());
        }
    }
}