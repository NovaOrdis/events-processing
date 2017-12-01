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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.api.event.GenericEvent;
import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.TimedEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class OutputFormatImplTest extends OutputFormatTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_Arguments() throws Exception {

        OutputFormatImpl f = new OutputFormatImpl("test-name", 3, "something-else");

        List<Object> identifiers = f.getPropertyIdentifiers();

        assertEquals(3, identifiers.size());
        assertEquals("test-name", identifiers.get(0));
        assertEquals(3, identifiers.get(1));
        assertEquals("something-else", identifiers.get(2));
    }

    @Test
    public void constructor_InvalidPropertyIdentifierType() throws Exception {

        try {

            new OutputFormatImpl("test-name", 7L, "something-else");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid property identifier"));
            assertTrue(msg.contains("7"));
            assertTrue(msg.contains("(java.lang.Long)"));
        }
    }

    @Test
    public void constructor_InvalidPropertyIndex() throws Exception {

        try {

            new OutputFormatImpl("test-name", -3, "something-else");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid property index"));
            assertTrue(msg.contains("-3"));
        }
    }

    // format() --------------------------------------------------------------------------------------------------------

    @Test
    public void format_OneProperty_EventDoesNotHaveIt() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");

        String s = f.format(new GenericEvent());
        assertNull(s);
    }

    @Test
    public void format_OneProperty_EventHasItButItIsNull() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", null);

        String s = f.format(e);
        assertNull(s);
    }

    @Test
    public void format_OneProperty_EventHasItButItIsEmptyString() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", "");

        String s = f.format(e);
        assertEquals("\n", s);
    }

    @Test
    public void format_OneProperty_EventHasIt() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", "bike");

        String s = f.format(e);
        assertEquals("bike\n", s);
    }

    @Test
    public void format_TwoProperties_EventDoesNotHaveThem() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");
        f.addPropertyName("green");

        GenericEvent e = new GenericEvent();

        String s = f.format(e);
        assertNull(s);
    }

    @Test
    public void format_TwoProperties_EventHasJustOne() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");
        f.addPropertyName("green");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", "bike");

        String s = f.format(e);
        assertEquals("bike,\n", s);
    }

    @Test
    public void format_TwoProperties_EventHasJustOne_ReverseOrder() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");
        f.addPropertyName("green");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("green", "leaf");

        String s = f.format(e);
        assertEquals(", leaf\n", s);
    }

    @Test
    public void format_TwoProperties_EventHasBoth() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");
        f.addPropertyName("green");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", "coffee");
        e.setStringProperty("green", "leaf");

        String s = f.format(e);
        assertEquals("coffee, leaf\n", s);
    }

    @Test
    public void format_formatHeader_TimedEvent_TimestampNotRequestedInFormat() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(1);

        GenericTimedEvent e = new GenericTimedEvent(777L);
        e.setStringProperty("A", "blue");

        String actual = f.format(e);

        String expected = f.getTimestampFormat().format(777L) + ", blue\n";
        assertEquals(expected, actual);

        String s2 = f.formatHeader(e);
        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", A\n", s2);
    }

    @Test
    public void format_formatHeader_TimedEvent_TimestampRequestedInFormat() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(0);
        f.addPropertyIndex(1);
        f.addPropertyName(TimedEvent.TIME_PROPERTY_NAME);

        GenericTimedEvent e = new GenericTimedEvent(777L);
        e.setStringProperty("A", "blue");

        String s = f.format(e);

        //
        // we don't convert the extra timestamp to its formatted representation on purpose, because we don't
        // think we'll ever need this. If we do, we will need to change this test.
        //
        assertEquals(f.getTimestampFormat().format(777L) + ", 777, blue, 777\n", s);

        String s2 = f.formatHeader(e);
        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", " +
                TimedEvent.TIME_PROPERTY_NAME + ", A, " + TimedEvent.TIME_PROPERTY_NAME + "\n", s2);
    }

    @Test
    public void format_formatHeader_NonTimedEvent_TimestampNotRequestedInFormat() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(0);

        GenericEvent e = new GenericEvent();
        e.setStringProperty("A", "blue");

        String s = f.format(e);
        assertEquals("blue\n", s);

        String s2 = f.formatHeader(e);
        assertEquals("# A\n", s2);
    }

    @Test
    public void format_formatHeader_NonTimedEvent_TimestampRequestedInFormat() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(0);
        f.addPropertyName(TimedEvent.TIME_PROPERTY_NAME);

        GenericEvent e = new GenericEvent();
        e.setStringProperty("A", "blue");

        String s = f.format(e);
        assertEquals("blue,\n", s);

        String s2 = f.formatHeader(e);
        assertEquals("# A, " + TimedEvent.TIME_PROPERTY_NAME + "\n", s2);
    }

    @Test
    public void format_formatHeader_TimedEvent_TimestampNotRequestedInFormat_FormatDoesNotMatch() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("A");

        GenericTimedEvent e = new GenericTimedEvent(777L);
        e.setStringProperty("B", "blue");

        String s = f.format(e);
        assertNull(s);

        String s2 = f.formatHeader(e);
        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", A\n", s2);
    }

    @Test
    public void format_formatHeader_TimedEvent_TimestampRequestedInFormat_RestOfFormatDoesNotMatch() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();

        f.addPropertyName("A");

        f.addPropertyName(TimedEvent.TIME_PROPERTY_NAME);

        GenericTimedEvent e = new GenericTimedEvent(777L);

        e.setStringProperty("B", "blue");

        String s = f.format(e);

        //
        // we don't convert the extra timestamp to its formatted representation on purpose, because we don't
        // think we'll ever need this. If we do, we will need to change this test.
        //
        assertEquals(f.getTimestampFormat().format(777L) + ", , 777\n", s);

        String s2 = f.formatHeader(e);
        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", A, " + TimedEvent.TIME_PROPERTY_NAME + "\n", s2);
    }

    @Test
    public void format_formatHeader_NonTimedEvent_TimestampNotRequestedInFormat_FormatDoesNotMatch() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("A");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("B", "blue");

        String s = f.format(e);
        assertNull(s);

        String s2 = f.formatHeader(e);
        assertEquals("# A\n", s2);
    }

    @Test
    public void format_formatHeader_NonTimedEvent_TimestampRequestedInFormat_FormatDoesNotMatch() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("A");
        f.addPropertyName(TimedEvent.TIME_PROPERTY_NAME);

        GenericEvent e = new GenericEvent();
        e.setStringProperty("B", "blue");

        String s = f.format(e);
        assertNull(s);

        String s2 = f.formatHeader(e);
        assertEquals("# A, " + TimedEvent.TIME_PROPERTY_NAME + "\n", s2);
    }

    // separator -------------------------------------------------------------------------------------------------------

    @Test
    public void separator() throws Exception {

        OutputFormatImpl f = new OutputFormatImpl();
        assertEquals("" + OutputFormatImpl.DEFAULT_SEPARATOR, f.getSeparator());
    }

    // header ----------------------------------------------------------------------------------------------------------

    @Test
    public void getHeader_TimedEvent_NoFields() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(1000L);
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl();

        String header = f.formatHeader(e);

        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + "\n", header);
    }

    @Test
    public void getHeader_TimedEvent_OneField() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(1000L);
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl("test-property");

        String header = f.formatHeader(e);

        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", test-property\n", header);
    }

    @Test
    public void getHeader_TimedEvent_TwoFields_EventContainsMoreProperties() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(1000L);
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl("test-property", "test-property2");

        String header = f.formatHeader(e);

        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", test-property, test-property2\n", header);
    }

    @Test
    public void getHeader_TimedEvent_TwoFields_EventContainsFewerProperties() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(1000L);
        e.setStringProperty("test-property", "test-property value");

        OutputFormatImpl f = new OutputFormatImpl("test-property", "test-property2");

        String header = f.formatHeader(e);

        assertEquals("# " + TimedEvent.TIME_PROPERTY_NAME + ", test-property, test-property2\n", header);
    }

    @Test
    public void getHeader_NonTimedEvent_NoFields() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl();

        String header = f.formatHeader(e);

        assertEquals("# \n", header);
    }

    @Test
    public void getHeader_NonTimedEvent_OneField() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl("test-property");

        String header = f.formatHeader(e);

        assertEquals("# test-property\n", header);
    }

    @Test
    public void getHeader_NonTimedEvent_TwoFields_EventContainsMoreProperties() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("test-property", "test-property value");
        e.setStringProperty("test-property2", "test-property2 value");
        e.setStringProperty("test-property3", "test-property3 value");

        OutputFormatImpl f = new OutputFormatImpl("test-property", "test-property2");

        String header = f.formatHeader(e);

        assertEquals("# test-property, test-property2\n", header);
    }

    @Test
    public void getHeader_NonTimedEvent_TwoFields_EventContainsFewerProperties() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("test-property", "test-property value");

        OutputFormatImpl f = new OutputFormatImpl("test-property", "test-property2");

        String header = f.formatHeader(e);

        assertEquals("# test-property, test-property2\n", header);
    }

    // addPropertyName()/addPropertyIndex() ----------------------------------------------------------------------------

    @Test
    public void addPropertyIndex_NegativeIndex() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();

        try {

            f.addPropertyIndex(-1);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid property index"));
            assertTrue(msg.contains("-1"));
        }
    }

    @Test
    public void addPropertyIndex() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(0);
    }

    @Test
    public void addPropertyName() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyIndex(0);
    }

    @Test
    public void addPropertyName_combinedWith_addPropertyIndex() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();

        f.addPropertyIndex(0);
        f.addPropertyName("something");
        f.addPropertyIndex(5);
        f.addPropertyName("something else");
    }

    @Test
    public void useAddPropertyNameToAddAnInteger() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();

        try {

            // this usually indicates a programming error, or the fact that the programmer did not understand
            // the naming scheme
            f.addPropertyName("0");
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid attempt to add a property index as property name"));
            assertTrue(msg.contains("consider using addPropertyIndex()"));
        }
    }

    // getTimestampFormat()/setTimestampFormat() -----------------------------------------------------------------------

    @Test
    public void getTimestampFormat_Default() throws Exception {

        OutputFormatImpl f = new OutputFormatImpl();

        DateFormat tf = f.getTimestampFormat();
        assertEquals(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT, tf);
    }

    @Test
    public void setTimestampFormat_getTimestampFormat() throws Exception {

        OutputFormatImpl f = new OutputFormatImpl();

        SimpleDateFormat tf = new SimpleDateFormat("M");

        f.setTimestampFormat(tf);

        assertEquals(tf, f.getTimestampFormat());

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected OutputFormatImpl getOutputFormatToTest() throws Exception {

        return new OutputFormatImpl();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
