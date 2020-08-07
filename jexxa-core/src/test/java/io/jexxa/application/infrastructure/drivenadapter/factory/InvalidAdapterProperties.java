package io.jexxa.application.infrastructure.drivenadapter.factory;

import java.util.Properties;

import io.jexxa.application.domainservice.IInvalidAdapterProperties;
import org.apache.commons.lang3.Validate;

@SuppressWarnings("unused")
public class InvalidAdapterProperties implements IInvalidAdapterProperties
{
    public InvalidAdapterProperties(Properties properties)
    {
        Validate.notNull(properties);
        throw new IllegalArgumentException("InvalidAdapterProperties test");
    }
}
