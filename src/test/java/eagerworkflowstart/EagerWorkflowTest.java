package eagerworkflowstart;


import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;

import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EagerWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @Before
    public void setUp() {
    }


    @Test
    public void testGreeting() {
        
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker(Shared.EAGER_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
        worker.registerActivitiesImplementations(new HelloActivitiesImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.EAGER_TASK_QUEUE)
                .build();

        GreetingWorkflow workflow = workflowClient.newWorkflowStub(GreetingWorkflow.class, options);
        
        workflow.getGreeting("Bob");
        
    }
    
    @After
    public void tearDown() {
        testEnv.close();
    }
}
