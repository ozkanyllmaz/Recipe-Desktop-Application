package view;

import business.MalzemelerController;
import business.TariflerController;
import core.Database;
import dao.DetailPageDao;
import dao.TariflerDao;
import entity.Malzemeler;
import entity.Tarifler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpdatePage extends JFrame{
    private JPanel panel1;
    private JPanel container;
    private JTabbedPane tabbedPane_updatePage;
    private JTable tbl_updatePage;
    private Tarifler tarifler;
    private TariflerController tariflerController;
    private Malzemeler malzemeler;
    private MalzemelerController malzemelerController;
    private DefaultTableModel defaultTableModel;
    private JScrollPane scrl_updatePage;


    public UpdatePage(){

    }
    public UpdatePage(Tarifler tarifler1){

        this.tariflerController = new TariflerController();
        this.malzemelerController = new MalzemelerController();

        // defaultTableModel'i başlat
        this.defaultTableModel = new DefaultTableModel();

        this.add(container);
        this.setTitle("Tarif Güncelleme Sayfası");
        this.setSize(900,600);
        setLocationRelativeTo(null);


        this.setVisible(true);


        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
        ArrayList<Malzemeler> malzemelers = this.malzemelerController.findAll();

        // Verileri tabloya yükle
        loadRecipesAndMalzemeler(tariflers);

        // Mouse Listener
        tbl_updatePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Tarife tiklandi");
                int selectedRow = tbl_updatePage.rowAtPoint(e.getPoint());
                tbl_updatePage.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        tbl_updatePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    TarifEklePage tarifEklePage = new TarifEklePage(new Tarifler());
                    TariflerDao tariflerDao = new TariflerDao();
                    String selectedName = (tbl_updatePage.getValueAt(tbl_updatePage.getSelectedRow(),0).toString());
                    System.out.println(selectedName);
                    tarifEklePage.tarifEklemeSayfasiniAc(selectedName);
                    tarifEklePage.setEditableValues(true);
                    tarifEklePage.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            tbl_updatePage.revalidate();
                            tbl_updatePage.repaint();
                            updateTarifler();
                        }
                    });
                }
            }
        });

    }

    public void updateTarifler(){
        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
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
        this.tbl_updatePage.setModel(defaultTableModel);
        this.tbl_updatePage.getTableHeader().setReorderingAllowed(false);
        this.tbl_updatePage.getColumnModel().getColumn(0).setMaxWidth(300);
        this.tbl_updatePage.getColumnModel().getColumn(0).setMinWidth(200);
        this.tbl_updatePage.setEnabled(false);
    }
}
