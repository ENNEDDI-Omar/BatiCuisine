package startup.utils;

import startup.domain.entities.Client;
import startup.exceptions.ClientNotFoundException;

public class Validations {
   public static void clientValidation(Client client)
   {
       if (client.getName() == null || client.getName().trim().isEmpty())
       {
           throw new ClientNotFoundException("Client cannot be empty");
       }if (client.getAddress() == null || client.getAddress().trim().isEmpty())
       {
           throw new ClientNotFoundException("Address cannot be empty");
       }if (client.getPhone() == null || client.getPhone().trim().isEmpty())
       {
              throw new ClientNotFoundException("Phone cannot be empty");
       }if (client.getPhone().length() > 20)
       {
           throw new ClientNotFoundException("Phone cannot be longer than 20 characters");
       }
   }
}
