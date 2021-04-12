/*
 * Copyright (c) 2021 sousaJ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ocp.book.chapter7.concurrency.executer_service.single_thread_executer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sousaJ on 10/04/2021
 * in package - ocp.book.chapter7.concurrency.executer_service.single_thread_executer
 **/
public class ZooInfo {
    /**
     * With a single‐thread executor, results are guaranteed to be executed sequentially.
     * Notice that the end text is output while our thread executor tasks are still running.
     * This is because the main() method is still an independent thread from the ExecutorService.
     * */

    public static final ExecutorService service = Executors.newSingleThreadExecutor();
    public static void main(String[] args) {
//        AtomicReference<ExecutorService> service = null;
        Runnable task1 = () -> System.out.println("Printing zoo inventory");

        Runnable task2 = () -> {
            for (int i = 0; i < 3; i++) System.out.println("Printing record: " + i);
        };

            try {
                System.out.println("Begin.");

                service.execute(task1);
                service.execute(task2);
                service.execute(task1);

                System.out.println("End.");
            }finally {
                if(service != null) service.shutdown();
            }
    }

    /**
     * The shutdown process for a thread executor involves first rejecting any new tasks submitted to the thread
     * executor while continuing to execute any previously submitted tasks. During this time, calling isShutdown()
     * will return true, while isTerminated() will return false
     *
     * If a new task is submitted to the thread executor while it is shutting down, a RejectedExecutionException
     * will be thrown. Once all active tasks have been completed, isShutdown() and isTerminated() will both return true.
     * Table 18.2
     *
     *
     * What if you want to cancel all running and upcoming tasks? The ExecutorService provides a method called
     * shutdownNow(), which attempts to stop all running tasks and discards any that have not been started yet.
     * It is possible to create a thread that will never terminate, so any attempt to interrupt it may be ignored.
     * Lastly, shutdownNow() returns a List<Runnable> of tasks that were submitted to the thread executor but that
     * were never started.
     *
     * */

    /**
     * Submitting tasks:
     *
     * The first method we presented, execute(), is inherited from the Executor interface,
     * which the ExecutorService interface extends.
     * The execute() method takes a Runnable lambda expression or instance and completes the task asynchronously.
     * */
}
