package ComModule;

import java.util.UUID;

public interface IMiddlewareRegisterService {
    public void register(String uuid, String serviceName, IMiddlewareCallableStub stub, boolean retain, int qos);
}
