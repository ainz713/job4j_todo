package ru.job4j.todo.tomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = Model.of("320");
            Model two = Model.of("520");
            Model three = Model.of("530");
            Model four = Model.of("x5");
            Model five = Model.of("x7");
            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);

            Marka bmw = Marka.of("BMW");
            bmw.addModel(session.load(Model.class, 1));
            bmw.addModel(session.load(Model.class, 2));
            bmw.addModel(session.load(Model.class, 3));
            bmw.addModel(session.load(Model.class, 4));
            bmw.addModel(session.load(Model.class, 5));

            session.save(bmw);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
