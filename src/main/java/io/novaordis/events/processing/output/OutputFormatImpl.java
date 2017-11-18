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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.TimedEvent;

/**
 * The default implementation of a command-line configured OutputFormat. It understands pre-parsed standard command-line
 * format specification arguments (property names and indexes), and encapsulates them in order to use them to
 * format the events that are being handed over. For timed events, the representation starts with a timestamp, whether
 * it was requested or not via format. If it was requested via format, it will displayed twice (or multiple times).
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/17
 */
public class OutputFormatImpl implements OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String DEFAULT_SEPARATOR = ", ";

    private static final String HEADER_MARKER = "# ";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String separator;

    // Property identifiers - can only be Strings or valid Integers. List will preserve order, we need it for the header

    private List<Object> propertyIdentifiers;

    private DateFormat timestampFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    public OutputFormatImpl() {

        //noinspection NullArgumentToVariableArgMethod
        this(null);
    }

    /**
     * @param propertyIdentifiers may be Strings, representing property names, or Integers, representing property
     *                            indices. Anything else will trigger an IllegalArgumentException.
     *
     * @exception IllegalArgumentException if an identifier instance is anything else but a String or a valid Integer
     * property index.
     */
    public OutputFormatImpl(Object ... propertyIdentifiers) throws IllegalArgumentException {

        this.propertyIdentifiers = new ArrayList<>();
        this.separator = "" + DEFAULT_SEPARATOR;
        this.timestampFormat = DefaultOutputFormat.DEFAULT_TIMESTAMP_FORMAT;

        if (propertyIdentifiers != null && propertyIdentifiers.length > 0) {

            for(Object i: propertyIdentifiers) {

                if (i instanceof String) {

                    addPropertyName((String)i);
                }
                else if (i instanceof Integer) {

                    addPropertyIndex((Integer) i);
                }
                else {

                    throw new IllegalArgumentException(
                            "invalid property identifier " + i + (i == null ? "" : "(" + i.getClass().getName() + ")"));
                }
            }
        }
    }

    // OutputFormat implementation -------------------------------------------------------------------------------------

    /**
     * We return *all* property names, irrespective of the event, because different events may carry different subsets
     * of properties, and we want the union of those.
     */
    @Override
    public String formatHeader(Event e) {

        String s = HEADER_MARKER;

        if (e instanceof TimedEvent) {

            s += TimedEvent.TIME_PROPERTY_NAME;
        }

        boolean first = true;

        for(Iterator<Object> i = propertyIdentifiers.iterator(); i.hasNext(); first = false) {

            if (first && !HEADER_MARKER.equals(s)) {

                s += ", ";
            }

            Object identifier = i.next();

            if (identifier instanceof String) {

                s += identifier;
            }
            else if (identifier instanceof Integer) {

                Property p = e.getProperty((Integer)identifier);

                if (p == null) {

                    s += " ";
                }
                else {

                    s += p.getName();
                }
            }
            else {

                throw new IllegalStateException(
                        "invalid property identifier " + identifier +
                                (identifier == null ? "" : "(" + identifier.getClass().getName() + ")"));
            }

            if (i.hasNext()) {

                s += getSeparator();
            }
        }

        return s;
    }

    @Override
    public String format(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        int i = 0;

        String s = null;

        for(Iterator<Object> si = propertyIdentifiers.iterator(); si.hasNext(); i ++) {

            Object propertyIdentifier = si.next();

            Property p;

            if (propertyIdentifier instanceof String) {

                p = e.getProperty((String)propertyIdentifier);
            }
            else if (propertyIdentifier instanceof Integer) {

                p = e.getProperty((Integer)propertyIdentifier);
            }
            else {

                throw new IllegalStateException(
                        "invalid property identifier " + propertyIdentifier +
                                (propertyIdentifier == null ? "" : "(" + propertyIdentifier.getClass().getName() + ")"));
            }

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

        if (s != null && e instanceof TimedEvent) {

            Long t = ((TimedEvent) e).getTime();

            if (t != null) {

                s = timestampFormat.format(t) + ", " + s;
            }
        }

        return s;
    }

    @Override
    public String getSeparator() {

        return separator;
    }

    @Override
    public DateFormat getTimestampFormat() {

        return timestampFormat;
    }

    @Override
    public void setTimestampFormat(DateFormat f) {

        this.timestampFormat = f;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Add the given property name to the list of properties that will be rendered by the format. The properties will be
     * rendered in the order in which their corresponding addPropertyName()/addPropertyIndex() was invoked.
     *
     * @exception IllegalArgumentException if a string that can be converted to an int is provide - this usually
     * means we should use addPropertyIndex()
     */
    public void addPropertyName(String s) {

        try {

            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(s);
            throw new IllegalArgumentException(
                    "invalid attempt to add a property index as property name; consider using addPropertyIndex()");
        }
        catch(NumberFormatException e) {

            //
            // we're fine, no need to do anything
            //
        }

        propertyIdentifiers.add(s);
    }

    /**
     * Add the given property index to the list of properties that will be rendered by the format. The properties will
     * be rendered in the order in which their corresponding addPropertyName()/addPropertyIndex() was invoked.
     *
     * Properties are indexed using a 0-based scheme.
     *
     * @exception IllegalArgumentException for negative indices.
     */
    public void addPropertyIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException("invalid property index: "  + index);
        }

        propertyIdentifiers.add(index);
    }

    @Override
    public String toString() {

        String s = "OutputFormatImpl[";

        for(Iterator<Object> i = propertyIdentifiers.iterator(); i.hasNext(); ) {

            Object o = i.next();

            if (o instanceof Integer) {

                s += "field[" + o + "]";
            }
            else {

                s += "\"" + o + "\"";
            }

            if (i.hasNext()) {

                s += ", ";
            }
        }

        s += "]";

        return s;

    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * @return the internal storage. The instances may only be Strings (property names) or Integers (property indexes).
     */
    List<Object> getPropertyIdentifiers() {

        return propertyIdentifiers;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
