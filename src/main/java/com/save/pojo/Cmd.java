package com.save.pojo;

public class Cmd {
    private String ip;
    private String username;
    private String password;
    private String shell;
    private String[] cmds;
    private String server;

    public String getServer() {
        return server;
    }
    public void setServer(String server) {
        this.server = server;
    }
    public String getShell() {
        return shell;
    }
    public void setShell(String shell) {
        this.shell = shell;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String[] getCmds() {
        return cmds;
    }
    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }  
}
