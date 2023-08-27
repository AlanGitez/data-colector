package org.datacol.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "datacol")
public interface DatacolDBConfig {
    String host();
    String user();
    String password();
    String database();
    Integer port();

}
