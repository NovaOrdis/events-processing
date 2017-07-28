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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.MapProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.TextOutputProcedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A procedure that looks at a stream of incoming events, and writes to the given OutputStream a description of distinct
 * events, as they arrive.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class Describe extends TextOutputProcedure {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String COMMAND_LINE_LABEL = "describe";

    public static final boolean YAML = true;

    public static final boolean YAML_INLINE = false;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @param yamlStandard if true, the output will use standard YAML notation, otherwise will use YAML in-line (
     *                     brackets for lists, braces for Maps) notation.
     */
    public static String getSignature(Event event, boolean yamlStandard) {

        String signature = "";
        int level = 0;

        Class c = event.getClass();

        signature += c.getSimpleName() + (yamlStandard ? "" : "[");

        level++;

        if (event instanceof TimedEvent) {

            signature += (yamlStandard ? "\n" + indentation(level) : "") + "timestamp";
        }

        Set<Property> properties = event.getProperties();

        if (!properties.isEmpty()) {

            signature += yamlStandard ? "" : ", ";

            List<Property> sortedProperties = new ArrayList<>(properties);
            Collections.sort(sortedProperties);

            for(Iterator<Property> i = sortedProperties.iterator(); i.hasNext(); ) {

                Property p = i.next();
                Class type = p.getType();

                signature += (yamlStandard ? "\n" + indentation(level) : "") +
                        p.getName() + "(" + type.getSimpleName() + ")";

                if (Map.class.equals(type)) {

                    level++;

                    signature += yamlStandard ? "" : "{";
                    List<String> keys = new ArrayList<>();
                    //noinspection Convert2streamapi
                    for(Object o: ((MapProperty)p).getMap().keySet()) {
                        keys.add(o.toString());
                    }
                    Collections.sort(keys);

                    if (yamlStandard && keys.isEmpty()) {
                        signature += "\n" + indentation(level) + "<empty>";
                    }

                    for(Iterator<String> j = keys.iterator(); j.hasNext(); ) {

                        signature += (yamlStandard ? "\n" + indentation(level) : "") + j.next();

                        if (j.hasNext()) {
                            signature += (yamlStandard ? "": ", ");
                        }
                    }

                    signature += yamlStandard ? "" : "}";

                    level--;
                }

                if (i.hasNext()) {
                    signature += yamlStandard ? "" : ", ";
                }
            }
        }

        signature += yamlStandard ? "\n" : "]";

        return signature;
    }


    // Attributes ------------------------------------------------------------------------------------------------------

    private Set<String> signatures;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Describe() {

        this.signatures = new HashSet<>();
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList(COMMAND_LINE_LABEL);
    }

    @Override
    public void process(Event in) throws EventProcessingException {

        invocationCount ++;

        String signature = getSignature(in, YAML_INLINE);

        if (!signatures.contains(signature)) {

            signatures.add(signature);

            String yamlSignature = getSignature(in, YAML);

            try {

                println(yamlSignature);

            }
            catch(IOException e) {

                throw new EventProcessingException(e);
            }
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Static Protected ------------------------------------------------------------------------------------------------

    static String indentation(int level) {

        //
        // two spaces per level
        //

        if (level == 0) {
            return "";
        }
        else if (level == 1) {
            return "  ";
        }
        else if (level == 2) {
            return "    ";
        }
        else if (level == 3) {
            return "      ";
        }
        else if (level == 4) {
            return "        ";
        }
        else {
            String s = "";
            for(int i = 0; i < level; i ++) {
                s += "  ";
            }
            return s;
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
