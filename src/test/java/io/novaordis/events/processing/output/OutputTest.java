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

package io.novaordis.events.processing.output;

import io.novaordis.events.api.event.GenericEvent;
import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.TextOutputProcedureTest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class OutputTest extends TextOutputProcedureTest {

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

        Output p = (Output)ProcedureFactory.find(Output.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(p);

        //
        // the instance must come with default configuration that should allow it to work correctly (albeit in a
        // simplest possible case)
        //

        OutputStream os = p.getOutputStream();
        assertNotNull(os);
    }

    @Test
    @Override
    public void commandLineLabel() throws Exception {

        Output p = getTextOutputProcedureToTest(true);

        List<String> commandLineLabels = p.getCommandLineLabels();

        assertEquals(1, commandLineLabels.size());
        assertEquals(Output.COMMAND_LINE_LABEL, commandLineLabels.get(0));
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // extra procedureFactoryFind() tests ------------------------------------------------------------------------------

    @Test
    public void procedureFactoryFind_ExtraArguments() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("output", "-o", "something"));

        Output p = (Output)ProcedureFactory.find("output", 1, args);
        assertNotNull(p);

        assertEquals(1, args.size());
        assertEquals("output", args.get(0));

        OutputFormat f = p.getFormat();
        assertFalse(f instanceof DefaultOutputFormat);

        String s = f.format(new GenericEvent(Collections.singletonList(new StringProperty("something", "else"))));

        assertTrue(s.contains("else"));
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_UninitializedOutput() throws Exception {

        Output o = new Output();
        assertNull(o.getOutputStream());

    }

    @Test
    public void constructor_InitializedOutput() throws Exception {

        OutputStream os = new ByteArrayOutputStream();

        Output o = new Output(os);

        assertEquals(os, o.getOutputStream());
    }

    // output format ---------------------------------------------------------------------------------------------------

    @Test
    public void nonNullOutputFormat() throws Exception {

        Output o = new Output();
        assertNotNull(o.getFormat());
    }

    // configureFromCommandLine ----------------------------------------------------------------------------------------

    @Test
    public void configureFromCommandLine_NoArguments() throws Exception {

        Output o = new Output();

        o.configureFromCommandLine(0, Collections.emptyList());

        assertTrue(o.getFormat() instanceof DefaultOutputFormat);
    }

    @Test
    public void configureFromCommandLine_UnknownArguments() throws Exception {

        Output o = new Output();

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "green"));

        o.configureFromCommandLine(0, args);

        assertTrue(o.getFormat() instanceof DefaultOutputFormat);

        assertEquals(3, args.size());
        assertEquals("blue", args.get(0));
        assertEquals("red", args.get(1));
        assertEquals("green", args.get(2));
    }

    @Test
    public void configureFromCommandLine_UnknownArguments_From() throws Exception {

        Output o = new Output();

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "green"));

        o.configureFromCommandLine(1, args);

        assertTrue(o.getFormat() instanceof DefaultOutputFormat);

        assertEquals(3, args.size());
        assertEquals("blue", args.get(0));
        assertEquals("red", args.get(1));
        assertEquals("green", args.get(2));
    }

    @Test
    public void configureFromCommandLine_PartiallyKnownArguments() throws Exception {

        Output o = new Output();

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "-o", "green", "yellow"));

        o.configureFromCommandLine(0, args);

        assertEquals(2, args.size());
        assertEquals("blue", args.get(0));
        assertEquals("red", args.get(1));

        OutputFormat f = o.getFormat();

        assertFalse(f instanceof DefaultOutputFormat);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("green", "box");
        e.setStringProperty("yellow", "cat");

        String formatted = f.format(e);
        assertEquals("box, cat", formatted);
    }

    @Test
    public void configureFromCommandLine_PartiallyKnownArguments_From() throws Exception {

        Output o = new Output();

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "-o", "green", "yellow"));

        o.configureFromCommandLine(1, args);

        assertEquals(2, args.size());
        assertEquals("blue", args.get(0));
        assertEquals("red", args.get(1));

        OutputFormat f = o.getFormat();

        assertFalse(f instanceof DefaultOutputFormat);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("green", "box");
        e.setStringProperty("yellow", "cat");

        String formatted = f.format(e);
        assertEquals("box, cat", formatted);
    }

    @Test
    public void configureFromCommandLine_OnlyKnownArguments() throws Exception {

        Output o = new Output();

        List<String> args = new ArrayList<>(Arrays.asList("-o", "green", "yellow"));

        o.configureFromCommandLine(0, args);

        assertTrue(args.isEmpty());

        OutputFormat f = o.getFormat();

        assertFalse(f instanceof DefaultOutputFormat);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("green", "box");
        e.setStringProperty("yellow", "cat");

        String formatted = f.format(e);
        assertEquals("box, cat", formatted);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Output getTextOutputProcedureToTest(boolean initialized) throws Exception {

        Output p = new Output();

        if (initialized) {

            p.setOutputStream(System.out);
        }

        return p;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
