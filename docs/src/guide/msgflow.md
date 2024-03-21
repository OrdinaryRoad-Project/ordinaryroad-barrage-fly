# 4 如何配置消息的过滤与转换

> QLExpress相关说明

- 支持的方法
    - 各种静态方法一般都可以用（需要先import）
        - [Hutool](https://www.hutool.cn/docs)
    - 向上下文添加变量 contextPut(key, value)
        - 添加后在前置操作、过滤、后置操作中均可直接通过key访问（注意顺序）
    - 发送弹幕 sendDanmu(taskId, danmu)
        - 别名：发送弹幕(taskId, danmu)
        - 任务必须正在运行中，弹幕内容String
    - 获取任务详情 getTask(taskId)

> 消息封装类`BarrageFlyMsgDTO`简介

| 变量名称     | 含义                     | 类型     | 备注                                                                                                                                                                                                             |
|----------|------------------------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| platform | 消息所属平台                 | String | BILIBILI/DOUYU/HUYA/DOUYIN/KUAISHOU                                                                                                                                                                            |
| roomId   | 消息所属直播间房间号             | String |                                                                                                                                                                                                                |
| type     | 框架定义的消息类型，提供了一些通用的属性   | String | [📌](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/tree/main/live-chat-client-commons/live-chat-client-commons-base/src/main/java/tech/ordinaryroad/live/chat/client/commons/base/msg) |
| msg      | 收到的原始消息，不同平台不同消息类型字段不同 | IMsg   | [ordinaryroad-live-chat-client](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client)                                                                                                         |

## 4.1 前置操作

### 4.1.1 方法参数

msg: `BarrageFlyMsgDTO`，框架对消息的简单封装

### 4.1.2 方法返回值

`Object?`，为空默认返回入参`msg`

## 4.2 过滤

### 4.2.1 方法参数

msg: `Object`，前置操作的返回值

### 4.2.2 方法返回值

`Boolean`，`false`表示丢弃此消息；`true`表示需要该消息，并继续进行后置处理

## 4.3 后置操作

### 4.3.1 方法参数

msg: `Object`，前置处理的返回值

### 4.3.2 方法返回值

`Object?`，Client最终将会收到的消息，为空默认返回入参`msg`

## 4.4 一些例子

### 4.4.1 消息过滤

#### 只接收框架定义的消息

```
return msg.type != null
```

#### 只接收弹幕和礼物消息

```
return NewList("DANMU", "GIFT").contains(msg.type);
```

### 4.4.2 消息扩展

#### 封装msg，增加随机数

```
import cn.hutool.core.util.RandomUtil;

map = new HashMap(2);
map.put("originalMsg",msg);
map.put("randomNumber",RandomUtil.randomNumbers(6));
return map;
```

#### 通过内置方法获取某个任务的信息，并扩充到msg中

```
import cn.hutool.core.bean.BeanUtil;

map = BeanUtil.beanToMap(msg, false, false);
map.put("task",getTask("taskId"));

return map;
```

#### 通过Http请求获取某个任务的信息，并扩充到msg中

```
import cn.hutool.http.HttpUtil;
import cn.hutool.core.bean.BeanUtil;

task = HttpUtil.createGet("http://localhost:8080/task?id=1701211747748265984").execute().body();
map = BeanUtil.beanToMap(msg, false, false);
map.put("task",task);
return map;
```

### 4.4.3 触发其他动作

#### 收到消息后发送弹幕

```
import cn.hutool.core.util.RandomUtil;

/**随机发送弹幕**/
if RandomUtil.randomInt(0,10) > 6
then
   发送弹幕("u2","666666"+RandomUtil.randomNumbers(1));

return msg
```