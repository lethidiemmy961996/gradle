import org.gradle.gradlebuild.PublicApi

plugins {
    gradlebuild.internal.java
}

dependencies {
    testImplementation(project(":baseServices"))
    testImplementation(project(":modelCore"))

    testImplementation(testLibrary("archunit_junit4"))
    testImplementation(library("guava"))

    rootProject.subprojects {
        val subproject = this.path
        plugins.withId("gradlebuild.distribution.core") {
            testRuntimeOnly(project(subproject))
        }
        plugins.withId("gradlebuild.distribution.plugins") {
            testRuntimeOnly(project(subproject))
        }
    }
}

tasks.withType<Test> {
    // Looks like loading all the classes requires more than the default 512M
    maxHeapSize = "700M"

    systemProperty("org.gradle.public.api.includes", PublicApi.includes.joinToString(":"))
    systemProperty("org.gradle.public.api.excludes", PublicApi.excludes.joinToString(":"))
}
