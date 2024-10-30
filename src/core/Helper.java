package core;

import javax.swing.*;

public class Helper {
    public static void setTheme(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                         InstantiationException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static void optionPaneDialogTR(){
        UIManager.put("OptionPane.okButtonText","Tamam");
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }
    public static boolean isFieldListEmpty(JTextField[] fields){
        for(JTextField field : fields){
            if(isFieldEmpty(field)){
                return true;
            }
        }
        return false;
    }



    public static void showMsg(String message){
        String msg;
        String title;

        optionPaneDialogTR();
        switch(message){
            case "fill":
                msg = "Lütfen tüm alanları doldurun";
                title = "HATA!";
                break;
            case "done":
                msg = "İşlem başarılı!";
                title = "Sonuç";
                break;
            case "error":
                msg = "HATA!";
                title = "Bir hata oluştu!";
                break;
            case "Tarif eklenemedi!":
                title = "Tarifid = 0";
            default:
                msg = message;
                title = "Mesaj";

        }
        JOptionPane.showMessageDialog(null,msg, title,JOptionPane.INFORMATION_MESSAGE);


    }

}
