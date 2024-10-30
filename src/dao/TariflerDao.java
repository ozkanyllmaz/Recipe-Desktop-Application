package dao;


import core.Database;
import entity.Malzemeler;
import entity.Tarifler;

import java.sql.*;
import java.util.ArrayList;

public class TariflerDao {
    private Connection connection;

    public TariflerDao() {
        this.connection = Database.getInstance();
    }

    public ArrayList<Tarifler> findAll() {
        ArrayList<Tarifler> tarifler = new ArrayList<>();
        String sql = "SELECT * FROM tarifler"; // Tüm tarifleri çek
        try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Tarifler tarif = new Tarifler();
                tarif.setTarifId(rs.getInt("tarifid")); // TarifID'yi al
                tarif.setTarifAdi(rs.getString("tarifadi"));
                tarif.setKategori(Tarifler.TYPE.valueOf(rs.getString("kategori")));
                tarif.setHazirlanmaSuresi(rs.getInt("hazirlamasuresi"));
                tarifler.add(tarif);
            }
            System.out.println("Toplam tarif sayısı: " + tarifler.size()); // Debug
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifler;
    }

    public Integer findTarifIDbyTarifName(String tarifName) {
        Integer tarifId = null; // Başlangıçta ID'yi null olarak tanımla
        String sql = "SELECT tarifid FROM tarifler WHERE tarifadi = ?"; // Belirli bir tarif adı için sorgu
        try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql)) {
            pstmt.setString(1, tarifName); // Sorguda parametreyi ayarla
            ResultSet rs = pstmt.executeQuery();

            // Sonuç kümesini kontrol et
            if (rs.next()) {
                tarifId = rs.getInt("tarifid"); // İlk bulduğu tarif ID'sini al
            }
            System.out.println("Bulunan tarif ID: " + tarifId); // Debug
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifId; // ID'yi döndür
    }


    public boolean save(Tarifler tarifler) {
        try {
            String query = "INSERT INTO tarifler (hazirlamaSuresi, kategori, talimatlar, tarifAdi) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Eklenen id'yi geri almak için
            pstmt.setInt(1, tarifler.getHazirlanmaSuresi());
            pstmt.setString(2, tarifler.getKategori().toString());
            pstmt.setString(3, tarifler.getTalimatlar());
            pstmt.setString(4, tarifler.getTarifAdi());

            int duzenlenenSatirlar = pstmt.executeUpdate();
            if (duzenlenenSatirlar > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        tarifler.setTarifId(rs.getInt(1));  // Yeni eklenen tarifin ID'sini ayarla
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int getMaxTarifId() {
        int maxTarifId = 0;
        try {
            String query = "SELECT MAX(tarifId) AS maxTarifId FROM tarifler";
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                maxTarifId = rs.getInt("maxTarifId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxTarifId;
    }

    public Tarifler findByTarifID(int tarifid) {
        Tarifler tarifler = null;
        String query = "SELECT * FROM tarifler WHERE tarifid = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tarifid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tarifler = new Tarifler();
                tarifler.setTarifId(rs.getInt("tarifid")); // TarifID'yi al
                tarifler.setTarifAdi(rs.getString("tarifadi"));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tarifler;
    }

    public boolean update(Tarifler tarifler) {
        String query = "UPDATE tarifler SET (hazirlanmasuresi, kategori, talimatlar, tarifadi) VALUES (?,?,?,?);";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tarifler.getHazirlanmaSuresi());
            pstmt.setString(2, tarifler.getKategori().toString());
            pstmt.setString(3, tarifler.getTalimatlar());
            pstmt.setString(4, tarifler.getTarifAdi());
            return pstmt.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Tarifler findTarifByName(String tarifAdi) {
        Tarifler tarif = null;
        String sql = "SELECT * FROM tarifler WHERE tarifadi = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tarifAdi);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tarif = new Tarifler();
                tarif.setTarifId(rs.getInt("tarifid"));
                tarif.setTarifAdi(rs.getString("tarifadi"));
                tarif.setKategori(Tarifler.TYPE.valueOf(rs.getString("kategori")));
                tarif.setHazirlanmaSuresi(Integer.parseInt((rs.getString("hazirlamasuresi"))));
                tarif.setTalimatlar(rs.getString("talimatlar"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tarif;
    }

    public ArrayList<Malzemeler> findMalzemelerByTarifId(int tarifId) {
        ArrayList<Malzemeler> malzemelerList = new ArrayList<>();

        try {
            String query = "SELECT m.malzemeadi, tm.malzememiktar, m.toplammiktar, m.malzemebirim " +
                    "FROM malzemeler m " +
                    "JOIN tarif_malzeme tm ON m.malzemeid = tm.malzemeid " +
                    "WHERE tm.tarifid = ?";


            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setInt(1, tarifId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Malzemeler malzeme = new Malzemeler();
                malzeme.setMalzemeAdi(rs.getString("malzemeadi"));
                malzeme.setToplamMiktar(rs.getString("toplammiktar")); // Eğer miktar double ise
                malzeme.setMalzemeBirim(Malzemeler.TYPE.valueOf(rs.getString("malzemebirim")));

                malzemelerList.add(malzeme);
                System.out.println(malzeme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return malzemelerList;
    }

    public boolean delete(int tarifid) {
        String query = "DELETE FROM tarifler WHERE tarifid = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tarifid);
            return pstmt.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Tarifler> query(String query) {
        ArrayList<Tarifler> tariflers = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()) {
                Tarifler tarif = new Tarifler();
                //tarif.setTarifId(rs.getInt("tarifid")); // TarifID'yi al
                tarif.setTarifAdi(rs.getString("tarifadi"));
                tarif.setKategori(Tarifler.TYPE.valueOf(rs.getString("kategori")));
                tarif.setHazirlanmaSuresi(rs.getInt("hazirlamasuresi"));
                tariflers.add(tarif);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tariflers;
    }

    public ArrayList<Tarifler> filterByMalzemeler(ArrayList<String> selectedMalzemeler) {
        ArrayList<Tarifler> filteredTarifler = new ArrayList<>();

        // SQL sorgusunu oluştur
        String sql = "SELECT DISTINCT t.* FROM tarifler t " +
                "JOIN tarif_malzeme tm ON t.TarifID = tm.TarifID " +
                "JOIN malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE m.MalzemeAdi IN (";

        // Malzeme isimlerini sorguya ekle
        for (int i = 0; i < selectedMalzemeler.size(); i++) {
            sql += "?"; // Sorguya '?' ekleyin
            if (i < selectedMalzemeler.size() - 1) {
                sql += ", "; // Son elemandan sonra ',' ekleyin
            }
        }
        sql += ")";

        try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql)) {
            // Malzeme isimlerini parametre olarak ayarla
            for (int i = 0; i < selectedMalzemeler.size(); i++) {
                pstmt.setString(i + 1, selectedMalzemeler.get(i)); // '?' yerini malzeme isimleri ile doldur
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Tarifi oluştur ve listeye ekle
                Tarifler tarif = new Tarifler();
                tarif.setTarifId(rs.getInt("TarifID")); // Veritabanındaki TarifID değerini al
                tarif.setTarifAdi(rs.getString("TarifAdi")); // Tarif adını al
                tarif.setKategori(Tarifler.TYPE.valueOf(rs.getString("Kategori"))); // Kategori değerini al
                tarif.setHazirlanmaSuresi(rs.getInt("HazirlanmaSuresi")); // Hazırlanma süresini al

                // Diğer alanları da ekleyin (örneğin: malzemeler, tarifin açıklaması vs.)
                // Örnek: tarif.setAciklama(rs.getString("Aciklama"));

                filteredTarifler.add(tarif); // Tarifi listeye ekle
            }
        } catch (SQLException e) {
            // Hata durumunda daha anlamlı bir mesaj verebilirsiniz
            System.err.println("Veritabanı hatası: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Geçersiz kategori değeri: " + e.getMessage());
        }

        // Sonuçların kontrolü
        if (filteredTarifler.isEmpty()) {
            System.out.println("Seçilen malzemelerle eşleşen tarif bulunamadı.");
        }

        return filteredTarifler; // Filtrelenmiş tarifleri döndür
    }

    public float maliyetHesaplama(int tarifId) {
        float totalCost = 0; // Tarifin toplam maliyetini hesaplamak için değişken

        String sql = "SELECT tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM tarifler t " +
                "JOIN tarif_malzeme tm ON t.TarifID = tm.TarifID " +
                "JOIN malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE t.TarifID = ?";

        try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql)) {
            pstmt.setInt(1, tarifId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                float malzemeMiktar = rs.getFloat("MalzemeMiktar");
                float birimFiyat = rs.getFloat("BirimFiyat");
                totalCost += malzemeMiktar * birimFiyat;
                float rounded = Math.round(totalCost * 100.0f) / 100.0f;
                totalCost = rounded;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCost; // Hesaplanan toplam maliyeti döndür
    }





}
