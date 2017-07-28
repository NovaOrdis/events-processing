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

package io.novaordis.events.processing.describe;

import io.novaordis.events.api.event.IntegerProperty;
import io.novaordis.events.api.event.MapProperty;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.processing.MockEvent;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.processing.ProcedureTest;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class DescribeTest extends ProcedureTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(DescribeTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // ProcedureFactory.find() -----------------------------------------------------------------------------------------

    @Test
    @Override
    public void procedureFactoryFind() throws Exception {

        Describe d = (Describe)ProcedureFactory.find(Describe.COMMAND_LINE_LABEL, 0, Collections.emptyList());
        assertNotNull(d);

        //
        // the instance must come with default configuration that should allow it to work correctly (albeit in a
        // simplest possible case)
        //

        OutputStream os = d.getOutputStream();
        assertNotNull(os);
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void commandLineLabel() throws Exception {

        Describe d = getProcedureToTest();

        List<String> cll = d.getCommandLineLabels();

        assertEquals(1, cll.size());
        assertEquals(Describe.COMMAND_LINE_LABEL, cll.get(0));
    }

    @Test
    public void initializedInstance() throws Exception {

        Describe d = new Describe();

        try {

            d.process(new MockEvent());
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("incorrectly initialized"));
            assertTrue(msg.contains("no output stream"));
        }
    }

    @Test
    public void happyPath() throws Exception {

        Describe d = getProcedureToTest();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        d.setOutputStream(baos);

        MockEvent me = new MockEvent();

        d.process(me);

        byte[] firstReading = baos.toByteArray();

        assertTrue(firstReading.length > 0);

        String s = new String(firstReading);
        assertTrue(s.contains("MockEvent"));

        //
        // process an event with the same signature, it should not produce extra output
        //

        MockEvent me2 = new MockEvent();

        d.process(me2);

        byte[] secondReading = baos.toByteArray();

        assertEquals(firstReading.length, secondReading.length);

        //
        // process an event with a different signature
        //

        MockEvent me3 = new MockEvent();
        me3.setStringProperty("mock-property", "something");

        d.process(me3);

        byte[] thirdReading = baos.toByteArray();

        assertTrue(thirdReading.length > secondReading.length);

        byte[] lastEvent = new byte[thirdReading.length - secondReading.length];
        System.arraycopy(thirdReading, secondReading.length, lastEvent, 0, lastEvent.length);

        String s2 = new String(lastEvent);

        assertTrue(s2.contains("MockEvent"));
        assertTrue(s2.contains("mock-property"));
        assertFalse(s2.contains("something"));

    }
    
    // getSignature() --------------------------------------------------------------------------------------------------

    @Test
    public void getSignature_String_Yaml() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        e.setProperty(new StringProperty("name1", "value1"));

        String s = Describe.getSignature(e, Describe.YAML);

        log.info(s);

        assertEquals("MockEvent\n  timestamp\n  name1(String)\n", s);
    }

    @Test
    public void getSignature_String_YamlInline() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        e.setProperty(new StringProperty("name1", "value1"));

        String s = Describe.getSignature(e, Describe.YAML_INLINE);

        log.info(s);

        assertEquals("MockEvent[timestamp, name1(String)]", s);
    }

    @Test
    public void getSignature_String_Integer_Yaml() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        e.setProperty(new StringProperty("name1", "value1"));
        e.setProperty(new IntegerProperty("name2", 2));

        String s = Describe.getSignature(e, Describe.YAML);

        log.info(s);

        assertEquals("MockEvent\n  timestamp\n  name1(String)\n  name2(Integer)\n", s);
    }

    @Test
    public void getSignature_String_Integer_YamlInline() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        e.setProperty(new StringProperty("name1", "value1"));
        e.setProperty(new IntegerProperty("name2", 2));

        String s = Describe.getSignature(e, Describe.YAML_INLINE);

        log.info(s);

        assertEquals("MockEvent[timestamp, name1(String), name2(Integer)]", s);
    }

    @Test
    public void getSignature_Map_NoElements_Yaml() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        //noinspection unchecked
        e.setProperty(new MapProperty("name1", new HashMap()));

        String s = Describe.getSignature(e, Describe.YAML);

        log.info(s);

        assertEquals("MockEvent\n  timestamp\n  name1(Map)\n    <empty>\n", s);
    }

    @Test
    public void getSignature_Map_NoElements_YamlInline() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        //noinspection unchecked
        e.setProperty(new MapProperty("name1", new HashMap()));

        String s = Describe.getSignature(e, Describe.YAML_INLINE);

        log.info(s);

        assertEquals("MockEvent[timestamp, name1(Map){}]", s);
    }

    @Test
    public void getSignature_Map_HasElements_Yaml() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        Map<String, Object> map = new HashMap<>();
        map.put("x", "val-x");
        map.put("a", "val-a");
        e.setProperty(new MapProperty("name1", map));

        String s = Describe.getSignature(e, Describe.YAML);

        log.info(s);

        assertEquals("MockEvent\n  timestamp\n  name1(Map)\n    a\n    x\n", s);
    }

    @Test
    public void getSignature_Map_HasElements_YamlInline() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        Map<String, Object> map = new HashMap<>();
        map.put("x", "val-x");
        map.put("a", "val-a");
        e.setProperty(new MapProperty("name1", map));

        String s = Describe.getSignature(e, Describe.YAML_INLINE);

        log.info(s);

        assertEquals("MockEvent[timestamp, name1(Map){a, x}]", s);
    }

    @Test
    public void getSignature_Map_HasElements_PropertyFollows_Yaml() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        Map<String, Object> map = new HashMap<>();
        map.put("x", "val-x");
        map.put("a", "val-a");
        e.setProperty(new MapProperty("name1", map));
        e.setProperty(new StringProperty("name2", "value2"));

        String s = Describe.getSignature(e, Describe.YAML);

        log.info(s);

        assertEquals("MockEvent\n  timestamp\n  name1(Map)\n    a\n    x\n  name2(String)\n", s);
    }

    @Test
    public void getSignature_Map_HasElements_PropertyFollows_YamlInline() throws Exception {

        MockEvent e = new MockEvent(new TimestampImpl(1));

        Map<String, Object> map = new HashMap<>();
        map.put("x", "val-x");
        map.put("a", "val-a");
        e.setProperty(new MapProperty("name1", map));
        e.setProperty(new StringProperty("name2", "value2"));

        String s = Describe.getSignature(e, Describe.YAML_INLINE);

        log.info(s);

        assertEquals("MockEvent[timestamp, name1(Map){a, x}, name2(String)]", s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Describe getProcedureToTest() throws Exception {

        Describe d = new Describe();
        d.setOutputStream(System.out);
        return d;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
