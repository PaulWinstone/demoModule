# Creating bespoke applications with the org.beryx.runtime plugin

This simple Windows application shows how to create multiple executable images, each one customized for a different client.

For each customer, we store its specific resources and configuration files in a subdirectory of the `config` directory. The name of the subdirectory is given by the `customerId`. In our example, we consider three customers: Amazon, Facebook, and Google, where the `customerId` is the customer name in lowercase.

The bespoke executables display the customer logo and a greeting. The greeting message is provided by a DLL available in the `dll` directory. The color of the greeting message is configured in the `customer.properties` file available in each customer-specific subdirectory.

customerId: amazon |customerId: facebook | customerId: google
-------------------|---------------------|-------------------
![amazon](/demoProject/doc/amazon.png)|![facebook](/demoProject/doc/facebook.png)|![google](/demoProject/doc/google.png)

To create the bespoke executables run:
```
gradlew jpackageAll
```

The images will be available in `build\jpackage`.
You can start the applications by executing:
```
build\jpackage\amazon\demoProject\demoProject.exe
build\jpackage\facebook\demoProject\demoProject.exe
build\jpackage\google\demoProject\demoProject.exe
```

You can also run the applications directly from Gradle:
```
gradlew -P customerId=amazon run
gradlew -P customerId=facebook run
gradlew -P customerId=google run
```

As you can see, we use the `customerId` property to tell Gradle which bespoke application to run. A default value for the `customerId` property is provided in `gradle.properties`:
```
customerId=google
```

You can also use the `installDist` task to create bespoke distributions:
```
gradlew -P customerId=amazon installDist
gradlew -P customerId=facebook installDist
gradlew -P customerId=google installDist
```

The Gradle script takes care of several things:
- configures `java.library.path` for the `run` task:
```
run {
    jvmArgs "-Djava.library.path=$projectDir/dll"
}
```
- configures `java.library.path` for the `test` task:
```
test {
    jvmArgs "-Djava.library.path=$projectDir/dll"
}
```
- copies customer-specific resources and configuration files:
```
tasks.processResources {
    from "config/$customerId"
    into sourceSets.main.java.outputDir
}
```
- copies the DLL into the `bin` directory of a distribution created by the `installDist` task and configures `java.library.path` in the start script:
```
tasks.installDist {
    doLast {
        copy {
            from "dll"
            into "$destinationDir/bin"
        }
    }
}
tasks.startScripts {
    def gen = windowsStartScriptGenerator
    gen.template = resources.text.fromString(gen.template.asString().replace(
    	'DEFAULT_JVM_OPTS=${defaultJvmOpts}',
    	'DEFAULT_JVM_OPTS="-Djava.library.path=%APP_HOME%\\\\bin" ${defaultJvmOpts}'))
}
```
- copies the DLL into the executable image created by `jpackage`:
```
tasks.jpackageImage {
    doLast {
        copy {
            from "dll"
            into "$jpackageData.imageOutputDir/$jpackageData.imageName"
        }
    }
}
```
Note that there is no need to set `java.library.path`, because it already includes the directory of the executable image.
- use the `GradleRunner` available in the [Gradle TestKit](https://docs.gradle.org/current/userguide/test_kit.html) to execute the `jpackage` task repeatedly, each time setting a different value for the `customerId` property:
```
task jpackageAll(group: 'distribution', description: 'Creating packages for all customers') {
    doFirst {
        file("config").eachDir {
            def customer = it.name
            GradleRunner.create()
                        .withDebug(true)
                        .withProjectDir(projectDir)
                        .forwardOutput()
                        .withArguments("-is", "-P", "customerId=$customer", "build", "jpackage")
                        .build();
        }
    }
}
```
