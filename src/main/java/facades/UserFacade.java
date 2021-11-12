package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDTO createUser (UserDTO userDTO){
        EntityManager em = emf.createEntityManager();
        User user = new User(userDTO);
        Role userRole = new Role("user");
        user.addRole(userRole);

        try{
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new UserDTO(user);
    }


    public UserDTO deleteUser (String userName){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            User user = em.find(User.class,userName);
            em.remove(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        }finally {
            em.close();
        }
    }


    public void setupUsers(){
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords

        User user = new User("user", "user1");
        User admin = new User("admin", "admin1");
        User both = new User("user_admin", "user_admin1");

        if(admin.getPassword().equals("test")||user.getPassword().equals("test")||both.getPassword().equals("test"))
            throw new UnsupportedOperationException("You have not changed the passwords");

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        user.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);
        em.getTransaction().commit();
        System.out.println("PW: " + user.getPassword());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");
    }

}
