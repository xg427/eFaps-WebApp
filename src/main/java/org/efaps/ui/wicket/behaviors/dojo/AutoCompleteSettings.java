/*
 * Copyright 2003 - 2014 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */


package org.efaps.ui.wicket.behaviors.dojo;

import java.io.Serializable;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class AutoCompleteSettings
    implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean hasDownArrow = false;

    private int minInputLength = 1;

    private String paramName = "p";

    /**
     * Delay in milliseconds between when user types something
     * and we start searching based on that value.
     */
    private int searchDelay = 200;

    /**
     * Getter method for the instance variable {@link #hasDownArrow}.
     *
     * @return value of instance variable {@link #hasDownArrow}
     */
    public boolean isHasDownArrow()
    {
        return this.hasDownArrow;
    }


    /**
     * Setter method for instance variable {@link #hasDownArrow}.
     *
     * @param _hasDownArrow value for instance variable {@link #hasDownArrow}
     */
    public void setHasDownArrow(final boolean _hasDownArrow)
    {
        this.hasDownArrow = _hasDownArrow;
    }



    /**
     * Getter method for the instance variable {@link #minInputLength}.
     *
     * @return value of instance variable {@link #minInputLength}
     */
    public int getMinInputLength()
    {
        return this.minInputLength;
    }



    /**
     * Setter method for instance variable {@link #minInputLength}.
     *
     * @param _minInputLength value for instance variable {@link #minInputLength}
     */
    public void setMinInputLength(final int _minInputLength)
    {
        this.minInputLength = _minInputLength;
    }



    /**
     * Getter method for the instance variable {@link #searchDelay}.
     *
     * @return value of instance variable {@link #searchDelay}
     */
    public int getSearchDelay()
    {
        return this.searchDelay;
    }

    /**
     * Setter method for instance variable {@link #searchDelay}.
     *
     * @param _searchDelay value for instance variable {@link #searchDelay}
     */
    public void setSearchDelay(final int _searchDelay)
    {
        this.searchDelay = _searchDelay;
    }

    /**
     * Getter method for the instance variable {@link #parameterName}.
     *
     * @return value of instance variable {@link #parameterName}
     */
    public String getParamName()
    {
        return this.paramName;
    }

    /**
     * Setter method for instance variable {@link #parameterName}.
     *
     * @param _parameterName value for instance variable {@link #parameterName}
     */
    public void setParamName(final String _parameterName)
    {
        this.paramName = _parameterName;
    }
}
