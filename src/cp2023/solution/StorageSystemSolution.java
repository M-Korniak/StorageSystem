package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;
import cp2023.base.StorageSystem;
import cp2023.exceptions.TransferException;

import java.util.HashMap;
import java.util.Map;

public class StorageSystemSolution implements StorageSystem {

    private HashMap<DeviceId, Device> devices = new HashMap<DeviceId, Device>();
    public StorageSystemSolution(Map<DeviceId, Integer> deviceTotalSlots,
                                 Map<ComponentId, DeviceId> componentPlacement) {
        for (DeviceId deviceId : deviceTotalSlots.keySet()) {
            int capacity = deviceTotalSlots.get(deviceId);
            if (capacity <= 0) {
                throw new IllegalArgumentException("At least one device has zero or less capacity!");
            }
            devices.put(deviceId, new Device(deviceId, capacity));
        }
        for (ComponentId componentId : componentPlacement.keySet()) {
            DeviceId placementOfComponent = componentPlacement.get(componentId);
            if (!devices.containsKey(placementOfComponent)) {
                throw new IllegalArgumentException("At least one component is assigned to a device without capacity");
            }
            devices.get(placementOfComponent).addInitialComponent(componentId);
        }
        for (Device device : devices.values()) {
            if (device.checkMoreThanCapacity())
                throw new IllegalArgumentException("More components are assigned to a device than it has capacity");
        }
    }

    @Override
    public void execute(ComponentTransfer transfer) throws TransferException {
        return;
    }
}
