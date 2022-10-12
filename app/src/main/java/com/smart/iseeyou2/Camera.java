package com.smart.iseeyou2;

public class Camera {
    private String IPAddress;
    private String port;

    public Camera(String IPAddress, String port){
        this.IPAddress = IPAddress;
        this.port = port;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public String getPort() {
        return port;
    }
}
