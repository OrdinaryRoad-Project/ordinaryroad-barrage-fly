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
import { APPLICATION_JSON, JsonSerializer, RSocketClient } from 'rsocket-core'
import RSocketWebSocketClient from 'rsocket-websocket-client'

export default {
  // create ({ commit }) {
  //   return new RSocketClient({
  //     setup: {
  //       dataMimeType: 'application/json',
  //       keepAlive: 30000,
  //       lifetime: 30000,
  //       metadataMimeType: 'application/json'
  //     },
  //     transport: new RSocketWebsocketClient({
  //       url: 'ws://or-barrage-fly:9898/sub'
  //     })
  //   })
  // },
  connect ({ commit }, { url, setupPayload = { data: {}, metadata: {} } }) {
    const client = new RSocketClient({
      serializers: {
        data: JsonSerializer,
        metadata: JsonSerializer
      },
      setup: {
        keepAlive: 60000,
        lifetime: 180000,
        dataMimeType: APPLICATION_JSON.string,
        metadataMimeType: APPLICATION_JSON.string
        // payload: setupPayload
      },
      transport: new RSocketWebSocketClient(
        {
          // debug: true,
          url
        }
      )
    })

    return client.connect()
  },
  connected (context, rsocket) {
    context.commit('status', 'CONNECTED')
    context.commit('rsocket', rsocket)
  },
  connectionError (context, error) {
    console.error(error)
    context.commit('status', 'ERROR')
    context.commit('connectionError', error)
  }
}
