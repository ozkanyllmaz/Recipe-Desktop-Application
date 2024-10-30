package business;

import dao.YeniMalzemePageDao;
import entity.Malzemeler;

import java.util.ArrayList;

public class YeniMalzemePageController {
    private final YeniMalzemePageDao yeniMalzemePageDao = new YeniMalzemePageDao();

    public boolean save(Malzemeler malzemeler){
        return this.yeniMalzemePageDao.save(malzemeler);
    }

}
