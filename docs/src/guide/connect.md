# 3 如何连接

大体上分别为三步

1. 创建Client，与Server建立连接
2. Client请求Channel，并发送要订阅/取消订阅的任务ID（直播间弹幕转发任务）
3. 监听Server通过Channel回复的消息（转发过来的直播间弹幕）

在 `connector-examples` 目录下提供了一些不同语言的例子可供参考

- python（[rsocket-py](https://rsocket.io/guides/rsocket-py)）
    - [x] websocket
- javascript（[rsocket-js](https://rsocket.io/guides/rsocket-js)）
    - [x] websocket
    - [x] vue（参考`barrage-fly-ui/components/or/barrage/fly/task/RealTimeBarrage.vue`）