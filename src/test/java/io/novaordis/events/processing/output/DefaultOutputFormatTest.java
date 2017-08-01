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

import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.MockTimedEvent;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void format_Null() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        String s = f.format(null);
        assertEquals("null", s);
    }

    @Test
    public void format_RawRepresentationPresent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();
        me.setRawRepresentation("something");

        String s = f.format(me);
        assertEquals("something", s);
    }

    @Test
    public void format_RawRepresentationNotPresent_NonTimedEvent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockEvent me = new MockEvent();
        assertNull(me.getRawRepresentation());

        String s = f.format(me);
        assertEquals("MockEvent", s);
    }

    @Test
    public void format_RawRepresentationNotPresent_TimedEvent() throws Exception {

        DefaultOutputFormat f = getOutputFormatToTest();

        MockTimedEvent me = new MockTimedEvent(new TimestampImpl(10L));
        assertNull(me.getRawRepresentation());

        String s = f.format(me);

        assertEquals(DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT.format(10L) + " MockTimedEvent", s);
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
