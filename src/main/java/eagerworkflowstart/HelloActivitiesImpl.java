package eagerworkflowstart;


public class HelloActivitiesImpl implements HelloActivities {



    @Override
    public String composeGreeting(String greeting, String name) {
      return greeting + " " + name + "!";
    }

    @Override
    public String composeIntroduction(String name, String interest) {
        String introduction = new StringBuilder()
        .append("I'm from Milwaukee, where they make delicious cheese. \n")
        .append("It's neat that we share an interest in " + interest + ".\n")
        .append("I am really glad to meet you, " + name + ". ")
        .toString();
        return introduction;
    }
}
