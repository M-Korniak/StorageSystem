package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AddTransfer extends Transfer{
    public AddTransfer(ComponentTransfer componentTransfer) {
        super(componentTransfer);
    }
    @Override
    public void execute(Semaphore mutex, HashSet<ComponentId> components,
                        HashSet<ComponentId> transferredComponents, HashMap<DeviceId, Device> devices) {
        Device myDevice = devices.get(destinationDeviceId);
        List<Transfer> outgoingFromMyDevice = myDevice.getOutgoingTransfers();
        List<Transfer> incomingToMyDevice = myDevice.getIncomingTransfers();

        try {
            mutex.acquire();
            components.add(componentId);
            transferredComponents.add(componentId);
            if (myDevice.isFree()) {
                myDevice.addComponent(componentId);
                wakeUp();
                mutex.release();
            }
            else if (myDevice.isTransferred()) {
                Transfer wakingTransfer = outgoingFromMyDevice.get(0);
                outgoingFromMyDevice.remove(wakingTransfer);
                wakingTransfer.setConnectedTransfer(this);
                mutex.release();
            }
            else {
                incomingToMyDevice.add(this);
                mutex.release();
                semaphore.acquire();
            }
        } catch (InterruptedException e) {
            mutex.release();
            throw new RuntimeException("panic: unexpected thread interruption");
        }
        componentTransfer.prepare();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException("panic: unexpected thread interruption");
        }
        componentTransfer.perform();
        try {
            mutex.acquire();
            transferredComponents.remove(componentId);
        } catch (InterruptedException e) {
            throw new RuntimeException("panic: unexpected thread interruption");
        } finally {
            mutex.release();
        }
    }

}
