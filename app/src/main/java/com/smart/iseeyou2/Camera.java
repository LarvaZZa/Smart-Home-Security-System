package com.smart.iseeyou2;

public class Camera {
    private String IPAddress;
    private String name;

    public Camera(String IPAddress, String name){
        this.IPAddress = IPAddress;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

}
