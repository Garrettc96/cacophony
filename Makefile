
.PHONY:test
test:
	mvn clean install

.PHONY:publish
publish:
	./publish.sh