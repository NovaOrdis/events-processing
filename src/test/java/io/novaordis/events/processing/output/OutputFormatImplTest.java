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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

        OutputFormatImpl f = new OutputFormatImpl("test-name");

        List<String> names = f.getPropertyNames();

        assertEquals(1, names.size());
        assertEquals("test-name", names.get(0));
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
        assertTrue(s.isEmpty());
    }

    @Test
    public void format_OneProperty_EventHasIt() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("blue", "bike");

        String s = f.format(e);
        assertEquals("bike", s);
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
        assertEquals("bike,", s);
    }

    @Test
    public void format_TwoProperties_EventHasJustOne_ReverseOrder() throws Exception {

        OutputFormatImpl f = getOutputFormatToTest();
        f.addPropertyName("blue");
        f.addPropertyName("green");

        GenericEvent e = new GenericEvent();
        e.setStringProperty("green", "leaf");

        String s = f.format(e);
        assertEquals(", leaf", s);
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
        assertEquals("coffee, leaf", s);
    }

    // separator -------------------------------------------------------------------------------------------------------

    @Test
    public void separator() throws Exception {

        OutputFormatImpl f = new OutputFormatImpl();
        assertEquals("" + OutputFormatImpl.DEFAULT_SEPARATOR, f.getSeparator());
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
