package supportClasses;

import daoClasses.ClientDao;
import entities.Client;
import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Scanner;

public class SearchClientSupport {
    public Client searchClientSupport(Scanner sc, ClientDao clientDao, EntityManager manager) {
        boolean b;
        Client client;
        do {
            b = false;
            Map map = new EnterNameSupport().getNameSurnameSupport(sc);
            String name = (String) map.get("name");
            String surname = (String) map.get("surname");
            client = clientDao.findClientDao(manager, name, surname);
            if (client == null) {
                b = true;
            }
        } while (b);
        return client;
    }
}
