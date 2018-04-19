package com.nerzid.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

import static com.nerzid.VariableModel.numberOfMethods;
import static com.nerzid.VariableModel.numberOfVariables;
import static com.nerzid.VariableModel.variablesWithFreqs;

/**
 * Created by @author nerzid on 28.03.2018.
 */
public class MethodProcessor extends AbstractProcessor<CtMethod> {
    @Override
    public void process(CtMethod element) {
        if (element.getBody() != null) {
//            System.out.println("*********************");
            System.out.println("Method: " + element.getSimpleName());
//            List<CtVariable> classVarList = ((CtClass)(element.getParent())).getFields();
//            for (int i = 0; i < classVarList.size(); i++) {
//                System.out.println(classVarList.get(0).getSimpleName());
//            }
            List<CtLocalVariable> vars = element.getElements(new TypeFilter(CtLocalVariable.class));

            for (CtLocalVariable var :
                    vars) {
                String varName = var.getSimpleName();
                if(varName.trim().length() > 2) {
                    if (variablesWithFreqs.containsKey(varName))
                        variablesWithFreqs.put(varName, variablesWithFreqs.get(varName)+1);
                    else
                        variablesWithFreqs.put(varName, 1);
                    numberOfVariables++;
                }
            }
            numberOfMethods++;
//            System.out.println(variableSet);
//            System.out.println("*********************");
        }
    }
}
