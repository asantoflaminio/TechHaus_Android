package com.techhaus.techhausandroid;

public class API {
    /*para cambiar el puerto hacerlo solo aca*/
    public static String baseUrl = "http://10.0.2.2:8080/api/";

    public static String getDevices(){
        return baseUrl + "devices/";
    }

    public static String getDeviceTypes(){
        return baseUrl + "deviceTypes/";
    }

    public static String getRoutines(){
        return baseUrl + "routines/";
    }


}
