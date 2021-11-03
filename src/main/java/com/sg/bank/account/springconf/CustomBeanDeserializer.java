package com.sg.bank.account.springconf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.util.NameTransformer;
import io.rabitka.core.ddd.Validatable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomBeanDeserializer extends BeanDeserializer {
    public CustomBeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
        super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
    }

    public CustomBeanDeserializer(BeanDeserializerBase src) {
        super(src);
    }

    protected CustomBeanDeserializer(BeanDeserializerBase src, boolean ignoreAllUnknown) {
        super(src, ignoreAllUnknown);
    }

    protected CustomBeanDeserializer(BeanDeserializerBase src, NameTransformer unwrapper) {
        super(src, unwrapper);
    }

    public CustomBeanDeserializer(BeanDeserializerBase src, ObjectIdReader oir) {
        super(src, oir);
    }

    public CustomBeanDeserializer(BeanDeserializerBase src, Set<String> ignorableProps) {
        super(src, ignorableProps);
    }

    public CustomBeanDeserializer(BeanDeserializerBase src, BeanPropertyMap props) {
        super(src, props);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        Object object = super.deserialize(p, ctxt);
        if (Validatable.class.isAssignableFrom(object.getClass())) {
            ((Validatable)object).validate();
        }
        return object;
    }

}
