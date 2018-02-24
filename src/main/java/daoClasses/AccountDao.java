package daoClasses;

import bank.Currency;
import entities.Account;
import entities.Client;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public class AccountDao {
    public Account getAccountDao(EntityManager manager, Client client, Currency currency) {
        Account account;
        long id = client.getId();
        Query query = manager.createQuery("select a from Account a where " +
                                                          "a.owner.id= :id and a.currency= :currency", Account.class);
        query.setParameter("id", id);
        query.setParameter("currency", currency);
        try {
            account = (Account) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            e.printStackTrace();
            return null;
        }
        return account;
    }
}
