# temporal-eagerworkflowstart-sample
Sample for working with Temporal Eager Workflow Start.
This sample demonstrates connecting to temporal cloud and the use of eager vs non-eager workflow starts.
There is also a sample of a workflow continuing the process that is less time-sensitive that can continue processing.




## Run the Workflow
First, make sure the [Temporal server](https://docs.temporal.io/docs/server/quick-install) is running, or connect to cloud by configuring .envrc.


Second, start the Worker, from the project root run:
```
$ ./gradlew startWorker --console=plain
```

To start the Workflow in eager mode, from the project root run:

```
$ ./gradlew initiateEagerHello
<snip>
Hello Jerry!
Eager runtime:266 milliseconds
<snip>
```

For comparison, start the workflow in non eager mode. From the project root run:
```
$ ./gradlew initiateNonEagerHello
<snip>
Hello Dave!
Non Eager runtime:938 milliseconds
<snip>
```



## Optional: Run Tests
Execute tests by doing:
```
./gradlew build
```