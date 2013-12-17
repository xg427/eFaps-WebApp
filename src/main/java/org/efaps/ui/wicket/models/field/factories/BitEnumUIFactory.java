/*
 * Copyright 2003 - 2013 The eFaps Team
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


package org.efaps.ui.wicket.models.field.factories;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.efaps.admin.datamodel.IEnum;
import org.efaps.admin.datamodel.ui.BitEnumUI;
import org.efaps.ui.wicket.components.values.CheckBoxField;
import org.efaps.ui.wicket.models.field.AbstractUIField;
import org.efaps.ui.wicket.util.EnumUtil;
import org.efaps.util.EFapsException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
//CHECKSTYLE:OFF
public class BitEnumUIFactory
    extends AbstractUIFactory
//CHECKSTYLE:ON
{

    /**
     * Factory Instance.
     */
    private static BitEnumUIFactory FACTORY;

    /**
     * Singelton.
     */
    private BitEnumUIFactory()
    {
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Component getEditable(final String _wicketId,
                                 final AbstractUIField _abstractUIField)
        throws EFapsException
    {
        Component ret = null;
        if (applies(_abstractUIField)) {
            ret = new CheckBoxField(_wicketId, Model.of(_abstractUIField),
                       (List<Object>) _abstractUIField.getValue().getEditValue(_abstractUIField.getParent().getMode()),
                            _abstractUIField.getFieldConfiguration());
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean applies(final AbstractUIField _abstractUIField)
        throws EFapsException
    {
        return _abstractUIField.getValue().getUIProvider() instanceof BitEnumUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getReadOnlyValue(final AbstractUIField _abstractUIField)
        throws EFapsException
    {
        final StringBuilder ret = new StringBuilder();
        final Object valueTmp = _abstractUIField.getValue().getReadOnlyValue(_abstractUIField.getParent().getMode());
        if (valueTmp instanceof List) {
            boolean first = true;
            for (final Object obj : (List<?>) valueTmp) {
                if (first) {
                    first = false;
                } else {
                    ret.append(", ");
                }
                ret.append(EnumUtil.getUILabel((IEnum) obj));
            }
        }
        return ret.toString();
    }

    /**
     * @return IComponentFactory instance
     */
    public static IComponentFactory get()
    {
        if (BitEnumUIFactory.FACTORY == null) {
            BitEnumUIFactory.FACTORY = new BitEnumUIFactory();
        }
        return BitEnumUIFactory.FACTORY;
    }
}
