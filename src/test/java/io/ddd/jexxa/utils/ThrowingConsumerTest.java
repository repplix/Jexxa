package io.ddd.jexxa.utils;

import static io.ddd.jexxa.utils.ThrowingConsumer.exceptionCollector;
import static io.ddd.jexxa.utils.ThrowingConsumer.exceptionLogger;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class ThrowingConsumerTest
{

    @Test
    public void exceptionLoggerTest()
    {
        //Arrange
        Integer[] values = {1,2,3};

        //Act
        Arrays.stream(values).
                forEach(
                        exceptionLogger(value -> Integer.divideUnsigned(value, 0))
                );

        //Assert => No assertion must occur
    }

    @Test
    public void exceptionCollectorTest()
    {
        //Arrange
        Integer[] values = {1,2,3};
        var exceptions = new ArrayList<Throwable>();

        //Act
        Arrays.stream(values).
                forEach(
                        exceptionCollector(value -> Integer.divideUnsigned(value, 0),exceptions)
                );

        //Assert
        assertFalse(exceptions.isEmpty());
        assertEquals(values.length, exceptions.size());
    }
}