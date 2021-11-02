package ru.job4j.todo.store;

import ru.job4j.todo.model.Filter;
import ru.job4j.todo.model.Item;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface Store extends AutoCloseable {
    void init();

    Item add(Item item);

    boolean replace(int id, Item item);

    boolean delete(int id);

    Collection<Item> findItemsByDone(boolean done);

    List<Item> findAll();

    Collection<Filter> findAllFilters();

    List<Item> findByDone(boolean key);

    Item findById(int id) throws SQLException;
}

