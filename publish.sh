mvn build-helper:parse-version versions:set \
    -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} \
    versions:commit; 
version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Parsed version ${version}";
export dockerTag="public.ecr.aws/j6e6l3c7/cacophony:${version}";
echo "Publishing new docker tag ${dockerTag}";
docker build . -t ${dockerTag};
docker push ${dockerTag};
