package models.dao;

import models.entity.Food;
import models.entity.Orders;

import java.util.List;

public class FoodDAO extends DAO<Food, Integer>{

    @Override
    public Food findById(Integer integer) {
        openCurrentSession();
        Food food = (Food) getCurrentSession().createQuery(
                "from " + Food.class.getSimpleName() +
                        " where food_id = " + integer).uniqueResult();
        closeCurrentSession();
        return food;

    }

    @Override
    public List<Food> findAll() {
        List<Food> foods;

        openCurrentSession();
        foods = getCurrentSession().createQuery("from " + Food.class.getSimpleName()).list();
        closeCurrentSession();

        return foods;
    }

    public List<Food> findByType(String type) {
        List<Food> foods;

        openCurrentSession();
        foods = getCurrentSession().createQuery("from " + Food.class.getSimpleName() + " where type ='" + type +"'").list();
        closeCurrentSession();

        return foods;
    }
}
