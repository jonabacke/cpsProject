package ComModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Logger;

public class SkeletonStub implements IMiddlewareCallableStub {

    private final Object obj;
    private final String uuid = UUID.randomUUID().toString();

    public String getUUID() {
        return uuid;
    }

    public SkeletonStub(String serviceName, Object obj, IMiddlewareRegisterService middleware, Boolean retain, Integer qos) {
        this.obj = obj;

        middleware.register(this.uuid, serviceName, this, retain, qos);
    }

    @Override
    public void call(String methodName, Object[] objectParams, Class<?>[] classParams) {
        Logger.getGlobal().finest("Received call Method: [" + methodName + "]");

        try {
            Method method = this.obj.getClass().getMethod(methodName, classParams);
            method.invoke(this.obj, objectParams);
        } catch (NoSuchMethodException e) {
            Logger.getGlobal().severe("No Such method" + e.getMessage());
        } catch (SecurityException e) {
            Logger.getGlobal().severe("Security" + e.getMessage());
        } catch (IllegalAccessException e) {
            Logger.getGlobal().severe("IllegalAcces" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Logger.getGlobal().severe("IllegalArgument" + e.getMessage());
        } catch (InvocationTargetException e) {
            Logger.getGlobal().severe("InvocationTarget Exception " + e.getCause() + "\n " + methodName);
        }
    }
}
