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

package org.efaps.ui.wicket.components.values;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.Model;
import org.efaps.ui.wicket.models.cell.FieldConfiguration;
import org.efaps.ui.wicket.models.field.AbstractUIField;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class HiddenField
    extends AbstractField<String>
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param _wicketId
     * @param _config
     */
    public HiddenField(final String _wicketId,
                       final Model<AbstractUIField> _model,
                       final FieldConfiguration _config)
        throws EFapsException
    {
        super(_wicketId, _config);
        setModel(Model.of((String) _model.getObject().getValue().getHiddenValue(
                        _model.getObject().getParent().getMode())));
    }

    @Override
    protected void onComponentTag(final ComponentTag _tag)
    {
        _tag.put("type", "hidden");
        super.onComponentTag(_tag);
    }

    @Override
    protected String[] getInputTypes()
    {
        return new String[] { "hidden" };
    }
}
