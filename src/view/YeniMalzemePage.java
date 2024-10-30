package view;

import business.MalzemelerController;
import business.YeniMalzemePageController;
import core.Helper;
import dao.MalzemelerDao;
import dao.YeniMalzemePageDao;
import entity.Malzemeler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class YeniMalzemePage extends JFrame{
    private JPanel panel1;
    private JPanel container;
    private JLabel lbl_yeniMalzemeGirişi;
    private JTextField textField_yeniMalzemeAdi;
    private JTextField textField_yeniMalzemeToplamMiktar;
    private JLabel lbl_yeniMalzemeBirim;
    private JLabel lbl_yeniMalzemeToplamMiktar;
    private JLabel lbl_yeniMalzemeAdi;
    private JComboBox<Malzemeler.TYPE> comboBox_yeniMalzemeBirim;
    private JLabel lbl_yeniMalzemeBirimFiyat;
    private JTextField textField_yeniMalzemeBirimFiyat;
    private JButton btn_yeniMalzemeEkle2;
    private Malzemeler malzemeler;
    private MalzemelerController malzemelerController;
    private YeniMalzemePage yeniMalzemePage;
    private YeniMalzemePageDao yeniMalzemePageDao;
    private YeniMalzemePageController yeniMalzemePageController;
    private MalzemelerDao malzemelerDao;
    private TarifEklePage tarifEklePage;

    public YeniMalzemePage(TarifEklePage tarifEklePage){
        this.add(container);
        this.setSize(400,400);
        this.setTitle("Yeni Malzeme Ekleme Sayfası");
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.comboBox_yeniMalzemeBirim.setModel(new DefaultComboBoxModel<>(Malzemeler.TYPE.values()));

        this.tarifEklePage = tarifEklePage;
        this.tarifEklePage = new TarifEklePage();
        this.malzemelerController = new MalzemelerController();

        btn_yeniMalzemeEkle2.addActionListener(e -> {

            String malzemeadi = textField_yeniMalzemeAdi.getText();
            String toplamMiktarStr = textField_yeniMalzemeToplamMiktar.getText();
            String birim = String.valueOf(Malzemeler.TYPE.valueOf(comboBox_yeniMalzemeBirim.getSelectedItem().toString()));
            String birimFiyatStr = textField_yeniMalzemeBirimFiyat.getText();
            if (malzemeadi.isEmpty() || toplamMiktarStr.isEmpty() || birimFiyatStr.isEmpty()){
                Helper.showMsg("fill");
                dispose();
            }

            double toplamMiktar;
            double birimFiyat;

            try {
                toplamMiktar = Double.parseDouble(toplamMiktarStr);
                birimFiyat = Double.parseDouble(birimFiyatStr);
            } catch (NumberFormatException ex) {
                Helper.showMsg("Miktar ve birim fiyatı sayısal bir değer olmalıdır!");
                return;
            }

            Malzemeler newMalzeme = new Malzemeler(birimFiyat,malzemeadi,birim,toplamMiktar);

            try{
                malzemelerController.save(newMalzeme);
                Helper.showMsg("done");

                // Yeni malzeme eklendikten sonra comboBox'ı güncelle
                if (tarifEklePage != null) {
                    tarifEklePage.loadMalzemelerToComboBox(); // ComboBox'ı güncelle
                }

                clearFields();
            }catch (Exception exception){
                exception.getMessage();
                Helper.showMsg("HATA!");
            }



        });

    }
    private void clearFields() {
        textField_yeniMalzemeAdi.setText("");
        textField_yeniMalzemeToplamMiktar.setText("");
        textField_yeniMalzemeBirimFiyat.setText("");
        comboBox_yeniMalzemeBirim.setSelectedIndex(0); // İlk elemanı seç
    }




}
