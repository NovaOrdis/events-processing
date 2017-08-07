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
import io.novaordis.events.processing.DefaultProcedureFactory;
import io.novaordis.events.processing.MockTimedEvent;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.TextOutputProcedureTest;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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

        ProcedureFactory f = new DefaultProcedureFactory();

        Output p = (Output) f.find(Output.COMMAND_LINE_LABEL, 0, Collections.emptyList());
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

        Output p = getTextOutputProcedureToTest(System.out);

        List<String> commandLineLabels = p.getCommandLineLabels();

        assertEquals(1, commandLineLabels.size());
        assertEquals(Output.COMMAND_LINE_LABEL, commandLineLabels.get(0));
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // extra procedureFactoryFind() tests ------------------------------------------------------------------------------

    @Test
    public void procedureFactoryFind_ExtraArguments() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("output", "-o", "something"));

        ProcedureFactory f = new DefaultProcedureFactory();

        Output p = (Output) f.find("output", 1, args);
        assertNotNull(p);

        assertEquals(1, args.size());
        assertEquals("output", args.get(0));

        OutputFormat outputFormat = p.getFormat();
        assertFalse(outputFormat instanceof DefaultOutputFormat);

        String s = outputFormat.format(
                new GenericEvent(Collections.singletonList(new StringProperty("something", "else"))));

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

    // process() -------------------------------------------------------------------------------------------------------

    @Test
    public void process_RepresentationMustAlwaysStartWitTimestampWhenFormatPresent_FormattingMatches()
            throws Exception {

        Output o = getTextOutputProcedureToTest(null);
        o.setOutputHeader(false);

        OutputFormatImpl f = new OutputFormatImpl("mock-property");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        o.setOutputStream(baos);
        o.setOutputFormat(f);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("mock-property", "mock-value");

        e.setTimestamp(new TimestampImpl(10L));

        o.process(e);

        String result = new String(baos.toByteArray());

        assertTrue(result.startsWith(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT.format(10L)));
        assertTrue(result.contains("mock-value"));
    }

    @Test
    public void process_FormattingDoesNotMatch() throws Exception {

        Output o = getTextOutputProcedureToTest(null);

        OutputFormatImpl f = new OutputFormatImpl("some-property");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        o.setOutputStream(baos);
        o.setOutputFormat(f);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("some-other-property", "mock-value");

        e.setTimestamp(new TimestampImpl(10L));

        //
        // the output format does not match
        //

        o.process(e);

        //
        // no output
        //
        assertTrue(baos.toByteArray().length == 0);
    }

    @Test
    public void process_FormatManufacturesLinesThatStartWithALeadingTimestamp() throws Exception {

        Output o = getTextOutputProcedureToTest(null);
        o.setOutputHeader(false);

        MockOutputFormat mof = new MockOutputFormat();
        mof.setLeadingTimestamp(true);
        mof.addMatchingProperty("mock-property");
        mof.setTimestampFormat(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        o.setOutputStream(baos);
        o.setOutputFormat(mof);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("mock-property", "mock-value");
        e.setTimestamp(new TimestampImpl(10L));

        //
        // the mock output will start the line with a timestamp and will also include the value of the property that
        // matches
        //

        o.process(e);

        String result = new String(baos.toByteArray());

        assertTrue(result.startsWith(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT.format(10L)));
        assertTrue(result.contains("mock-value"));
    }

    @Test
    public void process_FormatManufacturesLinesThatDoNOTStartWithALeadingTimestamp() throws Exception {

        Output o = getTextOutputProcedureToTest(null);
        o.setOutputHeader(false);

        MockOutputFormat mof = new MockOutputFormat();
        mof.setLeadingTimestamp(false);
        mof.addMatchingProperty("mock-property");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        o.setOutputStream(baos);
        o.setOutputFormat(mof);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("mock-property", "mock-value");
        e.setTimestamp(new TimestampImpl(10L));

        //
        // the mock output will NOT start the line with a timestamp but will include the value of the property that
        // matches
        //

        o.process(e);

        String result = new String(baos.toByteArray());

        //
        // the mock format does NOT add a timestamp but Output does:
        //

        assertEquals(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT.format(10L) + " mock-value", result.trim());
    }

    @Test
    public void process_GapInEventFlow_SomeEventsDoNotMatchTheOutputFilter() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Output o = getTextOutputProcedureToTest(baos);
        o.setOutputHeader(false);

        OutputFormatImpl of = new OutputFormatImpl("test-property");
        o.setOutputFormat(of);
        o.setTimestampFormat(new SimpleDateFormat("s"));

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test-property", "A");
        e.setTimestamp(new TimestampImpl(1000L));

        GenericTimedEvent e2 = new GenericTimedEvent();
        // missing 'test-property'
        e2.setTimestamp(new TimestampImpl(2000L));

        GenericTimedEvent e3 = new GenericTimedEvent();
        e3.setStringProperty("test-property", "C");
        e3.setTimestamp(new TimestampImpl(3000L));

        o.process(Arrays.asList(e, e2, e3));

        String result = new String(baos.toByteArray());
        assertEquals("1, A\n3, C\n", result);

    }

    // header tests ----------------------------------------------------------------------------------------------------

    @Test
    public void process_FormatDoesNotProvideHeader() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Output o = getTextOutputProcedureToTest(baos);
        o.setTimestampFormat(new SimpleDateFormat("s"));

        assertTrue(o.isOutputHeader());

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test-property", "A");
        e.setTimestamp(new TimestampImpl(1000L));

        MockOutputFormat mof = new MockOutputFormat();
        mof.addMatchingProperty("test-property");
        mof.setProvidingHeader(false);
        assertNull(mof.getHeader(e));
        o.setOutputFormat(mof);

        o.process(e);

        String result = new String(baos.toByteArray());
        assertEquals("1 A\n", result);
    }

    @Test
    public void process_FormatDoesProvideHeader() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Output o = getTextOutputProcedureToTest(baos);
        o.setTimestampFormat(new SimpleDateFormat("s"));

        assertTrue(o.isOutputHeader());

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test-property", "A");
        e.setTimestamp(new TimestampImpl(1000L));

        OutputFormatImpl of = new OutputFormatImpl("test-property");

        String header = of.getHeader(e);

        assertEquals("test-property", header);
        o.setOutputFormat(of);

        o.process(e);

        String result = new String(baos.toByteArray());
        assertEquals("# timestamp, test-property\n1, A\n", result);
    }

    @Test
    public void process_FormatDoesProvideHeader_OutputDoesNotPrintIt() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Output o = getTextOutputProcedureToTest(baos);
        o.setTimestampFormat(new SimpleDateFormat("s"));
        o.setOutputHeader(false);

        assertFalse(o.isOutputHeader());

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test-property", "A");
        e.setTimestamp(new TimestampImpl(1000L));

        OutputFormatImpl of = new OutputFormatImpl("test-property");
        String header = of.getHeader(e);
        assertEquals("test-property", header);
        o.setOutputFormat(of);

        o.process(e);

        String result = new String(baos.toByteArray());
        assertEquals("1, A\n", result);
    }

    @Test
    public void process_DefaultOutputFormat_PreferredRepresentationHeader() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Output o = getTextOutputProcedureToTest(baos);
        o.setTimestampFormat(new SimpleDateFormat("s"));

        assertTrue(o.isOutputHeader());

        assertTrue(o.getFormat() instanceof DefaultOutputFormat);

        MockTimedEvent mte = new MockTimedEvent();
        mte.setPreferredRepresentation("blue");
        mte.setPreferredRepresentationHeader("red");

        o.process(mte);

        String result = new String(baos.toByteArray());
        assertEquals("# red\nblue\n", result);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Output getTextOutputProcedureToTest(OutputStream os) throws Exception {

        return new Output(os);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
