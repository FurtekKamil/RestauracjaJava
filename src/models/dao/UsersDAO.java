package models.dao;

import models.entity.Users;

import java.util.List;

public class UsersDAO extends DAO<Users, Integer> {

    @Override
    public Users findById(Integer integer) {
        openCurrentSession();
        Users user = (Users) getCurrentSession().createQuery(
                "from " + Users.class.getSimpleName() +
                        " where users_id = " + integer).uniqueResult();
        closeCurrentSession();
        return user;
    }

    @Override
    public List<Users> findAll() {
        List<Users> users;

        openCurrentSession();
        users = getCurrentSession().createQuery("from " + Users.class.getSimpleName()).list();
        closeCurrentSession();

        return users;
    }

    public List<Users> findUserByRole(String role) {
        List<Users> users;

        openCurrentSession();
        users = getCurrentSession().createQuery("from "+Users.class.getSimpleName()+" where role = '"+role+"'").list();
        closeCurrentSession();

        return users;
    }

    public boolean findUserByLogin(String login){
        boolean state = false;

        openCurrentSession();

        if(getCurrentSession().createQuery("from "+Users.class.getSimpleName()+" where login = '"+login+"'").list().isEmpty()){
            state = true;
        }

        closeCurrentSession();

        return state;
    }

}
