## 相关链接

- https://pypi.org/project/rsocket/
- https://rsocket.io/guides/rsocket-py

## 1 安装依赖（Requires: Python >=3.8）

```shell
pip3 install rsocket==0.4.15
pip3 install aiohttp==3.9.3
```

## 2 运行查看效果

```
usage: websocket.py [-h] [--uri URI] -t T

optional arguments:
  -h, --help  show this help message and exit
  --uri URI   WebSocket Server Uri
  -t T        taskIds
```

```shell
# 默认ws://localhost:9898
python websocket.py -t taskId1 -t taskId2
```