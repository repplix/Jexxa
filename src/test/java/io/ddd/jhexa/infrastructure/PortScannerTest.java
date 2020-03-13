package io.ddd.Jexxa.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.ddd.Jexxa.applicationcore.SimpleApplicationService;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;

import javax.sound.sampled.Port;

import io.ddd.stereotype.applicationcore.ApplicationService;
import org.junit.Test;

public class PortScannerTest
{
    @Test
    public void findApplicationServiceWithPacakgeName() {
        PortScanner portScanner = new PortScanner("io.ddd.Jexxa.applicationcore");
        findApplicationService(portScanner);
    }

    @Test
    public void findApplicationServiceWithoutPacakgeName() {
        PortScanner portScanner = new PortScanner();
        findApplicationService(portScanner);
    }

    public void findApplicationService(PortScanner portScanner) {
        List<Class<?>> applicationServiceList = portScanner.findAnnotation(ApplicationService.class);

        assertFalse(applicationServiceList.isEmpty());
        assertEquals(1, applicationServiceList.size());
        assertTrue(applicationServiceList
                .stream()
                .anyMatch(element -> element.getName().equals(SimpleApplicationService.class.getName())));
    }
}
