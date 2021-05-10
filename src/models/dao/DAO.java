package models.dao;



import java.io.Serializable;
import java.util.List;

public abstract class DAO<T, Id extends Serializable> extends HibernateUtil {

    public void save(T entity) {
        openCurrentSessionWithTransaction();
        getCurrentSession().save(entity);
        closeCurrentSessionWithTransaction();
    }

    public void update(T entity) {
        openCurrentSessionWithTransaction();
        getCurrentSession().update(entity);
        closeCurrentSessionWithTransaction();
    }

    public abstract T findById(Id id);

    public abstract List<T> findAll();


    public void delete(T entity) {
        openCurrentSessionWithTransaction();
        getCurrentSession().delete(entity);
        closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) {
            delete(entity);
        }
    }


}
