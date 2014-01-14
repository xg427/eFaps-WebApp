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

package org.efaps.ui.wicket.models.field.factories;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.efaps.admin.datamodel.IEnum;
import org.efaps.admin.datamodel.ui.EnumUI;
import org.efaps.ui.wicket.components.values.RadioField;
import org.efaps.ui.wicket.models.field.AbstractUIField;
import org.efaps.ui.wicket.util.EnumUtil;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
// CHECKSTYLE:OFF
public class EnumUIFactory
    extends AbstractUIFactory
// CHECKSTYLE:ON
{

    /**
     * Factory Instance.
     */
    private static EnumUIFactory FACTORY;

    /**
     * Singelton.
     */
    private EnumUIFactory()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getEditable(final String _wicketId,
                                 final AbstractUIField _abstractUIField)
        throws EFapsException
    {
        Component ret = null;
        if (applies(_abstractUIField)) {
            ret = new RadioField(_wicketId, Model.of(_abstractUIField), _abstractUIField.getValue().getEditValue(
                                            _abstractUIField.getParent().getMode()),
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
        return _abstractUIField.getValue().getUIProvider() instanceof EnumUI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getReadOnlyValue(final AbstractUIField _abstractUIField)
        throws EFapsException
    {
        final Object valueTmp = _abstractUIField.getValue()
                        .getReadOnlyValue(_abstractUIField.getParent().getMode());
        return valueTmp == null ? null : EnumUtil.getUILabel((IEnum) valueTmp);
    }

    /**
     * @return IComponentFactory instance
     */
    public static IComponentFactory get()
    {
        if (EnumUIFactory.FACTORY == null) {
            EnumUIFactory.FACTORY = new EnumUIFactory();
        }
        return EnumUIFactory.FACTORY;
    }
}
