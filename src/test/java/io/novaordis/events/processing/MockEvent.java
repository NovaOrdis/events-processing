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

package io.novaordis.events.processing;

import io.novaordis.events.api.event.GenericEvent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class MockEvent extends GenericEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String rawRepresentation;
    private String preferredRepresentation;
    private String preferredRepresentationHeader;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockEvent() {

        super();
    }

    // GenericEvent overrides ------------------------------------------------------------------------------------------

    @Override
    public String getPreferredRepresentation(String fieldSeparator) {

        return preferredRepresentation;
    }

    @Override
    public String getPreferredRepresentationHeader(String fieldSeparator) {

        return preferredRepresentationHeader;
    }

    @Override
    public String getRawRepresentation() {

        return rawRepresentation;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setRawRepresentation(String s) {

        this.rawRepresentation = s;
    }

    public void setPreferredRepresentation(String s) {

        this.preferredRepresentation = s;
    }

    public void setPreferredRepresentationHeader(String s) {

        this.preferredRepresentationHeader = s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
