package com.societegenerale.commons.plugin.rules;

import java.nio.file.Path;

/**
 * Created by agarg020917 on 11/10/2017.
 */
@FunctionalInterface
public interface ArchRuleTest {

  void execute(Path output, Path testOutput);

}
