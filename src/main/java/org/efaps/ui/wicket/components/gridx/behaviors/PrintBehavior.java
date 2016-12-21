/*
 * Copyright 2003 - 2016 The eFaps Team
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
 */

package org.efaps.ui.wicket.components.gridx.behaviors;

import java.io.File;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.efaps.ui.wicket.models.objects.UIGrid;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
public class PrintBehavior
    extends AjaxFormSubmitBehavior
    implements IAjaxIndicatorAware
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * @param _event
     */
    public PrintBehavior()
    {
        super("click");
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget _target)
    {
        final File file = UIGrid.print((UIGrid) getComponent().getPage().getDefaultModelObject());
        System.out.println(file);
    }

    @Override
    public String getAjaxIndicatorMarkupId()
    {
        return "eFapsVeil";
    }

}
