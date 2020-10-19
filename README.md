# 跨境电商外贸工具箱

[![Build Status](https://travis-ci.org/lzk90s/foreign-trade-toolbox.svg?branch=master)](https://travis-ci.org/lzk90s/foreign-trade-toolbox)


## 功能特性

- [x] 成本预估，预估产品上架的价格
- [x] 新订单自动推送通知，官方的推送不及时，避免采购产品耽搁时间（vova）
- [x] 自动选品，找到最优惠的货源(vova)

## 架构

![architecture](architecture/container-diagram.svg)

## 手动编译方法

1. 安装好 docker, docker-compose
2. 执行编译命令:

```shell script
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/work -v /tmp/cache:/root/.m2  -it  maven:3-openjdk-11 bash /work/build.sh
```

## 运行方法

```shell script
docker-compose up -d
```
