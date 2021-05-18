package TrafficNode;

import ComModule.IMiddlewareInvoke;

public class TrafficNodeClientStub implements ITrafficNode {

    private final IMiddlewareInvoke middlewareInvoke;
    private final String name;


    public TrafficNodeClientStub(IMiddlewareInvoke middleware, String name) {
        this.middlewareInvoke = middleware;
        this.name = name;
    }

    public TrafficNodeClientStub(IMiddlewareInvoke middleware) {
        this.middlewareInvoke = middleware;
        this.name = TrafficNode.class.getName();
    }

    @Override
    public void signInTrafficNode(String trafficNodeUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficNodeUUID);
    }

    @Override
    public void signOutTrafficNode(String trafficNodeUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficNodeUUID);
    }

    @Override
    public void signInTrafficUser( String trafficUserUUID, String trafficUserNetworkString) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserNetworkString, trafficUserUUID);

    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID);
    }

    @Override
    public void setTempo(String trafficUserUUID, double tempo) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID, tempo);
    }

    @Override
    public void setPriority(String trafficUserUUID, String priority) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID, priority);
    }

    @Override
    public void setNextTrafficNode(String trafficUserUUID, String nextTrafficNode) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID, nextTrafficNode);
    }

    @Override
    public void setFinalTrafficNode(String trafficUserUUID, String finalTrafficNode) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID, finalTrafficNode);
    }
}
