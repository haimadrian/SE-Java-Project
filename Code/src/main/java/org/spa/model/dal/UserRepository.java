package org.spa.model.dal;

import org.spa.model.user.Customer;
import org.spa.model.user.Admin;
import org.spa.model.user.SystemAdmin;
import org.spa.common.User;
import org.spa.common.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class UserRepository implements Repository<User> {
    @Override
    public List<User> selectAll() {

        List<User> dummy = new ArrayList<>();
        dummy.add(new SystemAdmin("idan","1234"));
        dummy.add(new Customer("haim123","1234","054310257", new Date(),new Date(),
                "What is your favourite color ?", "red"));
        dummy.add(new Admin("Lior123", "4526","0523107",new Date(),new Date(),
                "What is your favourite color ?", "blue", 5005, 100));
        dummy.add(new Customer("Java123","1212","002500",new Date(),new Date(),
                "What is your favourite color ?", "dark-chocolate"));

        return dummy;
    }

    @Override
    public List<User> select(Predicate<User> filter) {
        return selectAll();
    }

    @Override
    public User add(User user) {
        return user;
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return users;
    }
}
