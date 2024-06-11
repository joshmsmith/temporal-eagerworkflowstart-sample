/*
 *  Copyright (c) 2024 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package eagerworkflowstart;

import java.io.FileNotFoundException;

import javax.net.ssl.SSLException;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
/**
 * Hello World Temporal workflow that executes a single local activity eagerly. Requires a local
 * instance the Temporal service to be running.
 *
 * <p>Some of the Activities are very short lived and do not need the queuing semantic, flow
 * control, rate limiting and routing capabilities. For these Temporal supports so called local
 * Activity feature. Local Activities are executed in the same worker process as the Workflow that
 * invoked them. Consider using local Activities for functions that are:
 *
 * <ul>
 *   <li>no longer than a few seconds
 *   <li>do not require global rate limiting
 *   <li>do not require routing to specific workers or pools of workers
 *   <li>can be implemented in the same binary as the Workflow that invokes them
 * </ul>
 *
 * <p>The main benefit of local Activities is that they are much more efficient in utilizing
 * Temporal service resources and have much lower latency overhead comparing to the usual Activity
 * invocation.
 */
public class InitiateEagerHello {

  public static void main(String[] args) throws FileNotFoundException, SSLException{
    
    // gRPC stubs wrapper that talks to the local instance of temporal
    // service.
    WorkflowClient client = TemporalClient.get();

    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);
    // Worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(Shared.HELLO_TASK_QUEUE);
    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
    // Activities are stateless and thread safe. So a shared instance is used.
    worker.registerActivitiesImplementations(new HelloActivitiesImpl());
    // Start listening to the workflow and activity task queues.
    factory.start();

    // Start a workflow execution. Usually this is done from another program.
    // Uses task queue from the GreetingWorkflow @WorkflowMethod annotation.
    GreetingWorkflow greetingWorkflow =
        client.newWorkflowStub(
            GreetingWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.HELLO_TASK_QUEUE)
                .setWorkflowId("Eager-Greeting")
                .setDisableEagerExecution(false)
                .build());

    
    long starttime = System.currentTimeMillis();
    String greeting = greetingWorkflow.getGreeting("Jerry");
    long endtime = System.currentTimeMillis();
    System.out.println(greeting);
    
    System.out.println("Eager runtime:" + (endtime - starttime) + " milliseconds");
    
    //WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient nonEagerClient = TemporalClient.get();
    IntroductionWorkflow introWorkflow =
    nonEagerClient.newWorkflowStub(
            IntroductionWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.HELLO_TASK_QUEUE)
                .setWorkflowId("Introduction")
                .setDisableEagerExecution(true)
                .build());

    
    String introduction = introWorkflow.getIntroduction("Jerry", "hiking");
    System.out.println(introduction);
    System.out.println(System.currentTimeMillis());
    // Asynchronous execution. This process will exit after making this call.
    //WorkflowExecution iwe = WorkflowClient.start(introWorkflow::getIntroduction, "Dave", "raising chickens");
    //System.out.printf("\nWorkflowID: %s RunID: %s", iwe.getWorkflowId(), iwe.getRunId());
    System.exit(0);
  }


}
