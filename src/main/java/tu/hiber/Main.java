package tu.hiber;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.hiber.ogm.Breed;
import tu.hiber.ogm.Dog;

/**
 * @author alex
 *
 */
public class Main {

    private static final transient Logger logger = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) {

        try {
            new Main().run();
        }
        catch (NotSupportedException |
               SystemException |
               IllegalStateException |
               SecurityException |
               HeuristicMixedException |
               HeuristicRollbackException |
               RollbackException e) {

            throw new RuntimeException(
                    "run() failed: " + e.getLocalizedMessage(), e);
        }
    }

    private void run() throws NotSupportedException,
                       SystemException,
                       IllegalStateException,
                       SecurityException,
                       HeuristicMixedException,
                       HeuristicRollbackException,
                       RollbackException {

        TransactionManager tManager = com.arjuna.ats.jta.TransactionManager
                .transactionManager();

        EntityManagerFactory emFactory = Persistence
                .createEntityManagerFactory("ogm-jpa-tutorial");

        // ==============
        tManager.begin();
        // ==============

        logger.info("About to store dog and breed");

        EntityManager eManager = emFactory.createEntityManager();

        Breed collie = new Breed();

        collie.setName("Collie");

        // ----------------------
        eManager.persist(collie);
        // ----------------------

        Dog dina = new Dog();

        dina.setName("Dina");
        dina.setBreed(collie);

        eManager.persist(dina);

        Long dinaId = dina.getId();

        eManager.flush();
        eManager.close();

        // ==============
        tManager.commit();
        // ==============

        logger.info("Dina-ID = " + dinaId);
    }
}
