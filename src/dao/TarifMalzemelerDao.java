package dao;

import core.Database;
import entity.Malzemeler;
import entity.Tarifler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class TarifMalzemelerDao {
    private Connection connection;

    public TarifMalzemelerDao() {
        this.connection = Database.getInstance();
    }

    public void addTarifMalzeme(int tarifId, int malzemeId, double malzemeMiktar) throws Exception {
        String query = "INSERT INTO tarif_malzeme (malzemeid, tarifid, malzememiktar) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, malzemeId);
            stmt.setInt(2, tarifId);
            stmt.setDouble(3, malzemeMiktar); // double türünde miktar ekle
            stmt.executeUpdate();

        }
    }




}




