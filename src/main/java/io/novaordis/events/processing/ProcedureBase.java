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

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public abstract class ProcedureBase implements Procedure {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    protected final AtomicLong invocationCount;
    protected volatile boolean endOfStream;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected ProcedureBase() {

        this.invocationCount = new AtomicLong(0L);
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public void process(List<Event> events) throws EventProcessingException {

        for(Event e: events) {

            process(e);
        }
    }

    /**
     * Override that does EOS accounting and invocation count incrementation. The method *will* send the EndOfStream
     * to subclass.
     */
    @Override
    public void process(Event e) throws EventProcessingException {

        invocationCount.incrementAndGet();

        if (endOfStream) {

            throw new IllegalStateException("event beyond EndOfStream");
        }

        if (e instanceof EndOfStreamEvent) {

            endOfStream = true;
        }

        process(invocationCount, e);
    }

    @Override
    public long getInvocationCount() {

        return invocationCount.get();
    }

    @Override
    public boolean isExitLoop() {

        //
        // all implementations indicate they want to keep receiving events by default. Subclasses may override if
        // they want a different behavior.
        //

        return endOfStream;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String s;

        List<String> labels = getCommandLineLabels();

        if (labels.isEmpty()) {

            s = getClass().getSimpleName() + " procedure";
        }
        else {

            return labels.get(0) + " procedure";
        }

        s += " " + Integer.toHexString(System.identityHashCode(this));

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * EndOfStream will be sent to subclasses, they may need to know when the stream ends.
     *
     * @param invocationCount is updated by the base class before invoking this method.
     */
    protected abstract void process(AtomicLong invocationCount, Event e) throws EventProcessingException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
