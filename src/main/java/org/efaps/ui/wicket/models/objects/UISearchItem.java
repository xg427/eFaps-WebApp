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

package org.efaps.ui.wicket.models.objects;

import java.util.UUID;

import org.efaps.admin.ui.Search;

/**
 * @author The eFaps Team
 * @version $Id$
 */
public class UISearchItem
    extends UIMenuItem
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * UUID of the Search.
     */
    private final UUID searchuuid;

    /**
     * @param _uuid UUID
     */
    public UISearchItem(final UUID _uuid)
    {
        super(_uuid, null);
        this.searchuuid = _uuid;
    }

    /**
     * @return the Search
     */
    public Search getSearch()
    {
        return Search.get(this.searchuuid);
    }

}
