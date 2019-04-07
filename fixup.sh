#!/usr/bin/env bash

if [ "$CIRCLE_PR_NUMBER" = false ] ; then
    echo 'Skipped build. This is not a pull request'
    exit 0
fi

if [ -z "$CIRCLE_PROJECT_USERNAME" ]
then
  echo "There is not CIRCLE_PROJECT_USERNAME defined"
  exit 1
fi

if [ -z "$CIRCLE_SHA1" ]
then
  echo "There is not CIRCLE_SHA1 defined"
  exit 1
fi

if [ -z "$OCTOPATCH_API_TOKEN" ] ; then
    echo "There is not OCTOPATCH_API_TOKEN defined"
    exit 1
fi

REQUEST="curl -X POST -H \"Content-Type: multipart/form-data\""
FILES=$(find . -type f -name "*.patch")
if [ -z "$FILES" ]
then
    echo "Perfect! There are not patch files"
    exit 0
fi

for FILE in $FILES
do
  REQUEST+=" -F \"data=@$FILE\""
done

REQUEST+=" -H \"Authorization: $OCTOPATCH_API_TOKEN\" api.octopatch.io/api/pulls/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME/$CIRCLE_PR_NUMBER/$CIRCLE_SHA1"
eval $REQUEST