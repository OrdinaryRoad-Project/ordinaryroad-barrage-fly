<!--
  - Copyright 2023 OrdinaryRoad
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <v-footer
    :app="app"
    :absolute="absolute"
    padless
    inset
  >
    <v-container>
      <div
        class="d-flex justify-center align-center font-weight-light"
        :class="$vuetify.breakpoint.smAndDown?'flex-column':null"
      >
        <or-link href="https://blog.ordinaryroad.tech" hover-able>
          OR BLOG
        </or-link>
      </div>
      <div
        class="d-flex justify-center align-center"
        :class="$vuetify.breakpoint.smAndDown?'flex-column':null"
      >
        <div class="d-flex text--grey">
          ©2019 - {{ new Date().getFullYear() }} 苗锦洲 {{ $t('allRightsReserved') }}
        </div>
        <div
          class="d-flex align-center"
          :class="$vuetify.breakpoint.smAndDown?null:'ms-2'"
        >
          <or-link href="https://ordinaryroad.tech" hover-able hide-icon>
            OrdinaryRoad
          </or-link>

          <v-btn
            icon
            class="ms-2"
            target="_blank"
            href="https://github.com/1962247851"
          >
            <v-icon>
              mdi-github
            </v-icon>
          </v-btn>
        </div>
      </div>
    </v-container>
  </v-footer>
</template>

<script>
export default {
  name: 'OrFooter',
  props: {
    app: {
      type: Boolean,
      default: false
    },
    absolute: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

</style>
