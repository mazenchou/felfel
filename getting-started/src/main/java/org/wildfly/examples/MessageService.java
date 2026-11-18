package org.wildfly.examples;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class MessageService {

    @PersistenceUnit(unitName = "minichatPU")
    private EntityManagerFactory emf;

    public void save(String sender, String content) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Message(sender, content));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Message> findRecent(int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Message> list = em.createQuery(
                    "SELECT m FROM Message m ORDER BY m.id DESC", Message.class)
                    .setMaxResults(limit)
                    .getResultList();
            Collections.reverse(list); // oldest first
            return list;
        } finally {
            em.close();
        }
    }
}