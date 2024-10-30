package view;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import core.Database;
import dao.DetailPageDao;
import entity.Malzemeler;
import entity.Tarifler;

import javax.swing.*;

public class DetailPage extends JFrame {
    private JPanel container;
    private JTextField textField_DetayKategori;
    private JTextField textField_DetayHazirlamaSuresi;
    private JList list_DetayMalzemeler;
    private JTextField textField_DetayMaliyet;
    private JTextPane textPane_DetayTalimatlar;
    private JPanel pnl_tarifDetayTitle;
    private JTextField textField_DetayTarifAdi;
    private JLabel lbl_Image;

    public DetailPage() {
        this.add(container);
        this.setSize(600, 800);
        this.setTitle("Tarif Detay Sayfasi");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void detaySayfasiniAc(String tarifAdi) {
        DetailPageDao detailPageDao = new DetailPageDao();
        Tarifler tarifDetay = detailPageDao.findTarifByName(tarifAdi);

        ArrayList<Malzemeler> malzemelerList = detailPageDao.findMalzemelerByTarifId(tarifDetay.getTarifId());
        //System.out.println(tarifDetay.getTarifId());

        if (tarifDetay != null) {
            this.textField_DetayTarifAdi.setText(tarifDetay.getTarifAdi());
            this.textField_DetayKategori.setText(tarifDetay.getKategori().name());
            this.textField_DetayHazirlamaSuresi.setText(String.valueOf(tarifDetay.getHazirlanmaSuresi()));

            // Malzemeleri listeye dolduruyoruz
            String[] malzemeAdiArray = new String[malzemelerList.size()];
            for (int i = 0; i < malzemelerList.size(); i++) {
                malzemeAdiArray[i] = malzemelerList.get(i).getMalzemeAdi();
            }
            this.list_DetayMalzemeler.setListData(malzemeAdiArray);
            this.textField_DetayMaliyet.setText(String.valueOf(toplamMaliyetBulByTarifAdi(tarifAdi)));
            this.textPane_DetayTalimatlar.setText(tarifDetay.getTalimatlar());

        }
    }




    private float toplamMaliyetBulByTarifAdi(String tarifAdi) {
        float toplamMaliyet = 0;

        // Belirli bir tarifin malzemelerini ve maliyetlerini sorgulamak için SQL
        String sql = "SELECT tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM tarifler t " +
                "JOIN tarif_malzeme tm ON t.TarifID = tm.TarifID " +
                "JOIN malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE t.tarifadi = ?"; // Belirli bir tarif adı için

        try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql)) {
            pstmt.setString(1, tarifAdi); // Tarifi adına göre filtrele
            ResultSet rs = pstmt.executeQuery();

            // Malzeme maliyetlerini hesapla
            while (rs.next()) {
                float malzemeMiktar = rs.getFloat("MalzemeMiktar"); // Malzeme miktarını al
                float birimFiyat = rs.getFloat("BirimFiyat"); // Malzemenin birim fiyatını al

                // Maliyet hesapla ve toplam maliyete ekle
                toplamMaliyet += malzemeMiktar * birimFiyat;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        float rounded = Math.round(toplamMaliyet * 100.0f) / 100.0f;

        return rounded; // 2 ondalık basamağa yuvarlama, Belirli tarifin toplam maliyetini döndür
    }



}

