package eagerworkflowstart;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.temporal.common.RetryOptions;

import java.time.Duration;

public class IntroductionWorkflowImpl implements IntroductionWorkflow {
    // RetryOptions specify how to automatically handle retries when Activities
    // fail.
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(500)
            .build();
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            // Timeout options specify when to automatically timeout Activities if the
            // process is taking too long.
            .setStartToCloseTimeout(Duration.ofSeconds(5))
            // Optionally provide customized RetryOptions.
            // Temporal retries failures by default, this is simply an example.
            .setRetryOptions(retryoptions)
            .build();
    
    private final HelloActivities helloActivities = Workflow.newActivityStub(HelloActivities.class, defaultActivityOptions);


    @Override
    public String getIntroduction(String name, String interest) {
        Workflow.sleep(10000);
        return helloActivities.composeIntroduction(name, interest);
    }
}
