package models.dao;

import models.entity.Reservation;

import java.util.ArrayList;

public class ReservationDAO extends DAO<Reservation, Integer> {
    @Override
    public ArrayList<Reservation> findAll() {
        openCurrentSession();
        ArrayList<Reservation> reservations = (ArrayList<Reservation>) getCurrentSession().createQuery("from " + Reservation.class.getSimpleName()).list();
        closeCurrentSession();
        return reservations;
    }

    @Override
    public Reservation findById(Integer integer) {
        openCurrentSession();
        Reservation reservation = (Reservation) getCurrentSession().createQuery(
                "from " + Reservation.class.getSimpleName() +
                " where reservation_id = " + integer).uniqueResult();
        closeCurrentSession();
        return reservation;
    }

    public ArrayList<String> findByDate(String date, String date2) {
        openCurrentSession();
        ArrayList<Reservation> reservations = (ArrayList<Reservation>) getCurrentSession().createQuery("from " + Reservation.class.getSimpleName() + " where date >='" + date +"' and date <'" + date2 +"'").list();
        closeCurrentSession();

        ArrayList<String> idsOfTables = new ArrayList<>();
        for (Reservation res:reservations) {
            idsOfTables.add("s"+res.getTableNumber());
        }
        return idsOfTables;
    }

    @Override
    public void save(Reservation entity) {
        super.save(entity);
    }

    @Override
    public void update(Reservation entity) {
        super.update(entity);
    }

    @Override
    public void delete(Reservation entity) {
        super.delete(entity);
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
    }
}
