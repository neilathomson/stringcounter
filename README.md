# String Counter Application

This application counts the number of occurrences of a word within a file.

## Assumptions

- All inputs are UTF-8 encoded.
- Hyphens, apostrophes and 'a' through to 'z' are valid characters as well as accented characters eg. ü
- The application will not be required to run on excessively large inputs (it has been tested up to 30MB file sizes - it can potentially do significantly more)
- The application will be built and tested to run on the maximum amount of environments from Java 8 to Java 21

## Usage

- Download the release artifact (either .zip or .tar)
- extract the artifact
- run (Windows) `./bin/stringcounter.bat <FILENAME>` eg `./bin/stringcounter.bat "C:\Users\neila\test.txt"`
- run (UNIX) `./bin/stringcounter <FILENAME>` eg `bin/stringcounter "./test.txt"`

The filename can be relative or full.

You can also specify the debug flag if you wish to see debug output `bin/stringcounter <FILENAME> --debug`

If you require fine-grained control over the JVM options then you can modify the `STRINGCOUNTER_OPTS` environment
variable. To see how this is used in the startup script see here:

```
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %STRINGCOUNTER_OPTS%  -classpath "%CLASSPATH%" dev.neilthomson.stringcounter.StringCounter %*
```

## Building

If you wish to build the application yourself then you should:

- Clone the repo
- run `./gradlew build` in the root of the project
- Extract a distribution (.zip or .tar) located in `build/distributions`

## Profiling

Three implementations were tested (SimpleWordCounter, SimpleParallelWordCounter, BufferedWordCounter), the best overall
performing implementation was BufferedWordCounter that had low memory usage, however it was slower than
SimpleParallelWordCounter, but the memory efficiency was preferred.

Please see the `prototypes` branch for all the implementations.

## Limitations

- The tool is not suitable for use on very large files

## Enhancements

- Add better logging and store logs in a temp directory so user can view them if needed
- Perform more profiling and implement a better strategy (poss. make BufferedWordCounter run chunks in parallel)
