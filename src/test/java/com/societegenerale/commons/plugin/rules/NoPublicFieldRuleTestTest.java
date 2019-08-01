package com.societegenerale.commons.plugin.rules;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatCode;

public class NoPublicFieldRuleTestTest {

    private Path output = Paths.get("./target/aut-target/classes/com/societegenerale/aut/main");
    private Path testOutput = Paths.get("./target/aut-target/test-classes/com/societegenerale/aut/test");

    @Test(expected = AssertionError.class)
    public void shouldThrowViolations() {
        new NoPublicFieldRuleTest().execute(output.resolve("ObjectWithPublicField.class"), testOutput);
    }

    @Test
    public void shouldNotThrowAnyViolation_even_with_publicStaticFinaField() {
        assertThatCode(() -> new NoPublicFieldRuleTest().execute(output.resolve("ObjectWithNoNonStaticPublicField.class"), testOutput))
                .doesNotThrowAnyException();
    }
}
