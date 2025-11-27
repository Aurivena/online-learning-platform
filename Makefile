dc = docker-compose

restart:
	$(dc) down -v
	$(dc) up

up:
	$(dc) up

logs:
	$(dc) logs -f

run:
	./gradlew bootRun --stacktrace

sql:
	docker exec -it lms-postgres psql -U postgres -d lms_db