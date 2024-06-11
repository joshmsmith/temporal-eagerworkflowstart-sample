package eagerworkflowstart;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface HelloActivities {

    @ActivityMethod
    String composeIntroduction(String name, String interest);

    @ActivityMethod
    String composeGreeting(String greeting, String name);

}

