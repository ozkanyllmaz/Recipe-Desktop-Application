package dao;

import core.Database;
import entity.Malzemeler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class YeniMalzemePageDao {
    private Connection connection;

    public YeniMalzemePageDao(){
        this.connection = Database.getInstance();
    }


    public boolean save(Malzemeler malzemeler){
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


}


