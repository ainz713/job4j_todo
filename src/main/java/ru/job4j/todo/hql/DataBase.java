package ru.job4j.todo.hql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "database")
public class DataBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacancy> vc = new ArrayList<>();

    public static DataBase of(String name) {
        DataBase db = new DataBase();
        db.name = name;
        return db;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vacancy> getVc() {
        return vc;
    }

    public void setVc(List<Vacancy> vc) {
        this.vc = vc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataBase dataBase = (DataBase) o;
        return id == dataBase.id && Objects.equals(name, dataBase.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "DataBase{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
