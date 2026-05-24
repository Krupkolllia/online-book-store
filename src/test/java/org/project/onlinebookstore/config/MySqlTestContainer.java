package org.project.onlinebookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class MySqlTestContainer extends MySQLContainer<MySqlTestContainer> {

    private static final String DB_IMAGE = "mysql:8";

    private static MySqlTestContainer mySqlTestContainer;

    private MySqlTestContainer() {
        super(DB_IMAGE);
    }

    public static synchronized MySqlTestContainer getInstance() {
        if (mySqlTestContainer == null) {
            mySqlTestContainer = new MySqlTestContainer();
        }
        return mySqlTestContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mySqlTestContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mySqlTestContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mySqlTestContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
