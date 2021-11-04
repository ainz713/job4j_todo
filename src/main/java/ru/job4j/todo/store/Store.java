package ru.job4j.todo.store;

import ru.job4j.todo.model.Filter;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import java.util.Collection;

public interface Store extends AutoCloseable {

    Item add(Item item);

    boolean delete(int id);

    Item update(Item item);

    User findUserByUsername(String username);

    User saveUser(User user);

    User findUserById(int id);

    Collection<Item> findAllItems(User user);

    Collection<Item> findItemsByDone(User user, boolean done);

    Collection<Filter> findAllFilters();
}

