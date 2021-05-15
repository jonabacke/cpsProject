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
    public void signInTrafficUser(String trafficUserNetworkString, String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserNetworkString, trafficUserUUID);

    }

    @Override
    public void signOutTrafficUser(String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), trafficUserUUID);
    }

    @Override
    public void setTempo(double tempo, String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), tempo, trafficUserUUID);
    }

    @Override
    public void setPriority(String priority, String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), priority, trafficUserUUID);
    }

    @Override
    public void setNextTrafficNode(String nextTrafficNode, String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), nextTrafficNode, trafficUserUUID);
    }

    @Override
    public void setFinalTrafficNode(String finalTrafficNode, String trafficUserUUID) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), finalTrafficNode, trafficUserUUID);
    }
}
