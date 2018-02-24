package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    private String surname;

    @OneToMany(mappedBy ="owner", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "clientFrom", cascade = CascadeType.ALL) //связано с полем client в классе Transaction
    private List<Transaction> transactionsFrom = new ArrayList<>();

    @OneToMany(mappedBy = "clientTo", cascade = CascadeType.ALL) //связано с полем client в классе Transaction
    private List<Transaction> transactionsTO = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public List<Account> getAccounts() {
        return  Collections.unmodifiableList( accounts);
    }

    public void setAccounts(Account account) {
        account.setOwner(this);
        accounts.add(account);
    }

    public List<Transaction> getTransactionsTO() {
        return Collections.unmodifiableList(transactionsTO);
    }

    public void setTransactionsTO(Transaction transaction) {
        transaction.setClientTo(this);
        transactionsTO.add(transaction);
    }

    public List<Transaction> getTransactionsFrom() { return Collections.unmodifiableList(transactionsFrom);
    }

    public void setTransactionsFrom(Transaction transaction) {
        transaction.setClientFrom(this);
        transactionsFrom.add(transaction);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accounts=" + accounts +
                '}';
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
