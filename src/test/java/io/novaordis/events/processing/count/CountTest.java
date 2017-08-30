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

package io.novaordis.events.processing.count;

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.processing.DefaultProcedureFactory;
import io.novaordis.events.processing.MockTimedEvent;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.TextOutputProcedureTest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class CountTest extends TextOutputProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void procedureFactoryFind() throws Exception {

        ProcedureFactory f = new DefaultProcedureFactory(null);

        Count p = (Count) f.find(Count.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p);

        //
        // make sure the instance is correctly initialized
        //

        assertTrue(System.out.equals(p.getOutputStream()));

        Count p2 = (Count) f.find(Count.ABBREVIATED_COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p2);

        //
        // make sure the instance is correctly initialized
        //

        assertTrue(System.out.equals(p2.getOutputStream()));

    }

    @Test
    @Override
    public void commandLineLabel() throws Exception {

        Count d = getTextOutputProcedureToTest(null);

        List<String> commandLineLabels = d.getCommandLineLabels();
        assertEquals(2, commandLineLabels.size());
        assertTrue(commandLineLabels.contains(Count.COMMAND_LINE_LABEL));
        assertTrue(commandLineLabels.contains(Count.ABBREVIATED_COMMAND_LINE_LABEL));
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void count() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Count d = getTextOutputProcedureToTest(baos);

        assertEquals(0L, d.getCount());

        assertTrue(baos.toByteArray().length == 0);

        MockTimedEvent me = new MockTimedEvent();

        d.process(me);

        assertEquals(1L, d.getCount());

        assertTrue(baos.toByteArray().length == 0);

        MockTimedEvent me2 = new MockTimedEvent();

        d.process(me2);

        assertEquals(2L, d.getCount());

        assertTrue(baos.toByteArray().length == 0);

        //
        // EndOfStream
        //

        d.process(new EndOfStreamEvent());

        assertEquals(2L, d.getCount());

        String expected = "2\n";

        assertEquals(expected, new String(baos.toByteArray()));
    }
    
    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Count getTextOutputProcedureToTest(OutputStream os) throws Exception {

        return new Count(os);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
