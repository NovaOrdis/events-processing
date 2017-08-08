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

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public abstract class ProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    /**
     * Each implementation must make sure it is built by ProcedureFactory.find().
     */
    @Test
    public abstract void procedureFactoryFind() throws Exception;

    /**
     * Each implementation must test its command line label(s).
     */
    @Test
    public abstract void commandLineLabel() throws Exception;

    // process() -------------------------------------------------------------------------------------------------------

    @Test
    public void processListOfEvents() throws Exception {

        Procedure p = getProcedureToTest();

        assertEquals(0, p.getInvocationCount());

        MockTimedEvent me = new MockTimedEvent();
        MockTimedEvent me2 = new MockTimedEvent();

        p.process(Arrays.asList(me, me2));

        assertEquals(2, p.getInvocationCount());

        assertFalse(p.isExitLoop());
    }

    @Test
    public void process_EndOfStreamEvent() throws Exception {

        Procedure p = getProcedureToTest();

        assertEquals(0, p.getInvocationCount());

        EndOfStreamEvent eos = new EndOfStreamEvent();

        p.process(eos);

        assertTrue(p.isExitLoop());

        //
        // an attempt to process an event after EOS was processed should thrown IllegalStateException
        //

        try {

            p.process(new MockTimedEvent());
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("event beyond EndOfStream"));
        }

        assertTrue(p.isExitLoop());
    }

    @Test
    public void process_EndOfStreamEventInList() throws Exception {

        Procedure p = getProcedureToTest();

        assertEquals(0, p.getInvocationCount());

        List<Event> events = Arrays.asList(new EndOfStreamEvent(), new MockTimedEvent());

        //
        // an attempt to process an event after EOS was processed should thrown IllegalStateException
        //

        try {

            p.process(events);
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("event beyond EndOfStream"));
        }

        assertTrue(p.isExitLoop());
    }

    // isExitLoop() ----------------------------------------------------------------------------------------------------

    @Test
    public void isExitLoop() throws Exception {

        Procedure p = getProcedureToTest();
        assertFalse(p.isExitLoop());
    }

    // miscellaneous ---------------------------------------------------------------------------------------------------

    @Test
    public void implementationHasANoArgumentConstructor() throws Exception {

        Procedure p = getProcedureToTest();

        // public no-argument constructor
        Constructor c = p.getClass().getConstructor();

        assertNotNull(c);
    }

    @Test
    public void atLeastOneNonNullCommandLineLabel() throws Exception {

        Procedure p = getProcedureToTest();
        List<String> commandLineLabels = p.getCommandLineLabels();
        assertTrue(commandLineLabels.size() >= 1);
        //noinspection Convert2streamapi
        for(String s: commandLineLabels) {
            assertNotNull(s);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Procedure getProcedureToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
