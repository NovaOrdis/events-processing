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

package io.novaordis.events.processing.exclude;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.TextOutputProcedureTest;
import io.novaordis.events.query.MatchNone;
import io.novaordis.events.query.NullQuery;
import io.novaordis.events.query.Query;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class ExcludeTest extends TextOutputProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // ProcedureFactory.find() -----------------------------------------------------------------------------------------

    @Test
    @Override
    public void procedureFactoryFind() throws Exception {

        Exclude e = (Exclude)ProcedureFactory.find(Exclude.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(e);

        //
        // the instance must come with default configuration that should allow it to work correctly (albeit in a
        // simplest possible case)
        //

        OutputStream os = e.getOutputStream();
        assertNotNull(os);

        Exclude e2 = (Exclude)ProcedureFactory.find(Exclude.ABBREVIATED_COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(e2);

        //
        // the instance must come with default configuration that should allow it to work correctly (albeit in a
        // simplest possible case)
        //

        OutputStream os2 = e2.getOutputStream();
        assertNotNull(os2);
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void commandLineLabel() throws Exception {

        Exclude e = getTextOutputProcedureToTest(true);

        List<String> clls = e.getCommandLineLabels();

        assertEquals(2, clls.size());
        assertTrue(clls.contains(Exclude.COMMAND_LINE_LABEL));
        assertTrue(clls.contains(Exclude.ABBREVIATED_COMMAND_LINE_LABEL));
    }

    @Test
    public void nonInitializedInstance_NoOutputStream() throws Exception {

        Exclude p = new Exclude();
        p.setQuery(new MatchNone());

        try {

            p.process(new MockEvent());
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("was not initialized"));
            assertTrue(msg.contains("no output stream"));
        }
    }

    @Test
    public void nonInitializedInstance_NoQuery() throws Exception {

        Exclude p = new Exclude();

        try {

            p.process(new MockEvent());
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("was not initialized"));
            assertTrue(msg.contains("no query"));
        }
    }

    @Test
    public void process() throws Exception {

        Exclude e = getTextOutputProcedureToTest(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        e.setOutputStream(baos);

        MockEvent willMatch = new MockEvent();
        willMatch.setRawRepresentation("we will never see this");
        
        MockEvent wontMatch = new MockEvent();
        wontMatch.setRawRepresentation("something");


        e.setQuery(event -> event == willMatch);

        e.process(willMatch);

        //
        // because it matches the query, the event is not displayed
        //

        assertEquals(0, baos.toByteArray().length);

        e.process(wontMatch);

        //
        // because it does not match the query, the event will be displayed
        //

        String s = new String(baos.toByteArray());

        assertEquals("something\n", s);
    }

    // setQuery() ------------------------------------------------------------------------------------------------------

    @Test
    public void setQuery_Null() throws Exception {

        Exclude ex = getTextOutputProcedureToTest(true);

        try {

            ex.setQuery(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.equals("null query"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Exclude getTextOutputProcedureToTest(boolean initialized) throws Exception {

        Exclude p = new Exclude();

        if (initialized) {

            p.setOutputStream(System.out);
            p.setQuery(new NullQuery());
        }

        return p;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
