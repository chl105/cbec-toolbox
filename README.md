# 外贸工具箱

[![Build Status](https://travis-ci.org/lzk90s/foreign-trade-toolbox.svg?branch=master)](https://travis-ci.org/lzk90s/foreign-trade-toolbox)


## 功能特性

- [x] 成本预估
- [x] 自动选品(vova)


## 编译方法

1. 安装好 docker
2. 执行编译命令:

```shell script
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/work  -it  maven:3-openjdk-11 bash

cd /work && mvn package
```

## 运行方法

```shell script
docker-compose up -d
```
