# 主从模式

## docker-compose.yml

```
version: '2'
services:
  master:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/master:/data/db
    command: mongod --dbpath /data/db  --master
  slaver:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/slaver:/data/db
    command: mongod --dbpath /data/db --slave --source master:27017
    links:
    - master
```

## 启动容器

`docker-compose up -d`

> `docker-compose`会默认将文件夹名字作为容器名的前缀, 我的文件夹为`script`, 所以容器名分别为`script_master_1`和`script_slaver_1`.

## 验证

在master中插入一条记录


```
docker-compose exec master
use test
db.test.insert({msg: "from master", ts: new Date()})
```

查看slaver中的数据


```
docker-compose exec slaver mongo
rs.slaveOk()
use test
db.test.find()
db.test.insert({msg: 'from slaver', ts: new Date()}) //报错, slaver只有写权限
```

查看slaver信息


`db.printReplicationInfo()`

测试故障转移, 先关闭master, `docker-compose stop master`, 其次连接slaver, 查看其信息, 插入数据测试


```
docker-compose exec slaver mongo
db.printReplicationInfo() //依然是slave, 没有自动切换为master
use test
db.testData.insert({msg: "from slave", ts: new Date()}) //插入失败

```

**简单的master-slave模型仅仅做了一个数据复制, 而且并不可靠, master挂了将整体无法写入操作**

<br/><br/>

# 副本集(Relica set)

> 1主 + 2次

## docker-compose.yml

```
version: '2'
services:
  rs1:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/rs1:/data/db
    command: mongod --dbpath /data/db --replSet myset
  rs2:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/rs2:/data/db
    command: mongod --dbpath /data/db --replSet myset
  rs3:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/rs3:/data/db
    command: mongod --dbpath /data/db --replSet myset
```

## 验证

**初始化副本集**

```
docker-compose exec rs1 mongo
rs.initiate()
rs.add('rs2:27017')
rs.add('rs3:27017')
rs.conf()
rs.status()
```

**数据复制功能**

```
docker-compose exec rs1 mongo
use test
db.test.insert({msg: 'from primary', ts: new Date()})
quit()

docker-compose exec rs2 mongo
rs.slaveOk()
use test
db.test.find()
quit()

docker-compose exec rs3 mongo
rs.slaveOk()
use test
db.test.find()
quit()
```

**故障转移功能**

> 副本集在`primary`挂掉以后, 可以在`secondary`中选取出新的`primary`

`docker-compose stop rs1`

> 登录rs2/rs3查看可知, 选出了新的`primary`, 这时再启动`rs1`, 它称为了`secondary`

<br/>

> 总结: 通过客户端的设置, 可以进行主副节点读写分离.

```
a). primary:默认参数, 只从主节点上进行读取操作;
b). primaryPreferred:大部分从主节点上读取数据, 只有主节点不可用时从secondary节点读取数据;
c). secondary:只从secondary节点上进行读取操作, 存在的问题是secondary节点的数据会比primary节点数据"旧";
d). secondaryPreferred:优先从secondary节点进行读取操作, secondary节点不可用时从主节点读取数据;
e). nearest:不管是主节点,secondary节点, 从网络延迟最低的节点上读取数据.
```

<br/><br/>

> 1主 + 1次 + 1仲裁

## docker-compose.yml

```
version: '2'
services:
  primary:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/rs1:/data/db
    command: mongod --dbpath /data/db --replSet myset --oplogSize 128
  secondary:
    image: mongo:3.4.4
    volumes:
    - /home/opt/mongo/rs2:/data/db
    command: mongod --dbpath /data/db --replSet myset --oplogSize 128
  arbiter:
    image: mongo:3.4.4
    command: mongod --dbpath /data/db --replSet myset --smallfiles --oplogSize 128
```

**启动**

`docker-compose up -d`

**初始化副本集**

```
docker-compose exec primary mongo
rs.initiate()
rs.add('secondary:27017')
rs.add('arbiter:27017')
rs.conf()
rs.status()
```

**数据复制和故障转移同上**

> 每个副本集对主节点都是全量拷贝, 数据压力增大时, 节点压力随之变大, 无法自动扩张.

<br/><br/>

# 高可用mongo集群

**shard**: 每个`shard`存储整个`sharding`集群数据的一个子集, 每个`shard`都是一个`replset`, 通常生产环境下一个`replset`是2个副本加1个仲裁

> mongodb提供两种分片策略, `hash sharding`和`range sharding`, 根据你的业务特征和数据特征选择.

**mongos**: 查询路由, 客户端通过其从`shard`中查询数据, 可理解为`proxy`

**config server**: 配置服务器, 存储整个`sharding`集群的元数据和配置

下面我们将测试 3 mongos + 3 config server + 3 shard (primary+secondary+arbiter) 的配置

**docker-compose.yml**

```
version: '2'
services:
  csrs1:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/cs/rs1:/data/db
    command: mongod --noauth --configsvr --replSet csrs --dbpath /data/db --port 27019
  csrs2:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/cs/rs2:/data/db
    command: mongod --noauth --configsvr --replSet csrs --dbpath /data/db --port 27019
  csrs3:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/cs/rs3:/data/db
    command: mongod --noauth --configsvr --replSet csrs --dbpath /data/db --port 27019
  mongos1:
    image: mongo:latest
    command: mongos --noauth --configdb csrs/csrs1:27019,csrs2:27019,csrs3:27019 --port 27017
  mongos2:
    image: mongo:latest
    command: mongos --noauth --configdb csrs/csrs1:27019,csrs2:27019,csrs3:27019 --port 27017
  mongos3:
    image: mongo:latest
    command: mongos --noauth --configdb csrs/csrs1:27019,csrs2:27019,csrs3:27019 --port 27017
  shard1_primary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh1/primary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard1
  shard1_secondary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh1/secondary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard1
  shard1_arbiter:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh1/arbiter:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard1
  shard2_primary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh2/primary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard2
  shard2_secondary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh2/secondary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard2
  shard2_arbiter:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh2/arbiter:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard2
  shard3_primary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh3/primary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard3
  shard3_secondary:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh3/secondary:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard3
  shard3_arbiter:
    image: mongo:latest
    volumes:
    - /home/opt/mongo/sh3/arbiter:/data/db
    command: mongod --noauth --dbpath /data/db --shardsvr --replSet shard3
```

**启动容器**

`docker-compose up -d`

**配置config server**

```
docker-compose exec csrs1 mongo --port 27019
rs.initiate()
rs.add('csrs2:27019')
rs.add('csrs3:27019')
rs.status()
quit()
```

**配置shard server**

```
docker-compose exec shard1_primary mongo --port 27018
rs.initiate()
var cfg = rs.conf()
cfg.members[0].host = 'shard1_primary:27018'
rs.reconfig(cfg)
rs.add('shard1_secondary:27018')
rs.add('shard1_arbiter:27018')
quit()
```

> 同理要在shard2_primary/shard3_primary上执行上述操作.

**配置mongos**

```
docker-compose exec mongos1 mongo --port=27017
sh.addShard('shard1/shard1_primary:27018,shard1_secondary:27018,shard1_arbiter:27018') //添加分片1到集群
sh.addShard('shard2/shard2_primary:27018,shard2_secondary:27018,shard2_arbiter:27018') //添加分片2到集群
sh.addShard('shard3/shard3_primary:27018,shard3_secondary:27018,shard3_arbiter:27018') //添加分片3到集群

sh.enableSharding('test')
sh.shardCollection('test.Log', {id: 1})

use test; //测试
for(var i = 1; i <= 100000; i++){
  db.Log.save({id:i,"message":"message"+i});
}

db.Log.stats() //默认情况下分片策略为 range sharding

db.Log.drop()
sh.shardCollection('test.Log', {id: 'hashed'})  //分片策略改为hashed sharding
for(var i = 1; i <= 100000; i++){
  db.Log.save({id:i,"message":"message"+i});
}
db.Log.stats()  //查看状态
```
