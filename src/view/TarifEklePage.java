package view;

import business.MalzemelerController;
import business.TarifMalzemelerController;
import business.TariflerController;
import core.Helper;
import dao.DetailPageDao;
import dao.TariflerDao;
import entity.Malzemeler;
import entity.Tarifler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TarifEklePage extends JFrame {
    private JPanel mainPanel; // İçerik paneli
    private JScrollPane scrollPane; // Kaydırma çubuğu
    private JPanel JPanel_0;
    private JPanel container;
    private JTextField txtFld_tarifAdi;
    private JTextField txtFld_hazirlamaSuresi;
    private JComboBox<Tarifler.TYPE> comboBox_kategori;
    private JComboBox comboBox_malzemeler;
    private JComboBox<Malzemeler.TYPE> comboBox_birim;
    private JTextPane textPane_talimatlar;
    private JButton btn_tarifEkle;
    private JTextField textField_malzemeMiktarFirst;
    private JButton btn_malzemeEkle;
    private JList<String> malzemeList;
    private JList list_malzemeEkleFirst;
    private JButton btn_yeniMalzemeEkle;
    private DefaultListModel<String> malzemeListModel; // Liste modeli
    private JPanel JPanel_1;
    private Tarifler tarifler;
    private TariflerController tariflerController;
    private Malzemeler malzemeler;
    private MalzemelerController malzemelerController;
    private TarifMalzemelerController tarifMalzemelerController;
    private Connection connection;

    public TarifEklePage() {

    }


    public TarifEklePage(Tarifler tarifler) {

        this.tarifler = (tarifler != null) ? tarifler : new Tarifler(); // Eğer tarifler null ise yeni bir Tarifler nesnesi oluştur

        this.malzemeler = new Malzemeler();

        this.tariflerController = new TariflerController();
        this.malzemelerController = new MalzemelerController();
        this.tarifMalzemelerController = new TarifMalzemelerController();

        this.add(container);
        this.setSize(600, 800);
        this.setTitle("Tarif Ekleme Sayfasi");
        this.setLocationRelativeTo(null);

        // JScrollPane'i oluşturun ve ana paneli ekleyin
        scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Dikey kaydırma çubuğunu gerektiğinde göster

        // JScrollPane'i frame'e ekleyin
        add(scrollPane);

        this.setVisible(true);

        this.comboBox_kategori.setModel(new DefaultComboBoxModel<>(Tarifler.TYPE.values()));
        this.comboBox_birim.setModel(new DefaultComboBoxModel<>(Malzemeler.TYPE.values()));

        /*if(this.tarifler.getTarifId() != 0){
            this.setTitle("Tarif Düzenleme Ekranı");

        }*/

        System.out.println(this.tarifler);

        loadMalzemelerToComboBox();


        malzemeListModel = new DefaultListModel<>(); // Liste modeli
        malzemeList = new JList<>(malzemeListModel); // JList'i oluştur ve modele bağla

        list_malzemeEkleFirst.setModel(malzemeListModel);


        // Malzemeleri JList'e eklemek için iyileştirme
        this.btn_malzemeEkle.addActionListener(e -> {
            String malzemeAdi = (String) this.comboBox_malzemeler.getSelectedItem();
            String miktar = this.textField_malzemeMiktarFirst.getText();
            String birim = String.valueOf(Malzemeler.TYPE.valueOf(this.comboBox_birim.getSelectedItem().toString()));

            if (malzemeAdi != null && !miktar.isEmpty()) {
                // Eklenen malzemenin tekrarını önlemek için kontrol
                if (!malzemeListModel.contains(malzemeAdi)) {
                    malzemeListModel.addElement(malzemeAdi + " - " + birim + " - " + miktar);
                    this.textField_malzemeMiktarFirst.setText("");
                } else {
                    Helper.showMsg("Bu malzeme zaten eklenmiş.");
                }
            } else {
                Helper.showMsg("Malzeme veya miktar boş olamaz.");
            }
        });


        this.btn_tarifEkle.addActionListener(e -> {
            JTextField[] checkList = {
                    this.txtFld_tarifAdi,
                    this.txtFld_hazirlamaSuresi,
            };
            if (Helper.isFieldListEmpty(checkList)) {
                Helper.showMsg("fill");
            } else {
                boolean result = false;

                // Tarif bilgilerini ayarla
                this.tarifler.setTarifAdi(this.txtFld_tarifAdi.getText());
                this.tarifler.setKategori((Tarifler.TYPE) this.comboBox_kategori.getSelectedItem());
                this.tarifler.setHazirlanmaSuresi(Integer.parseInt(this.txtFld_hazirlamaSuresi.getText()));
                this.tarifler.setTalimatlar(this.textPane_talimatlar.getText());

                if (this.tarifler.getTarifId() == 0) {
                    // Yeni tarif ekleme
                    result = this.tariflerController.save(this.tarifler);
                } else {
                    // Güncelleme işlemi
                    result = this.tariflerController.update(this.tarifler);
                }

                if (result) {
                    int tarifId = this.tarifler.getTarifId(); // Güncellenen veya eklenen tarifin ID'sini al
                    System.out.println("Tarif işlemi başarılı: " + result);

                    // Malzeme listesindeki tüm malzemeleri işle
                    for (int i = 0; i < malzemeListModel.getSize(); i++) {
                        String malzemeDetay = malzemeListModel.getElementAt(i);
                        String[] detaylar = malzemeDetay.split(" - ");
                        String malzemeAdi = detaylar[0];
                        String malzemeBirim = detaylar[1];
                        String miktarString = detaylar[2];

                        double miktar = Double.parseDouble(miktarString);

                        // Malzemeyi veritabanında kontrol et
                        Malzemeler existingMalzeme = this.malzemelerController.findByMalzemeAdi(malzemeAdi);

                        if (existingMalzeme == null) {
                            // Malzeme yoksa ekle
                            Malzemeler newMalzeme = new Malzemeler(malzemeAdi, String.valueOf(miktar), Malzemeler.TYPE.valueOf(malzemeBirim));
                            this.malzemelerController.save(newMalzeme);
                            existingMalzeme = this.malzemelerController.findByMalzemeAdi(malzemeAdi);
                        }

                        if (existingMalzeme != null) {
                            // Malzeme mevcutsa tarif malzeme ilişkisi kur
                            tarifMalzemelerController.addTarifMalzeme(tarifId, existingMalzeme.getMalzemeId(), miktar);
                        }
                    }

                    Helper.showMsg("done");
                    dispose();
                } else {
                    Helper.showMsg("Tarif işlemi başarısız.");
                }
            }
        });


        btn_yeniMalzemeEkle.addActionListener(e -> {
            YeniMalzemePage yeniMalzemePage = new YeniMalzemePage(this);
            yeniMalzemePage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadMalzemelerToComboBox();
                }
            });
        });
    }

    public void tarifEklemeSayfasiniAc(String tarifadi) {

        TariflerDao tariflerDao = new TariflerDao();
        Tarifler tarifDetay = tariflerDao.findTarifByName(tarifadi);

        ArrayList<Malzemeler> malzemelerList = tariflerDao.findMalzemelerByTarifId(tarifDetay.getTarifId());
        //ArrayList<Tarifler> tariflerList = tariflerDao.findByTarifID(tarifDetay.getTarifId());

        this.txtFld_tarifAdi.setText(tarifDetay.getTarifAdi());
        this.comboBox_kategori.getModel().setSelectedItem(tarifDetay.getKategori());
        this.txtFld_hazirlamaSuresi.setText(String.valueOf(tarifDetay.getHazirlanmaSuresi()));
        this.comboBox_malzemeler.getModel().setSelectedItem(malzemeler.getMalzemeAdi());
        this.comboBox_birim.getModel().setSelectedItem(malzemeler.getMalzemeBirim());
        this.textField_malzemeMiktarFirst.setText(malzemeler.getToplamMiktar());
        this.textPane_talimatlar.setText(tarifDetay.getTalimatlar());

        malzemeListModel.clear();
        for (Malzemeler malzeme : malzemelerList) {
            malzemeListModel.addElement(malzeme.getMalzemeAdi() + " - " + malzeme.getMalzemeBirim() + " - " + malzeme.getToplamMiktar());
        }

    }

    public void setEditableValues(boolean isEditable) {
        this.txtFld_tarifAdi.setEditable(true);
        this.comboBox_kategori.setEditable(true);
        this.txtFld_hazirlamaSuresi.setEditable(true);
        this.comboBox_malzemeler.setEditable(true);
        this.comboBox_birim.setEditable(true);
        this.textField_malzemeMiktarFirst.setEditable(true);
        this.textPane_talimatlar.setEditable(true);
    }


    public void loadMalzemelerToComboBox() {
        // Malzemeleri veritabanından çekme
        List<String> malzemelerList = this.malzemelerController.findAllMalzemeAdi();

        // ComboBox için model oluşturma
        DefaultComboBoxModel<String> malzemeModel = new DefaultComboBoxModel<>();

        // Her malzemeyi modele ekleme
        for (String malzemeAdlari : malzemelerList) {
            malzemeModel.addElement(malzemeAdlari);
        }

        // ComboBox'a model yükleme
        this.comboBox_malzemeler.setModel(malzemeModel);
    }


}