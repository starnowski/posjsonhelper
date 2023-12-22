#!/usr/bin/env bash

function resolveScriptDirectory {
    echo "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
}

function waitUntilDockerContainerIsReady {
    checkCount=1
    timeoutInSeconds=180
    while : ; do
        set +e
        results=`psql -qtAX -U postgres -p $DATABASE_PORT --host="$DOCKER_DB_IP" -c "SELECT 1;"`
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for database to start"
        sleep 1
    done
    set -e
}

function shutdownDockerContainer {
    lastCommandStatus="$?"
    echo "Shutting down docker container"
    sudo docker rm -f -v test-postgres
    exit $lastCommandStatus
}

function exportScriptDirEnvironment {
    SCRIPT_DIR=`resolveScriptDirectory`
    export SCRIPT_DIR="$SCRIPT_DIR"
}

function startPostgresDockerContainer {
    sudo docker run --rm --name test-postgres -e POSTGRES_PASSWORD=postgres -p 127.0.0.1:$DATABASE_PORT:5432/tcp -d postgres:$POSTGRES_DOCKER_VERSION
}

function copyCustomDictionaryToDatabaseDockerContainer {
exportScriptDirEnvironment
tmp_dictionary_dir=`mktemp -d`
echo "Created temporary directory for dictionary ${tmp_dictionary_dir}"
pushd dictionary
tar -xvjf sjp-*-src.tar.bz2 -C "${tmp_dictionary_dir}"
cp polish.stopwords.txt "${tmp_dictionary_dir}"

pushd "${tmp_dictionary_dir}"
iconv -f ISO_8859-2 -t utf-8 polish.aff > polish.affix
iconv -f ISO_8859-2 -t utf-8 polish.all > polish.dict
mv polish.stopwords.txt polish.stop
popd

popd
}
