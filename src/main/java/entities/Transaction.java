package entities;

import bank.Currency;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountFromId")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountToId")
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    private Currency currency_transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Client_id_from")
// для того, чтобы в классе клиент заполнить лист с транзакциями для этого клиента, когда ему приходили деньги на любой из его счетов
    private Client clientFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Client_id_to")
    private Client clientTo;
    @Column(name = "date_transaction", nullable = false)
    private Date date_transaction;
    @Column
    private double incoming_balance;
    @Column
    private double outgoing_balance;
    @Column
    private double amount_in_account_currency;

    public Transaction() { }
    public Transaction(Account fromAccount, Account toAccount, Currency currency_transaction,
                       Date date_transaction, double outgoing_balance, Double money, Double incomingBalance) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.currency_transaction = currency_transaction;
        this.date_transaction = date_transaction;
        this.outgoing_balance = outgoing_balance;
        this.incoming_balance = incomingBalance;
        amount_in_account_currency = money;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public Currency getCurrency_transaction() {
        return currency_transaction;
    }

    void setCurrency_transaction(Currency currency_transaction) {
        this.currency_transaction = currency_transaction;
    }

    public Client getClientFrom() {
        return clientFrom;
    }

    public void setClientFrom(Client clientFrom) {
        this.clientFrom = clientFrom;
    }

    public Client getClientTo(Client clientTo) {
        return clientTo;
    }

    public void setClientTo(Client clientTo) {
        this.clientTo = clientTo;
    }

    public Date getDate_transaction() {
        return date_transaction;
    }

    void setDate_transaction(Date date_transaction) {
        this.date_transaction = date_transaction;
    }

    public double getOutgoing_balance() {
        return outgoing_balance;
    }

    void setOutgoing_balance(double sum) {
        this.outgoing_balance = sum;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", currency_transaction=" + currency_transaction +
                ", clientFrom=" + clientFrom +
                ", clientTo=" + clientTo +
                ", date_transaction=" + date_transaction +
                ", sum=" + outgoing_balance +
                '}';
    }

    public double getIncoming_balance() {
        return incoming_balance;
    }

    public void setIncoming_balance(double incoming_balance) {
        this.incoming_balance = incoming_balance;
    }

    public double getAmount_in_account_currency() {
        return amount_in_account_currency;
    }

    public void setAmount_in_account_currency(double amount_in_account_currency) {
        this.amount_in_account_currency = amount_in_account_currency;
    }
}
