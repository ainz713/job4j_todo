package ru.job4j.todo.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        Candidate rsl = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate one = Candidate.of("Alex", "middle", 200000);
            Candidate two = Candidate.of("Nikolay", "junior", 150000);
            Candidate three = Candidate.of("Nikita", "senior", 350000);

            session.save(one);
            session.save(two);
            session.save(three);

            /*
            Query query = session.createQuery("from Candidate s where s.id = :fId");
            query.setParameter("fId", 1);
            System.out.println(query.uniqueResult());

            Query query1 = session.createQuery("from Candidate s where s.name = :fNa");
            query1.setParameter("fNa", "Nikolay");
            for (Object st : query1.list()) {
                System.out.println(st);
            }

            Query query2 = session.createQuery(
                    "update Candidate s set s.name = :newName, s.experience = :newExp where s.id = :fId2"
            );
            query2.setParameter("newName", "AlexTheGreat");
            query2.setParameter("newExp", "senior");
            query2.setParameter("fId2", 1);
            query2.executeUpdate();

            session.createQuery("delete from Candidate where id = :fId3")
            .setParameter("fId3", 3)
            .executeUpdate();
            */

            rsl = session.createQuery(
                    "select distinct st from Candidate st "
                            + "join fetch st.db a "
                            + "join fetch a.vc b "
                            + "where st.id = :sId", Candidate.class
            ).setParameter("sId", 1).uniqueResult();

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        System.out.println(rsl);
    }
}
