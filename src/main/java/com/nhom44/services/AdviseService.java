package com.nhom44.services;

import com.nhom44.DAO.AdviseDAO;
import com.nhom44.bean.Advise;
import com.nhom44.db.JDBIConnector;
import org.jdbi.v3.core.Jdbi;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class AdviseService implements Serializable {
    private static AdviseService instance;
    private Jdbi conn;

    private AdviseService() {
        conn = JDBIConnector.get();
    }

    public static AdviseService getInstance() {
        return instance != null ? instance : (instance = new AdviseService());
    }

    public Advise add(Advise cart) {
        String now = Timestamp.valueOf(java.time.LocalDateTime.now()).toString();
        cart.setCreatedAt(now);
        cart.setUpdatedAt(now);
        int check = conn.withExtension(AdviseDAO.class, dao -> dao.add(cart));
        cart.setId(check == 1 ? getByObject(cart).getId() : 0);
        return cart;
    }


    public void addService(int cartId, int serviceId) {
        conn.withExtension(AdviseDAO.class, dao -> dao.addService(cartId,serviceId));
    }

    private Advise getByObject(Advise cart) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getByObject(cart));
    }

    public void addImage(int id, int imageId) {
        conn.withExtension(AdviseDAO.class, dao -> dao.addImage(id, imageId));
    }

    public boolean updateSuccessVerifyAdvise(int cartId) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.updateSuccessVerifyAdvise(cartId));
    }

    public List<Advise> getAll() {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getAll());
    }

    public static void main(String[] args) {
        List<Advise> carts = AdviseService.getInstance().getAll();

    }

    public Advise getById(int cartId) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getById(cartId));
    }

    public List<Integer> getServices(int id) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getServices(id));
    }

    public List<String> getImages(int id) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getImages(id));
    }
}
