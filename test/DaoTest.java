import dao.ContactDAO;
import entity.Contact;
import org.junit.Test;
import play.db.jpa.JPA;

/**
 * @Author ValentineS. Created 24.03.2015.
 */
public class DaoTest {

    @Test
    public void ContactDAOTest() {
        ContactDAO contactDAO = new ContactDAO(JPA.em());

        Contact contact = new Contact();
        contact.setFirstName("Ivan");
        contact.setLastName("Ivanov");
        contact.setMiddle_name("Ivanovich");

        contactDAO.create(contact);
    }
}
