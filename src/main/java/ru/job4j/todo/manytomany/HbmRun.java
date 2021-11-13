package ru.job4j.todo.manytomany;

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

            Book one = Book.of("Красная шапка");
            Book two = Book.of("Морозко");
            Book three = Book.of("Сборник стихов");

            Author first = Author.of("Nikolay");
            first.getBooks().add(one);
            first.getBooks().add(three);

            Author second = Author.of("Anatoliy");
            second.getBooks().add(two);
            second.getBooks().add(three);

            session.persist(first);
            session.persist(second);

            Author author = session.load(Author.class, 2);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}