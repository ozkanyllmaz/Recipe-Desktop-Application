package view;

import business.MalzemelerController;
import business.TariflerController;
import core.Database;
import core.Helper;
import entity.Malzemeler;
import entity.Tarifler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeletePage extends JFrame {
    private JPanel panel1;
    private JPanel container;
    private JLabel lbl_tarifSil;
    private JTable tbl_deletePage;
    private DefaultTableModel defaultTableModel;
    private TariflerController tariflerController;
    private MalzemelerController malzemelerController;
    private JPopupMenu popupMenu_tarifler = new JPopupMenu();
    private Tarifler tarifler;

    public DeletePage(Tarifler tarifler) {

        this.tarifler = new Tarifler();
        this.tariflerController = new TariflerController();
        this.malzemelerController = new MalzemelerController();

        // defaultTableModel'i başlat
        this.defaultTableModel = new DefaultTableModel();

        this.add(container);
        this.setTitle("Tarif Sil Ekranı");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
        ArrayList<Malzemeler> malzemelers = this.malzemelerController.findAll();

        if (tariflers.isEmpty()) {
            System.out.println("Hiç tarif bulunamadı."); // Debug
        } else {
            loadRecipesAndMalzemeler(tariflers);
        }

        // Verileri tabloya yükle
        //loadRecipesAndMalzemeler(tariflers);


        loadTariflerPopUpMenu();

    }

    /*public void selectedID(){
        int selectedID = Integer.parseInt(tbl_deletePage.getValueAt(tbl_deletePage.getSelectedRow(), 0).toString());
        if (this.tariflerController.delete(selectedID)) {
            Helper.showMsg("done");
            updateTarifler();
        } else {
            Helper.showMsg("error");
        }
    }*/
    public void loadTariflerPopUpMenu(){
        this.tbl_deletePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_deletePage.rowAtPoint(e.getPoint());
                tbl_deletePage.setRowSelectionInterval(selectedRow,selectedRow);
            }
        });

        this.popupMenu_tarifler.add("Sil").addActionListener(e -> {
            String selectedName = (tbl_deletePage.getValueAt(tbl_deletePage.getSelectedRow(), 0).toString());
            if(this.tariflerController.delete(this.tariflerController.findTarifIDbyTarifName(selectedName))){
                Helper.showMsg("done");
                tbl_deletePage.revalidate();
                tbl_deletePage.repaint();
                updateTarifler();
            } else {
                Helper.showMsg("errror");
            }
        });
        this.tbl_deletePage.setComponentPopupMenu(this.popupMenu_tarifler);
    }


    public void updateTarifler() {
        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
        this.defaultTableModel.setRowCount(0);
        loadRecipesAndMalzemeler(tariflers);
    }


    private void loadRecipesAndMalzemeler(ArrayList<Tarifler> tariflerArrayList) {

        // Tarif ve malzemeler sütunlarını ekle
        Object[] columnTarifler = {"TarifAdi", "Kategori", "HazirlanmaSuresi", "Maliyet"};
        this.defaultTableModel.setColumnIdentifiers(columnTarifler);

        // Tarifler ve malzemeleri aynı tabloda göstermek için verileri birleştir
        for (Tarifler tarifler : tariflerArrayList) {
            float toplamMaliyet = 0; // Maliyeti hesaplamak için değişken

            // İlişkili malzemeleri bul ve maliyetlerini topla
            String sql = "SELECT tm.MalzemeMiktar, m.BirimFiyat " +
                    "FROM tarifler t " +
                    "JOIN tarif_malzeme tm ON t.TarifID = tm.TarifID " +
                    "JOIN malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                    "WHERE t.TarifID = ?"; // Belirli bir tarif için

            try (PreparedStatement pstmt = Database.getInstance().prepareStatement(sql)) {
                pstmt.setInt(1, tarifler.getTarifId()); // Belirli tarifin ID'sini ayarlayın
                //System.out.println("Sorgulanan TarifID: " + tarifler.getTarifId());
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    float malzemeMiktar = rs.getFloat("MalzemeMiktar"); // Malzeme miktarını al
                    float birimFiyat = rs.getFloat("BirimFiyat"); // Birim fiyatı al
                    //System.out.println("MalzemeMiktar : " + malzemeMiktar);
                    //System.out.println("BirimFiyat : " + birimFiyat);

                    // Maliyet hesapla ve toplam maliyete ekle
                    toplamMaliyet += malzemeMiktar * birimFiyat;
                    float rounded = Math.round(toplamMaliyet * 100.0f) / 100.0f;
                    toplamMaliyet = rounded;

                }

                //System.out.println("Toplam Maliyet: " + toplamMaliyet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Object[] rowObject = {
                    tarifler.getTarifAdi(),
                    tarifler.getKategori(),
                    tarifler.getHazirlanmaSuresi(),
                    toplamMaliyet // Hesaplanan maliyet
            };
            this.defaultTableModel.addRow(rowObject);
        }

        // Tablonun modelini ayarla
        this.tbl_deletePage.setModel(defaultTableModel);
        this.tbl_deletePage.getTableHeader().setReorderingAllowed(false);
        this.tbl_deletePage.getColumnModel().getColumn(0).setMaxWidth(300);
        this.tbl_deletePage.getColumnModel().getColumn(0).setMinWidth(200);
        this.tbl_deletePage.setEnabled(false);
    }


}
