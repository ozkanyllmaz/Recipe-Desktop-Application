package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame{
    private JPanel panel1;

    public WelcomePage() {
        this.setTitle("Giriş Ekranı");
        this.setSize(500, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ana container paneli
        JPanel container = new JPanel(new BorderLayout());

        // Resim ekleme
        ImageIcon imageIcon = new ImageIcon("C:/intellij_myProject/YemekTarifiApp/YemekTarifiApp/src/Images/welcomeImage.png");
        JLabel lbl_image = new JLabel();

        // Resim ölçekleme
        Image scaledImage = imageIcon.getImage().getScaledInstance(500, 620, Image.SCALE_SMOOTH);
        lbl_image.setIcon(new ImageIcon(scaledImage));
        lbl_image.setLayout(new BorderLayout()); // Butonu üzerine eklemek için layout ayarı

        // Giriş butonu
        JButton btn_welcome = new JButton();
        btn_welcome.setOpaque(false);
        btn_welcome.setContentAreaFilled(false);

        // Buton işlevi
        btn_welcome.addActionListener(e -> {
            HomePage homePage = new HomePage();
            dispose();
        });

        // Butonu resmin altına ekle
        lbl_image.add(btn_welcome, BorderLayout.CENTER);

        // Resmi container paneline ekle
        container.add(lbl_image, BorderLayout.CENTER);

        // Ana pencereye ekle
        add(container);
        setVisible(true);
    }
}
