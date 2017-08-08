/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.events.processing;

import io.novaordis.events.api.event.Event;

import java.util.List;

/**
 * The implementations must have a public no-argument constructor, as they will be instantiated via reflection.
 *
 * @see DefaultProcedureFactory#find(String, int, List)
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public interface Procedure {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the list of equivalent labels that should be used on "events" components command line to invoke this
     * procedure. The labels are case sensitive, and the list must have at least one non-null element. The list
     * may specify long labels ("count") or abbreviated labels ("-c")
     */
    List<String> getCommandLineLabels();

    /**
     * Process an incoming event. If corresponding output events are produced, they will be offered for consumption
     * though means particular to a specific implementation.
     *
     * The implementation should be prepared to handle special events such as EndOfStreamEvent, etc.
     *
     * @exception EventProcessingException if processing of an individual event fail, but the procedure is able to
     * continue processing events.
     * @exception IllegalStateException if the procedure is not able to process events anymore, as it is the case
     * when receives an event after EndOfStreamEvent was processed.
     */
    void process(Event in) throws EventProcessingException;

    /**
     * Convenience method, has the same semantics as process(Event), just that it processes events in batches.
     *
     * @exception EventProcessingException if processing of an individual event fail, but the procedure is able to
     * continue processing events.
     * @exception IllegalStateException if the procedure is not able to process events anymore, as it is the case
     * when receives an event after EndOfStreamEvent was processed.
     */
    void process(List<Event> in) throws EventProcessingException;

    /**
     * The number of individual process() invocations since the instance was constructed.
     */
    long getInvocationCount();

    /**
     * @return true if the procedure signals to the enclosing loop it wants to exit the loop and avoid receiving
     * more events for processing. The event loop may ignore this result and keep sending events to the procedure,
     * which will most likely be ignored.
     */
    boolean isExitLoop();


}
