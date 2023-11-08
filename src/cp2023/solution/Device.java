package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.DeviceId;

import java.util.HashSet;

public class Device {
    private final DeviceId id;
    private final int capacity;
    private int taken = 0;
    private final HashSet<ComponentId> components = new HashSet<ComponentId>();

    public Device(DeviceId id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }
    public boolean containsComponent(ComponentId componentId) {
        return components.contains(componentId);
    }

    public void addInitialComponent(ComponentId componentId) {
        components.add(componentId);
        taken++;
    }

    public boolean checkMoreThanCapacity() {
        return taken > capacity;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
