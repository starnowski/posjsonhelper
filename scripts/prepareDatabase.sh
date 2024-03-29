#!/usr/bin/env bash
DIRNAME="$(dirname $0)"
POSTGRES_HOST=127.0.0.1
DATABASE_PORT=5432

# Call getopt to validate the provided input.
options=$(getopt -o "" --long postgres_host:,postgres_port: -- "$@")
[ $? -eq 0 ] || {
    echo "Incorrect options provided"
    exit 1
}
eval set -- "$options"
while true; do
    case "$1" in
    --postgres_host)
        shift; # The arg is next in position args
        export POSTGRES_HOST="$1"
        ;;
    --postgres_port)
        shift; # The arg is next in position args
        export DATABASE_PORT="$1"
        ;;
    --)
        shift
        break
        ;;
    esac
    shift
done

export PGPASSWORD=postgres
psql -f "${DIRNAME}/../test-utils/src/main/resources/create-database-owner.sql" -U postgres -h "${POSTGRES_HOST}" -p "${DATABASE_PORT}"
psql -f "${DIRNAME}/../test-utils/src/main/resources/create-database.sql" -U postgres -h "${POSTGRES_HOST}" -p "${DATABASE_PORT}"
psql -f "${DIRNAME}/../test-utils/src/main/resources/create-database-core-database-and-data.sql" -U postgres -h "${POSTGRES_HOST}" -p "${DATABASE_PORT}"