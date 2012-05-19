/*
 * Copyright 2003 - 2012 The eFaps Team
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

package org.efaps.ui.wicket.components.split.header;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.string.StringValue;
import org.efaps.db.Context;
import org.efaps.ui.wicket.components.split.header.SplitHeaderPanel.PositionUserAttribute;
import org.efaps.util.EFapsException;

/**
 * Class renders an ajax post link which is used to store the position of the
 * horizontal splitter.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class AjaxStoreHorizontalPositionBehavior
    extends
    AbstractDefaultAjaxBehavior
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * String used as the parameter name.
     */
    private static final String POSITION_PARAMETERNAMERNAME = "eFapsPositionH";

    /**
     * Method to get the JavaScript.
     *
     * @param _paneId id of the pane.
     * @return String containing JavaScript
     */
    public String getJavaScript(final String _paneId)
    {
        final StringBuilder ret = new StringBuilder();
//        ret.append("function storePositionH(){\n")
//            .append("  var pane = dojo.byId(\"").append(_paneId).append("\");\n")
//            .append(generateCallbackScript("wicketAjaxPost('" + getCallbackUrl(false)
//                                        + "','" + AjaxStoreHorizontalPositionBehavior.POSITION_PARAMETERNAMERNAME
//                                        + "=' + pane.clientWidth"))
//            .append("\n" + "  }\n");
        return ret.toString();
    }

    /**
     * On request the values are stored.
     *
     * @param _target AjaxRequestTarget
     */
    @Override
    protected void respond(final AjaxRequestTarget _target)
    {
        final StringValue position = getComponent().getRequest().getRequestParameters().getParameterValue(
                        AjaxStoreHorizontalPositionBehavior.POSITION_PARAMETERNAMERNAME);
        try {
            Context.getThreadContext().setUserAttribute(
                            PositionUserAttribute.HORIZONTAL.getKey(), position.toString());
            Context.getThreadContext().setUserAttribute(
                            PositionUserAttribute.HORIZONTAL_COLLAPSED.getKey(), "false");
        } catch (final EFapsException e) {
            e.printStackTrace();
        }
    }
}
