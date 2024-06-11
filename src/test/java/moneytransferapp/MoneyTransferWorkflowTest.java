package moneytransferapp;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eagerworkflowstart.HelloActivities;
import eagerworkflowstart.HelloActivitiesImpl;
import eagerworkflowstart.IntroductionWorkflow;
import eagerworkflowstart.IntroductionWorkflowImpl;
import eagerworkflowstart.Shared;

public class MoneyTransferWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @Before
    public void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker(Shared.HELLO_TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(IntroductionWorkflowImpl.class);
        workflowClient = testEnv.getWorkflowClient();
    }

    @After
    public void tearDown() {
        testEnv.close();
    }

    @Test
    public void testTransfer() {
        HelloActivities activities = mock(HelloActivitiesImpl.class);
        worker.registerActivitiesImplementations(activities);
        testEnv.start();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.HELLO_TASK_QUEUE)
                .build();
        IntroductionWorkflow workflow = workflowClient.newWorkflowStub(IntroductionWorkflowImpl.class, options);
        workflow.getIntroduction("Bob", "fooseball");
        
        //verify(activities).deposit(eq("account2"), eq("reference1"), eq(1.23));
    }
}
