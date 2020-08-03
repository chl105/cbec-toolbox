# foreign-trade-toolbox

外贸工具箱，vova 平台自动计算发货成本，目前只支持燕文物流。

## 编译方法

1. 安装好 docker
2. 执行编译命令:

```shell script
docker build -t builder -v /var/run/docker.sock:/var/run/docker.sock
```

## 运行方法

```shell script
docker-compose up -d
```

## 模块说明

### auth

### logistics

### trade
