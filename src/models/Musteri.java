package models;

public class Musteri {
    private int id;
    private String adSoyad;
    private String telefon;

    public Musteri(int id, String adSoyad, String telefon) {
        this.id = id;
        this.adSoyad = adSoyad;
        this.telefon = telefon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    @Override
    public String toString() {
        return adSoyad;

    }
}
