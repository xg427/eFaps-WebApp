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

package org.efaps.ui.wicket.components.footer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.efaps.db.Context;
import org.efaps.ui.wicket.components.modalwindow.ModalWindowContainer;
import org.efaps.ui.wicket.models.TableModel;
import org.efaps.ui.wicket.models.objects.AbstractUIPageObject;
import org.efaps.ui.wicket.models.objects.UITable;
import org.efaps.ui.wicket.models.objects.UIWizardObject;
import org.efaps.ui.wicket.pages.content.AbstractContentPage;
import org.efaps.ui.wicket.pages.content.table.TablePage;
import org.efaps.ui.wicket.pages.error.ErrorPage;
import org.efaps.util.EFapsException;

/**
 * Link used to submit a Search.
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class SearchSubmitLink
    extends AjaxSubmitLink
{
    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param _wicketId wicket id of this component
     * @param _model model for this component
     * @param _form form of this submit link
     */
    public SearchSubmitLink(final String _wicketId,
                            final IModel<?> _model,
                            final Form<?> _form)
    {
        super(_wicketId, _form);
        super.setDefaultModel(_model);
    }

    /**
     * This method is executed when the form is submitted.
     */
    @Override
    protected void onSubmit(final AjaxRequestTarget target,
                            final Form<?> form)
    {
        super.onSubmit();
        final AbstractUIPageObject uiObject = (AbstractUIPageObject) getDefaultModelObject();
        try {
            final UITable newTable = new UITable(uiObject.getCommandUUID(), uiObject.getInstanceKey(), uiObject
                            .getOpenerId());
            final UIWizardObject wizard = new UIWizardObject(newTable);
            uiObject.setWizard(wizard);
            wizard.addParameters(uiObject, Context.getThreadContext().getParameters());
            wizard.insertBefore(uiObject);
            newTable.setWizard(wizard);
            newTable.setPartOfWizardCall(true);
            newTable.setRenderRevise(uiObject.isTargetCmdRevise());
            if (uiObject.isSubmit()) {
                newTable.setSubmit(true);
                newTable.setCallingCommandUUID(uiObject.getCallingCommandUUID());
            }
            final FooterPanel footer = findParent(FooterPanel.class);
            final ModalWindowContainer modal = footer.getModalWindow();
            final TablePage page = new TablePage(new TableModel(newTable), modal, false);

            page.setMenuTreeKey(((AbstractContentPage) getPage()).getMenuTreeKey());
            getRequestCycle().setResponsePage(page);
        } catch (final EFapsException e) {
            getRequestCycle().setResponsePage(new ErrorPage(e));
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink#onError(org.apache.wicket.ajax.AjaxRequestTarget, org.apache.wicket.markup.html.form.Form)
     */
    @Override
    protected void onError(final AjaxRequestTarget _target,
                           final Form<?> _form)
    {
        // TODO Auto-generated method stub
    }
}
