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
import io.novaordis.events.api.event.TimedEvent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/17
 */
public class MockOutputFormat implements OutputFormat {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean leadingTimestamp;
    // list because we need to preserve order
    private List<String> matchingProperties;
    private DateFormat timestampFormat;
    private boolean isProvidingHeader;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockOutputFormat() {

        this.matchingProperties = new ArrayList<>();
        this.isProvidingHeader = true;
    }

    // OutputFormat implementation -------------------------------------------------------------------------------------

    @Override
    public String formatHeader(Event e) {

        if (!isProvidingHeader) {

            return null;
        }

        String s = "";

        for(Iterator<String> pi = matchingProperties.iterator(); pi.hasNext(); ) {

            s += pi.next();

            if (pi.hasNext()) {

                s += getSeparator();
            }
        }

        return s;
    }

    @Override
    public String format(Event e) {

        String s = "";

        if (leadingTimestamp && e instanceof TimedEvent) {

            Long t = ((TimedEvent)e).getTime();
            s += timestampFormat.format(t);
        }

        List<Object> values = new ArrayList<>();

        for(String propertyName: matchingProperties) {

            Property p = e.getProperty(propertyName);

            if (p != null) {

                values.add(p.getValue());
            }
        }

        if (!s.isEmpty() && !values.isEmpty()) {

            s += getSeparator();
        }

        for(Iterator<Object> i = values.iterator(); i.hasNext(); ) {

            s += i.next();

            if (i.hasNext()) {

                s += getSeparator();
            }
        }

        return s;
    }

    @Override
    public boolean isLeadingTimestamp() {

        return leadingTimestamp;
    }

    @Override
    public String getSeparator() {

        return " ";
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

    public void setLeadingTimestamp(boolean b) {

        this.leadingTimestamp = b;
    }

    public void addMatchingProperty(String propertyName) {

        matchingProperties.add(propertyName);
    }

    public void setProvidingHeader(boolean b) {

        this.isProvidingHeader = b;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
