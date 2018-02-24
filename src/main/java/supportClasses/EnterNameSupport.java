package supportClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EnterNameSupport {
    public Map getNameSurnameSupport(Scanner sc) {
        Map<String,String> map=new HashMap<>();
        System.out.println("Enter Client name ");
        String name= sc.nextLine();
        System.out.println("Enter Client surname ");
//todo: добавить проверки на валидность имени и если пробел
        String surname = sc.nextLine();
        map.put("name", name);
        map.put("surname", surname);
        return map;
    }
}
