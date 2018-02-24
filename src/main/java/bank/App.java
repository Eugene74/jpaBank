package bank;

import daoClasses.AccountDao;
import daoClasses.CurrencyExchangeDao;
import entities.CurrencyExchange;
import entities.Transaction;
import supportClasses.EnterMoneySupport;
import supportClasses.HelpChoiceCurrencySupport;
import daoClasses.ClientDao;
import daoClasses.TransactionDao;
import supportClasses.EnterNameSupport;
import entities.Account;
import entities.Client;
import supportClasses.SearchClientSupport;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
private static EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("jpaBnk");
private static EntityManager manager = managerFactory.createEntityManager();
    public static void main(String[] args) {
        initCurrencyExchangeTable();
        try (Scanner sc = new Scanner(System.in)) {
                while (true) {
                System.out.println("1: Open a bank account");
                System.out.println("2: Deposit money into an account");
                System.out.println("3: Withdraw money from the account");
                System.out.println("4: Transfer of funds to the beneficiary's account");
                System.out.println("5: Exchange of own funds");
                System.out.println("6: Total amount of founds in UAH");
                System.out.println("7: Total funds in the account");
                System.out.println("8: all customer transactions on the selected account ");
                System.out.print("-> ");
                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        addClient(sc);
                        break;
                    case "2":
                        depositMoney(sc);
                        break;
                    case "3":
                        withdrawMoney(sc);
                        break;
                    case "4":
                        moneyTransfer(sc);
                        break;
                    case "5":
                        currencyExchange(sc);
                        break;
                    case "6":
                        totalAmount(sc);
                        break;
                    case "7":
                        totalFoundsInAccount(sc);
                        break;
                    case "8":
                        allTransactions(sc);
                        break;
                    default:
                        return;
                }
            }
        } finally {
            manager.close();
            managerFactory.close();
        }
    }

//предположим что регулярно происходит обновление курсов
    private static void initCurrencyExchangeTable() {
        try {
            manager.getTransaction().begin();
            manager.persist(new CurrencyExchange("UAHUSD", 0.03689));
            manager.persist(new CurrencyExchange("USDUAH", 26.8));

            manager.persist(new CurrencyExchange("UAHEUR", 0.02949));
            manager.persist(new CurrencyExchange("EURUAH", 33.4));

            manager.persist(new CurrencyExchange("EURUSD", 1.24688));
            manager.persist(new CurrencyExchange("USDEUR", 0.80257));
            manager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(" rollback addClientDao");
            manager.getTransaction().rollback();
        }

    }

    private static void addClient(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        String name;
        String surname;
        Map map = new EnterNameSupport().getNameSurnameSupport(sc);
        name = (String) map.get("name");
        surname = (String) map.get("surname");
        clientDao.addClientDao(manager, name, surname);
    }

    private static void depositMoney(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client clientTo = new SearchClientSupport().searchClientSupport(sc, clientDao, manager); //todo: при полном совпадении имени и фамилии надо спрашивать номер счёта или ещё что-то - не стал делывать пока
        Double money;

        Currency currency = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        money = new EnterMoneySupport().getMoneySupport(sc);
// Положим на счёт нужную сумму и достанем из мапы всё что нужн чтобы заполнить таблицу транзакций
        Map map = clientDao.depositMoneyDao(manager, money, clientTo, currency);

        Account accountTo = (Account) map.get("accountTo");
        Date date = (Date) map.get("date");
        Double outgoingBalance = (Double) map.get("outgoingBalance");
        Double incomingBalance = (Double) map.get("incomingBalance");
//внесём транзакцию в табл, ведь денежку положили на счёт
 new TransactionDao().transactionDepositDao(manager, null, clientTo, null, accountTo,
                                                          currency, date, outgoingBalance, money, incomingBalance);
    }

    private static void withdrawMoney(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client clientFrom = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        Double money;

        Currency currency = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        money = new EnterMoneySupport().getMoneySupport(sc);
// снимем со счёта нужную сумму и достанем из мапы всё что нужн чтобы заполнить таблицу транзакций
        Map map = clientDao.withdrawMoneyDao(manager, money, clientFrom, currency);
        if (map != null) {
            Account accountFrom = (Account) map.get("accountFrom");
            Date date = (Date) map.get("date");
            Double outgoingBalance = (Double) map.get("outgoingBalance");
            Double incomingBalance = (Double) map.get("incomingBalance");
//внесём транзакцию в табл, ведь денежку сняли со счёта
  new TransactionDao().transactionWithdrawMoneyDao(manager, clientFrom, null, accountFrom, null,
                                                            currency, date, outgoingBalance, money, incomingBalance);
        }
    }

    private static void moneyTransfer(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        System.out.println("Enter the name and surname of the sender of funds");
        Client clientFrom = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        Double money;

        System.out.println("Enter the name and surname of the recipient of funds");
        Client clientTo = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);

        Currency currency = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        money = new EnterMoneySupport().getMoneySupport(sc);
// переводим денежку
        clientDao.moneyTransferDao(manager, money, clientFrom, clientTo, currency);

    }

    private static void currencyExchange(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client client = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        Double money_for_change;
        Double money_for_receive = null;
        System.out.println("What currency do you want exchange?");
        Currency currencyForExchange = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        money_for_change = new EnterMoneySupport().getMoneySupport(sc);
        System.out.println("What currency do you want to receive?");
        Currency currencyForReceive = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        String currency_pair= currencyForExchange.toString() + currencyForReceive.toString();

        List<CurrencyExchange> currencyExchangeList = new CurrencyExchangeDao().currencyExchangeDao(manager);

        for (CurrencyExchange curex : currencyExchangeList) {
            if (curex.getCurrency_pair().equals(currency_pair)) {
                double currency_rates = curex.getCurrency_rates();
                money_for_receive = currency_rates * money_for_change;
            }
        }
        Map map = clientDao.withdrawMoneyDao(manager, money_for_change, client, currencyForExchange);
        if (map != null & money_for_receive!=null) {
            Account accountFrom = (Account) map.get("accountFrom");
            Date date = (Date) map.get("date");
            Double outgoingBalanceWithdraw = (Double) map.get("outgoingBalance");
            Double incomingBalanceWithdraw = (Double) map.get("incomingBalance");

            Map map1 = clientDao.depositMoneyDao(manager, money_for_receive, client, currencyForReceive);
            Account accountTo = (Account) map1.get("accountTo");
            Date date1 = (Date) map1.get("date");
            Double outgoingBalanceDeposit = (Double) map1.get("outgoingBalance");
            Double incomingBalanceDeposit = (Double) map1.get("incomingBalance");

//внесём транзакцию в табл,  валюта на обмен
new TransactionDao().transactionWithdrawMoneyDao(manager, client, client, accountFrom, accountTo,
        currencyForExchange, date, outgoingBalanceWithdraw, money_for_change, incomingBalanceWithdraw);
//внесём транзакцию в табл, валюта после обмена, то есть отражает движение по счёту получателя - входящий баланс меньше чем исходящий, то есть- пришли денежки
 new TransactionDao().transactionDepositDao(manager, client, client, accountFrom, accountTo,
         currencyForReceive, date1, outgoingBalanceDeposit, money_for_receive, incomingBalanceDeposit);
        }
    }

    private static void totalAmount(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client client = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        Double totalAmount;
        double accUSD = new AccountDao().getAccountDao(manager, client, Currency.USD).getBalance();
        double accEUR = new AccountDao().getAccountDao(manager, client, Currency.EUR).getBalance();
        double accUAH = new AccountDao().getAccountDao(manager, client, Currency.UAH).getBalance();
// так как у нас только две валюты то без лишних заморочек тупо переведём эти валюты в гривну
        String currency_paire1 = "USDUAH";
        String currency_paire2 = "EURUAH";
        List<CurrencyExchange> currencyExchangeList = new CurrencyExchangeDao().currencyExchangeDao(manager);
        for (CurrencyExchange curex : currencyExchangeList) {
            if (curex.getCurrency_pair().equals(currency_paire1)) {
                double currency_rates = curex.getCurrency_rates();
                accUSD = currency_rates * accUSD;
            }
            if (curex.getCurrency_pair().equals(currency_paire2)) {
                double currency_rates = curex.getCurrency_rates();
                accEUR = currency_rates * accEUR;
            }
        }
        totalAmount = accEUR + accUAH + accUSD;
        System.out.println("Total amount of founds in UAH is: " + totalAmount);
    }

    private static void totalFoundsInAccount(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client client = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        System.out.println("In what currency is it necessary to know the amount of funds?");
        Currency currency = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        Account account = new AccountDao().getAccountDao(manager, client, currency);
System.out.println(client.getName() + " " + client.getSurname() + " " + " have the account " +
        account.getAccount_number() + " and total funds in the account " + account.getBalance() + " " + currency);
    }

    private static void allTransactions(Scanner sc) {
        ClientDao clientDao = new ClientDao();
        Client client = new SearchClientSupport().searchClientSupport(sc, clientDao, manager);
        System.out.println("In what currency is it necessary to know transactions?");
        Currency currency = new HelpChoiceCurrencySupport().helpChoiceCurrency(sc);
        Account account = new AccountDao().getAccountDao(manager, client, currency);
        List<Transaction> transactionListTo = account.getTransactionsTo();
        List<Transaction> transactionListFrom = account.getTransactionsFrom();
        for (Transaction transaction : transactionListTo) {
            System.out.println(transaction.toString());
        }
        for (Transaction transaction : transactionListFrom) {
            System.out.println(transaction.toString());
        }
    }
}
