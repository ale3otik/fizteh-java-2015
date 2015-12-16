package ru.mipt.diht.students.ale3otik.moduletests.miniorm;

import junit.framework.TestCase;
import org.junit.Test;
import ru.mipt.diht.students.ale3otik.miniorm.DatabaseService;
import ru.mipt.diht.students.ale3otik.miniorm.DatabaseService.Column;
import ru.mipt.diht.students.ale3otik.miniorm.DatabaseService.PrimaryKey;
import ru.mipt.diht.students.ale3otik.miniorm.DatabaseService.Table;

import java.util.Date;

/**
 * Created by alex on 15.12.15.
 */
public class DatabaseServiceTest extends TestCase {

    @Table
    public static class SimpleStudent {
        public SimpleStudent(String newName, int newAge, Date newBirthDate) {
            userName = newName;
            userAge = newAge;
            userDateOfBirth = newBirthDate;
            dummy = 1;
        }

        @Column
        @PrimaryKey
        public String userName;

        @Column
        public int userAge;

        @Column
        public Date userDateOfBirth;

        public int dummy;

        @Override
        public String toString() {
            return "User{"+
                    "name='" + userName + '\'' +
                    ",age=" + userAge +
                    ",birthday=" + userDateOfBirth.toString() +
                    '}';
        }
    }

    @Test
    public void testCreation() {
        try {
            DatabaseService<SimpleStudent> service = new DatabaseService<>(SimpleStudent.class);
            service.dropTable();
            service.createTable();
            service.dropTable();
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed: " + e.getMessage());
        }
    }

    @Test
    public void testOperetions() {
        try {
            DatabaseService<SimpleStudent> service = new DatabaseService<>(SimpleStudent.class);
            service.createTable();
            service.insert(new SimpleStudent("Alexey", 18, new Date(1345633237)));
            service.insert(new SimpleStudent("Semyon", 18, new Date(1345332323)));
            service.insert(new SimpleStudent("Peter", 17, new Date(13323237)));
            service.update(new SimpleStudent("Alexander", 20, new Date(144255427)));
            service.deleteById("Sergey");
            service.delete(new SimpleStudent("Other", 20, new Date(144255427)));
            service.delete(new SimpleStudent("Semyon", 20, new Date(144255427)));

            assertNull(service.queryById("Semyon"));
            assertNull(service.queryById("Sergey"));
            assertEquals("[User{name='Alexey',age=18,birthday=1970-01-16},"
                            +" User{name='Peter',age=17,birthday=1970-01-01}]",
                    service.queryForAll().toString());

            service.dropTable();
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed: " + e.getMessage());
        }
    }
}