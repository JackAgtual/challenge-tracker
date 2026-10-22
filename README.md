# Challenge Tracker

## Containerization

api and web containers are built and pushed to ghcr.io on every push to main.
Containers are tagged with git commit hash and latest.

### api

Run the container with the following command:

```
docker run -d \
-p <port>:8080 \
--network <network> \
--name <desired-container-name>
-e SPRING_DATASOURCE_URL=<datasource-url> \
-e SPRING_DATASOURCE_USERNAME=<datasource-username> \
-e SPRING_DATASOURCE_PASSWORD=<datasource-password> \
challenge-tracker-api:latest
```

Datasource (Postgres) must be on the same network as this container.

### web

Run the container with the following command:

```
docker run -d \
-p <port>:3000 \
--network <network> \
--name <desired-container-name>
-e AUTH0_DOMAIN=<auth0-domain> \
-e AUTH0_CLIENT_ID=<auth0-client-id> \
-e AUTH0_CLIENT_SECRET=<auth0-client-secret> \
-e AUTH0_SECRET=<auth0-secret> \
-e AUTH0_AUDIENCE=https://challenge-tracker-api \
-e APP_BASE_URL=http://localhost:3000 \
-e API_BASE_URL=http://<api-container-name>:<api-port> \
ghcr.io/jackagtual/challenge-tracker-web:latest
```

api must be on the same network as this container.
