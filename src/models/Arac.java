package models;

public class Arac {

    // ── Şanzuman tipi enum ────────────────────────────────────────────────────
    public enum Sanzuman {
        MANUEL("Manuel"),
        OTOMATIK("\u00d6tomatik");

        private final String etiket;
        Sanzuman(String etiket) { this.etiket = etiket; }
        public String getEtiket() { return etiket; }

        @Override public String toString() { return etiket; }

        /** Dosyadan okurken güvenli parse — bilinmeyende MANUEL döner */
        public static Sanzuman fromString(String s) {
            if (s == null) return MANUEL;
            if (s.trim().equalsIgnoreCase("OTOMATIK")
                    || s.trim().equalsIgnoreCase("\u00d6tomatik")) return OTOMATIK;
            return MANUEL;
        }
    }

    // ── Alanlar ───────────────────────────────────────────────────────────────
    private int      id;
    private String   marka;
    private String   model;
    private double   gunlukFiyat;
    private boolean  musaitMi;
    private Sanzuman sanzuman;   // ← YENİ

    // ── Constructor (yeni — şanzuman dahil) ───────────────────────────────────
    public Arac(int id, String marka, String model,
                double gunlukFiyat, boolean musaitMi, Sanzuman sanzuman) {
        this.id          = id;
        this.marka       = marka;
        this.model       = model;
        this.gunlukFiyat = gunlukFiyat;
        this.musaitMi    = musaitMi;
        this.sanzuman    = sanzuman != null ? sanzuman : Sanzuman.MANUEL;
    }

    // ── Eski constructor (geriye dönük uyumluluk — şanzuman varsayılan MANUEL) ─
    public Arac(int id, String marka, String model,
                double gunlukFiyat, boolean musaitMi) {
        this(id, marka, model, gunlukFiyat, musaitMi, Sanzuman.MANUEL);
    }

    // ── Getter / Setter ───────────────────────────────────────────────────────
    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public String getMarka()             { return marka; }
    public void setMarka(String marka)   { this.marka = marka; }

    public String getModel()             { return model; }
    public void setModel(String model)   { this.model = model; }

    public double getGunlukFiyat()                   { return gunlukFiyat; }
    public void setGunlukFiyat(double gunlukFiyat)   { this.gunlukFiyat = gunlukFiyat; }

    public boolean isMusaitMi()              { return musaitMi; }
    public void setMusaitMi(boolean musait)  { this.musaitMi = musait; }

    public Sanzuman getSanzuman()                { return sanzuman; }
    public void setSanzuman(Sanzuman sanzuman)   { this.sanzuman = sanzuman; }

    @Override
    public String toString() { return marka + " " + model + " (" + sanzuman + ")"; }
}