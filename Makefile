
.PHONY:test
test:
	mvn clean install -Dliquibase.skip -Djooq.codegen.skip

.PHONY:publish
publish:
	./scripts/publish.sh

.PHONY:performance
performance:
	./scripts/performance_test.sh