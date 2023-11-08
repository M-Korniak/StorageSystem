package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;
import cp2023.base.StorageSystem;
import cp2023.exceptions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StorageSystemSolution implements StorageSystem {

    private final HashMap<DeviceId, Device> devices = new HashMap<DeviceId, Device>();
    private final HashSet<ComponentId> components = new HashSet<ComponentId>();
    private final HashSet<ComponentId> transferredComponents = new HashSet<ComponentId>();
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
            components.add(componentId);
        }
        for (Device device : devices.values()) {
            if (device.checkMoreThanCapacity())
                throw new IllegalArgumentException("More components are assigned to a device than it has capacity");
        }
    }

    @Override
    public void execute(ComponentTransfer componentTransfer) throws TransferException {
        Transfer transfer = new Transfer(componentTransfer);
        final DeviceId sourceDeviceId = componentTransfer.getSourceDeviceId();
        final DeviceId destinationDeviceId = componentTransfer.getDestinationDeviceId();
        final ComponentId componentId = componentTransfer.getComponentId();

        //Check Correctness of Transfer
        if (sourceDeviceId == null && destinationDeviceId == null)
            throw new IllegalTransferType(componentId);
        else if (sourceDeviceId != null && !devices.containsKey(sourceDeviceId))
            throw new DeviceDoesNotExist(sourceDeviceId);
        else if (destinationDeviceId != null && !devices.containsKey(destinationDeviceId))
            throw new DeviceDoesNotExist(destinationDeviceId);
        else if (sourceDeviceId == null && components.contains(componentId))
            throw new ComponentAlreadyExists(componentId);
        else if (sourceDeviceId != null && !devices.get(sourceDeviceId).containsComponent(componentId))
            throw new ComponentDoesNotExist(componentId, sourceDeviceId);
        else if (destinationDeviceId != null && devices.get(destinationDeviceId).containsComponent(componentId))
            throw new ComponentDoesNotNeedTransfer(componentId, destinationDeviceId);
        else if (transferredComponents.contains(componentId))
            throw new ComponentIsBeingOperatedOn(componentId);


    }
}
