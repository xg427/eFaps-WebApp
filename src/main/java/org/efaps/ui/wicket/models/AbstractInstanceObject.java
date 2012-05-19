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

package org.efaps.ui.wicket.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.request.cycle.RequestCycle;
import org.efaps.db.Instance;
import org.efaps.ui.wicket.EFapsRequestCycleListener;
import org.efaps.ui.wicket.util.EFapsKey;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: AbstractInstanceObject.java 3447 2009-11-29 22:46:39Z
 *          tim.moxter $
 */
public abstract class AbstractInstanceObject
    implements Serializable
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * key to the instance.
     */
    private String instanceKey;

    public AbstractInstanceObject()
    {
    }

    public AbstractInstanceObject(final String _instanceKey)
    {
        this.instanceKey = _instanceKey;
    }

    /**
     * Getter method for instance variable {@link #instanceKey}.
     *
     * @return value of instance variable {@link #instanceKey}
     */
    public String getInstanceKey()
    {
        return this.instanceKey;
    }

    /**
     * @see org.efaps.ui.wicket.models.AbstractInstanceObject#getInstance()
     * @return Instance of the object
     * @throws EFapsException on error
     */
    @SuppressWarnings("unchecked")
    public Instance getInstance()
        throws EFapsException
    {
        Instance ret = null;
        Map<String, Instance> map;

        final EFapsRequestCycleListener cycle = (EFapsRequestCycleListener) RequestCycle.get().getListeners().iterator()
                        .next();

        map = (Map<String, Instance>) cycle.getFromCache(EFapsKey.INSTANCE_CACHEKEY.getKey());
        if (map == null) {
            map = new HashMap<String, Instance>();
            cycle.putIntoCache(EFapsKey.INSTANCE_CACHEKEY.getKey(), map);
        }
        if (map.containsKey(this.instanceKey)) {
            ret = map.get(this.instanceKey);
        } else {
            if (hasInstanceManager()) {
                ret = getInstanceFromManager();
            } else if ((this.instanceKey != null)
                            && (this.instanceKey.length() > 0)) {
                ret = Instance.get(getInstanceKey());
            }
            map.put(this.instanceKey, ret);
        }
        return ret;
    }

    /**
     * Setter method for instance variable {@link #instanceKey}.
     *
     * @param _instanceKey value for instance variable {@link #instanceKey}
     */
    public void setInstanceKey(final String _instanceKey)
    {
        this.instanceKey = _instanceKey;
    }

    public abstract Instance getInstanceFromManager()
        throws EFapsException;

    public abstract boolean hasInstanceManager();
}
