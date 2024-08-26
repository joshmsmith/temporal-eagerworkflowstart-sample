package eagerworkflowstart;

import io.temporal.activity.LocalActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

/** GreetingWorkflow implementation that calls GreetingsActivities#composeGreeting. */
public class GreetingWorkflowImpl implements GreetingWorkflow {

    /**
     * Activity stub implements activity interface and proxies calls to it to Temporal activity
     * invocations. Because activities are reentrant, only a single stub can be used for multiple
     * activity invocations.
     */
    private final HelloActivities activities =
        Workflow.newLocalActivityStub(
            HelloActivities.class,
            LocalActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(2))
                .build());

    @Override
    public String getGreeting(String name) {
      // This is a blocking call that returns only after the activity has completed.
      //return activities.composeGreeting("Hello", name);
      String result = activities.composeGreeting("Hello", name);
      return result;
    }
  }