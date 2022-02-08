package com.gesker;

import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Local
@Stateless
public class MyEJB {
    private static final Logger LOGGER = Logger.getLogger(MyEJB.class.getName());
    @PersistenceContext(unitName = MySingleton.WILDFLY_PERSISTENCE_UNIT)
    private EntityManager em;

    public List<MyJPAObject> getAllMyJpaObjects() {
        LOGGER.log(Level.INFO, "Get some records from Database");
        TypedQuery<MyJPAObject> qry = em.createNamedQuery("MyJPAObject.findAll", MyJPAObject.class);
        return qry.getResultList();
    }


}