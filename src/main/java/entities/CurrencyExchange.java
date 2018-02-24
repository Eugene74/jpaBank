package entities;

import javax.persistence.*;

@Entity
@Table(name = "currencyexchange")
public class CurrencyExchange {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String currency_pair;
    @Column
    private double currency_rates;

    public CurrencyExchange() { }
    public CurrencyExchange(String currency_pair, double currency_rates) {
        this.currency_pair = currency_pair;
        this.currency_rates = currency_rates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency_pair() {
        return currency_pair;
    }

    public void setCurrency_pair(String currency_paire) {
        this.currency_pair = currency_paire;
    }

    public double getCurrency_rates() {
        return currency_rates;
    }
    public void setCurrency_rates(double currency_rates) {
        this.currency_rates = currency_rates;
    }
}
