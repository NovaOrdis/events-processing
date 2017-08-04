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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.Property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class OutputFormatImpl implements OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String DEFAULT_SEPARATOR = ", ";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String separator;

    private List<String> propertyNames;

    // Constructors ----------------------------------------------------------------------------------------------------

    public OutputFormatImpl() {

        //noinspection NullArgumentToVariableArgMethod
        this(null);
    }

    public OutputFormatImpl(String ... propertyNames) {

        this.propertyNames = new ArrayList<>();
        this.separator = "" + DEFAULT_SEPARATOR;

        if (propertyNames != null && propertyNames.length > 0) {

            for(String n: propertyNames) {

                addPropertyName(n);
            }
        }
    }

    // OutputFormat implementation -------------------------------------------------------------------------------------

    @Override
    public String format(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        int i = 0;
        String s = null;

        for(Iterator<String> si = propertyNames.iterator(); si.hasNext(); i ++) {

            String propertyName = si.next();

            Property p = e.getProperty(propertyName);
            Object v = p == null ? null : p.getValue();

            if (v != null) {

                if (s == null) {

                    //
                    // add all previous commas and placeholders
                    //

                    s = "";

                    while(i-- > 0) {

                        s += ", ";
                    }

                    s += v.toString();
                }
                else {

                    s += " " + v;
                }
            }

            if (s != null && si.hasNext()) {

                s += ",";
            }
        }

        return s;
    }

    @Override
    public boolean isLeadingTimestamp() {

        return false;
    }

    @Override
    public String getSeparator() {

        return separator;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void addPropertyName(String s) {

        propertyNames.add(s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * @return the internal storage.
     */
    List<String> getPropertyNames() {

        return propertyNames;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
