package daoClasses;

import entities.Account;
import entities.Client;
import bank.Currency;
import entities.Transaction;
import supportClasses.RoundingOffSupport;

import javax.persistence.EntityManager;
import java.util.Date;

public class TransactionDao {
    public synchronized void transactionDepositDao(EntityManager manager, Client clientFrom, Client clientTo,
         Account accountFrom, Account accountTo, Currency currency, Date date, double outgoingBalance,
                                                                             Double money, Double incomingBalance) {
         money = new RoundingOffSupport().roundingOffSupport(money);
        try {
Transaction transaction = new Transaction(accountFrom, accountTo,currency,date,outgoingBalance,money,incomingBalance);
            if (accountTo != null) {
                manager.getTransaction().begin();
                accountTo.setTransactionsTo(transaction);
                clientTo.setTransactionsTO(transaction);
                transaction.setClientFrom(clientFrom);//для установки контрагента в переводах
                manager.persist(transaction);
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback transDepositDao");
            manager.getTransaction().rollback();
        }
    }

    public synchronized void transactionWithdrawMoneyDao(EntityManager manager, Client clientFrom, Client clientTo,
Account accountFrom, Account accountTo, Currency currency, Date date, double outgoingBalance, Double money,
                                                                                           Double incomingBalance) {
        money = new RoundingOffSupport().roundingOffSupport(money);
        try {
Transaction transaction = new Transaction(accountFrom, accountTo, currency,date,outgoingBalance,money,incomingBalance);
            if (accountFrom != null) {
                manager.getTransaction().begin();
                accountFrom.setTransactionsFrom(transaction);
                clientFrom.setTransactionsFrom(transaction);
                transaction.setClientTo(clientTo);//для установки контрагента в переводах
                manager.persist(transaction);
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback WithdrawMoneyDao");
            manager.getTransaction().rollback();
        }
    }
}
