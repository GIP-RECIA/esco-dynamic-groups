/**
 * 
 */
package org.esco.dynamicgroups.dao.db;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Base class for the hibernate dao support, with facilities to handle the sessions and the transactions.
 * @author GIP RECIA - A. Deman
 * 13 janv. 2009
 *
 */
public class AbstractHibernateDAOSupport  extends HibernateDaoSupport {
    
    /** General  jocker constant. */
    protected static final String STD_JOCKER = "\\*";

    /** SQL Jocker constant. */
    protected static final String SQL_JOCKER = "%";
    
    /** From constant. */
    protected static final String FROM = "from ";

    /** Where constant. */
    protected static final String WHERE = " where ";

    /** Escape sequence. */
    protected static final String ESCAPE = "\\";
    
    /** Session for the current thread. */
    private static final ThreadLocal<Session> THREADED_SESSION = new ThreadLocal<Session>();
    
    /** Transaction for the current thread. */
    private static final ThreadLocal<Transaction> THREADED_TRANSACTION = new ThreadLocal<Transaction>();
    
    /**
     * Builds an instance of AbstractHibernateDAOSupport.
     */
    public AbstractHibernateDAOSupport() {
        super();
    }
    
    /**
     * Gives the session for the thread.
     * @return The session for the current thread.
     */
    public Session openOrRetrieveSessionForThread() {
        Session session = THREADED_SESSION.get();
        if (session == null) {
            session = getSessionFactory().openSession();
            THREADED_SESSION.set(session);
        }
        return session;
    }
    
    /**
     * Closes the session for the current thread.
     */
    public void closeSessionForThread() {
        Session session = THREADED_SESSION.get();
        THREADED_SESSION.set(null);
        THREADED_TRANSACTION.set(null);
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
    
    /**
     * Starts the transaction for the current thread.
     * @return The transaction.
     */
    public Transaction startTransaction() {
        Transaction transaction = THREADED_TRANSACTION.get();
        if (transaction == null) {
            transaction = openOrRetrieveSessionForThread().beginTransaction();
            THREADED_TRANSACTION.set(transaction);
        }
        return transaction;
    }

    /**
     * Commits the transaction.
     */
    public void commit() {
        Transaction transaction = THREADED_TRANSACTION.get();
        if (transaction != null) {
            THREADED_TRANSACTION.set(null);
            if (!(transaction.wasCommitted() || transaction.wasRolledBack())) {
                transaction.commit();
            }
        }
    }
    
    /**
     * Rollbacks the transaction.
     */
    public void rollback() {
        Transaction transaction = THREADED_TRANSACTION.get();
        if (transaction != null) {
            THREADED_TRANSACTION.set(null);
            if (!(transaction.wasCommitted() || transaction.wasRolledBack())) {
                transaction.rollback();
            }
        }
        
    }
    
    /**
     * Retrieves an unique instance on the value of an attribute.
     * The transation is started at the begining and the session is closed at the end.
     * @param instanceClass The class of the instance to retrieve.
     * @param attributeName The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @return The instance if found, null otherwise.
     */
    protected Object retrieveUniqueInstanceByAttribute(final Class< ? extends Serializable > instanceClass, 
            final String attributeName, 
            final String attributeValue) {
        final Object result = retrieveUniqueInstanceByAttributeInternal(openOrRetrieveSessionForThread(), 
                instanceClass, 
                attributeName, 
                attributeValue);
        closeSessionForThread();
        return result;
    }
    
    /**
     * Retrieves an unique instance on the value of an attribute.
     * The session and the transaction are managed outside of this method.
     * @param session The session to use.
     * @param instanceClass The class of the instance to retrieve.
     * @param attributeName The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @return The instance if found, null otherwise.
     */
    protected Object retrieveUniqueInstanceByAttributeInternal(final Session session, 
                final Class< ? extends Serializable > instanceClass, 
                final String attributeName, 
                final String attributeValue) {
        final StringBuilder queryString = new StringBuilder("from ");
        queryString.append(instanceClass.getSimpleName());
        queryString.append(" where ");
        queryString.append(attributeName);
        queryString.append("='");
        queryString.append(attributeValue);
        queryString.append("'");
        Query query = session.createQuery(queryString.toString());
        query.setReadOnly(true);
        query.setCacheable(true);
        Object result =  query.uniqueResult();
        return result;
    }
    
    /**
     * Stores an instance.
     * @param instance The instance to store.
     */
    protected void store(final Serializable instance) {
        startTransaction();
        storeInternal(openOrRetrieveSessionForThread(), instance);
        commit();
        closeSessionForThread();
    }
    
    /**
     * Stores an instance.
     * @param session The session to use.
     * @param instance The instance to store.
     */
    protected void storeInternal(final Session session, final Serializable instance) {
        session.save(instance);  
    }

    /**
     * Stores an instance.
     * @param instance The instance to store.
     */
    protected void modify(final Serializable instance) {
        startTransaction();
        modifyInternal(openOrRetrieveSessionForThread(), instance);
        commit();
        closeSessionForThread();
    }
    
    /**
     * Stores an instance.
     * @param session The session to use.
     * @param instance The instance to modify.
     */
    protected void modifyInternal(final Session session, final Serializable instance) {
        session.update(instance);  
    }
    
    /**
     * Deletes an instance.
     * @param instance The instance to delete.
     */
    protected void delete(final Serializable instance) {
        startTransaction();
        deleteInternal(openOrRetrieveSessionForThread(), instance);
        commit();
        closeSessionForThread();
    }
    
    /**
     * Deletes an instance.
     * @param session The session to use.
     * @param instance The instance to delete.
     */
    protected void deleteInternal(final Session session, final Serializable instance) {
        session.delete(instance);  
    }
}
