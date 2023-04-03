package org.jucajo.bj_on.models;

public class Box {

    private String owner;

    private int mount;


    public Box(String owner, int mount){
        this.owner = owner;
        this.mount = mount;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }
}
