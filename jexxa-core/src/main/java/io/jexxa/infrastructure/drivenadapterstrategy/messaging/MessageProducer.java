package io.jexxa.infrastructure.drivenadapterstrategy.messaging;

import static io.jexxa.utils.json.JSONManager.getJSONConverter;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import io.jexxa.utils.annotations.CheckReturnValue;

@CheckReturnValue
public class MessageProducer
{
    public enum DestinationType { TOPIC, QUEUE }
    private Properties properties;
    private final Object message;
    private final MessageSender messageSender;

    private DestinationType destinationType;
    private String destination;

    protected <T> MessageProducer(T message, MessageSender messageSender)
    {
        this.message = Objects.requireNonNull(message);
        this.messageSender = Objects.requireNonNull(messageSender);
    }

    @CheckReturnValue
    public MessageProducer toQueue(String destination)
    {
        this.destination = Objects.requireNonNull(destination);
        this.destinationType = DestinationType.QUEUE;

        return this;
    }

    @CheckReturnValue
    public MessageProducer toTopic(String destination)
    {
        this.destination = Objects.requireNonNull(destination);
        this.destinationType = DestinationType.TOPIC;

        return this;
    }

    @CheckReturnValue
    public MessageProducer addHeader(String key, String value)
    {
        if (properties == null)
        {
            properties = new Properties();
        }

        properties.put(key, value);

        return this;
    }

    public void asJson()
    {
        as(getJSONConverter()::toJson);
    }

    public void asString()
    {
        as(message::toString);
    }

    public void as( Function<Object, String> serializer )
    {
        Objects.requireNonNull(destination, "No destination in MessageProducer set");

        if (destinationType == DestinationType.QUEUE)
        {
            messageSender.sendToQueue( serializer.apply(message), destination, properties);
        }
        else
        {
            messageSender.sendToTopic( serializer.apply(message), destination, properties);
        }
    }

    private void as( Supplier<String> serializer )
    {
        Objects.requireNonNull(destination,  "No destination in MessageProducer set");

        if (destinationType == DestinationType.QUEUE)
        {
            messageSender.sendToQueue(serializer.get(), destination, properties);
        }
        else
        {
            messageSender.sendToTopic(serializer.get(), destination, properties);
        }
    }


}
