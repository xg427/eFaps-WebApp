/*
 * Copyright 2003 - 2011 The eFaps Team
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.efaps.ui.wicket.models.cell.FieldConfiguration;
import org.efaps.ui.wicket.models.objects.DropDownOption;

/**
 * Render a DropDown Field.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class DropDownField
    extends DropDownChoice<DropDownOption>
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     *  Configurationobject for this component.
     */
    private final FieldConfiguration config;

    /**
     * @param _wicketId     wicket id for this component
      *@param _value        value for this component
     * @param _choices    Choices for the dropdowns
     * @param _fieldConfiguration    Configurationobject for this component.
     */
    public DropDownField(final String _wicketId,
                         final Object _value,
                         final IModel<Map<Object, Object>> _choices,
                         final FieldConfiguration _fieldConfiguration)
    {
        super(_wicketId);
        if (_value != null) {
            setDefaultModel(Model.of(new DropDownOption(String.valueOf(_value), null)));
        } else {
            setDefaultModel(new Model<String>());
        }
        this.config = _fieldConfiguration;
        setChoices(DropDownField.getSelectChoices(_choices.getObject()));
        setChoiceRenderer(new ChoiceRenderer());
    }

    @Override
    protected void onComponentTag(final ComponentTag _tag)
    {
        _tag.setName("select");
        _tag.append("style", "text-align:" + this.config.getAlign(), ";");
        super.onComponentTag(_tag);
    }

    @Override
    public String getInputName()
    {
        return this.config.getName();
    }

    private static List<DropDownOption> getSelectChoices(final Map<Object, Object> _values)
    {
        final List<DropDownOption> list = new ArrayList<DropDownOption>();
        for (final Entry<Object, Object> entry : _values.entrySet()) {
            list.add(new DropDownOption(String.valueOf(entry.getKey()),
                            String.valueOf(entry.getValue())));
        }
        return list;
    }

    public final class ChoiceRenderer
        implements IChoiceRenderer<DropDownOption>
    {

        private static final long serialVersionUID = 1L;

        @Override
        public Object getDisplayValue(final DropDownOption _option)
        {
            return _option.getLabel();
        }

        @Override
        public String getIdValue(final DropDownOption _object,
                                 final int _index)
        {
            return _object.getValue();
        }
    }
}