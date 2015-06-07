package com.example.duarte.canoefortwo;

/**
 * Information about the player's stats
 */
public class Player {
    private int rowSpeed;

    public Player(){
        this.rowSpeed = 0;
    }

    public int getRowSpeed() {
        return rowSpeed;
    }

    public void setRowSpeed(int rowSpeed) {
        this.rowSpeed = rowSpeed;
    }
}
