package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Log;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class LogDao {
    private final Jdbi jdbi;

    public LogDao() {
        jdbi = JDBIConnect.get();
    }

    public int insertLog(Log log) {
        String sql = "INSERT INTO logs (account_id, level, action, resource, before_data, after_data, time) " +
                "VALUES (:accountId, :level, :action, :resource, :beforeData, :afterData, NOW())";

        return JDBIConnect.get().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("accountId", log.getAccountId())
                        .bind("level", log.getLevel())
                        .bind("action", log.getAction())
                        .bind("resource", log.getResource())
                        .bind("beforeData", log.getBeforeData())
                        .bind("afterData", log.getAfterData())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }


    public List<Log> getAllLogs() {
        String sql = "SELECT * FROM logs ORDER BY time DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Log.class)
                        .list()
        );
    }
}
