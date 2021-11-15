package facades;

import Webscraper.GetCurrentPrice;
import dtos.CoinOrderDTO;
import entities.CoinOrder;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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


    public CoinOrderDTO deleteOrder (Long id){
        EntityManager em = getEntityManager();

        try{
            em.getTransaction().begin();
            CoinOrder coinOrder = em.find(CoinOrder.class,id);
            em.remove(coinOrder);
            em.getTransaction().commit();
            return new CoinOrderDTO(coinOrder);
        } finally {
            em.close();
        }
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


    public List<CoinOrderDTO> getCurrentPrice (List<CoinOrderDTO> coinOrderDTOList) throws ExecutionException, InterruptedException {

        class task implements Callable<GetCurrentPrice> {
            GetCurrentPrice gcp;
            public task(GetCurrentPrice gcp) {
                this.gcp = gcp;
            }

            @Override
            public GetCurrentPrice call() throws Exception {
                gcp.doWork();
                return gcp;
            }
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        List<GetCurrentPrice> coins = new ArrayList<>();
        List<Future> futures = new ArrayList<>();
        List<GetCurrentPrice> result = new ArrayList<>();
        List<CoinOrderDTO> coinOrderDTOList1 = new ArrayList<>();


        for (CoinOrderDTO dto: coinOrderDTOList ) {
            coins.add(new GetCurrentPrice(dto));
        }

        for(GetCurrentPrice gcp : coins) {
            Callable<GetCurrentPrice> task = new task(gcp);
            Future future = executor.submit(task);
            futures.add(future);
        }

        for (Future<GetCurrentPrice> f : futures){
            result.add(f.get());
        }
        for(GetCurrentPrice gpc : result){
            coinOrderDTOList1.add(gpc.getDto());
        }

        return coinOrderDTOList1;
    }

}
