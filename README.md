
# Storage System
Storage systems must simultaneously meet various requirements related to the performance of data access operations, utilization of storage capacity, and resilience to device failures. In order to achieve this, data systems often move data fragments between different devices. Your task is to implement mechanisms in Java that coordinate concurrent operations of such data transfers according to the requirements outlined below. Use the provided template for implementation.

## Specification

In our system model, data is grouped into components and stored on devices. Each device and each data component have an immutable and unique identifier within the system (objects of classes cp2023.base.DeviceId and cp2023.base.ComponentId, respectively). Each device also has a specified capacity, which is the maximum number of components it can store at any given time. The assignment of components to devices is managed by the system (an object implementing the cp2023.base.StorageSystem interface, presented below):

public interface StorageSystem {

    void execute(ComponentTransfer transfer) throws TransferException;
    
}

More specifically, each existing component in the system is located on exactly one device unless the user of the system has initiated a transfer of that component to another device (by calling the execute method on the StorageSystem object and passing a ComponentTransfer object representing the requested transfer).

public interface ComponentTransfer {

    public ComponentId getComponentId();
    
    public DeviceId getSourceDeviceId();
    
    public DeviceId getDestinationDeviceId();
    
    public void prepare();
    
    public void perform();

}

A component transfer is also initiated when a user wants to add a new component to the system (in this case, the getSourceDeviceId method of the transfer object returns null) or remove an existing component from the system (in this case, symmetrically, the getDestinationDeviceId method of the transfer object returns null). In other words, a single transfer represents one of three available operations on a component:

Adding a new component to a device in the system (getSourceDeviceId of the transfer object returns null, and getDestinationDeviceId returns a non-null identifier representing the device on which the component should be located).
Transferring an existing component between devices in the system (both getSourceDeviceId and getDestinationDeviceId return non-null values representing the identifiers of the current device and the destination device, respectively).
Removing an existing component from a device and, consequently, the system (getSourceDeviceId returns a non-null identifier representing the device on which the component is located, and getDestinationDeviceId returns null).
The initiation of the three types of operations by the user is beyond the control of the implemented solution. The task of your solution is to carry out the requested transfers in a synchronous manner (i.e., if the requested transfer is valid, the execute method called on the object implementing StorageSystem with the transfer represented as a ComponentTransfer parameter must not complete until the transfer is complete). Since many different operations can be initiated simultaneously by the user, your implemented system must coordinate them according to the following rules.

At any given time for a given component, at most one transfer can be requested. As long as this transfer is not complete, the component is considered to be in the process of transfer, and any subsequent transfer requested for this component should be treated as invalid.

The actual transfer of a component is a two-stage process and may take a considerable amount of time, especially during its second stage. Initiating a transfer involves preparation (calling the prepare method on the ComponentTransfer object representing the transfer). Only after this preparation can the data constituting the component be sent (done by calling the perform method on the same object). The transfer is considered complete once the data has been sent (i.e., when the perform method finishes its execution). Both the prepare and perform methods must be executed in the context of the thread that initiated the transfer.

## Safety

A transfer can be valid or invalid. The safety requirements below apply to valid transfers. Handling invalid transfers is described in a later section.

If a transfer represents a component removal operation, its initiation is allowed without any additional prerequisites. Otherwise, the initiation of a transfer is allowed only if the destination device has sufficient space for the transferred component. More specifically, the initiation is allowed if one of the following conditions is met:

The destination device has free space for the component, which has not been reserved by the system for another component that is being moved/added to that device.
The destination device has a component "Cy" transferred from that device, and the initiation of its transfer is allowed, and the space released by that component has not been reserved by the system for another component.
OR
The component "Cx" belongs to a set of transferred components such that the destination device for each component in the set is the device on which exactly one other component from the set is located, and the space for none of the components in the set has been reserved.
If the transfer of component "Cx" is allowed but is to take place in a location still occupied by another transferred component "Cy" (the last two cases above), the second stage of the transfer of component "Cx" (i.e., calling the perform function for that transfer) cannot begin before the completion of the first stage of the transfer of component "Cy" (i.e., calling the prepare function for that transfer).

Of course, if a component transfer is not allowed, it cannot be initiated (i.e., neither the prepare nor the perform function can be called on the object representing this transfer).

Your solution must absolutely ensure all of the above safety conditions.

## Liveliness

Regarding liveness, a transfer (both its prepare and perform phases) should start as soon as it is allowed, and the remaining safety requirements are satisfied. In the case where multiple transfers compete for space on a device, your algorithm should locally prioritize transfers that have been waiting longer for that device. Globally, this can potentially lead to the starvation of certain transfers (we encourage you to come up with a scenario for such a situation). Solving this problem is possible, but it complicates the code beyond what we would like to require from you. Therefore, it should not be implemented, especially since, in practice, a user of the system, seeing that a transfer to a certain device is taking a long time, could transfer other components from that device.

## Error Handling

Finally, the proposed solution should check whether the transfer requested by the user is invalid (which should result in the execute method of the StorageSystem interface raising an appropriate exception that inherits from the cp2023.exceptions.TransferException class). According to previous explanations, a transfer is invalid if at least one of the following conditions is met:

The transfer does not represent any of the three available component operations or does not indicate any component (exception: IllegalTransferType).
The device indicated by the transfer as the source or destination does not exist in the system (exception: DeviceDoesNotExist).
A component with the same identifier as the one being added in the transfer already exists in the system (exception: ComponentAlreadyExists).
A component with the identifier being removed or moved in the transfer does
