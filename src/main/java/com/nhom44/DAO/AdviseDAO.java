package com.nhom44.DAO;

import com.nhom44.bean.Advise;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Advise.class)
public interface AdviseDAO {
    @SqlUpdate("INSERT INTO advises(email, categoryId, provinceId,width,height,representProjectId, createdAt, updatedAt) VALUES (:email,:categoryId, :provinceId,:width,:height,:representProjectId, :createdAt, :updatedAt)")
    Integer add(@BindBean Advise cart);

    @SqlQuery("SELECT * From advises WHERE email=:email AND categoryId=:categoryId AND provinceId=:provinceId AND width=:width AND height=:height AND representProjectId=:representProjectId")
    Advise getByObject(@BindBean Advise cart);

    @SqlUpdate("INSERT INTO advises_images(cartId, imageId) VALUES(:id, :imageId)")
    Integer addImage(@Bind("id") int id, @Bind("imageId") int imageId);

    @SqlUpdate("UPDATE advises SET isCheck=1 WHERE id=:cartId")
    Boolean updateSuccessVerifyAdvise(@Bind("cartId") int cartId);

    @SqlQuery("SELECT c.*, categories.name as category, provinces.name as province FROM advises c " +
            "JOIN categories ON c.categoryId=categories.id " +
            "JOIN provinces ON c.provinceId=provinces.id " +
            "order by c.id desc"
                )
    List<Advise> getAll();

    @SqlQuery("SELECT * FROM advises WHERE id=:cartId")
    Advise getById(@Bind("cartId") int cartId);

    @SqlUpdate("INSERT INTO advises_services(cartId, serviceId) VALUES(:check, :integer)")
    Integer addService(@Bind("check") int check, @Bind("integer") Integer integer);

    @SqlQuery("SELECT serviceId FROM advises_services WHERE cartId=:id")
    List<Integer> getServices(@Bind("id") int id);

    @SqlQuery("SELECT  concat(i.path,'/',i.name)  FROM advises_images ci join images i on ci.imageId=i.id WHERE cartId=:id")
    List<String> getImages(@Bind("id") int id);
}
