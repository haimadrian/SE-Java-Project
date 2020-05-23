package model.dal;

import model.Customer;
import model.Manager;
import model.SystemAdmin;
import model.User;
import org.spa.common.Repository;
import org.spa.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserRepository implements Repository<User> {
    @Override
    public List<User> selectAll() {

        List<User> dummy = new ArrayList<>();
        dummy.add(new SystemAdmin("111"));
        dummy.add(new Customer("1234","054310257","01/01/2000","02/02/2020",
                "What is your favourite color ?", "red"));
        dummy.add(new Manager("4526","0523107","20/02/1994","03/05/1996",
                "What is your favourite color ?", "blue", 5005, 100));
        dummy.add(new Customer("1212","002500","01/01/2000","02/02/2020",
                "What is your favourite color ?", "dark-chocolate"));

        return dummy;
    }

    @Override
    public List<User> select(Predicate<User> filter) {
        return selectAll();
    }

    @Override
    public User add(User item) {
        return item;
    }

    @Override
    public List<User> saveAll(List<User> items) {
        return items;
    }
}
