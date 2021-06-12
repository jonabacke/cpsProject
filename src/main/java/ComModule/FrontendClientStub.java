package ComModule;

public class FrontendClientStub {


    private final IMiddlewareInvoke middlewareInvoke;
    private final String name;

    public FrontendClientStub(IMiddlewareInvoke middleware, String name) {
        this.middlewareInvoke = middleware;
        this.name = name;
    }


    public void publishVisualizationData(String msg) {
        middlewareInvoke.invoke(this.name, new Throwable().getStackTrace()[0].getMethodName(), msg);
    }
}
