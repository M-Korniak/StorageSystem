package cp2023.solution;

import cp2023.base.ComponentId;
import cp2023.base.ComponentTransfer;
import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MoveTransfer extends Transfer {
    public MoveTransfer(ComponentTransfer componentTransfer) {
        super(componentTransfer);
    }

    @Override
    public void execute(Semaphore mutex, HashSet<ComponentId> components, HashSet<ComponentId> transferredComponents, HashMap<DeviceId, Device> devices) {
        Device sourceDevice = devices.get(sourceDeviceId);
        Device destinationDevice = devices.get(destinationDeviceId);

        List<Transfer> incomingToSourceDevice = sourceDevice.getIncomingTransfers();
        List<Transfer> outgoingFromSourceDevice = sourceDevice.getOutgoingTransfers();
        List<Transfer> incomingToDestinationDevice = destinationDevice.getIncomingTransfers();
        List<Transfer> outgoingFromDestinationDevice = destinationDevice.getOutgoingTransfers();

        DFSCalculator dfsCalculator = new DFSCalculator(devices);
        try {
            mutex.acquire();
            transferredComponents.add(componentId);
            if (destinationDevice.isFree()) {
                destinationDevice.addComponent(componentId);
                wakeUp();
                if (!incomingToSourceDevice.isEmpty()) {
                    connectedTransfer = incomingToSourceDevice.get(0);
                    incomingToSourceDevice.remove(connectedTransfer);
                    connectedTransfer.wakeUp();
                }
                else {
                    outgoingFromSourceDevice.add(this);
                }
                mutex.release();
            }
            else if (destinationDevice.isTransferred()) {
                Transfer wakingTransfer = outgoingFromDestinationDevice.get(0);
                outgoingFromDestinationDevice.remove(wakingTransfer);
                wakingTransfer.setConnectedTransfer(this);
                if (!incomingToSourceDevice.isEmpty()) {
                    connectedTransfer = incomingToSourceDevice.get(0);
                    incomingToSourceDevice.remove(connectedTransfer);
                    connectedTransfer.wakeUp();
                }
                else {
                    outgoingFromSourceDevice.add(this);
                }
                mutex.release();
            }
            else if (dfsCalculator.isCycle(this)) {
                List<Transfer> cycle = dfsCalculator.getCycle();
                for (int i = 0; i < cycle.size() - 1; i++) {
                    cycle.get(i).setConnectedTransfer(cycle.get(i + 1));
                }
                cycle.get(cycle.size() - 1).setConnectedTransfer(this);
                for (int i = 1; i < cycle.size(); i++) {
                    Transfer transfer = cycle.get(i);
                    devices.get(transfer.getDestinationDeviceId()).getIncomingTransfers().remove(transfer);
                    transfer.wakeUp();
                }
                mutex.release();
            }
            else {
                incomingToDestinationDevice.add(this);
                mutex.release();
                semaphore.acquire();
                mutex.acquire();
                if (!incomingToSourceDevice.isEmpty()) {
                    connectedTransfer = incomingToSourceDevice.get(0);
                    incomingToSourceDevice.remove(connectedTransfer);
                    connectedTransfer.wakeUp();
                }
                else {
                    outgoingFromSourceDevice.add(this);
                }
                mutex.release();
            }
        } catch (InterruptedException e) {
            mutex.release();
            throw new RuntimeException("panic: unexpected thread interruption");
        }
        componentTransfer.prepare();
        try {
            mutex.acquire();
            sourceDevice.removeComponent(componentId);
            if (connectedTransfer == null) {
                outgoingFromSourceDevice.remove(this);
            }
            else {
                sourceDevice.addComponent(connectedTransfer.getComponentId());
                connectedTransfer.wakeUp();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("panic: unexpected thread interruption");
        } finally {
            mutex.release();
        }

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
