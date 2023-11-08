package cp2023.tests;

import cp2023.base.ComponentId;
import cp2023.base.DeviceId;
import cp2023.solution.StorageSystemFactory;

import java.util.HashMap;

public class TestLegalityOfArguments {

    public static void main(String[] args) {
        DeviceId dev1 = new DeviceId(1);
        DeviceId dev2 = new DeviceId(2);
        DeviceId dev3 = new DeviceId(3);

        ComponentId comp1 = new ComponentId(101);
        ComponentId comp2 = new ComponentId(102);
        ComponentId comp3 = new ComponentId(103);
        ComponentId comp4 = new ComponentId(104);
        ComponentId comp5 = new ComponentId(105);
        ComponentId comp6 = new ComponentId(106);
        ComponentId comp7 = new ComponentId(107);
        ComponentId comp8 = new ComponentId(108);
        ComponentId comp9 = new ComponentId(109);

        HashMap<DeviceId, Integer> deviceCapacities = new HashMap<>(3);
        deviceCapacities.put(dev1, 3);
        deviceCapacities.put(dev2, 3);
        deviceCapacities.put(dev3, 5);

        HashMap<ComponentId, DeviceId> initialComponentMapping = new HashMap<>(9);

        initialComponentMapping.put(comp1, dev1);
        initialComponentMapping.put(comp2, dev1);
        initialComponentMapping.put(comp3, dev1);

        initialComponentMapping.put(comp4, dev2);
        initialComponentMapping.put(comp5, dev2);
        initialComponentMapping.put(comp6, dev2);

        initialComponentMapping.put(comp7, dev3);
        initialComponentMapping.put(comp8, dev3);
        initialComponentMapping.put(comp9, dev3);

        StorageSystemFactory.newSystem(deviceCapacities, initialComponentMapping);
    }
}
