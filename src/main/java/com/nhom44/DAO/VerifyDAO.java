package com.nhom44.DAO;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface VerifyDAO {
    @SqlUpdate("INSERT INTO verification(code, userId) VALUES (:code, :userId)")
    Integer insert(@Bind("code") String var1, @Bind("userId") int var2);

    @SqlQuery("SELECT userId FROM verification WHERE code =:code")
    Integer getUserIdByCode(@Bind("code") String var1);
    @SqlUpdate("INSERT INTO verifi_advises(code, adviseId) VALUES (:code, :adviseId)")
    Integer insertVerifyAdvises(@Bind("code") String verifyCode,@Bind("adviseId") int adviseId);
    @SqlQuery("SELECT adviseId FROM verifi_advises WHERE code =:code")
    Integer getAdvisesIdByCode(@Bind("code") String var1);

}
