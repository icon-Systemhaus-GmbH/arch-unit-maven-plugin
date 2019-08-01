package com.societegenerale.commons.plugin.utils;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by agarg020917 on 11/17/2017.
 */
public class ArchUtils {

    private ArchUtils() {
        throw new UnsupportedOperationException();
    }

    public static JavaClasses importAllClassesInPackage(List<Path> paths) {
        return new ClassFileImporter().importPaths(paths);
    }

    public static JavaClasses importAllClassesInPackage(Path... paths) {
        return new ClassFileImporter().importPaths(paths);
    }
}
