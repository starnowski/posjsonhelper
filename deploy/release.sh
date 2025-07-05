#!/bin/bash

DIRNAME="$(dirname $0)"

# Setting gpg directory path
export GPG_DIR="${DIRNAME}"

function removeSecretKey {
    lastCommandStatus="$?"
    echo "Removing keys"
    gpg --batch --yes --delete-secret-key "${GPG_PUBLIC_KEYNAME}"
    exit $lastCommandStatus
}

trap removeSecretKey EXIT SIGINT

# Decrypting key files
openssl aes-256-cbc -d -pass "pass:${ENCRYPTION_PASSWORD}" -pbkdf2 -in "${GPG_DIR}/private.key.enc" -out "${GPG_DIR}/private.key"
openssl aes-256-cbc -d -pass "pass:${ENCRYPTION_PASSWORD}" -pbkdf2 -in "${GPG_DIR}/public.key.enc" -out "${GPG_DIR}/public.key"

gpg --batch --yes --pinentry-mode loopback --import "${GPG_DIR}/private.key"
gpg --import "${GPG_DIR}/public.key"

#Test
#"${DIRNAME}/../mvnw" clean install -DperformRelease=true -DskipTests=true

# Prod
"${DIRNAME}/../mvnw" deploy --settings "${GPG_DIR}/settings.xml" -DperformRelease=true -DskipTests=true
exit $?