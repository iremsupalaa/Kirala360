package models;

public class Arac {
    private int id;
    private String marka;
    private String model;
    private double gunlukFiyat;
    private boolean musaitMi;


    public Arac(int id, String marka, String model, double gunlukFiyat, boolean musaitMi) {
        this.id = id;
        this.marka = marka;
        this.model = model;
        this.gunlukFiyat = gunlukFiyat;
        this.musaitMi = musaitMi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getGunlukFiyat() {
        return gunlukFiyat;
    }

    public void setGunlukFiyat(double gunlukFiyat) {
        this.gunlukFiyat = gunlukFiyat;
    }

    public boolean isMusaitMi() {
        return musaitMi;
    }

    public void setMusaitMi(boolean musaitMi) {
        this.musaitMi = musaitMi;
    }
    @Override
    public String toString() {
        return marka + " " + model;
    }
}
