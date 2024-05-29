package com.nhom44.services;

import com.nhom44.DAO.ProvinceDAO;
import com.nhom44.DAO.UserDAO;
import com.nhom44.bean.User;
import com.nhom44.db.JDBIConnector;
import com.nhom44.util.StringUtil;
import org.jdbi.v3.core.Jdbi;

import java.sql.Date;
import java.util.List;


public class UserService {
    private static UserService instance;
    private Jdbi conn;

    private UserService() {
        conn = JDBIConnector.get();
    }

    public static UserService getInstance() {
        return instance != null ? instance : (instance = new UserService());
    }

    public List<User> getAllUser() {
        return conn.withExtension(UserDAO.class, dao -> dao.getAllUser());
    }

    public int getIdUserWithEmail(String email) {
        return conn.withExtension(UserDAO.class, dao -> dao.getIdUserWithEmail(email));

    }

    public boolean isContainEmail(String email) {
        return conn.withExtension(UserDAO.class, dao -> dao.NumOfSameEmailContain(email)) == 1;
    }


    public User additional(String email, String password, String name, Date birthday, String phone, String province, String isMale, String status, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(StringUtil.hash(password));
        user.setFullName(name);
        user.setBirthday(new Date(birthday.getTime()));
        user.setPhone(phone);
        user.setProvince(province);
        user.setGender(isMale != null ? 1 : 0);
        user.setStatus(Integer.parseInt(status));
        user.setRole(Integer.parseInt(role));
        String idProvince = conn.withExtension(ProvinceDAO.class, handle -> handle.getSpecificId(user.getProvince()));
        int line = Integer.MIN_VALUE;
        line = conn.withExtension(UserDAO.class, handle -> handle.insertUser(user.getFullName(),
                user.getEmail(), user.getPassword(),
                user.getRole(), user.getPhone(), Integer.parseInt(idProvince),
                user.getGender(), (Date) user.getBirthday(), user.getStatus()));
        if (line == 1) {
            user.setPassword(null);
            return user;
        }
        return user;
    }

    private int updateProvinceId(int id, String email) {
        return conn.withExtension(UserDAO.class, dao -> dao.updateProvinceForUser(id, email));
    }
public User update(User user){
    System.out.println("userupdated"+user.toString());
       int check= conn.withExtension(UserDAO.class,dao->dao.updateUser(user));
       return check==1?conn.withExtension(UserDAO.class,dao->dao.login(user.getEmail(), user.getPassword())):null;
}
    public int update(String oldEmail, String email, String password, String name, Date birthday, String phone, int province, String isMale, String status, String role) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(StringUtil.hash(password));
            user.setFullName(name);
            user.setBirthday(new Date(birthday.getTime()));
            user.setPhone(phone);
            user.setGender(isMale != null ? 1 : 0);
            user.setStatus(Integer.parseInt(status));
            user.setRole(Integer.parseInt(role));
            user.setProvinceId(province);
            System.out.println("user "+user.toString());
            int checkUpdateOther = conn.withExtension(UserDAO.class, dao -> dao.updateUser(user, oldEmail));
            System.out.println("checkUpdateOther "+checkUpdateOther);
            return checkUpdateOther;
    }

    public User getUserOwnerOfProject(int projectId) {
        return conn.withExtension(UserDAO.class, dao -> dao.getUserOwnerOfProject(projectId));
    }


    public User getUserByEmail(String email) {
        return conn.withExtension(UserDAO.class, dao -> dao.getUserByEmail(email));
    }

    public List<String> getEmailOwner() {
        return conn.withExtension(UserDAO.class, dao -> dao.getEmailOwner());
    }


    public User login(String email, String password) {
        return conn.withExtension(UserDAO.class, dao -> {
            System.out.println(email);
            String hash=StringUtil.hash(password).trim();
            System.out.println("hash "+hash);
            return dao.login(email, hash);
        });
    }

    public boolean updateSuccessVerify(int id) {
        return conn.withExtension(UserDAO.class, dao -> dao.updateSuccessVerify(id));
    }

    public User getUserByEmailForCustomer(String email) {
        return conn.withExtension(UserDAO.class, dao -> dao.getUserByEmailForCustomer(email));
    }
    public static void main(String[] args) {
        System.out.println(StringUtil.hash("e79c5ca3-"));
        System.out.println(getInstance().login("tandanmin@gmail.com","5b240e80-"));
    }

    public void GoogleAdditional(User user) {
        user.setPassword(StringUtil.hash(user.getPassword()));
        conn.withExtension(UserDAO.class, dao -> dao.insertGoogleUser(user));
    }
}
