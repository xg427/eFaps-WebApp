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

package org.efaps.ui.wicket.components.connection;

import java.util.List;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.IWebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.efaps.ui.wicket.EFapsApplication;
import org.efaps.ui.wicket.components.bpm.AbstractSortableProvider;
import org.efaps.ui.wicket.components.connection.MessageTablePanel.CheckBoxPanel;
import org.efaps.ui.wicket.models.PushMsg;
import org.efaps.ui.wicket.models.objects.UIUser;
import org.efaps.ui.wicket.resources.EFapsContentReference;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class MessagePanel
    extends Panel
{

    /**
     * Reference to the style sheet.
     */
    public static final EFapsContentReference CSS = new EFapsContentReference(AbstractSortableProvider.class,
                    "BPM.css");

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param _wicketId wicketId of this component
     * @param _pageReference reference to the page
     * @throws EFapsException on error
     */
    public MessagePanel(final String _wicketId,
                        final PageReference _pageReference)
        throws EFapsException
    {
        super(_wicketId);
        final Form<Void> msgForm = new Form<Void>("msgForm");
        add(msgForm);

        final AjaxSubmitLink sendMsgBtn = new AjaxSubmitLink("sendMsgBtn", msgForm)
        {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onAfterSubmit(final AjaxRequestTarget _target,
                                         final Form<?> _form)
            {
                final StringBuilder msg = new StringBuilder();
                _form.visitChildren(TextArea.class, new IVisitor<TextArea<String>, Void>()
                {
                    @Override
                    public void component(final TextArea<String> _textArea,
                                          final IVisit<Void> _visit)
                    {
                        _textArea.setEscapeModelStrings(false);
                        msg.append(_textArea.getDefaultModelObjectAsString());
                        _visit.stop();
                    }
                });

                if (msg.length() > 0) {
                    _form.visitChildren(CheckBox.class, new IVisitor<CheckBox, Void>()
                    {

                        @Override
                        public void component(final CheckBox _checkBox,
                                              final IVisit<Void> _visit)
                        {
                            final Boolean selected = (Boolean) _checkBox.getDefaultModelObject();
                            if (selected) {
                                final CheckBoxPanel panel = (CheckBoxPanel) _checkBox.getParent();
                                final UIUser user = (UIUser) panel.getDefaultModelObject();
                                final List<IWebSocketConnection> conns = EFapsApplication.get().getConnectionRegistry()
                                                .getConnections4User(user.getUserName());
                                for (final IWebSocketConnection conn : conns) {
                                    conn.sendMessage(new PushMsg(msg.toString()));
                                }
                            }
                        }
                    });
                }
            }
        };
        msgForm.add(sendMsgBtn);

        final AjaxSubmitLink broadcastMsgBtn = new AjaxSubmitLink("broadcastMsgBtn", msgForm)
        {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onAfterSubmit(final AjaxRequestTarget _target,
                                         final Form<?> _form)
            {

                final StringBuilder msg = new StringBuilder();
                _form.visitChildren(TextArea.class, new IVisitor<TextArea<String>, Void>()
                {

                    @Override
                    public void component(final TextArea<String> _textArea,
                                          final IVisit<Void> _visit)
                    {
                        _textArea.setEscapeModelStrings(false);
                        msg.append(_textArea.getDefaultModelObjectAsString());
                        _visit.stop();
                    }
                });

                if (msg.length() > 0) {
                    final IWebSocketSettings webSocketSettings = IWebSocketSettings.Holder.get(getApplication());
                    final WebSocketPushBroadcaster broadcaster =
                                    new WebSocketPushBroadcaster(webSocketSettings.getConnectionRegistry());
                    broadcaster.broadcastAll(EFapsApplication.get(), new PushMsg(msg.toString()));
                }
            }
        };
        msgForm.add(broadcastMsgBtn);

        final TextArea<String> msg = new TextArea<String>("msg", Model.of(""));
        msgForm.add(msg);

        final MessageTablePanel messageTable = new MessageTablePanel("messageTable", _pageReference,
                        new UserProvider());
        msgForm.add(messageTable);
    }
}
