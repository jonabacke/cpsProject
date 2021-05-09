package TrafficUser;

import ComModule.IMiddlewareInvoke;
import TrafficNode.ITrafficNode;

public class TrafficUserClientStub {

    private final IMiddlewareInvoke middlewareInvoke;
    private final String name;


    public TrafficUserClientStub(IMiddlewareInvoke middleware, String name) {
        this.middlewareInvoke = middleware;
        this.name = name;
    }

    public TrafficUserClientStub(IMiddlewareInvoke middleware) {
        this.middlewareInvoke = middleware;
        this.name = ITrafficNode.class.getName();
    }


    public void registerTrafficUser(String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID);
    }


    public void deleteTrafficUser(String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID);
    }
}
