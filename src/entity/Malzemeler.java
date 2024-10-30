package entity;

public class Malzemeler {
    private int malzemeId;
    private String malzemeAdi;
    private String toplamMiktar;
    private TYPE malzemeBirim;
    private float birimFiyat;

    public Malzemeler(double birimFiyat, String malzemeadi, String birim, double toplamMiktar) {
        this.birimFiyat = (float) birimFiyat;
        this.malzemeAdi = malzemeadi;
        this.malzemeBirim = TYPE.valueOf(birim);
        this.toplamMiktar = String.valueOf(toplamMiktar);
    }

    public enum TYPE {
        kg,
        adet,
        L,
        kaşık;
    }

    public Malzemeler(){

    }
    public Malzemeler(String malzemeAdi, String toplamMiktar, TYPE malzemeBirim) {
        this.malzemeAdi = malzemeAdi;
        this.toplamMiktar = toplamMiktar;
        this.malzemeBirim = malzemeBirim;
    }

    public int getMalzemeId() {
        return malzemeId;
    }

    public void setMalzemeId(int malzemeId) {
        this.malzemeId = malzemeId;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public String getToplamMiktar() {
        return toplamMiktar;
    }

    public void setToplamMiktar(String toplamMiktar) {
        this.toplamMiktar = toplamMiktar;
    }

    public TYPE getMalzemeBirim() {
        return malzemeBirim;
    }

    public void setMalzemeBirim(TYPE malzemeBirim) {
        this.malzemeBirim = malzemeBirim;
    }

    public float getBirimFiyat() {
        return birimFiyat;
    }

    public void setBirimFiyat(float birimFiyat) {
        this.birimFiyat = birimFiyat;
    }

    @Override
    public String toString() {
        return "Malzemeler{" +
                "malzemeId=" + malzemeId +
                ", malzemeAdi='" + malzemeAdi + '\'' +
                ", toplamMiktar='" + toplamMiktar + '\'' +
                ", malzemeBirim=" + malzemeBirim +
                ", birimFiyat=" + birimFiyat +
                '}';
    }
}
