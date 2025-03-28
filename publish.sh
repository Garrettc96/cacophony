version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
mvn build-helper:parse-version versions:set \
    -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} \
    versions:commit; 
echo "Parsed version ${version}";
export dockerTag="public.ecr.aws/j6e6l3c7/cacophony:${version}";
echo "Publishing new docker tag ${dockerTag}";
docker build . -t ${dockerTag};
docker push ${dockerTag};

ssh -L localhost:3306:127.0.0.1:3306 root@159.69.25.95 -fN -i ~/.ssh/cacophony_rsa
