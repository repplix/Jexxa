package io.ddd.jexxa.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import io.ddd.jexxa.infrastructure.drivingadapter.CompositeDrivingAdapter;
import io.ddd.jexxa.infrastructure.drivingadapter.IDrivingAdapter;
import org.apache.commons.lang.Validate;

public class Jexxa
{
    private Properties properties;
    CompositeDrivingAdapter compositeDrivingAdapter;
    ClassFactory classFactory;

    public Jexxa(Properties properties)
    {
        this.properties = properties;
        compositeDrivingAdapter = new CompositeDrivingAdapter();
        classFactory = new ClassFactory(properties);
    }

    public void bind(Class<? extends IDrivingAdapter> adapter, Class<?> port) {
        var drivingAdapter = classFactory.createByConstructor(adapter);
        Validate.notNull(drivingAdapter);

        var inboundPort = classFactory.createByConstructor(port);
        Validate.notNull(inboundPort);
        drivingAdapter.register(inboundPort);

        compositeDrivingAdapter.add(drivingAdapter);
    }

    public void bindByAnnotation(Class<? extends Annotation> adapter, Class<? extends Annotation> port) {
        var annotationScanner = new AnnotationScanner();
        var scannedDrivingAdapters = annotationScanner.findClassAnnotation(adapter);
        var scannedInboundPorts = annotationScanner.findClassAnnotation(port);

        System.out.println("Found annotations " + scannedDrivingAdapters.size() + " Ports: " + scannedInboundPorts.size());

        //Create ports and adapter
        var createdDrivingAdapters = new ArrayList<IDrivingAdapter>();
        scannedDrivingAdapters.forEach(element -> System.out.println(element.getSimpleName()));

        scannedDrivingAdapters.forEach(element -> createdDrivingAdapters.add((IDrivingAdapter)classFactory.createByConstructor(element)));
        Validate.isTrue(scannedDrivingAdapters.size() == createdDrivingAdapters.size());

        var createdInboundPorts = new ArrayList<Object>();
        scannedInboundPorts.forEach(element -> createdInboundPorts.add(classFactory.createByConstructor(element)));
        Validate.isTrue(scannedInboundPorts.size() == createdInboundPorts.size());

        //register ports and adapter
        createdDrivingAdapters.forEach(drivingAdapter -> createdInboundPorts.forEach(drivingAdapter::register));
        createdDrivingAdapters.forEach(drivingAdapter -> compositeDrivingAdapter.add(drivingAdapter));
    }


    void startDrivingAdapters()
    {
        compositeDrivingAdapter.start();
    }

    void stopDrivingAdapters()
    {
        compositeDrivingAdapter.stop();
    }
}