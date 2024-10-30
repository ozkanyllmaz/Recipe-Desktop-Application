package dao;

import core.Database;
import entity.Malzemeler;
import entity.Tarifler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MalzemelerDao {
    private Connection connection;

    public MalzemelerDao(){
        this.connection = Database.getInstance();
    }

    public ArrayList<Malzemeler> findAll(){
        ArrayList<Malzemeler> malzemelers = new ArrayList<>();
        try{
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM malzemeler");
            while(rs.next()){
                malzemelers.add(this.match(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return malzemelers;
    }
    public Malzemeler match(ResultSet rs) throws SQLException {
        Malzemeler malzemeler = new Malzemeler();

        malzemeler.setMalzemeId(rs.getInt("malzemeid"));
        malzemeler.setMalzemeAdi(rs.getString("malzemeadi"));
        malzemeler.setMalzemeBirim(Malzemeler.TYPE.valueOf(rs.getString("malzemebirim")));
        malzemeler.setBirimFiyat(rs.getFloat("birimfiyat"));
        malzemeler.setToplamMiktar(rs.getString("toplammiktar"));
        return malzemeler;
    }

    public List<String> getAllMalzemeToplamMiktar() {
        List<String> malzemeAdlari = new ArrayList<>();
        try {
            String query = "SELECT MalzemeAdi FROM malzemeler";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                malzemeAdlari.add(rs.getString("toplammiktar"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return malzemeAdlari;
    }


    public ArrayList<String> findAllMalzemeAdi(){
        List<String> malzemeAdlari = new ArrayList<>();
        Connection connection1 = Database.getInstance();
        String query = "SELECT DISTINCT malzemeadi FROM malzemeler ";
        try {
            PreparedStatement pstmt = connection1.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                malzemeAdlari.add(rs.getString("malzemeAdi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (ArrayList<String>) malzemeAdlari;
    }

    public Malzemeler findByMalzemeAdi(String malzemeAdi) {
        Malzemeler malzeme = null;
        String query = "SELECT * FROM malzemeler WHERE malzemeadi = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, malzemeAdi);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                malzeme = match(rs); // Bulunan malzemeyi döndür
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return malzeme; // Eğer bulunamazsa null döner
    }

    public boolean save(Malzemeler malzemeler) {
        String query = "INSERT INTO malzemeler (birimfiyat,malzemeadi,malzemebirim,toplammiktar) VALUES (?,?,?,?)";

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(query);
            pstmt.setFloat(1, malzemeler.getBirimFiyat());
            pstmt.setString(2, malzemeler.getMalzemeAdi());
            pstmt.setString(3, String.valueOf(malzemeler.getMalzemeBirim()));
            pstmt.setString(4, malzemeler.getToplamMiktar());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int findMalzemeIDbyMalzemeName(String malzemeAdi){
        int malzemeID = -1;
        String query = "SELECT malzemeid FROM malzemeler WHERE malzemeadi = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,malzemeAdi);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                malzemeID = rs.getInt("malzemeid");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return malzemeID;
    }






}
