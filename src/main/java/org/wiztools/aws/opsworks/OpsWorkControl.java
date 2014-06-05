package org.wiztools.aws.opsworks;

import com.amazonaws.services.opsworks.AWSOpsWorksClient;
import com.amazonaws.services.opsworks.model.StartInstanceRequest;
import com.amazonaws.services.opsworks.model.StartStackRequest;
import com.amazonaws.services.opsworks.model.StopInstanceRequest;
import com.amazonaws.services.opsworks.model.StopStackRequest;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author subhash
 */
public class OpsWorkControl {
    private final AWSOpsWorksClient client;
    private final List<String> parameters;

    public OpsWorkControl(AWSOpsWorksClient client, List<String> parameters) {
        this.client = client;
        this.parameters = Collections.unmodifiableList(parameters);
    }
    
    public void startInstances() {
        for(String instance: parameters) {
            StartInstanceRequest req = new StartInstanceRequest();
            req.setInstanceId(instance);
            client.startInstance(req);
        }
    }
    
    public void stopInstances() {
        for(String instance: parameters) {
            StopInstanceRequest req = new StopInstanceRequest();
            req.setInstanceId(instance);
            client.stopInstance(req);
        }
    }
    
    public void startStacks() {
        for(String stackId: parameters) {
            StartStackRequest req = new StartStackRequest();
            req.setStackId(stackId);
            client.startStack(req);
        }
    }
    
    public void stopStacks() {
        for(String stackId: parameters) {
            StopStackRequest req = new StopStackRequest();
            req.setStackId(stackId);
            client.stopStack(req);
        }
    }
}
