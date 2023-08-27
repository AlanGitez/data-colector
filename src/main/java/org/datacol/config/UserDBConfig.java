package org.datacol.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "users")
public interface UserDBConfig {
    String host();
    String user();
    String password();
    String database();
    Integer port();
}
