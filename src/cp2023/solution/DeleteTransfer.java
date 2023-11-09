package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DeleteTransfer extends Transfer {
    public DeleteTransfer(ComponentTransfer componentTransfer) {
        super(componentTransfer);
    }
    @Override
    public void execute(Semaphore mutex, HashSet<ComponentId> components, HashSet<ComponentId> transferredComponents, HashMap<DeviceId, Device> devices) {
        Device myDevice = devices.get(sourceDeviceId);
        List<Transfer> outgoingFromMyDevice = myDevice.getOutgoingTransfers();
        List<Transfer> incomingToMyDevice = myDevice.getIncomingTransfers();

        try {
            mutex.acquire();
            transferredComponents.add(componentId);
            if (!incomingToMyDevice.isEmpty()) {
                connectedTransfer = incomingToMyDevice.get(0);
                incomingToMyDevice.remove(connectedTransfer);
                connectedTransfer.wakeUp();
            }
            else {
                outgoingFromMyDevice.add(this);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("panic: unexpected thread interruption");
        } finally {
            mutex.release();
        }
        componentTransfer.prepare();
        try {
            mutex.acquire();
            myDevice.removeComponent(componentId);
            if (connectedTransfer == null) {
                outgoingFromMyDevice.remove(this);
            }
            else {
                myDevice.addComponent(connectedTransfer.getComponentId());
                connectedTransfer.wakeUp();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mutex.release();
        }
        componentTransfer.perform();
        try {
            mutex.acquire();
            transferredComponents.remove(componentId);
            components.remove(componentId);
        } catch (InterruptedException e) {
            throw new RuntimeException("panic: unexpected thread interruption");
        } finally {
            mutex.release();
        }
    }
}
