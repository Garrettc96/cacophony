mvn build-helper:parse-version versions:set \
    -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} \
    versions:commit; 
version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Parsed version ${version}";
export dockerTag="public.ecr.aws/j6e6l3c7/cacophony:${version}";
echo "Publishing new docker tag ${dockerTag}";
docker buildx create --platform linux/arm64,linux/amd64 --use
docker buildx build --platform linux/arm64 --load . -t ${dockerTag};
echo "${AWS_ACCESS_KEY_ID}"
aws ecr-public get-login-password --region us-east-1  | docker login --username AWS --password-stdin public.ecr.aws/j6e6l3c7
docker push ${dockerTag};