#!/bin/bash

set -ev

GH_REPO="@github.com/InvisibleTeam/GoInvisible.git"
GH_ARTIFACTS_DIR="$TRAVIS_BUILD_DIR/gh-page"
FULL_REPO="https://$GH_TOKEN$GH_REPO"
FULL_BUILD_DIR="$TRAVIS_BUILD_DIR/app/build"

rm -rf ${GH_ARTIFACTS_DIR} || exit 0;
mkdir ${GH_ARTIFACTS_DIR}
cd ${GH_ARTIFACTS_DIR}

git init
git config user.name "InvisibleTeam-travis"
git config user.email "travis"

# if exist copy necessary directories and files:
# - lint results
# - checkstyle
# - findbugs
# - jacoco testReport
if [ -e ${FULL_BUILD_DIR}/outputs/lint-results-debug.html ]
then
    cp -R ${FULL_BUILD_DIR}/outputs/lint-results-debug.html ${GH_ARTIFACTS_DIR}
fi

if [ -e ${FULL_BUILD_DIR}/outputs/lint-results-release-fatal.html ]
then
    cp -R ${FULL_BUILD_DIR}/outputs/lint-results-release-fatal.html ${GH_ARTIFACTS_DIR}
fi

if [ -e ${FULL_BUILD_DIR}/reports/checkstyle/checkstyle.html ]
then
    cp -R ${FULL_BUILD_DIR}/reports/checkstyle/checkstyle.html ${GH_ARTIFACTS_DIR}
fi

if [ -e ${FULL_BUILD_DIR}/reports/findbugs/findbugs.html ]
then
    cp -R ${FULL_BUILD_DIR}/reports/findbugs/findbugs.html ${GH_ARTIFACTS_DIR}
fi

if [ -e ${FULL_BUILD_DIR}/reports/jacoco/testReport/html ]
then
    cp -R ${FULL_BUILD_DIR}/reports/jacoco/testReport/html ${GH_ARTIFACTS_DIR}
fi

# generate directory tree in html
tree -H . -P 'lint*html|checkstyle*html|findbugs*html|index.html' -o ${GH_ARTIFACTS_DIR}/index.html

git add .
# [skip ci] added as travis build for gh-pages should not run
git commit -m "$TRAVIS_BUILD_NUMBER - artifacts deployed to github pages [skip ci]"
git push --force --quiet ${FULL_REPO} master:gh-pages
