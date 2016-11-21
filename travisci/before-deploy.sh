#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_7d915bc7a3f5_key -iv $encrypted_7d915bc7a3f5_iv -in travisci/codesigning.asc.enc -out travisci/codesigning.asc -d
    gpg --fast-import cd/signingkey.asc
fi
