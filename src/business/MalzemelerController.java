package business;

import dao.MalzemelerDao;
import entity.Malzemeler;

import java.util.ArrayList;
import java.util.List;

public class MalzemelerController {
    private final MalzemelerDao malzemelerDao = new MalzemelerDao();


    public ArrayList<Malzemeler> findAll(){
        return this.malzemelerDao.findAll();
    }

    public List<String> findAllMalzemeAdi(){
        MalzemelerDao malzemelerDao1 = new MalzemelerDao();
        return malzemelerDao1.findAllMalzemeAdi();
    }

    public Malzemeler findByMalzemeAdi(String malzemeAdi) {
        MalzemelerDao malzemelerDao = new MalzemelerDao();
        return malzemelerDao.findByMalzemeAdi(malzemeAdi);
    }

    public boolean save(Malzemeler malzemeler){
        return this.malzemelerDao.save(malzemeler);
    }




}
