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

    public boolean add(Advise advise) {
        String now = Timestamp.valueOf(java.time.LocalDateTime.now()).toString();
        advise.setCreatedAt(now);
        advise.setUpdatedAt(now);
        Advise finalAdvise = advise;
//        2.1.2 lưu yêu cầu của người dùng vào CSDL(add(advise))
        //trả về số dòng thay đổi trên CSDL
        int check = conn.withExtension(AdviseDAO.class, dao -> dao.add(finalAdvise));
//      lấy ra id của yêu cầu
        advise.setId(conn.withExtension(AdviseDAO.class, dao -> dao.getIdByObject(finalAdvise)));
//        3. lưu các dịch vụ được người dùng chọn trong biểu mẫu vào CSDL(addService(adviseId, serviceId))
        for (int serviceId : advise.getServices()) {
            addService(advise.getId(), serviceId);
        }
//        trả về trạng thái thành công hay thất bại của việc lưu yêu cầu
        return check == 1;
    }


    public void addService(int adviseId, int serviceId) {
        conn.withExtension(AdviseDAO.class, dao -> dao.addService(adviseId, serviceId));
    }

    private Advise getByObject(Advise advise) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getByObject(advise));
    }

    public void addImage(int id, int imageId) {
        conn.withExtension(AdviseDAO.class, dao -> dao.addImage(id, imageId));
    }

    public boolean updateSuccessVerifyAdvise(int adviseId) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.updateSuccessVerifyAdvise(adviseId));
    }

    public List<Advise> getAll() {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getAll());
    }

    public static void main(String[] args) {
        List<Advise> advises = AdviseService.getInstance().getAll();

    }

    public Advise getById(int adviseId) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getById(adviseId));
    }

    public List<Integer> getServices(int id) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getServices(id));
    }

    public List<String> getImages(int id) {
        return conn.withExtension(AdviseDAO.class, dao -> dao.getImages(id));
    }
}
