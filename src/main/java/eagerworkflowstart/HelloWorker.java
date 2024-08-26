package eagerworkflowstart;


import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class HelloWorker {

    public static void main(String[] args) throws Exception{

        // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker
        // instance of the Temporal server.
        //WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkerFactory factory = WorkerFactory.newInstance(TemporalClient.get());
        // Worker factory is used to create Workers that poll specific Task Queues.
        Worker worker = factory.newWorker(Shared.NON_EAGER_TASK_QUEUE);
        
        
        // This Worker hosts both Workflow and Activity implementations.
        // Workflows are stateful so a type is needed to create instances.
        worker.registerWorkflowImplementationTypes(IntroductionWorkflowImpl.class, GreetingWorkflowImpl.class);
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(new HelloActivitiesImpl());
        // Start listening to the Task Queue.
        factory.start();
    }
}
