package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

public class Transfer {
    private final ComponentTransfer componentTransfer;
    private final DeviceId sourceDeviceId;
    private final DeviceId destinationDeviceId;
    private final ComponentId componentId;

    public Transfer(ComponentTransfer componentTransfer) {
        this.componentTransfer = componentTransfer;
        this.componentId = componentTransfer.getComponentId();
        this.sourceDeviceId = componentTransfer.getSourceDeviceId();
        this.destinationDeviceId = componentTransfer.getDestinationDeviceId();
    }


}
