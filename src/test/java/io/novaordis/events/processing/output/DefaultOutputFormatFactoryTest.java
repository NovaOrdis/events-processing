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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.api.event.GenericEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class DefaultOutputFormatFactoryTest extends OutputFormatFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void fromArguments_NullArguments() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        try {

            f.fromArguments(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null argument list"));
        }
    }

    @Test
    public void fromArguments_NoArguments() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        DefaultOutputFormat fmt = (DefaultOutputFormat)f.fromArguments(Collections.emptyList());
        assertNotNull(fmt);
    }

    @Test
    public void fromArguments_PropertyNames() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        List<String> args = new ArrayList<>(Arrays.asList("blue", "red", "green"));

        OutputFormatImpl fmt = (OutputFormatImpl) f.fromArguments(args);

        assertNotNull(fmt);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = fmt.format(ge);
        assertEquals("sky, apple, bean\n", s);
    }

    @Test
    public void fromArguments_PropertyIndices() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        List<String> args = new ArrayList<>(Arrays.asList("0", "2", "5"));

        OutputFormatImpl fmt = (OutputFormatImpl) f.fromArguments(args);

        assertNotNull(fmt);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = fmt.format(ge);

        // a property with an index of 5 does not exist, hence the trailing comma

        assertEquals("sky, bean,\n", s);
    }

    @Test
    public void fromArguments_CombinationOfPropertyNamesAndIndices() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        List<String> args = new ArrayList<>(Arrays.asList("0", "blue", "7", "green", "1"));

        OutputFormatImpl fmt = (OutputFormatImpl) f.fromArguments(args);

        assertNotNull(fmt);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = fmt.format(ge);
        assertEquals("sky, sky,, bean, apple\n", s);
    }

    @Test
    public void fromArguments_NoneOfNamesOrIndicesExist() throws Exception {

        DefaultOutputFormatFactory f = getOutputFormatFactoryToTest();

        List<String> args = new ArrayList<>(Arrays.asList("777", "i-am-sure-there-is-no-such-property"));

        OutputFormatImpl fmt = (OutputFormatImpl) f.fromArguments(args);

        assertNotNull(fmt);

        assertTrue(args.isEmpty());

        GenericEvent ge = new GenericEvent();
        ge.setStringProperty("blue", "sky");
        ge.setStringProperty("red", "apple");
        ge.setStringProperty("green", "bean");

        String s = fmt.format(ge);
        assertNull(s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected DefaultOutputFormatFactory getOutputFormatFactoryToTest() throws Exception {

        return new DefaultOutputFormatFactory();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
