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

import org.junit.Test;

import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.MockTimedEvent;
import io.novaordis.utilities.time.TimestampImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class DefaultOutputFormatTest extends OutputFormatTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // format() --------------------------------------------------------------------------------------------------------

    @Test
    public void format_PreferredRepresentationPresent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();
        me.setPreferredRepresentation("A");
        me.setRawRepresentation("B");

        String s = f.format(me);
        assertEquals("A", s);
    }

    @Test
    public void format_RawRepresentationPresent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();
        assertNull(me.getPreferredRepresentation("does not matter"));
        me.setRawRepresentation("something");

        String s = f.format(me);
        assertEquals("something\n", s);
    }

    @Test
    public void format_RawRepresentationNotPresent_NonTimedEvent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();
        assertNull(me.getRawRepresentation());

        String s = f.format(me);
        assertEquals("MockEvent\n", s);
    }

    @Test
    public void format_RawRepresentationNotPresent_TimedEvent_NullTimestamp() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockTimedEvent me = new MockTimedEvent(null);
        assertNull(me.getTime());
        assertNull(me.getTimestamp());
        assertNull(me.getRawRepresentation());

        String s = f.format(me);

        assertEquals("MockTimedEvent\n", s);
    }

    @Test
    public void format_RawRepresentationNotPresent_TimedEvent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockTimedEvent me = new MockTimedEvent(new TimestampImpl(10L));
        assertNull(me.getRawRepresentation());

        String s = f.format(me);

        assertEquals("MockTimedEvent\n", s);
    }

    // formatHeader() --------------------------------------------------------------------------------------------------

    @Test
    public void getHeader_NullEvent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        try {

            f.formatHeader(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null event"));
        }
    }

    @Test
    public void getHeader_EventHasPreferredRepresentation_HasPreferredRepresentationHeader() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();

        me.setPreferredRepresentation("blue");
        me.setPreferredRepresentationHeader("red");

        String header = f.formatHeader(me);

        assertEquals("red", header);
    }

    @Test
    public void getHeader_EventHasPreferredRepresentation_DoesNotHavePreferredRepresentationHeader() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();

        me.setPreferredRepresentation("blue");
        me.setPreferredRepresentationHeader(null);

        String header = f.formatHeader(me);

        assertNull(header);
    }

    @Test
    public void getHeader_EventHasRawRepresentation() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();

        me.setPreferredRepresentation(null);
        me.setRawRepresentation("blue");

        String header = f.formatHeader(me);

        assertNull(header);
    }

    @Test
    public void getHeader_EventDoesNotHaveAnything() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();

        me.setPreferredRepresentation(null);
        me.setRawRepresentation(null);

        String header = f.formatHeader(me);

        assertEquals("event type", header);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected DefaultOutputFormat getOutputFormatToTest() throws Exception {

        return new DefaultOutputFormat();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
