package entities;

import bank.Currency;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private long account_number;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client owner ;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private List<Transaction> transactionsFrom = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private List<Transaction> transactionsTo = new ArrayList<>();

    public Account() { }
      public Account(long account_number, Currency currency, double balance) {
        this.account_number = account_number;
        this.currency = currency;
        this.balance = balance;
    }

    public Client getOwner() {

        return owner;
    }

    void setOwner(Client owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public long getAccount_number() {
        return account_number;
    }

    void setAccount_number(long account_number) {
        this.account_number = account_number;
    }

    public Currency getCurrency() {
        return currency;
    }

    void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionsFrom() {
        return Collections.unmodifiableList(transactionsFrom);
    }

    public void setTransactionsFrom(Transaction transaction) {
        transactionsFrom.add(transaction);
    }

    public List<Transaction> getTransactionsTo() {
        return Collections.unmodifiableList(transactionsTo);
    }

    public void setTransactionsTo(Transaction transaction) {
        transactionsTo.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", account_number=" + account_number +
                ", balance=" + balance +
                '}';
    }
}

