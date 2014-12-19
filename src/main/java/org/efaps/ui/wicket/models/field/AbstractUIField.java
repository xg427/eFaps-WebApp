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

package org.efaps.ui.wicket.models.field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.efaps.admin.datamodel.ui.UIValue;
import org.efaps.admin.dbproperty.DBProperties;
import org.efaps.admin.event.EventDefinition;
import org.efaps.admin.event.EventType;
import org.efaps.admin.event.Parameter.ParameterValues;
import org.efaps.admin.event.Return;
import org.efaps.admin.ui.field.Field;
import org.efaps.db.Context;
import org.efaps.db.Instance;
import org.efaps.ui.wicket.behaviors.dojo.AutoCompleteBehavior;
import org.efaps.ui.wicket.components.values.LabelField;
import org.efaps.ui.wicket.models.AbstractInstanceObject;
import org.efaps.ui.wicket.models.cell.AutoCompleteSettings;
import org.efaps.ui.wicket.models.cell.AutoCompleteSettings.EditValue;
import org.efaps.ui.wicket.models.cell.FieldConfiguration;
import org.efaps.ui.wicket.models.cell.UIPicker;
import org.efaps.ui.wicket.models.field.factories.AutoCompleteFactory;
import org.efaps.ui.wicket.models.field.factories.BitEnumUIFactory;
import org.efaps.ui.wicket.models.field.factories.BooleanUIFactory;
import org.efaps.ui.wicket.models.field.factories.DateTimeUIFactory;
import org.efaps.ui.wicket.models.field.factories.DateUIFactory;
import org.efaps.ui.wicket.models.field.factories.DecimalUIFactory;
import org.efaps.ui.wicket.models.field.factories.EnumUIFactory;
import org.efaps.ui.wicket.models.field.factories.HRefFactory;
import org.efaps.ui.wicket.models.field.factories.IComponentFactory;
import org.efaps.ui.wicket.models.field.factories.JaxbUIFactory;
import org.efaps.ui.wicket.models.field.factories.LinkWithRangesUIFactory;
import org.efaps.ui.wicket.models.field.factories.NumberUIFactory;
import org.efaps.ui.wicket.models.field.factories.StringUIFactory;
import org.efaps.ui.wicket.models.field.factories.TypeUIFactory;
import org.efaps.ui.wicket.models.field.factories.UITypeFactory;
import org.efaps.ui.wicket.models.field.factories.UserUIFactory;
import org.efaps.ui.wicket.models.objects.AbstractUIModeObject;
import org.efaps.ui.wicket.models.objects.AbstractUIObject;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractUIField
    extends AbstractInstanceObject
    implements IPickable, IHidden, IFilterable, IAutoComplete
{
    /**
     * Needed for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The factories used to construct the components.
     */
    private static final Map<String, IComponentFactory> FACTORIES = new LinkedHashMap<>();

    static {
        AbstractUIField.FACTORIES.put(HRefFactory.get().getKey(), HRefFactory.get());
        AbstractUIField.FACTORIES.put(AutoCompleteFactory.get().getKey(), AutoCompleteFactory.get());
        AbstractUIField.FACTORIES.put(NumberUIFactory.get().getKey(), NumberUIFactory.get());
        AbstractUIField.FACTORIES.put(StringUIFactory.get().getKey(), StringUIFactory.get());
        AbstractUIField.FACTORIES.put(LinkWithRangesUIFactory.get().getKey(), LinkWithRangesUIFactory.get());
        AbstractUIField.FACTORIES.put(BooleanUIFactory.get().getKey(), BooleanUIFactory.get());
        AbstractUIField.FACTORIES.put(DateUIFactory.get().getKey(), DateUIFactory.get());
        AbstractUIField.FACTORIES.put(DateTimeUIFactory.get().getKey(), DateTimeUIFactory.get());
        AbstractUIField.FACTORIES.put(DecimalUIFactory.get().getKey(), DecimalUIFactory.get());
        AbstractUIField.FACTORIES.put(UserUIFactory.get().getKey(), UserUIFactory.get());
        AbstractUIField.FACTORIES.put(TypeUIFactory.get().getKey(), TypeUIFactory.get());
        AbstractUIField.FACTORIES.put(EnumUIFactory.get().getKey(), EnumUIFactory.get());
        AbstractUIField.FACTORIES.put(BitEnumUIFactory.get().getKey(), BitEnumUIFactory.get());
        AbstractUIField.FACTORIES.put(JaxbUIFactory.get().getKey(), JaxbUIFactory.get());
        AbstractUIField.FACTORIES.put(UITypeFactory.get().getKey(), UITypeFactory.get());
    }

    /**
     * Configuration of the related field.
     */
    private FieldConfiguration fieldConfiguration;

    /**
     * Parent Object.
     */
    private final AbstractUIModeObject parent;

    /**
     * UserInterface Value.
     */
    private UIValue value;

    /**
     * Picker related to this field.
     */
    private UIPicker picker;

    /**
     * Already added.
     */
    private boolean added;

    /**
     * Factory applied for this field.
     */
    private String factoryKey;

    /**
     * Value as shown for a picklist.
     */
    private String pickListValue;

    /**
     * Settings for the AutoComplete.
     */
    private AutoCompleteSettings autoCompleteSetting;

    /**
     * @param _instanceKey key to the instance
     * @param _parent       parent object
     * @param _value        value
     * @throws EFapsException on error
     */
    public AbstractUIField(final String _instanceKey,
                           final AbstractUIModeObject _parent,
                           final UIValue _value)
        throws EFapsException
    {
        super(_instanceKey);
        this.parent = _parent;
        this.value = _value;
        this.fieldConfiguration = getNewFieldConfiguration();
    }

    /**
     * Getter method for the instance variable {@link #autoCompleteSetting}.
     *
     * @return value of instance variable {@link #autoCompleteSetting}
     */
    @Override
    public AutoCompleteSettings getAutoCompleteSetting()
    {
        if (this.autoCompleteSetting == null && isAutoComplete()) {
            this.autoCompleteSetting = new AutoCompleteSettings();

            this.autoCompleteSetting.setFieldName(getFieldConfiguration().getField().getName());
            final List<EventDefinition> events = getFieldConfiguration().getField().getEvents(
                            EventType.UI_FIELD_AUTOCOMPLETE);
            for (final EventDefinition event : events) {
                this.autoCompleteSetting.setMinInputLength(event.getProperty("MinInputLength") == null
                                ? 1 : Integer.valueOf(event.getProperty("MinInputLength")));
                this.autoCompleteSetting.setMaxChoiceLength(event.getProperty("MaxChoiceLength") == null
                                ? -1 : Integer.valueOf(event.getProperty("MaxChoiceLength")));
                this.autoCompleteSetting.setMaxValueLength(event.getProperty("MaxValueLength") == null
                                ? -1 : Integer.valueOf(event.getProperty("MaxValueLength")));
                if (event.getProperty("MaxResult") != null) {
                    this.autoCompleteSetting.setMaxResult(Integer.valueOf(event.getProperty("MaxResult")));
                }
                if (event.getProperty("HasDownArrow") != null) {
                    this.autoCompleteSetting
                                    .setHasDownArrow("true".equalsIgnoreCase(event.getProperty("HasDownArrow")));
                }
                if (event.getProperty("Required") != null) {
                    this.autoCompleteSetting
                                    .setRequired(!"false".equalsIgnoreCase(event.getProperty("Required")));
                }

                if (event.getProperty("AutoType") != null) {
                    this.autoCompleteSetting.setAutoType(EnumUtils.getEnum(AutoCompleteBehavior.Type.class,
                                    event.getProperty("AutoType")));
                }

                // add the ExtraParameter definitions
                final String ep = event.getProperty("ExtraParameter");
                if (ep != null) {
                    this.autoCompleteSetting.getExtraParameters().add(ep);
                }
                int i = 1;
                String keyTmp = "ExtraParameter" + String.format("%02d", i);
                while(event.getProperty(keyTmp) != null) {
                    this.autoCompleteSetting.getExtraParameters().add(event.getProperty(keyTmp));
                    i++;
                    keyTmp = "ExtraParameter" + String.format("%02d", i);
                }

                final String value4EditStr = event.getProperty("Value4Edit");
                if (value4EditStr != null) {
                    this.autoCompleteSetting.setValue4Edit(EditValue.valueOf(value4EditStr));
                }
            }
        }
        return this.autoCompleteSetting;
    }

    /**
     * Getter method for the instance variable {@link #parent}.
     *
     * @return value of instance variable {@link #parent}
     */
    @Override
    public AbstractUIModeObject getParent()
    {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasInstanceManager()
        throws EFapsException
    {
        return getParent() != null ? getParent().hasInstanceManager() : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instance getInstanceFromManager()
        throws EFapsException
    {
        return getParent().getInstanceFromManager();
    }

    /**
     * Getter method for the instance variable {@link #value}.
     *
     * @return value of instance variable {@link #value}
     */
    public UIValue getValue()
    {
        return this.value;
    }

    /**
     * Setter method for instance variable {@link #value}.
     *
     * @param _value value for instance variable {@link #value}
     */
    public void setValue(final UIValue _value)
    {
        this.value = _value;
    }

    /**
     * Getter method for the instance variable {@link #fieldConfiguration}.
     *
     * @return value of instance variable {@link #fieldConfiguration}
     */
    public FieldConfiguration getFieldConfiguration()
    {
        return this.fieldConfiguration;
    }

    /**
     * @return the label for the UserInterface
     * @throws CacheReloadException on error
     */
    @Override
    public String getLabel()
        throws EFapsException
    {
        String key;
        if (getFieldConfiguration() != null) {
            if (getFieldConfiguration().getField().getLabel() == null) {
                if (getValue() != null && getValue().getAttribute() != null) {
                    if (getInstanceKey() != null) {
                        key = getInstance().getType().getName() + "/" + getValue().getAttribute().getName() + ".Label";
                    } else {
                        key = getValue().getAttribute().getLabelKey();
                    }
                } else {
                    key = FieldConfiguration.class.getName() + ".NoLabel";
                }
            } else {
                key = getFieldConfiguration().getField().getLabel();
            }
        } else {
            key = FieldConfiguration.class.getName() + ".NoLabel";
        }
        return DBProperties.getProperty(key);
    }

    /**
     * Setter method for instance variable {@link #fieldConfiguration}.
     *
     * @param _fieldConfiguration value for instance variable {@link #fieldConfiguration}
     */

    protected void setFieldConfiguration(final FieldConfiguration _fieldConfiguration)
    {
        this.fieldConfiguration = _fieldConfiguration;
    }

    /**
     * @return a new FieldConfiguration
     * @throws EFapsException on error
     */
    protected FieldConfiguration getNewFieldConfiguration()
        throws EFapsException
    {
        FieldConfiguration ret;
        if (getValue() == null) {
            ret = null;
        } else {
            ret = new FieldConfiguration(getValue().getField().getId());
        }
        return ret;
    }

    /**
     * @return is this value editable
     */
    public boolean editable()
    {
        return getValue().getField().isEditableDisplay(getParent().getMode());
    }

    /**
     * @return is this value editable
     */
    public boolean hidden()
    {
        return getValue().getField().isHiddenDisplay(getParent().getMode());
    }

    /**
     * @return the List of Factories used for this Field on construction of the component.
     */
    public Map<String, IComponentFactory> getFactories()
    {
        return AbstractUIField.FACTORIES;
    }

    /**
     * @param _wicketId wicket id
     * @return Component
     * @throws EFapsException on error
     */
    @Override
    public Component getComponent(final String _wicketId)
        throws EFapsException
    {
        Component ret = null;
        final IComponentFactory factory = getFactory();
        if (factory == null) {
            ret = new LabelField(_wicketId, Model.of("No Factory was applied successfully"),
                            this.fieldConfiguration, "NONE");
        } else {
            if (hidden()) {
                ret = factory.getHidden(_wicketId, this);
            } else if (editable()) {
                ret = factory.getEditable(_wicketId, this);
            } else {
                ret = factory.getReadOnly(_wicketId, this);
            }
        }
        return ret;
    }

    public List<Return> executeEvents(final EventType _eventType,
                                      final Object _others,
                                      final Map<String, String> _uiID2Oid)
        throws EFapsException
    {
        List<Return> ret = new ArrayList<Return>();
        final Field field = getFieldConfiguration().getField();
        if (field.hasEvents(_eventType)) {
            final Context context = Context.getThreadContext();
            final String[] contextoid = { getInstanceKey() };
            context.getParameters().put("oid", contextoid);
            ret = field.executeEvents(_eventType,
                            ParameterValues.INSTANCE, getInstance(),
                            ParameterValues.OTHERS, _others,
                            ParameterValues.PARAMETERS, context.getParameters(),
                            ParameterValues.CLASS, this,
                            ParameterValues.OIDMAP4UI, _uiID2Oid,
                            ParameterValues.CALL_INSTANCE, getParent().getInstance(),
                            ParameterValues.CALL_CMD, ((AbstractUIObject) getParent()).getCommand());
        }
        return ret;
    }

    /**
     * Getter method for the instance variable {@link #picker}.
     *
     * @return value of instance variable {@link #picker}
     */
    @Override
    public UIPicker getPicker()
    {
        return this.picker;
    }

    /**
     * Setter method for instance variable {@link #picker}.
     *
     * @param _picker value for instance variable {@link #picker}
     */
    public void setPicker(final UIPicker _picker)
    {
        this.picker = _picker;
    }

    /**
     * @return true if a picker is assigned else false;
     */
    public boolean hasPicker()
    {
        return getPicker() != null;
    }

    /**
     *
     */
    public boolean hideLabel()
    {
       return hasPicker() && getPicker().isButton() || getFieldConfiguration().isHideLabel();
    }

    @Override
    public String toString()
    {
        return getValue().toString();
    }

     @Override
    public IHidden setAdded(final boolean _added)
    {
         this.added = _added;
        return this;
    }

    @Override
    public boolean isAdded()
    {
        return this.added;
    }

    @Override
    public boolean belongsTo(final Long _fieldId)
    {
        return getFieldConfiguration().getField().getId() == _fieldId;
    }

    @Override
    public String getPickListValue()
        throws EFapsException
    {
        if (this.pickListValue == null) {
            this.pickListValue = getFactory().getPickListValue(this);
        }
        return this.pickListValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comparable<?> getCompareValue()
    {
        Comparable<?> ret = null;
        try {
            ret = getFactory().getCompareValue(this);
        } catch (final EFapsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final ISortable _arg0)
    {
         return ObjectUtils.compare(getCompareValue(), _arg0.getCompareValue());
    }

    /**
     * Getter method for the instance variable {@link #factory}.
     *
     * @return value of instance variable {@link #factory}
     */
    public IComponentFactory getFactory()
        throws EFapsException
    {
        if (getFactoryKey() == null) {
            for (final IComponentFactory factory : getFactories().values()) {
                if (factory.applies(this)) {
                    setFactory(factory);
                    break;
                }
            }
        }
        return getFactories().get(getFactoryKey());
    }

    /**
     * Setter method for instance variable {@link #factory}.
     *
     * @param _factory value for instance variable {@link #factory}
     */
    public void setFactory(final IComponentFactory _factory)
    {
        this.factoryKey = _factory.getKey();
    }

    /**
     * Getter method for the instance variable {@link #factoryKey}.
     *
     * @return value of instance variable {@link #factoryKey}
     */
    public String getFactoryKey()
    {
        return this.factoryKey;
    }

    /**
     * Setter method for instance variable {@link #pickListValue}.
     *
     * @param _pickListValue value for instance variable {@link #pickListValue}
     */
    public void setPickListValue(final String _pickListValue)
    {
        this.pickListValue = _pickListValue;
    }

    /**
     * @return
     */
    public boolean isAutoComplete()
    {
        return getFieldConfiguration().getField().hasEvents(EventType.UI_FIELD_AUTOCOMPLETE);
    }

    @Override
    public boolean isFieldUpdate()
    {
        return getFieldConfiguration().getField().hasEvents(EventType.UI_FIELD_UPDATE);
    }

    @Override
    public List<Return> getAutoCompletion(final String _input,
                                          final Map<String, String> _uiID2Oid)
        throws EFapsException
    {
        return executeEvents(EventType.UI_FIELD_AUTOCOMPLETE, _input, _uiID2Oid);
    }

    @Override
    public String getAutoCompleteValue()
        throws EFapsException
    {
        return String.valueOf(getValue().getReadOnlyValue(getParent().getMode()));
    }

    @Override
    public Instance getInstance()
        throws EFapsException
    {
        Instance ret;
        // in case of an autocomplete in editmode give the chance to set the instance
        if (isAutoComplete() && getParent().isEditMode() && editable()
                        && getFieldConfiguration().getField().hasEvents(EventType.UI_FIELD_VALUE)) {
            getValue().getEditValue(getParent().getMode());
            ret = getValue().getInstance();
        } else {
            ret = super.getInstance();
        }
        return ret;
    }
}
