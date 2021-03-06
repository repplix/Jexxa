package io.jexxa.core;

import java.util.Objects;
import java.util.function.Consumer;

public class BootstrapService<T>
{
    private final Class<T> bootstrapServiceClass;
    private final JexxaMain jexxaMain;

    BootstrapService(Class<T> bootstrapService, JexxaMain jexxaMain)
    {
        this.bootstrapServiceClass = Objects.requireNonNull(bootstrapService);
        this.jexxaMain = Objects.requireNonNull(jexxaMain);
    }

    public JexxaMain with(Consumer<T> initFunction)
    {
        jexxaMain.addBootstrapService(bootstrapServiceClass, initFunction);
        return jexxaMain;
    }
}
