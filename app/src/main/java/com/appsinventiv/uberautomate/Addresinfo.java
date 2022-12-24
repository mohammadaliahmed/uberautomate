package com.appsinventiv.uberautomate;

public class Addresinfo {

    public String pickup,droppoff;

    public Addresinfo(String pickup, String droppoff) {
        this.pickup = pickup;
        this.droppoff = droppoff;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDroppoff() {
        return droppoff;
    }

    public void setDroppoff(String droppoff) {
        this.droppoff = droppoff;
    }
}
