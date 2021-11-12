package facades;

import dtos.CoinOrderDTO;
import entities.CoinOrder;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinFacadeTest {

    private static EntityManagerFactory emf;
    private static CoinFacade facade;
    private static User user;

    public CoinFacadeTest(){}



    @BeforeAll
    public static void setUpClass(){
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CoinFacade.getCoinFacade(emf);



        user = new User("testUser", "user1");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createQuery("delete from CoinOrder").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        em.createQuery("delete from Role").executeUpdate();

        Role userRole = new Role("user");
        user.addRole(userRole);
        em.persist(userRole);
        em.persist(user);
        em.getTransaction().commit();


    }


    @Test
    void createOrder() {

        CoinOrderDTO dto = new CoinOrderDTO("BTC",4,30000,user.getUsername());
        CoinOrderDTO newdot = facade.createOrder(dto);

        assertEquals(dto.getAmount(),newdot.getAmount());

    }

    @Test
    void getAllordersForUser() {

        CoinOrderDTO dto = new CoinOrderDTO("ETH",4,30000,user.getUsername());
        facade.createOrder(dto);
        CoinOrderDTO dto2 = new CoinOrderDTO("LCR",4,30000,user.getUsername());
        facade.createOrder(dto2);

        List<CoinOrderDTO> coinOrderDTOList = facade.getAllordersForUser(user.getUsername());
        System.out.println(coinOrderDTOList.size());
        assertEquals(coinOrderDTOList.size(),2);
    }
}