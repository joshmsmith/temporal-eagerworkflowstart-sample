package eagerworkflowstart;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface GreetingWorkflow {

    // The Workflow method is called by the initiator either via code or CLI.
    @WorkflowMethod
    String getGreeting(String name);
}
