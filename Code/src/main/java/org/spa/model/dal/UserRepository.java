package org.spa.model.dal;

import org.spa.common.Repository;
import org.spa.common.User;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRepository implements Repository<User> {
    List<User> dummy;
    @Override
    public List<User> selectAll() {

        dummy = new ArrayList<>();
        dummy.add(new SystemAdmin("idan","1234"));
        dummy.add(new Customer("haim123","1234","054310257", new Date(),new Date(),
                "What is your favourite color ?", "red"));
        dummy.add(new Admin("Lior123", "1","0523107",new Date(),new Date(),
                "What is your favourite color ?", "blue", 5005, 100));
        dummy.add(new Customer("Java123","1212","002500",new Date(),new Date(),
                "What is your favourite color ?", "dark-chocolate"));

        return dummy;
    }

    @Override
    public User create(User user) {
       if(user instanceof Customer){
           dummy.add(new Customer(user.getUserId(),((Customer) user).getPassword(),
                   user.getPhoneNumber(),user.getBirthDay(),user.getRegistrationDate(),
                   ((Customer) user).getSecretQuestion(),((Customer) user).getSecretAnswer()));
       }
       else if(user instanceof SystemAdmin){
           dummy.add(new SystemAdmin(user.getUserId(),((SystemAdmin) user).getKey()));
       }
       return user;
    }

    @Override
    public User update(User item) {
        // TODO: update list
        return null;
    }

    @Override
    public User delete(User item) {
        // TODO: remove from list
        return null;
    }

    @Override
    public void saveAll(Iterable<User> users) {
       // TODO implement save
    }
}
