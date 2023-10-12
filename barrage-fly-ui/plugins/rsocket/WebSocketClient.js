/*
 * Copyright 2023 OrdinaryRoad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const events = require('events')
const {
  APPLICATION_JSON,
  BufferEncoders,
  decodeCompositeMetadata,
  encodeCompositeMetadata,
  encodeRoute,
  MESSAGE_RSOCKET_COMPOSITE_METADATA,
  MESSAGE_RSOCKET_ROUTING,
  RSocketClient
} = require('rsocket-core')
const RSocketWebSocketClient = require('rsocket-websocket-client').default
const { Flowable } = require('rsocket-flowable')

const CMD = {
  SUBSCRIBE: 'SUBSCRIBE',
  UNSUBSCRIBE: 'UNSUBSCRIBE'
}

const EVENT_NAME = {
  CONNECTED: 'CONNECTED',
  CLOSED: 'CLOSED',
  ERROR: 'ERROR',
  MSG: 'MSG',
  MSG_SYSTEM: 'MSG_SYSTEM',
  STATUS: 'STATUS'
}

class WebSocketClient {
  constructor ({
    url,
    setupPayloadMetadataRoute = '',
    setupPayloadData = {},
    wsCreator = null
  }) {
    this.rsocketClient = new RSocketClient({
      setup: {
        keepAlive: 30 * 1000,
        lifetime: 3600 * 24 * 1000,
        dataMimeType: APPLICATION_JSON.string,
        metadataMimeType: MESSAGE_RSOCKET_COMPOSITE_METADATA.string,
        payload: this.payload(setupPayloadData, setupPayloadMetadataRoute)
      },
      transport: new RSocketWebSocketClient(
        {
          debug: true,
          url,
          wsCreator
        },
        BufferEncoders
      )
    })
    this.eventEmitter = new events.EventEmitter()
  }

  connect () {
    return new Promise((resolve, reject) => {
      this.rsocketClient.connect()
        .then((socket) => {
          this.socket = socket
          // 订阅连接状态
          this.socket.connectionStatus()
            .subscribe({
              onSubscribe: (sub) => {
                sub.request(0x7FFFFFFF)
              },
              onNext: (msg) => {
                // CONNECTED CLOSED ERROR
                console.log('onNext', msg)
                const kind = msg.kind
                this._emitMsg(EVENT_NAME.STATUS, kind)
                switch (kind) {
                  case 'CLOSED':
                    this._emitMsg(EVENT_NAME.CLOSED)
                    break
                  case 'ERROR':
                    this._emitMsg(EVENT_NAME.ERROR, msg)
                    break
                  default:
                    // ignore
                }
              },
              onComplete: () => {
                console.log('onComplete')
              },
              onError: (e) => {
                console.error('onError', e, e.source)
                this._emitMsg(EVENT_NAME.ERROR, e)
              }
            })
          this._emitMsg(EVENT_NAME.CONNECTED)
          resolve(this)
        }, (e) => {
          console.error('onError', e, e.source)
          reject(e)
          this._emitMsg(EVENT_NAME.ERROR, e)
        })
    })
  }

  _requestChannel (payload) {
    // channel是多对0/1/多
    // 使用Flowable不断向Channel请求消息
    this.socket.requestChannel(
      new Flowable((subscriber) => {
        let interval
        // 只发送一条消息
        const max = 1
        let sent = 0
        subscriber.onSubscribe({
          request: (n) => {
            // console.log('request', n)
            // console.log('sent', sent)
            // 只会触发两次，一次1，一次最大值
            if (n === 1) {
              interval = setInterval(() => {
                subscriber.onNext(sent)
                sent++
                if (sent >= max) {
                  clearInterval(interval)
                }
              }, 1000)
            }
          },
          cancel: () => {
            clearInterval(interval)
          }
        })
      })
        .map((n) => {
          // 2 发送订阅Task的请求
          return payload
        })
    )
    // 3 订阅channel
      .subscribe({
        // channel连接建立
        onSubscribe: (sub) => {
          this.subscription = sub
          // 发送订阅请求，发送的消息个数为最大值（实测只要大于0，request触发一次1，和2147483647）
          this.subscription.request(0x7FFFFFFF)
        },
        onNext: (payload) => {
          const msg = this._decodePayload(payload)
          if (msg.data.status) {
            if (msg.data.status === 200) {
              console.log('channel ok')
            } else {
              console.error('channel failed', msg)
            }
            this._emitMsg(EVENT_NAME.MSG_SYSTEM, msg)
          } else {
            this._emitMsg(EVENT_NAME.MSG, msg)
          }
        },
        // channel被server关闭
        onComplete: () => {
          console.log('Received end of server stream.')
        },
        onError: (e) => {
          this._emitMsg(EVENT_NAME.ERROR, e)
          console.error('An error occurred', e, e.source)
        }
      })
    return this
  }

  requestChannel (data, { route, cmd } = { route: '', cmd: null }) {
    return this._requestChannel(cmd ? this.cmdPayload(data, cmd, route) : this.payload(data, route))
  }

  payload (data, route = '') {
    return this._encodePayload({
      data,
      metadata: [
        [MESSAGE_RSOCKET_ROUTING, encodeRoute(route ?? '')]
      ]
    })
  }

  cmdPayload (data, cmd = null, route = '') {
    return this._encodePayload({
      data: { ...data, cmd },
      metadata: [
        ['cmd', Buffer.from(cmd ?? '')],
        [MESSAGE_RSOCKET_ROUTING, encodeRoute(route ?? '')]
      ]
    })
  }

  on (eventName, listener) {
    this.eventEmitter.on(eventName, listener)
    return this
  }

  onStatus (listener) {
    this.eventEmitter.on(EVENT_NAME.STATUS, listener)
  }

  onConnected (listener) {
    this.eventEmitter.on(EVENT_NAME.CONNECTED, listener)
    return this
  }

  onClosed (listener) {
    this.eventEmitter.on(EVENT_NAME.CLOSED, listener)
    return this
  }

  onError (listener) {
    this.eventEmitter.on(EVENT_NAME.ERROR, listener)
    return this
  }

  onMsg (listener) {
    this.eventEmitter.on(EVENT_NAME.MSG, listener)
    return this
  }

  onSystemMsg (listener) {
    this.eventEmitter.on(EVENT_NAME.MSG_SYSTEM, listener)
    return this
  }

  destroy () {
    this.subscription && this.subscription.cancel()
    this.socket && this.socket.close()
  }

  _encodePayload ({ data, metadata }) {
    return {
      data: Buffer.from(JSON.stringify(data ?? {})),
      metadata: encodeCompositeMetadata(metadata)
    }
  }

  _decodePayload ({ data, metadata }) {
    return {
      data: JSON.parse(data.toString()),
      metadata: decodeCompositeMetadata(metadata)
    }
  }

  _emitMsg (eventName, ...args) {
    this.eventEmitter.emit(eventName, args)
  }
}

module.exports = {
  CMD,
  EVENT_NAME,
  WebSocketClient
}
