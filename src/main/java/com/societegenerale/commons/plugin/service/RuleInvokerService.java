package com.societegenerale.commons.plugin.service;

import com.societegenerale.commons.plugin.model.ConfigurableRule;
import com.societegenerale.commons.plugin.service.InvokableRules.InvocationResult;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.societegenerale.commons.plugin.utils.ArchUtils.importAllClassesInPackage;
import static com.societegenerale.commons.plugin.utils.ReflectionUtils.loadClassWithContextClassLoader;

public class RuleInvokerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleInvokerService.class);

    private static final String EXECUTE_METHOD_NAME = "execute";

    public String invokePreConfiguredRule(String ruleClassName, Path output, Path testOutput) {
        Class<?> ruleClass = loadClassWithContextClassLoader(ruleClassName);

        String errorMessage = "";
        try {
            Method method = ruleClass.getDeclaredMethod(EXECUTE_METHOD_NAME, Path.class, Path.class);
            method.invoke(ruleClass.newInstance(), output, testOutput);
        } catch (ReflectiveOperationException re) {
            errorMessage = re.getCause().toString();
        }
        return errorMessage;
    }

    public String invokeConfigurableRules(ConfigurableRule rule, Path output, Path testOutput) {
        if (rule.isSkip()) {
            LOGGER.info("Skipping rule {}", rule.getRule());
            return "";
        }

        InvokableRules invokableRules = InvokableRules.of(rule.getRule(), rule.getChecks());

        String packageOnRuleToApply = getPackageNameOnWhichToApplyRules(rule);
        List<Path> paths = filterAppliedPaths(rule, output, testOutput, packageOnRuleToApply);
        JavaClasses classes = importAllClassesInPackage(paths);

        InvocationResult result = invokableRules.invokeOn(classes);
        return result.getMessage();
    }

    private List<Path> filterAppliedPaths(ConfigurableRule rule, Path output, Path testOutput, String packageOnRuleToApply) {
        List<Path> result = new ArrayList<>(1);

        if (rule.getApplyOn() != null) {
            if (rule.getApplyOn().getScope() != null && "test".equals(rule.getApplyOn().getScope())) {
                result.add(testOutput.resolve(packageOnRuleToApply));
            }
        }
        if (result.isEmpty()) {
            result.add(output.resolve(packageOnRuleToApply));
        }
        return result;
    }

    private String getPackageNameOnWhichToApplyRules(ConfigurableRule rule) {

        StringBuilder packageNameBuilder = new StringBuilder();
        if (rule.getApplyOn() != null) {
            if (rule.getApplyOn().getPackageName() != null) {
                packageNameBuilder.append("/").append(rule.getApplyOn().getPackageName());
            }
        }
        return packageNameBuilder.toString().replace(".", "/");
    }
}
