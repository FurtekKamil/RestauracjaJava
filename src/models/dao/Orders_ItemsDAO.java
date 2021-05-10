package models.dao;

import models.entity.Food;
import models.entity.Orders_Items;

import java.util.List;

public class Orders_ItemsDAO extends DAO<Orders_Items, Integer> {

    @Override
    public Orders_Items findById(Integer integer) {
        return null;
    }

    @Override
    public List<Orders_Items> findAll() {
        List<Orders_Items> orders_items;

        openCurrentSession();
        orders_items = getCurrentSession().createQuery("from " + Orders_Items.class.getSimpleName()).list();
        closeCurrentSession();
        return orders_items;
    }



}

