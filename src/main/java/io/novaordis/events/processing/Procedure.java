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
 * @see ProcedureFactory#find(String)
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public interface Procedure {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the label that should be used on "events" components command line to invoke this procedure. It is
     * case sensitive.
     */
    String getCommandLineLabel();

    /**
     * Process an incoming event. If corresponding output events are produced, they will be offered for consumption
     * though means particular to a specific implementation.
     *
     * Implementations are advised to increment the invocation count counter upon each invocation, otherwise the
     * counter returned with getInvocationCount() will not be accurate.
     */
    void process(Event in) throws EventProcessingException;

    /**
     * Convenience method, has the same semantics as process(Event), just that it processes events in batches.
     */
    void process(List<Event> in) throws EventProcessingException;

    /**
     * The number of individual process() invocations since the instance was constructed.
     */
    long getInvocationCount();


}
