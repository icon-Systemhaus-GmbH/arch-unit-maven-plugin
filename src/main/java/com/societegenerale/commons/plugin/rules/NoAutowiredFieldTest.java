package com.societegenerale.commons.plugin.rules;

import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.nio.file.Path;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

/**
 * We usually favor constructor injection rather than field injection through Spring's @Autowired : this way we can make sure the object is in correct state whenever it's used.
 */
public class NoAutowiredFieldTest implements ArchRuleTest  {

    protected static final String NO_AUTOWIRED_FIELD_MESSAGE = "Favor constructor injection and avoid autowiring fields - ";

    @Override
    public void execute(Path output, Path testOutput) {

        fields().should(notBeAutowired()).check(ArchUtils.importAllClassesInPackage(output));
    }

    protected static ArchCondition<JavaField> notBeAutowired() {

        return new ArchCondition<JavaField>("not be autowired") {

            @Override
            public void check(JavaField javaField, ConditionEvents events) {

                if(javaField.isAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")){

                    events.add(SimpleConditionEvent.violated(javaField, NO_AUTOWIRED_FIELD_MESSAGE
                            +" - class: "+javaField.getOwner().getName()
                            +" - field name: "+javaField.getName()));

                }


            }
        };
    }

}
