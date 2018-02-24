package supportClasses;

public class AccountNumbersGenerate {
    Long number;
    public Long [] generateNumbers(String name, String surname, long id) {
// гарантируем 4-х значность числа, так как если короткое имя и фамилия (по одной букве, например)то число 3-х значное
        number = 1000 + Math.abs(name.hashCode()) + Math.abs(surname.hashCode()) + id;
        long tmp=number%10000; // отсекаем 4-е последние цифры - первая группа 4-х цифр
// System.out.println(tmp);
        long rdm= (long) (1000+Math.random()*8999);// случайное 4-х значное число  - вторая группа
// формируем номер счёта
        String uah= "100"+Long.toString(tmp)+Long.toString(rdm);
        String usd= "200"+Long.toString(tmp)+Long.toString(rdm);
        String eur= "300"+Long.toString(tmp)+Long.toString(rdm);
        Long[] numers = {Long.parseLong(uah), Long.parseLong(usd), Long.parseLong(eur)};
        return numers;
    }
}
