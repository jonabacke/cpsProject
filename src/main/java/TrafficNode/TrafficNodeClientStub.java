package TrafficNode;

import ComModule.IMiddlewareInvoke;
import TrafficUser.ITrafficUser;

public class TrafficNodeClientStub {

    private final IMiddlewareInvoke middlewareInvoke;
    private final String name;


    public TrafficNodeClientStub(IMiddlewareInvoke middleware, String name) {
        this.middlewareInvoke = middleware;
        this.name = name;
    }

    public TrafficNodeClientStub(IMiddlewareInvoke middleware) {
        this.middlewareInvoke = middleware;
        this.name = ITrafficNode.class.getName();
    }


    public void addTrafficNode(String trafficNodeUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficNodeUUID);
    }


    public void deleteTrafficNode(String trafficNodeUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficNodeUUID);
    }
}
