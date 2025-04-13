### Commands

From layers

``` docker build -t manumura/demo-gateway:latest . ```

``` docker run -p 8080:8080 -d --name demo-gateway manumura/demo-gateway ```

``` docker container logs demo-gateway -f ```

``` docker rm -f demo-gateway ```

# Login to docker hub account

``` docker login ```

``` echo $DOCKER_HUB_PRINCIPAL_PASSWORD | docker login --username $DOCKER_HUB_PRINCIPAL --password-stdin docker.io ```

``` echo ${{ secrets.ACR_SERVICE_PRINCIPAL_PASSWORD }} | docker login --username ${{ secrets.ACR_SERVICE_PRINCIPAL }} --password-stdin registry.azurecr.io ```

# tag image

``` docker tag demo-gateway manumura/demo-gateway:latest ```

# push image

``` docker push manumura/demo-gateway:latest ```

``` docker run -p 8080:8080 -d --name demo-gateway manumura/demo-gateway ```

# NOTES

Users are hardcoded in UserService.java

Tokens are hardcoded in TokenDecoderServiceImpl.java
