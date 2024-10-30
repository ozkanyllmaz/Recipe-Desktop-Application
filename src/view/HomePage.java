package view;

import business.MalzemelerController;
import business.TariflerController;
import core.Database;
import dao.DetailPageDao;
import dao.MalzemelerDao;
import dao.TariflerDao;
import entity.Malzemeler;
import entity.Tarifler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.*;

public class HomePage extends JFrame {
    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_welcome;
    private JTabbedPane tabbedPane_Anasayfa;
    private JPanel pnl_YemekListesi;
    private JScrollPane scrl_homePage;
    private JTable tbl_homePage;
    private JPanel pnl_homePage_filter;
    private JTextField txtFld_arama;
    private JLabel lbl_searchRecipe;
    private JLabel lbl_Siralama;
    private JComboBox<String> comboBox_Siralama;
    private JComboBox<String> comboBox_Filtreleme;
    private JComboBox<String> comboBox_MalzemeyeGoreAra;
    private JButton btn_aramaYap;
    private JList list1;
    private JPanel pnl_Jlist;
    private JTextField textField6;
    private TariflerController tariflerController;
    private MalzemelerController malzemelerController;
    private DefaultTableModel defaultTableModel;
    private ArrayList<Tarifler> tariflerArrayList;
    private Malzemeler malzemeler;
    private TariflerDao tariflerDao;
    private MalzemelerDao malzemelerDao;


    public HomePage() {
        // tariflerController'ı başlat
        this.malzemelerDao = new MalzemelerDao();
        this.tariflerDao = new TariflerDao();
        this.tariflerController = new TariflerController();
        this.malzemelerController = new MalzemelerController();
        tariflerArrayList = new ArrayList<>();

        // defaultTableModel'i başlat
        this.defaultTableModel = new DefaultTableModel();

        this.add(container);
        this.setTitle("Yemek Tarifi Uygulaması");
        //this.setSize(900, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setLocationRelativeTo(null); // Ekranın ortasında açılması için
        this.setVisible(true);


        // Malzeme JList'ini oluştur
        ArrayList<String> malzemeAdlari = this.malzemelerDao.findAllMalzemeAdi(); // Malzemeleri al
        DefaultListModel<String> malzemeListModel = new DefaultListModel<>();
        for (String malzeme : malzemeAdlari) {
            malzemeListModel.addElement(malzeme); // Her bir malzemeyi modele ekle
        }

        list1.setModel(malzemeListModel);
        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list1.setVisibleRowCount(3);

        // Örnek: paneli ve listeleri başlatma
        this.pnl_Jlist = new JPanel(); // pnel_Jlist'i başlat
        this.list1 = new JList(); // list1'i başlat
        this.container = new JPanel(); // container'ı başlat
        this.list1.setMaximumSize(new Dimension(150, 50)); // Maksimum boyut
        this.list1.setVisibleRowCount(3); // Görünür satır sayısını ayarla
        this.list1.setMinimumSize(new Dimension(100, 50));
        this.pnl_Jlist.setSize(150, 100);


// Ekleme işlemini burada yapabilirsiniz
        //this.pnl_Jlist.add(new JScrollPane(this.list1)); // JList'i JScrollPane içine ekle
        //this.container.add(this.pnl_Jlist); // pnel_Jlist'i container'a ekle


        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("MENÜ");
        JMenuItem menuItem_tarifEkle = new JMenuItem("TARİF EKLE");
// added icon add
        ImageIcon icon_add = new ImageIcon("C:/intellij_myProject/YemekTarifiApp/YemekTarifiApp/src/Images/add.png"); // Resmi kaynaklardan yükle
        Image image = icon_add.getImage(); // Resmi al
        Image scaledImage = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH); // Resmi yeniden boyutlandır
        icon_add = new ImageIcon(scaledImage); // Yeniden boyutlandırılmış resmi ImageIcon olarak ayarla
        menuItem_tarifEkle.setIcon(icon_add); // Menü öğesine ikonu ekle

        JMenuItem menuItem_tarifGuncelle = new JMenuItem("TARİF GÜNCELLE");
// added icon edit
        ImageIcon icon_edit = new ImageIcon("C:/intellij_myProject/YemekTarifiApp/YemekTarifiApp/src/Images/edit.png"); // Resmi kaynaklardan yükle
        Image image_edit = icon_edit.getImage(); // Resmi al
        Image scaledImage_edit = image_edit.getScaledInstance(16, 16, Image.SCALE_SMOOTH); // Resmi yeniden boyutlandır
        icon_edit = new ImageIcon(scaledImage_edit); // Yeniden boyutlandırılmış resmi ImageIcon olarak ayarla
        menuItem_tarifGuncelle.setIcon(icon_edit); // Menü öğesine ikonu ekle


        JMenuItem menuItem_tarifSil = new JMenuItem("TARİF SİL");
// added icon delete
        ImageIcon icon_delete = new ImageIcon("C:/intellij_myProject/YemekTarifiApp/YemekTarifiApp/src/Images/delete.png"); // Resmi kaynaklardan yükle
        Image image_delete = icon_delete.getImage(); // Resmi al
        Image scaledImage_delete = image_delete.getScaledInstance(16, 16, Image.SCALE_SMOOTH); // Resmi yeniden boyutlandır
        icon_delete = new ImageIcon(scaledImage_delete); // Yeniden boyutlandırılmış resmi ImageIcon olarak ayarla
        menuItem_tarifSil.setIcon(icon_delete); // Menü öğesine ikonu ekle

        menu.add(menuItem_tarifEkle);
        menu.add(menuItem_tarifGuncelle);
        menu.add(menuItem_tarifSil);

        menuBar.add(menu);

        setJMenuBar(menuBar);

        // tarifler ve malzemeler verilerini al
        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
        ArrayList<Malzemeler> malzemelers = this.malzemelerController.findAll();

        // Verileri tabloya yükle
        loadRecipesAndMalzemeler(tariflers);

        menuItem_tarifEkle.addActionListener(e -> {
            TarifEklePage tarifEklePage = new TarifEklePage(new Tarifler());
            tarifEklePage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateTarifler();
                }
            });
        });

        menuItem_tarifGuncelle.addActionListener(e -> {
            UpdatePage updatePage = new UpdatePage(new Tarifler());
            //int selectedID = Integer.parseInt(tbl_.getValueAt(tbl_homePage.getSelectedRow(),0).toString());
            //UpdatePage updatePage1 = new UpdatePage(this.tariflerController.getByTarifID(selectedID));
            updatePage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateTarifler();
                }
            });
        });

        menuItem_tarifSil.addActionListener(e -> {
            DeletePage deletePage = new DeletePage(new Tarifler());
            deletePage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateTarifler();
                }
            });
        });


        //updateMalzemeComboBox();
        comboBoxSiralama();
        comboBoxFiltreleme();


        // Mouse Listener
        tbl_homePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Tarife tiklandi");
                int selectedRow = tbl_homePage.rowAtPoint(e.getPoint());
                tbl_homePage.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        tbl_homePage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DetailPage detailPage = new DetailPage();
                    DetailPageDao detailPageDao = new DetailPageDao();
                    String selectedName = (tbl_homePage.getValueAt(tbl_homePage.getSelectedRow(), 0).toString());
                    System.out.println(selectedName);
                    detailPage.detaySayfasiniAc(selectedName);
                }
            }
        });

        btn_aramaYap.addActionListener(e -> {
            String aramaMetni = this.txtFld_arama.getText();
            ArrayList<Tarifler> tumTarifler = this.tariflerController.findAll();
            ArrayList<Tarifler> filteredTarifler = new ArrayList<>(tumTarifler); // Başlangıçta tüm tariflerle doldur

            if (aramaMetni == null || aramaMetni.isEmpty()) {
                defaultTableModel.setRowCount(0);
                ArrayList<Tarifler> arrayList = this.tariflerController.findAll();
                loadRecipesAndMalzemeler(arrayList);
            } else {
                ArrayList<Tarifler> filteredTariflers = this.tariflerController.filter(this.txtFld_arama.getText());
                defaultTableModel.setRowCount(0);
                loadRecipesAndMalzemeler(filteredTariflers);
            }


            /*// Seçilen malzemeleri al
            ArrayList<Malzemeler> selectedMalzemeler = new ArrayList<>(list1.getSelectedValuesList()); // JList'ten seçilen öğeleri al

            // Seçilen malzemeler boş değilse filtreleme işlemi yap
            if (!selectedMalzemeler.isEmpty()) {
                ArrayList<String> selectedMalzemeAdlari = new ArrayList<>();

                for (Malzemeler malzeme : selectedMalzemeler) {
                    selectedMalzemeAdlari.add(malzeme.getMalzemeAdi()); // Malzeme adını al
                }

                ArrayList<Tarifler> filteredByMalzemeler = this.tariflerController.filterByMalzemeler(selectedMalzemeAdlari);
                filteredTarifler.retainAll(filteredByMalzemeler); // İki listeyi kesiştir
            }

            // Tabloyu güncelle
            defaultTableModel.setRowCount(0);
            loadRecipesAndMalzemeler(filteredTarifler);*/

        });


        comboBox_Siralama.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siralaVeGuncelle();
            }
        });

        comboBox_Filtreleme.addActionListener(e -> {
            filtreleVeGuncelle();
        });
    }

    public void updateTarifler() {
        ArrayList<Tarifler> tariflers = this.tariflerController.findAll();
        loadRecipesAndMalzemeler(tariflers);
    }

    private void updateMalzemeComboBox() {
        // MalzemelerDao nesnesini oluştur
        MalzemelerDao malzemelerDao = new MalzemelerDao();


        // Malzeme adlarını al
        ArrayList<String> malzemeAdlari = (ArrayList<String>) malzemelerDao.findAllMalzemeAdi();

        // ComboBox modelini oluştur
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Malzeme adlarını modele ekle
        for (String malzemeAdi : malzemeAdlari) {
            model.addElement(malzemeAdi);
        }

        // ComboBox modelini ayarla
        comboBox_MalzemeyeGoreAra.setModel(model);
        this.comboBox_MalzemeyeGoreAra.setSelectedItem(null);
    }

    private void guncelleTarifListesi(ArrayList<Tarifler> filtrelenmisTarifler) {

        if (defaultTableModel == null) {
            System.out.println("defaultTableModel boş!");
            return;
        }

        if (filtrelenmisTarifler == null || filtrelenmisTarifler.isEmpty()) {
            System.out.println("Filtrelenmiş tarif listesi boş!");
            return;
        }

        defaultTableModel.setRowCount(0);
        for (Tarifler tarif : filtrelenmisTarifler) {
            // toString() metoduyla basit bir gösterim ekleyin
            defaultTableModel.addRow(new Object[]{tarif.getTarifAdi(), tarif.getKategori(), tarif.getHazirlanmaSuresi(), tariflerController.maliyetHesaplama(tarif.getTarifId())});
        }
    }


    private void siralaVeGuncelle() {
        Tarifler tarifler = new Tarifler();
        String secim = (String) comboBox_Siralama.getSelectedItem();
        float maliyet = tariflerController.maliyetHesaplama(tarifler.getTarifId());

        ArrayList<Tarifler> siraliTarifler = this.tariflerController.findAll(); // mevcut tarifleri sıralamadım

        //System.out.println(siraliTarifler);

        Collator turkceCollator = Collator.getInstance(new Locale("tr","TR"));

        switch (secim) {
            case "A'dan Z'ye":
                siraliTarifler.sort(Comparator.comparing(Tarifler::getTarifAdi,turkceCollator));
                break;
            case "Z'den A'ya":
                siraliTarifler.sort(Comparator.comparing(Tarifler::getTarifAdi,turkceCollator).reversed());
                break;
            case "Fiyata Göre Artan":
                siraliTarifler.sort(Comparator.comparing(t -> tariflerController.maliyetHesaplama(t.getTarifId())));
                break;
            case "Fiyata Göre Azalan":
                siraliTarifler.sort(Comparator.comparingDouble((Tarifler t) ->
                        tariflerController.maliyetHesaplama(t.getTarifId())
                ).reversed());
                break;
            case "Seçiniz":
                updateTarifler();
                break;
            default:
                break; // Seçim "Seçiniz" ise hiçbir şey yapma
        }

        guncelleTarifListesi(siraliTarifler);
    }

    private void filtreleVeGuncelle() {
        String secim = (String) this.comboBox_Filtreleme.getSelectedItem();
        ArrayList<Tarifler> tumTarifler = this.tariflerController.findAll(); // tüm tarifler
        ArrayList<Tarifler> siralanmisTarifler = new ArrayList<>(); // siralanmis tarifleri buraya aticam
        for (Tarifler tarif : tumTarifler) {
            switch (secim) {
                case "Ana Yemekler":
                    if (tarif.getKategori() == Tarifler.TYPE.AnaYemek) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "Çorbalar":
                    if (tarif.getKategori() == Tarifler.TYPE.Çorba) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "Tatlılar":
                    if (tarif.getKategori() == Tarifler.TYPE.Tatli) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "İçecekler":
                    if (tarif.getKategori() == Tarifler.TYPE.İçecek) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "Salatalar":
                    if (tarif.getKategori() == Tarifler.TYPE.Salata) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "10 dakikadan kısa":
                    if (tarif.getHazirlanmaSuresi() < 10) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "10-30 dakika arası":
                    if (tarif.getHazirlanmaSuresi() > 10 && tarif.getHazirlanmaSuresi() < 30) {
                        siralanmisTarifler.add(tarif);
                    }
                    break;
                case "Seçiniz":
                    updateTarifler();
                    break;
                default:
                    break;
            }
        }
        guncelleTarifListesi(siralanmisTarifler);

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
        this.tbl_homePage.setModel(defaultTableModel);
        this.tbl_homePage.getTableHeader().setReorderingAllowed(false);
        this.tbl_homePage.getColumnModel().getColumn(0).setMaxWidth(300);
        this.tbl_homePage.getColumnModel().getColumn(0).setMinWidth(200);
        this.tbl_homePage.setEnabled(false);
    }

    public void comboBoxSiralama() {
        comboBox_Siralama.addItem("Seçiniz");
        comboBox_Siralama.addItem("A'dan Z'ye");
        comboBox_Siralama.addItem("Z'den A'ya");
        comboBox_Siralama.addItem("Fiyata Göre Artan");
        comboBox_Siralama.addItem("Fiyata Göre Azalan");
    }

    public void comboBoxFiltreleme() {
        comboBox_Filtreleme.addItem("Seçiniz");
        comboBox_Filtreleme.addItem("Ana Yemekler");
        comboBox_Filtreleme.addItem("Çorbalar");
        comboBox_Filtreleme.addItem("Tatlılar");
        comboBox_Filtreleme.addItem("İçecekler");
        comboBox_Filtreleme.addItem("Salatalar");
        comboBox_Filtreleme.addItem("10 dakikadan kısa");
        comboBox_Filtreleme.addItem("10-30 dakika arası");
    }


}
