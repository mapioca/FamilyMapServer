package genericData;

import model.Location;

public class Locations {
    Location[] data;

    public Locations(Location[] data) {
        this.data = data;
    }

    public Locations() {
    }

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
