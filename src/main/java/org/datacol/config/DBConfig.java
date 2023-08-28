package org.datacol.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "connection")
public interface DBConfig {

    DatacolConfig datacol();
    UsersConfig users();

    @ConfigMapping(prefix = "datacol")
    interface DatacolConfig {
        String host();
        String user();
        String password();
        String database();
        Integer port();
    }
    @ConfigMapping(prefix = "users")
    interface UsersConfig {
        String host();
        String user();
        String password();
        String database();
        Integer port();
    }

}
