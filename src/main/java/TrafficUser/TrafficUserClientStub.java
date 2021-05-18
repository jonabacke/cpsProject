package TrafficUser;

import ComModule.IMiddlewareInvoke;
import TrafficNode.ITrafficNode;

public class TrafficUserClientStub implements ITrafficUser {

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

    @Override
    public void setTempo(double tempo) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), tempo);
    }

    @Override
    public void buildEmergencyCorridor() {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void setNextTrafficNode(String trafficNodeUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficNodeUUID);
    }


    public void publishVisualizationData(String msg) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), msg);
    }
}
