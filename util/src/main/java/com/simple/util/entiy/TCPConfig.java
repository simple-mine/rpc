package com.simple.util.entiy;

import java.io.Serializable;

/**
 * @Author: huangjun
 * @Date: 2022/7/12 11:17
 * @Version 1.0
 */
public class TCPConfig implements Serializable {
    protected TCPConfig() {
    }


    private String serverName;
    private String ip;
    private Integer port;
    private Boolean isServer;

    public TCPConfig(String serverName, String ip, Integer port, Boolean isServer) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
        this.isServer = isServer;
    }

    public Boolean getServer() {
        return isServer;
    }

    public void setServer(Boolean server) {
        isServer = server;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
