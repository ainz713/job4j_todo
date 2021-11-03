package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Filter;
import ru.job4j.todo.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Item add(Item item) {
        return this.tx(
                session -> {
                    if (item.getId() == 0) {
                        session.save(item);
                    } else {
                        session.update(item);
                    }
                    return item;
                });
    }

    @Override
    public Collection<Item> findItemsByDone(boolean done) {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Item "
                            + "where done = :pDone")
                            .setParameter("pDone", done)
                            .list()
        );
    }

    @Override
    public Collection<Filter> findAllFilters() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Filter order by id").list()
        );
    }

    @Override
    public boolean replace(int id, Item item) {
        return this.tx(
                session -> {
                    int result = session.createQuery("update Item set description = :description,"
                            + " done = :done, "
                            + "created = :created where id = :id")
                            .setParameter("description", item.getDescription())
                            .setParameter("done", item.getDone())
                            .setParameter("created", item.getCreated())
                            .setParameter("id", id)
                            .executeUpdate();
                    return result > 0;
                }
        );
    }

    @Override
    public boolean delete(int id) {
        return this.tx(
                session -> {
                    int result = session.createQuery("delete Item where id = :id")
                            .setParameter("id", id)
                            .executeUpdate();
                    return result > 0;
                }
        );
    }

    @Override
    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery("from Item").list()
        );
    }

    @Override
    public List<Item> findByDone(boolean key) {
        return this.tx(
                session -> session.createQuery(
                        "from Item where done=:key").setParameter("key", key).list()
        );
    }

    @Override
    public Item findById(int id) throws SQLException {
        return (Item) this.tx(
                session -> session.createQuery(
                        "from Item where id=:key").setParameter("key", id).list()
        );
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
