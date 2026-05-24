package org.project.onlinebookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class MySqlContainer extends MySQLContainer<MySqlContainer> {

    private static final String DB_IMAGE = "mysql:8";

    private static MySqlContainer mySqlContainer;

    private MySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized MySqlContainer getInstance() {
        if (mySqlContainer == null) {
            mySqlContainer = new MySqlContainer();
        }
        return mySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mySqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mySqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mySqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
