install-skill:
	cp -r skill/. ~/.claude/skills/

publish-local:
	./gradlew :mosaic-core:publishToMavenLocal :mosaic-server:publishToMavenLocal :mosaic-client:publishToMavenLocal

run-sample-server:
	./gradlew :sample-server:run