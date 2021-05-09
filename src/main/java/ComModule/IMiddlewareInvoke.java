package ComModule;

import java.util.UUID;

public interface IMiddlewareInvoke {
    void invoke(String serviceName, String functionName, Object ...paramVals);
}
