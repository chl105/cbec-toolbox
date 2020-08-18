# foreign-trade-toolbox

外贸工具箱，vova 平台自动计算发货成本，目前只支持燕文物流。

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

## 模块说明

### auth

### logistics

### trade
