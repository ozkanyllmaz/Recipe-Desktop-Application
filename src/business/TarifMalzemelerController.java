package business;

import dao.TarifMalzemelerDao;

public class TarifMalzemelerController {
    private final TarifMalzemelerDao tarifMalzemelerDao = new TarifMalzemelerDao();

    public void addTarifMalzeme(int tarifId, int malzemeId, double malzemeMiktar) {
        try {
            tarifMalzemelerDao.addTarifMalzeme(tarifId, malzemeId, malzemeMiktar);
        } catch (Exception e) {
            System.out.println("Tarif malzeme eklenirken hata (TarifMalzemelerController): " + e.getMessage());
        }
    }
}
