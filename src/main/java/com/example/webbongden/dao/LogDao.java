package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Log;
import org.jdbi.v3.core.Jdbi;

public class LogDao {
    private final Jdbi jdbi;

    public LogDao() {
        jdbi = JDBIConnect.get();
    }

    public void insertLog(Log log) {
        String sql = "INSERT INTO logs (account_id, level, action, resource, before_data, after_data) " +
                "VALUES (:accountId, :level, :action, :resource, :beforeData, :afterData)";

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("accountId", log.getAccountId())
                        .bind("level", log.getLevel())
                        .bind("action", log.getAction())
                        .bind("resource", log.getResource())
                        .bind("beforeData", log.getBeforeData())
                        .bind("afterData", log.getAfterData())
                        .execute()
        );
    }
}
