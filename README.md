### About
This is a repo intended to practice API development, deployment, security, performance testing/optimizations. This API is mocking a very basic chat service similar to Discord. 
#### Current Features
- Creating Users
- Creating Chats
- Creating Channels
- Sending messages
- Searching messages

#### Technologies
This repo is written using Spring Boot and JPA. It connects to a postgres database. It then uses K8S for deploying and running the service.

### Swagger
Local Swagger URL: http://localhost:8080/cacophony/swagger-ui/index.html

### Testing
Tests can be run via `make test`. These tests run automatically on push to any branch via Github actions

### Publish
The docker image for this repo can be pushed by running `make publish`. On any merge into main, this will publish a new version of the image.

### Performance testing
Performance tests are run via K6. The scripts for executing them can be found in the k6 directory. To run them, execute:
`make performance`. The script expects the BASE_URL environment variable to be set for the service.
To create an SSH tunnel to the k8s cluster run the following command:

### Connectivity
To connect to the cluster, you can create an ssh tunnel.
`ssh -L 3307:${K8S_SERVICE_IP}:80 root@${SERVER_IP} -fN -i ~/.ssh/${SSH_CERT_PATH}`