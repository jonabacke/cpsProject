package ComModule;

public interface IMiddlewareRegisterService {
    void register(String uuid, String serviceName, IMiddlewareCallableStub stub, boolean retain, int qos);
}
