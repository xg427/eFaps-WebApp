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

package org.efaps.ui.wicket.components.menu;

import org.apache.wicket.model.IModel;
import org.efaps.ui.wicket.models.objects.UIMenuItem;
import org.efaps.ui.wicket.pages.task.TaskAdminPage;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Render an home link.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TaskAdminItem
    extends LinkItem
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TaskAdminItem.class);


    /**
     * @param _wicketId     wicketId for this item
     * @param _model        model for this item
     */
    public TaskAdminItem(final String _wicketId,
                         final IModel<UIMenuItem> _model)
    {
        super(_wicketId, _model);
    }

    /**
     * On click it is evaluated what must be responded.
     */
    @Override
    public void onClick()
    {
        try {
            setResponsePage(new TaskAdminPage(getPage().getPageReference()));
        } catch (final EFapsException e) {
            TaskAdminItem.LOG.error("", e);
        }
    }
}
