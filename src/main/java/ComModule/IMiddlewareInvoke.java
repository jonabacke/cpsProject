package ComModule;

public interface IMiddlewareInvoke {
    void invoke(String serviceName, String functionName, Object ...paramValues);
}
