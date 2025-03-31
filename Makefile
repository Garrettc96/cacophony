
.PHONY:test
test:
	mvn clean install

.PHONY:publish
publish:
	./scripts/publish.sh

.PHONY:performance
performance:
	./scripts/performance_test.sh