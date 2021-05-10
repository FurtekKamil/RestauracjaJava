package models.dao;


import models.entity.Orders;
import models.entity.Reservation;

import java.util.List;

public class OrderDAO extends DAO<Orders, Integer> {
    @Override
    public Orders findById(Integer integer) {
        openCurrentSession();
        Orders orders = (Orders) getCurrentSession().createQuery(
                "from " + Orders.class.getSimpleName() +
                        " where order_id = " + integer).uniqueResult();
        closeCurrentSession();
        return orders;
    }

    @Override
    public List<Orders> findAll() {
        List<Orders> orders;

        openCurrentSession();
        orders = getCurrentSession().createQuery("from " + Orders.class.getSimpleName()).list();
        closeCurrentSession();

        return orders;
    }

    public List<Orders> findAllNotFinished() {
        List<Orders> orders;

        openCurrentSession();
        orders = getCurrentSession().createQuery("from " + Orders.class.getSimpleName()+" where finish = 0").list();
        closeCurrentSession();

        return orders;
    }



    public List<Orders> findAllInProgress() {
        List<Orders> orders;

        openCurrentSession();
        orders = getCurrentSession().createQuery("from " + Orders.class.getSimpleName() + " where finish = 0").list();
        closeCurrentSession();

        return orders;
    }

    public List<Orders> findAllWithinDates(String from, String to) {
        List<Orders> orders;

        openCurrentSession();
        orders = getCurrentSession().createQuery("from " + Orders.class.getSimpleName() + " WHERE date BETWEEN '" + from + " 00:00:00" + "' AND '" + to + " 23:59:59" + "'" ).list();
        closeCurrentSession();

        return orders;
    }
}
