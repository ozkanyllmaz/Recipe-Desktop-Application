package dao;

import core.Database;
import entity.Malzemeler;
import entity.Tarifler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailPageDao {
    private Connection connection;

    public DetailPageDao() {
        this.connection = Database.getInstance();
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



}
