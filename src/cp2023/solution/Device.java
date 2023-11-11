package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.DeviceId;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Device {
    private final DeviceId id;
    private final int capacity;
    private final HashSet<ComponentId> components = new HashSet<ComponentId>();

    List<Transfer> incomingTransfers = new LinkedList<Transfer>();
    List<Transfer> outgoingTransfers = new LinkedList<Transfer>();
    public Device(DeviceId id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }
    public boolean isTransferred() {
        return outgoingTransfers.size() > 0;
    }
    public boolean isFree() {
        return components.size() < capacity;
    }
    public List<Transfer> getOutgoingTransfers() {
        return outgoingTransfers;
    }

    public List<Transfer> getIncomingTransfers() {
        return incomingTransfers;
    }
    public boolean containsComponent(ComponentId componentId) {
        return components.contains(componentId);
    }

    public void addComponent(ComponentId componentId) {
        components.add(componentId);
    }
    public void removeComponent(ComponentId componentId) {
        components.remove(componentId);
    }

    public boolean checkMoreThanCapacity() {
        return components.size() > capacity;
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
