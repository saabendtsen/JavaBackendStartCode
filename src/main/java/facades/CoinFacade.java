package facades;

import dtos.CoinOrderDTO;
import entities.CoinOrder;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class CoinFacade {

    private static CoinFacade facade;
    private static EntityManagerFactory emf;


    private CoinFacade(){}

    public static CoinFacade getCoinFacade(EntityManagerFactory _emf){
        if (facade ==null){
            emf = _emf;
            facade = new CoinFacade();
        }
        return facade;
    }

    private EntityManager getEntityManager() {return emf.createEntityManager();}



    public CoinOrderDTO createOrder (CoinOrderDTO dto){
        EntityManager em = getEntityManager();
        CoinOrder coinOrder = new CoinOrder(dto);
        try{
            em.getTransaction().begin();
            coinOrder.setUser(em.find(User.class,dto.getUser()));
            em.persist(coinOrder);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new CoinOrderDTO(coinOrder);

    }


    public List<CoinOrderDTO> getAllordersForUser (String userName){
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CoinOrder> query = em.createQuery("SELECT c from CoinOrder c WHERE c.user=:user",CoinOrder.class);
            query.setParameter("user",em.find(User.class,userName));
            List<CoinOrder> coinOrderList = query.getResultList();

            return CoinOrder.toDtos(coinOrderList);
        }finally {
            em.close();
        }
    }

}
