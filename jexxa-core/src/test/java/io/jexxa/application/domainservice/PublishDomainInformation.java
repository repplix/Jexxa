package io.jexxa.application.domainservice;

import io.jexxa.application.domain.valueobject.JexxaValueObject;

public class PublishDomainInformation
{
    private final IJexxaPublisher jexxaPublisher;

    public PublishDomainInformation(IJexxaPublisher jexxaPublisher)
    {
        this.jexxaPublisher = jexxaPublisher;
    }

    public void sendToQueue(JexxaValueObject jexxaValueObject)
    {
        jexxaPublisher.sendToQueue(jexxaValueObject);
    }

    public void sendToTopic(JexxaValueObject jexxaValueObject)
    {
        jexxaPublisher.sendToTopic(jexxaValueObject);
    }

}
