IMAGE_ORG := cnapcloud
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
	docker build --no-cache --load -t $(IMAGE_REPOSITORY):$(IMAGE_TAG) .
	docker tag $(IMAGE_REPOSITORY):$(IMAGE_TAG) $(IMAGE_REPOSITORY):latest
	make clean
	
	
docker-build-mutiplatform:
	@echo "[docker-build] Build the Docker for arm64, amd64"
	docker buildx build \
		--platform linux/amd64,linux/arm64 \
		--no-cache \
		--push \
		-t $(IMAGE_REPOSITORY):$(IMAGE_TAG) \
		-t $(IMAGE_REPOSITORY):latest .
	make clean	

docker-push:
	@echo "[docker-push] Push the Docker image."
	docker push $(IMAGE_REPOSITORY):$(IMAGE_TAG)

clean:
	@echo "[clean] Clean up Docker builder cache."
	yes| docker builder prune

.PHONY: help build report run-project run-orchestrator docker-build docker-push clean