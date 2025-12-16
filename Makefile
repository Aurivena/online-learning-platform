dc = docker-compose

restart:
	$(dc) down -v
	$(dc) up

up:
	$(dc) up

logs:
	$(dc) logs -f