package com.nerzid.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

/**
 * Created by @author nerzid on 28.03.2018.
 */
public class MethodProcessor extends AbstractProcessor<CtMethod> {

    @Override
    public void process(CtMethod element) {
        if (element.getBody() != null) {

        }
    }
}
