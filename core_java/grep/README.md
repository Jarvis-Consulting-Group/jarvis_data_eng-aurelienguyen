# Introduction
The GREP application scans a specified directory recursively to find a given text pattern and outputs the matched lines into a file.
## What are the technologies used?
- **Java 8**: Streams API, lambda functions
- **Docker**: image creation (pushed to Docker Hub) 
- **MVN**: log management (_slf4j_, _log4j_), dependency management, project packaging
- **IntelliJ**: IDE

# Quick Start
Download the Docker image from Docker Hub and run the following command:
```docker run --rm -v `pwd`/data:/data -v `pwd`/out:/out {docker_username}/grep ${regex_pattern} ${source_path} /out/${outfile}```

# Implementation
## Pseudocode
`process` method pseudocode:
```
matchedLines = []
for file in listFilesRecursively(rootDir)
    for line in readLines(file)
        if containsPattern(line)
            matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
A memory issue that could arise is if the heap memory size is insufficient to process the data. Indeed, the text file `shakespeare.txt` used to test the project is around 5.3MB. Therefore, if the heap size is set to 5MB, an OutOfMemoryError will be thrown while reading the file.
A potential solution to the problem would be to set the heap memory size to a higher number using `java -xmx` flag when running the program.

# Test
The application was manually tested with the debugger of IntelliJ. The `shakespeare.txt` sample text file was used as the input data. The results were compared to the output file for accuracy.

# Deployment
Deployment using Docker:
```
docker login

# create DockerFile
cat > Dockerfile << EOF
FROM openjdk:8-alpine
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]
EOF

# package java app
mvn clean package

# build docker image locally
docker build -t ${docker_user}/grep .

# push image to Docker hub
docker push ${docker_user}/grep
```

# Improvement
1. Use unit testing for the functions in the grep app.
2. Use `BufferedReader` instead of `Scanner` for speed and efficiency since the former reads a block of characters instead of a single token at a time.