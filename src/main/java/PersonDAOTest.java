/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author otavio
 */
public class PersonDAOTest {

    private static PersonDAO dao = new PersonDAO();

    public static void main(String[] arg) {
        insertTest();
//    listTest();
//    overrideTest();
//    removeTest();
//    overrideTest();
//    removeTest();
//    removefromRowKeyTest();
//    removeTest();
//    
    }

    public static void insertTest() {
        System.out.println("Inserindo exemplo");
        Person person = getPerson();
        person.setId(1l);
        Address address = getAddress();
        person.setAddress(address);
        dao.insert(person);
    
    }

    public static void retrieveTest() {
        Person person = dao.retrieve(1l);

    }

    public static void overrideTest() {

        Person person = getPerson();
        person.setId(1l);

    }

    public static void removefromRowKeyTest() {
    }

    public static void removeTest() {
        Person person = getPerson();
        person.setId(1l);


    }

    public static void cantRetrieve() {
        Person person = dao.retrieve(new Long(1));

    }

    public static void listTest() {
        Person person = getPerson();
        person.setId(1l);
        dao.insert(person);


    }

    private static Address getAddress() {
        Address address = new Address();
        address.setCep("40243-543");
        address.setCity("Salvaor");
        address.setState("Bahia");
        return address;

    }

    private static Person getPerson() {
        Person person = new Person();
        person.setYear(10);
        person.setName("Name Person ");
        person.setSex(Sex.MALE);

        return person;

    }
}
