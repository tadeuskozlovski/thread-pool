package learnthreadpool;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegister {
    private final List<TaskService> serviceList = new ArrayList<>();

    public void registerService(final TaskService service){
        serviceList.add(service);
    }

    public List<TaskService> getServiceList(){
        return serviceList;
    }
}
