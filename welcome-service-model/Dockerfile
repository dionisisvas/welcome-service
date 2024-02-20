FROM postgres:14-alpine

# postgres vars
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

COPY create-db-and-users.sql /docker-entrypoint-initdb.d/