package tu.hiber;

import static java.lang.String.format;

import java.util.concurrent.atomic.AtomicLong;

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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tu.hiber.ogm.Breed;
import tu.hiber.ogm.Dog;

/**
 * @author alex
 *
 */
//
@Configuration
//
@EnableAutoConfiguration(exclude = {
        // there is no Datasource, just Infinispan
        HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        XADataSourceAutoConfiguration.class })

//
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Bean
    public TransactionManager myTransactionManager() {

        return com.arjuna.ats.jta.TransactionManager.transactionManager();
    }

    @Bean
    public EntityManagerFactory myEntityManagerFactory() {

        return Persistence.createEntityManagerFactory("ogm-jpa-tutorial");
    }

    @Bean
    public EntityManager myEntityManager(EntityManagerFactory emFactory) {

        return emFactory.createEntityManager();
    }

    @Bean
    public AtomicLong myId() {

        return new AtomicLong(-1);
    }

    @Bean
    @Order(value = 1)
    //
    public CommandLineRunner createEntities(TransactionManager tManager,
                                            EntityManager eManager,
                                            AtomicLong id)
                                                           throws NotSupportedException,
                                                           SystemException,
                                                           IllegalStateException,
                                                           SecurityException,
                                                           HeuristicMixedException,
                                                           HeuristicRollbackException,
                                                           RollbackException {

        return (args) -> {

            // ==============
            tManager.begin();
            // ==============

            logger.info("Creating collie 'Dina'...");

            // EntityManager eManager = emFactory.createEntityManager();

            Breed collie = new Breed();
            collie.setName("Collie");

            // ----------------------
            eManager.persist(collie);
            // ----------------------

            Dog dina = new Dog();
            dina.setName("Dina");
            dina.setBreed(collie);

            // --------------------
            eManager.persist(dina);
            // --------------------

            id.set(dina.getId());

            // --------------
            eManager.flush();
            // --------------

            // ==============
            tManager.commit();
            // ==============

            logger.info("Dina-ID = " + id.get());
        };
    }

    @Bean
    @Order(value = 2)
    //
    public CommandLineRunner lookupEntities(TransactionManager tManager,
                                            EntityManager eManager,
                                            AtomicLong id)
                                                           throws NotSupportedException,
                                                           SystemException,
                                                           IllegalStateException,
                                                           SecurityException,
                                                           HeuristicMixedException,
                                                           HeuristicRollbackException,
                                                           RollbackException {

        return (args) -> {

            // ==============
            tManager.begin();
            // ==============

            logger.info(
                    "Retrieving entity of class 'Dog' with ID = " + id.get());

            Dog dina = eManager.find(Dog.class, id.get());

            logger.info(format("Found '%s' named '%s'",
                    dina.getBreed().getName(),
                    dina.getName()));

            // --------------
            eManager.flush();
            // --------------

            // ==============
            tManager.commit();
            // ==============

            logger.info("Exit with Ctrl+C");
        };
    }

    @Bean
    public DisposableBean shutDownHook(EntityManager eManager,
                                       EntityManagerFactory emFactory) {

        return () -> {

            logger.info("Executing my shutdown hook...");

            // isOpen() doesn't work

            // if (eManager.isOpen()) {
            // eManager.close();
            // }
            //
            // if (emFactory.isOpen()) {
            // emFactory.close();
            // }

            logger.info("Done!");
        };
    }

    private static final transient Logger logger = LoggerFactory
            .getLogger(Main.class);
}
