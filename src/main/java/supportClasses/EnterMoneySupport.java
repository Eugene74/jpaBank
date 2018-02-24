package supportClasses;

import java.util.Scanner;

public class EnterMoneySupport {
    public Double getMoneySupport(Scanner sc) {
        boolean b;
        Double money = null;
        do {b=false;
            System.out.println("Enter amount of money");
            String moneyStr= sc.nextLine();
            try {
                money = Double.parseDouble(moneyStr);
            } catch (NumberFormatException ex) {
                System.out.println("Wrong!!! Please enter correct digit");
                b=true;
            }
        } while (b);
        return money;
    }
}
