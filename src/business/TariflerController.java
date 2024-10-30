package business;

import core.Database;
import core.Helper;
import dao.TariflerDao;
import entity.Tarifler;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TariflerController {
    private final TariflerDao tariflerDao = new TariflerDao();


    public ArrayList<Tarifler> findAll() {
        return this.tariflerDao.findAll();
    }

    public boolean save(Tarifler tarifler) {
        return this.tariflerDao.save(tarifler);
    }

    public Tarifler getByTarifID(int tarifid) {
        return this.tariflerDao.findByTarifID(tarifid);
    }

    public Integer findTarifIDbyTarifName(String tarifName) {
        return this.tariflerDao.findTarifIDbyTarifName(tarifName);
    }
    public ArrayList<Tarifler> filterByMalzemeler(ArrayList<String> selectedMalzemeler){
        return this.tariflerDao.filterByMalzemeler(selectedMalzemeler);
    }

    public boolean update(Tarifler tarifler) {
        if (this.getByTarifID(tarifler.getTarifId()) == null) {
            Helper.showMsg(tarifler.getTarifId() + " ID kayıtlı müşteri bulunamadı!");
            return false;
        }
        return this.tariflerDao.update(tarifler);
    }
    public float maliyetHesaplama(int tarifid){
        return this.tariflerDao.maliyetHesaplama(tarifid);
    }

    public boolean delete(int tarifid) {
        if (this.getByTarifID(tarifid) == null) {
            Helper.showMsg(tarifid + " ID kayıtlı müşteri bulunamadı!");
            return false;
        }
        return this.tariflerDao.delete(tarifid);
    }

    public ArrayList<Tarifler> filter(String name) {
        String query = "SELECT * FROM tarifler WHERE ";
        ArrayList<String> whereList = new ArrayList<>();
        if (name.length() > 0) {
            whereList.add("tarifadi LIKE '%" + name + "%'");
        }
        if(whereList.size() > 0){
            String whereQuery = String.join(" AND ", whereList);
            query += whereQuery;
        }
        return this.tariflerDao.query(query);
    }





}
