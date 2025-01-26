 APP_NAME = "system-toolbox"
 
up: discovery-up gateway-up user-up stock-up
	@echo "MAKE UP"
	
down: stock-down user-down gateway-down discovery-down
	@echo "MAKE DOWN"

dev: ## Run container in development mode
	@echo "DEV"

##UP
discovery-up:
	docker-compose -f discovery.yaml up -d
gateway-up:
	docker-compose -f gateway.yaml up -d
user-up:
	docker-compose -f user.yaml up -d
stock-up: ##
	docker-compose -f stock.yaml up -d


##DOWN
discovery-down:
	docker-compose -f discovery.yaml down
gateway-down:
	docker-compose -f gateway.yaml down
user-down:
	docker-compose -f user.yaml down
stock-down:
	docker-compose -f stock.yaml down
