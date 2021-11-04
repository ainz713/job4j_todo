package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Filter;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

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
    public Collection<Filter> findAllFilters() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Filter order by id").list()
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
    public Item update(Item item) {
        return this.tx(session -> {
            session.update(item);
                    return item;
                }
        );
    }

    @Override
    public Collection<Item> findAllItems(User user) {
        return this.tx(
                session -> session.createQuery("from Item where user = :pUser order by created")
                        .setParameter("pUser", user).list()
        );
    }

    @Override
    public List<Item> findItemsByDone(User user, boolean key) {
        return this.tx(
                session -> session.createQuery(
                        "from Item where done=:key AND user = :pUser"
                                + " order by created")
                        .setParameter("key", key)
                        .setParameter("pUser", user)
                        .list()
        );
    }

    @Override
    public User findUserByUsername(String username) {
        Object result = tx(session -> session.createQuery("FROM ru.job4j.todo.model.User WHERE username = :pUsername")
                    .setParameter("pUsername", username)
                    .setMaxResults(1)
                    .uniqueResult());
        return result == null ? null : (User) result;
    }

    @Override
    public User findUserById(int id) {
        Object result = null;
        try {
            result = tx(session -> session.createQuery(
                        "from User where id=:pId").setParameter("pId", id)
                    .setMaxResults(1)
                    .uniqueResult());
        } catch (Exception e) {

        }
        return result == null ? null : (User) result;
    }

    @Override
    public User saveUser(User user) {
        return this.tx(
                session -> {
                    if (user.getId() == 0) {
                        session.save(user);
                    } else {
                        session.update(user);
                    }
                    return user;
                });
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
