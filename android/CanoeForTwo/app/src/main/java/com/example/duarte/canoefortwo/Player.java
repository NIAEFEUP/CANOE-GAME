package com.example.duarte.canoefortwo;

/**
 * Created by Duarte on 05/06/2015.
 */
public class Player {
    private static int rowSpeed;

    public Player(){
        int rowSpeed = 0;
    }


    public static int getRowSpeed() {
        return rowSpeed;
    }

    public void setRowSpeed(int rowSpeed) {
        this.rowSpeed = rowSpeed;
    }
}
