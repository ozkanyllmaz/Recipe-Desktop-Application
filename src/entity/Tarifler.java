package entity;

public class Tarifler {
    private int tarifId;
    private int hazirlanmaSuresi;
    private TYPE kategori;
    private String talimatlar;
    private String tarifAdi;

    public enum TYPE {
        AnaYemek,
        Çorba,
        Salata,
        Tatli,
        İçecek;
    }
    public enum TYPE_BIRIM{
        l,
        kg
    }

    public Tarifler() {

    }

    public int getTarifId() {
        return tarifId;
    }

    public void setTarifId(int tarifId) {
        this.tarifId = tarifId;
    }

    public int getHazirlanmaSuresi() {
        return hazirlanmaSuresi;
    }

    public void setHazirlanmaSuresi(int hazirlanmaSuresi) {
        this.hazirlanmaSuresi = hazirlanmaSuresi;
    }

    public TYPE getKategori() {
        return kategori;
    }

    public void setKategori(TYPE kategori) {
        this.kategori = kategori;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    @Override
    public String toString() {
        return "Tarifler{" +
                "tarifId=" + tarifId +
                ", hazirlanmaSuresi='" + hazirlanmaSuresi + '\'' +
                ", kategori=" + kategori +
                ", talimatlar='" + talimatlar + '\'' +
                ", tarifAdi='" + tarifAdi + '\'' +
                '}';
    }
}
