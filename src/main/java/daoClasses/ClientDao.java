package daoClasses;

import entities.Account;
import supportClasses.AccountNumbersGenerate;
import entities.Client;
import bank.Currency;
import supportClasses.RoundingOffSupport;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientDao {
    public  void addClientDao(EntityManager manager , String name, String surname){
        manager.getTransaction().begin();
        try {
            Client client = new Client(name, surname);
            manager.persist(client);
            Long[] numbers = new AccountNumbersGenerate().generateNumbers(name, surname, client.getId());
            Account accountUAH = new Account(numbers[0],Currency.UAH,0);
            client.setAccounts(accountUAH);
            Account accountUSD = new Account(numbers[1],Currency.USD,0);
            client.setAccounts(accountUSD);
            Account accountEUR = new Account(numbers[2],Currency.EUR,0);
            client.setAccounts(accountEUR);
            manager.persist(client);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback addClientDao");
            manager.getTransaction().rollback();
        }
    }
    public Client findClientDao(EntityManager manager, String name, String surname) {
        Client client;
                Query query = manager.createQuery("select c from Client c where" +
                                                            " c.name= :name and c.surname= :surname", Client.class);
                query.setParameter("name", name);
                query.setParameter("surname", surname);
        try {
            client = (Client) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("wrong name of client or customer does not exist");
            return null;
        } catch (NonUniqueResultException ex) {
            System.out.println("full coincidence of name and surname");
            //todo если всё совпало то можно предложить уточнить номер счёта
            return null;
        }
          return client;
    }
    public synchronized Map depositMoneyDao(EntityManager manager, double money, Client client, Currency currency) {
        Map<String,Object> map = new HashMap<>();
        try {
            List<Account> accountList = client.getAccounts();
            Account accountTo;
            double outgoingBalance;
            double incomingBalance;
            Date date ;
            for (Account acc : accountList) {
                if (acc.getCurrency() == currency) {
                    manager.getTransaction().begin();
                 //   System.out.println(acc.toString());
                    incomingBalance=acc.getBalance();
                    acc.setBalance(new RoundingOffSupport().roundingOffSupport( acc.getBalance() + money)); // кладём денежку при этом установим точность
                    manager.merge(acc);
                    accountTo = acc;
                    date = new Date(System.currentTimeMillis());
                    outgoingBalance = acc.getBalance();
                    map.put("accountTo", accountTo);
                    map.put("date", date);
                    map.put("outgoingBalance", outgoingBalance);
                    map.put("incomingBalance", incomingBalance);
                    manager.getTransaction().commit();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback depositMoney");
            manager.getTransaction().rollback();
        }
        return map;
    }
    public synchronized Map withdrawMoneyDao(EntityManager manager, double money, Client client, Currency currency) {
        Map<String,Object> map = new HashMap<>();
        try {
            List<Account> accountList = client.getAccounts();
            Account accountFrom;
            double outgoingBalance;
            double incomingBalance;
            Date date;
                for (Account acc:  accountList ) {
                    if(acc.getCurrency()==currency){
                        if ((acc.getBalance() - money) >= 0) {
                            manager.getTransaction().begin();// памятка: начинаю транзакцию в этом месте так как если начать раньше то может не произойти коммита(закрытия)
                            incomingBalance=acc.getBalance();
                            acc.setBalance(new RoundingOffSupport().roundingOffSupport( acc.getBalance() - money) ); // снимаем денежку
                            manager.merge(acc);
                            outgoingBalance = acc.getBalance();
                            accountFrom = acc;
                            date = new Date(System.currentTimeMillis());
                            map.put("accountFrom", accountFrom);
                            map.put("date", date);
                            map.put("outgoingBalance", outgoingBalance);
                            map.put("incomingBalance", incomingBalance);
                            manager.getTransaction().commit();
                        } else {
           System.out.println("you have "+ acc.getBalance()+" "+currency +" in your account "+ acc.getAccount_number());
           System.out.println("and you can not withdraw "+ money+" "+ currency+" from your account");
                            return null;
                        }
                    }
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback withdrawMoneyDao");
            manager.getTransaction().rollback();
        }
        return map;
    }
    public synchronized void moneyTransferDao(EntityManager manager, double money, Client clientFrom, Client clientTo, Currency currency){
        Map mapFrom;
        Map mapTo;
        mapFrom = withdrawMoneyDao(manager, money, clientFrom, currency);// сначала пытаюсь снять деньгу и если есть, то кладу кому надо
        if (mapFrom != null) {
            mapTo = depositMoneyDao(manager, money, clientTo, currency);// кладу денежку
// а теперь отражаем это в транзакциях - сначала это транзакция отправителя
                Account accountFrom= (Account) mapFrom.get("accountFrom");
                Date date= (Date) mapFrom.get("date");
                Double outgoingBalance = (Double) mapFrom.get("outgoingBalance");
                Double incomingBalance = (Double) mapFrom.get("incomingBalance");

            Account accountTo= (Account) mapTo.get("accountTo");
            Date dateTo= (Date) mapTo.get("date");
            Double outgoingBalanceTO = (Double) mapTo.get("outgoingBalance");
            Double incomingBalanceTO = (Double) mapTo.get("incomingBalance");

//внесём транзакцию в табл отправителя
new TransactionDao().transactionWithdrawMoneyDao(manager,clientFrom,clientTo,accountFrom,accountTo,
                                                                currency,date,outgoingBalance,money,incomingBalance);
//внесём транзакцию в табл получателя
new TransactionDao().transactionDepositDao(manager, clientFrom,clientTo, accountFrom, accountTo,
                                                            currency,dateTo,outgoingBalanceTO,money,incomingBalanceTO);
        }
    }
}
