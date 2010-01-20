/*
 * Copyright 2003 - 2009 The eFaps Team
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
 * Revision:        $Rev:1510 $
 * Last Changed:    $Date:2007-10-18 09:35:40 -0500 (Thu, 18 Oct 2007) $
 * Last Changed By: $Author:jmox $
 */

package org.efaps.ui.wicket.components.table.cell;

import org.apache.wicket.PageMap;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.efaps.admin.ui.AbstractCommand.Target;
import org.efaps.admin.ui.field.Field.Display;
import org.efaps.ui.wicket.behaviors.AjaxFieldUpdateBehavior;
import org.efaps.ui.wicket.behaviors.ExpandTextareaBehavior;
import org.efaps.ui.wicket.behaviors.SetSelectedRowBehavior;
import org.efaps.ui.wicket.components.LabelComponent;
import org.efaps.ui.wicket.components.autocomplete.AutoCompleteField;
import org.efaps.ui.wicket.components.efapscontent.StaticImageComponent;
import org.efaps.ui.wicket.models.cell.UITableCell;
import org.efaps.ui.wicket.models.objects.UITable;

/**
 * Class is used to render a cell inside a table.
 *
 * @author The eFaps Team
 * @version $Id:CellPanel.java 1510 2007-10-18 14:35:40Z jmox $
 */
public class CellPanel
    extends Panel
{

    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor used to get a cell which only contains a check box.
     *
     * @param _wicketId wicket id for this component
     * @param _oid oid for the ceck box
     */
    public CellPanel(final String _wicketId,
                     final String _oid)
    {
        super(_wicketId);
        add(new CheckBoxComponent("checkbox", _oid));
        add(new WebMarkupContainer("link").setVisible(false));
        add(new WebMarkupContainer("icon").setVisible(false));
        add(new WebMarkupContainer("label").setVisible(false));
    }

    /**
     * Constructor for all cases minus checkbox.
     *
     * @see #CellPanel(String, String)
     * @param _wicketId wicket id for this component
     * @param _model model for this component
     * @param _updateListMenu must the list be updated
     * @param _uitable uitable
     */
    public CellPanel(final String _wicketId,
                     final IModel<UITableCell> _model,
                     final boolean _updateListMenu,
                     final UITable _uitable)
    {
        super(_wicketId, _model);
        final UITableCell uiTableCell = (UITableCell) super.getDefaultModelObject();
        // set the title of the cell
        add(new SimpleAttributeModifier("title", uiTableCell.getCellTitle()));
        if (uiTableCell.isAutoComplete() && _uitable.isCreateMode()) {
            add(new AutoCompleteField("checkbox", _model, true));
            add(new WebMarkupContainer("link").setVisible(false));
            add(new WebMarkupContainer("icon").setVisible(false));
            add(new WebMarkupContainer("label").setVisible(false));
        } else {
            // make the checkbox invisible
            add(new WebMarkupContainer("checkbox").setVisible(false));

            WebMarkupContainer celllink;
            if (uiTableCell.getReference() == null) {
                celllink = new WebMarkupContainer("link");
                celllink.setVisible(false);
            } else {
                if (_updateListMenu && uiTableCell.getTarget() != Target.POPUP) {
                    celllink = new AjaxLinkContainer("link", _model);
                } else {
                    if (uiTableCell.isCheckOut()) {
                        celllink = new CheckOutLink("link", _model);
                    } else {
                        if (_uitable.isSearchMode() && uiTableCell.getTarget() != Target.POPUP) {
                            // do we have "connectmode",then we don't want a
                            // link in a popup
                            if (_uitable.isSubmit()) {
                                celllink = new WebMarkupContainer("link");
                                celllink.setVisible(false);
                            } else {
                                celllink = new AjaxLoadInOpenerLink("link", _model);
                            }
                        } else {
                            celllink = new ContentContainerLink<UITableCell>("link", _model);
                            if (uiTableCell.getTarget() == Target.POPUP) {
                                final PopupSettings popup = new PopupSettings(PageMap.forName("popup"));
                                ((ContentContainerLink<?>) celllink).setPopupSettings(popup);
                            }
                        }
                    }
                }
            }
            add(celllink);

            if (celllink.isVisible()) {
                celllink.add(new LabelComponent("linklabel", uiTableCell.getCellValue()));

                if (uiTableCell.getIcon() == null) {
                    celllink.add(new WebMarkupContainer("linkicon").setVisible(false));
                } else {
                    celllink.add(new StaticImageComponent("linkicon", uiTableCell.getIcon()));
                }
                add(new WebMarkupContainer("icon").setVisible(false));
                add(new WebMarkupContainer("label").setVisible(false));
            } else {
                final LabelComponent label = new LabelComponent("label", uiTableCell.getCellValue());
                if (_uitable.isCreateMode() && uiTableCell.getDisplay().equals(Display.EDITABLE)) {
                    label.add(new SetSelectedRowBehavior(uiTableCell.getName()));
                    if (uiTableCell.isFieldUpdate()) {
                        label.add(new AjaxFieldUpdateBehavior(uiTableCell.getFieldUpdateEvent(), _model));
                    }
                    if (uiTableCell.isMultiRows()) {
                        label.add(new ExpandTextareaBehavior());
                    }
                }
                add(label);
                if (uiTableCell.getIcon() == null) {
                    add(new WebMarkupContainer("icon").setVisible(false));
                } else {
                    add(new StaticImageComponent("icon", uiTableCell.getIcon()));
                }
            }
        }
    }
}
