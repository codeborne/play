package play.db.jpa;


import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.type.Type;

import java.io.Serializable;


public class HibernateInterceptor extends EmptyInterceptor {

    public HibernateInterceptor() {

    }

    @Override
    public int[] findDirty(Object o, Serializable id, Object[] arg2, Object[] arg3, String[] arg4, Type[] arg5) {
        if (o instanceof JPABase && !((JPABase) o).willBeSaved) {
            return new int[0];
        }
        return null;
    }
    
    private static final ThreadLocal<Object> entities = new ThreadLocal<Object>();

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        entities.set(entity);
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void afterTransactionCompletion(org.hibernate.Transaction tx) {
        entities.remove();
    }
}