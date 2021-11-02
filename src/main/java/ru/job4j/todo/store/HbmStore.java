package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Filter;
import ru.job4j.todo.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HbmStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public void init() {
    }

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        if (item.getId() == 0) {
            session.save(item);
        } else {
            session.update(item);
        }
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public Collection<Item> findItemsByDone(boolean done) {
        List<Item> list = new ArrayList<>();
        Session session = sf.openSession();
        session.beginTransaction();
        list = session.createQuery("from ru.job4j.todo.model.Item "
                + "where done = :pDone")
                .setParameter("pDone", done)
                .list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public Collection<Filter> findAllFilters() {
        List<Filter> list = new ArrayList<>();
        Session session = sf.openSession();
        session.beginTransaction();
        list = session.createQuery("from ru.job4j.todo.model.Filter order by id").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public boolean replace(int id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
        int result = session.createQuery("update Item set description = :description,"
                + " done = :done, "
                + "created = :created where id = :id")
                .setParameter("description", item.getDescription())
                .setParameter("done", item.getDone())
                .setParameter("created", item.getCreated())
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }

    @Override
    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("delete Item where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findByDone(boolean key) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery(
                "from Item where done=:key").setParameter("key", key).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findById(int id) throws SQLException {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
