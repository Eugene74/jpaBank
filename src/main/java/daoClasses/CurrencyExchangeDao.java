package daoClasses;

import entities.CurrencyExchange;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CurrencyExchangeDao {
    public List<CurrencyExchange> currencyExchangeDao(EntityManager manager) {
        Query query = manager.createQuery("select e from CurrencyExchange e", CurrencyExchange.class);
        return (List<CurrencyExchange>) query.getResultList();
    }
}

