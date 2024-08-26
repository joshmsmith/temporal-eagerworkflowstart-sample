package eagerworkflowstart;


import java.io.FileNotFoundException;

import javax.net.ssl.SSLException;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;

public class InitiateNonEagerHello {

    public static void main(String[] args) throws FileNotFoundException, SSLException {

        
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.NON_EAGER_TASK_QUEUE)
                // A WorkflowId prevents this it from having duplicate instances, remove it to
                // duplicate.
                .setWorkflowId("Non-Eager-Greeting")
                .build();
        // WorkflowClient can be used to start, signal, query, cancel, and terminate
        // Workflows.
        WorkflowClient client = TemporalClient.get();

        
        long totalruntime = 0;
        int max_attempts = 100;
        for(int i = 0; i<max_attempts; i++) {
            // WorkflowStubs enable calls to methods as if the Workflow object is local, but
            // actually perform an RPC.
            GreetingWorkflow greetingWorkflow = client.newWorkflowStub(GreetingWorkflow.class, options);
            
            

            // Asynchronous execution. This process will exit after making this call.
            //WorkflowExecution gwe = WorkflowClient.start(greetingWorkflow::getGreeting, "Dave");
            //System.out.printf("\nWorkflowID: %s RunID: %s", gwe.getWorkflowId(), gwe.getRunId());


            //synchronously execute the workflow
            long starttime = System.currentTimeMillis();
            String greeting = greetingWorkflow.getGreeting("Dave");
            long endtime = System.currentTimeMillis();
            totalruntime += endtime - starttime;
            
            //System.out.println("Non Eager runtime:" + (endtime - starttime) + " milliseconds");
        }
        
        long averageruntime = totalruntime / max_attempts;
        System.out.println("Non Eager Average runtime:" + averageruntime + " milliseconds");

        // options = WorkflowOptions.newBuilder()
        //         .setTaskQueue(Shared.NON_EAGER_TASK_QUEUE)
        //         // A WorkflowId prevents this it from having duplicate instances, remove it to
        //         // duplicate.
        //         .setWorkflowId("Introduction")
        //         .build();
        // IntroductionWorkflow introWorkflow = client.newWorkflowStub(IntroductionWorkflow.class, options);

        // //synchronously execute the workflow
        // String introduction = introWorkflow.getIntroduction("Dave", "raising chickens");
        // System.out.println(introduction);


        //WorkflowExecution iwe = WorkflowClient.start(introWorkflow::getIntroduction, "Dave", "raising chickens");
        //System.out.printf("\nWorkflowID: %s RunID: %s", iwe.getWorkflowId(), iwe.getRunId());
        
        
        System.exit(0);
    }
}
