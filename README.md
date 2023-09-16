# barrage-fly 让弹幕飞

一个弹幕转发平台

[在线文档](https://ordinaryroad.tech/or_module/barrage-fly/)

## 1 项目简介

### 1.1 功能

1. 监听不同平台直播间的弹幕
2. 使用统一的协议将弹幕转发出去
3. 内置简易实时弹幕显示

| <img src=".docs/task.png" width="400"/>              | <img src=".docs/task-detail-1.png" width="400"/> | <img src=".docs/task-detail-2.png" width="400"/> |
|------------------------------------------------------|--------------------------------------------------|--------------------------------------------------|
| <img src=".docs/real-time-barrage.png" width="400"/> | <img src=".docs/stats-1.png" width="400"/>       | <img src=".docs/stats-2.png" width="400"/>       |

### 1.2 作用

屏蔽不同平台直播平台弹幕协议的差异，便于开发其他与弹幕相关的应用：实时弹幕大屏、弹幕互动、弹幕存档、弹幕分析、弹幕词云图等

### 1.3 相关技术栈

- [Netty](https://netty.io)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [RSocket](https://rsocket.io)
- [QLExpress](https://github.com/alibaba/QLExpress)

## 2 项目部署

项目前后端分离，可以使用Docker Compose进行部署，或者分别打包后部署

### 2.1 Docker Compose

TODO

## 3 如何连接

大体上分别为三步

1. 创建Client，与Server建立连接
2. Client请求Channel，并发送要订阅/取消订阅的任务ID（直播间弹幕转发任务）
3. 监听Server通过Channel回复的消息（转发过来的直播间弹幕）

> 浏览器环境用WebSocket，其他环境可以用TCP

在 `connector-examples` 目录下提供了一些不同语言的例子可供参考

- python
    - [x] websocket
- vue参考`barrage-fly-ui/components/or/barrage/fly/task/RealTimeBarrage.vue`

## 4 如何配置消息的过滤与转换

> QLExpress相关说明

- 支持的方法
    - 各种静态方法一般都可以用（需要先import）
        - [Hutool](https://www.hutool.cn/docs)
    - 向上下文添加变量 contextPut(key, value)
        - 添加后可直接通过key访问
    - 发送弹幕 sendDanmu(taskId, danmu)
        - 别名：发送弹幕(taskId, danmu)
        - 任务必须正在运行中，弹幕内容String
    - 获取任务详情 getTask(taskId)

> 消息封装类`BarrageFlyMsgDTO`简介

| 变量名称     | 含义                     | 类型     | 备注                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|----------|------------------------|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| platform | 消息所属平台                 | String | BILIBILI/DOUYU                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| roomId   | 消息所属直播间房间号             | String |                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| type     | 框架定义的消息类型，提供了一些通用的属性   | String | [DANMU](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/blob/main/live-chat-client-commons/live-chat-client-commons-base/src/main/java/tech/ordinaryroad/live/chat/client/commons/base/msg/IDanmuMsg.java)/[GIFT](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/blob/main/live-chat-client-commons/live-chat-client-commons-base/src/main/java/tech/ordinaryroad/live/chat/client/commons/base/msg/IGiftMsg.java) |
| msg      | 收到的原始消息，不同平台不同消息类型字段不同 | IMsg   | [ordinaryroad-live-chat-client](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client)                                                                                                                                                                                                                                                                                                                                                          |

### 4.1 前置操作

#### 4.1.1 方法参数

msg: `BarrageFlyMsgDTO`，框架对消息的简单封装

#### 4.1.2 方法返回值

`Object?`，为空默认返回入参`msg`

### 4.2 过滤

#### 4.2.1 方法参数

msg: `Object`，前置操作的返回值

#### 4.2.2 方法返回值

`Boolean`，`false`表示丢弃此消息；`true`表示需要该消息，并继续进行后置处理

### 4.3 后置操作

#### 4.3.1 方法参数

msg: `Object`，前置处理的返回值

#### 4.3.2 方法返回值

`Object?`，Client最终将会收到的消息，为空默认返回入参`msg`

### 4.4 一些例子

#### 4.4.1 只接收框架定义的消息

```
return msg.type != null
```

#### 4.4.2 封装msg，增加随机数

```
import cn.hutool.core.util.RandomUtil;

map = new HashMap(2);
map.put("originalMsg",msg);
map.put("randomNumber",RandomUtil.randomNumbers(6));
return map;
```

#### 4.4.3 通过Http请求获取某个任务的信息，并扩充到msg中

```
import cn.hutool.http.HttpUtil;
import cn.hutool.core.bean.BeanUtil;

task = HttpUtil.createGet("http://localhost:8080/task?id=1701211747748265984").execute().body();
map = BeanUtil.beanToMap(msg, false, false);
map.put("task",task);
return map;
```

#### 4.4.4 收到消息后发送弹幕

```
import cn.hutool.core.util.RandomUtil;

/**随机发送弹幕**/
if RandomUtil.randomInt(0,10) > 6
then
   发送弹幕("u2","666666"+RandomUtil.randomNumbers(1));

return msg
```

#### 4.4.5 获取任务详情

```
import cn.hutool.core.bean.BeanUtil;

map = BeanUtil.beanToMap(msg, false, false);
map.put("task",getTask("taskId"));

return map;
```

## 其他

- [ordinaryroad-live-chat-client](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client)

## 免责声明

免责声明：仅供学术研究使用。对于违反相关法律、造成危害的滥用行为，开发者不负任何责任。