IMAGE_ORG := harbor.cnapcloud.com/library
IMAGE_REPOSITORY := ${IMAGE_ORG}/spring-msa-demo
IMAGE_TAG := $(shell git rev-parse --short=7 HEAD)

help:
	@echo "Available targets:"; awk '/^[a-zA-Z0-9_-]+:/ {t=$$1; sub(/:$$/, "", t); \
	if(t=="help") next; getline; if($$0 ~ /@echo/) {sub(/.*@echo[ \t]*/, "", $$0); \
	printf "  %-15s %s\n", t, $$0}}' $(MAKEFILE_LIST)	

build:
	@echo "[build] Build all projects."
	./gradlew clean build -x test --no-build-cache
		
report:
	@echo "[report] Generate test."
	./gradlew test jacocoTestReport
	
publish:
	./gradlew publish -x test -Pusername=admin -Ppassword=password
	
run-project:
	@echo "[build] run Project service."
	./gradlew project-service:bootRun
	
run-orchestrator:
	@echo "[build] run Orchestrator service."
	./gradlew orchestrator-service:bootRun		
		
docker-build:
	@echo "[docker-build] Build the Docker image"
	buildctl --addr tcp://buildkitd.cicd.svc:1234 build \
	--frontend dockerfile.v0 \
	--local context=. \
	--local dockerfile=. \
	--output type=image,name=$(IMAGE_REPOSITORY):$(IMAGE_TAG),push=true
	
clean:
	@echo "[clean] Clean up Docker builder cache."
	buildctl --addr tcp://buildkitd.cicd.svc:1234 prune --all --force

.PHONY: help build report run-project run-orchestrator docker-build docker-push clean