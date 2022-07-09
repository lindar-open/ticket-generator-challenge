build-docker:
	docker build -f distribution/docker/Dockerfile --build-arg JAR_FILE=build/libs/\*.jar -t lindar/bingoticketgenerator-apalfi:0.0.1-SNAPSHOT .

build-gradle:
	./gradlew clean build

build: build-gradle build-docker

run-docker:
	docker run -p 8080:8080 lindar/bingoticketgenerator-apalfi:0.0.1-SNAPSHOT

boot-run:
	./gradlew bootRun

run-tests:
	./gradlew test --tests "com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator*"

run-perf-tests:
	./gradlew test --tests 'com.attilapalfi.lindar.bingoticketgenerator.performancetest*'
