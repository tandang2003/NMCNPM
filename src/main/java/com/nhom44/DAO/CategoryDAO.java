package com.nhom44.DAO;

import com.nhom44.bean.Category;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Category.class)
public interface CategoryDAO {
    @SqlQuery("SELECT c.id, c.name, IFNULL(count(p.id),0) AS numberOfProject, c.updatedAt, c.status FROM Categories c LEFT JOIN Projects p ON p.categoryId = c.Id GROUP BY c.id, c.name,c .updatedAt, c.status")
    List<Category> getAll();
//    @SqlQuery("SELECT id, name FROM Categories WHERE status = 1")
//    @SqlQuery("SELECT c.id, c.name, IFNULL(count(p.id),0) AS numberOfProject, c.updatedAt, c.status " +
//            "FROM Categories c LEFT JOIN Projects p ON p.categoryId = c.Id " +
//            "JOIN histoies h ON h.postId = p.postId" +
//            "WHERE c.status = 1 GROUP BY c.id, c.name,c .updatedAt, c.status")
    @SqlQuery("Select c.id, c.name,  IFNULL(s.view,0) as catViews FROM categories c left JOIN (Select p.categoryId as id, count(p.id) as view From projects p JOIN histories h ON h.postId=p.postId AND p.status=1 ) s on c.id=s.id AND c.status=1 ORDER BY catViews DESC")
    List<Category> getAllActiveOrderByNumOfViews();
    @SqlQuery("SELECT * FROM Categories WHERE id = :id")
    Category getById(@Bind("id") int id);

    @SqlUpdate("INSERT INTO Categories(name,status) VALUES (:name,:status)")
    int add(@BindBean Category category);
    @SqlQuery("SELECT EXISTS(SELECT * FROM Categories WHERE name = :name)")
    Boolean existCategory(@Bind("name")String name);
    @SqlUpdate("UPDATE Categories SET name = :name, status = :status WHERE id = :id")
    Integer update(@BindBean Category category);
}
