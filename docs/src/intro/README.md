# 1 项目简介

## 1.1 功能

1. 监听不同平台不同直播间的弹幕，支持B站、斗鱼、虎牙、抖音、快手
2. 使用统一的协议将弹幕转发出去，DANMU、GIFT、SUPER_CHAT、ENTER_ROOM、LIKE、LIVE_STATUS_CHANGE
3. 支持弹幕流的过滤与转换，前置处理、消息过滤、后置处理、弹幕发送
4. 内置简易实时弹幕显示、实时任务状态统计

| <img src="/assets/image/task.png" width="400"/>              | <img src="/assets/image/task-detail-1.png" width="400"/> | <img src="/assets/image/task-detail-2.png" width="400"/> |
|--------------------------------------------------------------|----------------------------------------------------------|----------------------------------------------------------|
| <img src="/assets/image/real-time-barrage.png" width="400"/> | <img src="/assets/image/stats-1.png" width="400"/>       | <img src="/assets/image/stats-2.png" width="400"/>       |

## 1.2 作用

屏蔽不同平台直播平台弹幕协议的差异，便于开发其他与弹幕相关的应用：实时弹幕大屏、弹幕互动、弹幕存档、弹幕分析、弹幕词云图等

## 1.3 相关技术栈

- [Netty](https://netty.io)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [RSocket](https://rsocket.io)
- [QLExpress](https://github.com/alibaba/QLExpress)