/*
 * Copyright 2003 - 2010 The eFaps Team
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

package org.efaps.ui.wicket;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.InvalidUrlException;
import org.apache.wicket.session.ISessionStore;
import org.efaps.ui.wicket.pages.error.ErrorPage;
import org.efaps.ui.wicket.pages.login.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends the
 * {@link org.apache.wicket.protocol.http.WebRequestCycle} to throw a own
 * ErrorPage and to open/close the Context on begin/end of a Request.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class EFapsWebRequestCycle
    extends WebRequestCycle
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EFapsWebRequestCycle.class);

    /**
     * Map used as a cache.
     */
    private final Map<String, Object> cache = new HashMap<String, Object>();

    /**
     * Constructor for a WebRequest.
     *
     * @param _application the Webapplication which received the WebRequest
     * @param _request the WebRequest to be used for this RequestCycle
     * @param _response the Response to be used for this RequestCycle
     */
    public EFapsWebRequestCycle(final WebApplication _application,
                                final WebRequest _request,
                                final Response _response)
    {
        super(_application, _request, _response);
    }

    /**
     * Called when the request cycle object is beginning its response. It opens
     * a new Context for the Session.
     *
     * @see #onEndRequest()
     */
    @Override
    protected void onBeginRequest()
    {
        final EFapsSession session = getEFapsSession();
        if (session != null) {
            session.openContext();
        }
        super.onBeginRequest();
    }

    /**
     * Method to get the EFapsSession.
     *
     * @return EFapsSession
     */
    private EFapsSession getEFapsSession()
    {
        final ISessionStore sessionStore = getApplication().getSessionStore();
        final EFapsSession session = (EFapsSession) sessionStore.lookup(this.request);
        return session;
    }

    /**
     * Called when the request cycle object has finished its response. It
     * closses the Context opened for teh Session.
     *
     * @see #onBeginRequest()
     */
    @Override
    protected void onEndRequest()
    {
        super.onEndRequest();
        final EFapsSession session = getEFapsSession();
        if (session != null) {
            session.closeContext();
        }
    }

    /**
     * @see org.apache.wicket.RequestCycle#onRuntimeException(org.apache.wicket.Page,
     *      java.lang.RuntimeException)
     * @param _page Page that throws the error
     * @param _exception exception that was thrown
     * @return Page
     */
    @Override
    public Page onRuntimeException(final Page _page,
                                   final RuntimeException _exception)
    {
        final Page ret;
        if (_exception instanceof AuthorizationException) {
            ret =  super.onRuntimeException(_page, _exception);
        } else if (_exception instanceof PageExpiredException || _exception instanceof InvalidUrlException) {
            final EFapsSession session = (EFapsSession) Session.get();
            if (session.isTemporary() || !session.isLogedIn()) {
                // this was an actual session expiry or the user has loged out
                EFapsWebRequestCycle.LOG.info("session expired and request cannot be honored, "
                                + "redirected to LoginPage");
                ret =  new LoginPage();
            } else {
                EFapsWebRequestCycle.LOG.error("unable to find page for an active session!");
                ret = new ErrorPage(_exception);
            }
        } else {
            ret = new ErrorPage(_exception);
        }
        return ret;
    }

    /**
     * @see org.apache.wicket.RequestCycle#logRuntimeException(java.lang.RuntimeException)
     * @param _exception RuntimeException
     */
    @Override
    protected void logRuntimeException(final RuntimeException _exception)
    {
        if (!(_exception instanceof PageExpiredException || _exception instanceof InvalidUrlException)) {
            super.logRuntimeException(_exception);
        }
    }

    /**
     * This Method stores a Component in the Cache.
     *
     * @param _key Key the Component should be stored in
     * @param _object Object to be stored
     * @see #componentcache
     */
    public void putIntoCache(final String _key,
                             final Object _object)
    {
        this.cache.put(_key, _object);
    }

    /**
     * Retrieve a Component from the ComponentCache.
     *
     * @param _key Key of the Component to be retrieved
     * @return Component if found, else null
     * @see #componentcache
     */
    public Object getFromCache(final String _key)
    {
        return this.cache.get(_key);
    }
}
