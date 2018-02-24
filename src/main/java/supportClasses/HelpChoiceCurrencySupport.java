package supportClasses;

import bank.Currency;

import java.util.Scanner;

public class HelpChoiceCurrencySupport {
    public Currency helpChoiceCurrency(Scanner sc) {
        Currency currency = null;
        String curr;
        boolean b;
        do {
            b = false;
            System.out.println("Choose Currency: UAH-1, USD-2, EUR-3 (Enter 1,2 or 3 accordingly)");
            curr = sc.nextLine();
            try {
                int i = Integer.parseInt(curr);
                if (i == 1) curr = "UAH";
                if (i == 2) curr = "USD";
                if (i == 3) curr = "EUR";
                if (i < 1 || i > 3) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("make the right choice, you have just Three variant");
                b = true;
            }
        } while (b);
        if (Currency.UAH.toString().equals(curr)) {
            currency = Currency.UAH;
        }
        if (Currency.USD.toString().equals(curr)) {
            currency = Currency.USD;
        }
        if (Currency.EUR.toString().equals(curr)) {
            currency = Currency.EUR;
        }
        return currency;
    }
}
