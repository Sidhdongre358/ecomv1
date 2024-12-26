#!/bin/bash

# Define the base path for all services
BASE_PATH="/path/to/ecomv1"

# Start the discovery service first
echo "Starting discovery-service..."
nohup java -jar "$BASE_PATH/discovery-service/target/discovery-service.jar" > "$BASE_PATH/discovery-service/discovery-service.log" 2>&1 &
echo "discovery-service started!"

# Wait for a few seconds to give the discovery service time to start
sleep 10

# List all services (excluding discovery-service since it's already started)
SERVICES=("api-gateway" "favourite-service" "inventory-service" "notification-service" \
          "order-service" "payment-service" "product-service" "promotion-service" "proxy-client" "rating-service" \
          "search-service" "shipping-service" "tax-service" "user-service")

# Iterate over each service and start them
for service in "${SERVICES[@]}"; do
  echo "Starting $service..."
  nohup java -jar "$BASE_PATH/$service/target/$service.jar" > "$BASE_PATH/$service/$service.log" 2>&1 &
  echo "$service started!"
done

echo "All services are started!"
