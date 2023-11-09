package cp2023.solution;

import cp2023.base.DeviceId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DFSCalculator {
    private final HashMap<DeviceId, Device> devices;
    private List<Transfer> result = null;
    private final HashSet<Device> visited = new HashSet<Device>();
    public DFSCalculator(HashMap<DeviceId, Device> devices) {
        this.devices = devices;
    }
    public boolean isCycle(Transfer transfer) {
        List<Transfer> trail = new LinkedList<>();
        trail.add(transfer);
        dfs(trail, devices.get(transfer.getSourceDeviceId()), devices.get(transfer.getDestinationDeviceId()));
        return result != null;
    }
    public void dfs(List<Transfer> trail, Device currentDevice, Device destination) {
        if (currentDevice != null && result == null && !visited.contains(currentDevice)) {
            if (currentDevice == destination)
                result = new LinkedList<>(trail);
            else {
                visited.add(currentDevice);
                List<Transfer> incomingTransfers = currentDevice.getIncomingTransfers();
                for (Transfer transfer : incomingTransfers) {
                    trail.add(transfer);
                    dfs(trail, devices.get(transfer.getSourceDeviceId()), destination);
                    trail.remove(transfer);
                }
            }
        }
    }
    public List<Transfer> getCycle() {
        return result;
    }

}
