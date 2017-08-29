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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class OutputFormatFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void fromArguments_NullArguments() throws Exception {

        try {

            OutputFormatFactory.fromArguments(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null argument list"));
        }
    }

    @Test
    public void fromArguments_NoArguments() throws Exception {

        DefaultOutputFormat f = (DefaultOutputFormat)OutputFormatFactory.fromArguments(Collections.emptyList());
        assertNotNull(f);
    }

    @Test
    public void fromArguments_PropertyNames() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "green"));

        OutputFormatImpl f = (OutputFormatImpl)OutputFormatFactory.fromArguments(args);

        assertNotNull(f);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = f.format(ge);
        assertEquals("sky, apple, bean", s);
    }

    @Test
    public void fromArguments_PropertyIndices() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("0", "2", "5"));

        OutputFormatImpl f = (OutputFormatImpl)OutputFormatFactory.fromArguments(args);

        assertNotNull(f);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = f.format(ge);

        // a property with an index of 5 does not exist, hence the trailing comma

        assertEquals("sky, bean,", s);
    }

    @Test
    public void fromArguments_CombinationOfPropertyNamesAndIndices() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("0", "blue", "7", "green", "1"));

        OutputFormatImpl f = (OutputFormatImpl)OutputFormatFactory.fromArguments(args);

        assertNotNull(f);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = f.format(ge);
        assertEquals("sky, sky,, bean, apple", s);
    }

    @Test
    public void fromArguments_NoneOfNamesOrIndicesExist() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("777", "i-am-sure-there-is-no-such-property"));

        OutputFormatImpl f = (OutputFormatImpl)OutputFormatFactory.fromArguments(args);

        assertNotNull(f);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = f.format(ge);
        assertNull(s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
