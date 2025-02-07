#!/bin/bash

# 使用传入的第一个参数作为版本标签
VERSION_TAG=$1

if [ -z "$VERSION_TAG" ]; then
    echo "Error: No version tag provided."
    exit 1
fi

# Maven 构建并跳过测试
mvn clean package -Dmaven.test.skip=true

# 构建 Docker 镜像并设置标签
mvn dockerfile:build -Ddocker.image.tag=$VERSION_TAG

# 推送 Docker 镜像
docker push aolifu/online-server:$VERSION_TAG
