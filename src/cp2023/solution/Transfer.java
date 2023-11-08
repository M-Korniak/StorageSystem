package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public abstract class Transfer {
    protected Transfer connectedTransfer = null;
    protected final ComponentTransfer componentTransfer;
    protected final DeviceId sourceDeviceId;
    protected final DeviceId destinationDeviceId;
    protected final ComponentId componentId;
    protected final Semaphore semaphore = new Semaphore(0, true);

    public Transfer(ComponentTransfer componentTransfer) {
        this.componentTransfer = componentTransfer;
        this.componentId = componentTransfer.getComponentId();
        this.sourceDeviceId = componentTransfer.getSourceDeviceId();
        this.destinationDeviceId = componentTransfer.getDestinationDeviceId();
    }
    public abstract void execute(Semaphore mutex, HashSet<ComponentId> components,
                                 HashSet<ComponentId> transferredComponents, HashMap<DeviceId, Device> devices);
    public ComponentId getComponentId() {
        return componentId;
    }
    public void wakeUp() {
        semaphore.release();
    }
    public void setConnectedTransfer(Transfer connectedTransfer) {
        this.connectedTransfer = connectedTransfer;
    }
}
