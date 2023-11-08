package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class MoveTransfer extends Transfer {
    public MoveTransfer(ComponentTransfer componentTransfer) {
        super(componentTransfer);
    }

    @Override
    public void execute(Semaphore mutex, HashSet<ComponentId> components, HashSet<ComponentId> transferredComponents, HashMap<DeviceId, Device> devices) {
        // TODO usuwaj z listy outgoing gdy zajmujesz jego miejsce i incoming gdy zajmujesz miejsce
    }
}
