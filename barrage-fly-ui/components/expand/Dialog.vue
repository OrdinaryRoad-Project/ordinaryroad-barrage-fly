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
  <v-dialog
    ref="dialog"
    v-model="dialog"
    :width="params.width||400"
    content-class="rounded-0"
    :dark="params.dark"
    :light="params.light"
    :persistent="loading||(params.persistent==null?params.loading:params.persistent)"
    :internal-activator="params.internalActivator"
    :overlay-color="params.overlayColor"
    :overlay-opacity="params.overlayOpacity"
    :close-delay="params.closeDelay"
    :transition="params.transition"
    @input="destroyDom"
  >
    <v-card :tile="params.tile">
      <v-card-title>
        <v-icon class="me-3">
          {{ params.icon || 'mdi-information' }}
        </v-icon>
        {{ params.title || i18n.$t('attention') }}
      </v-card-title>
      <v-card-text v-if="params.content" class="text-subtitle-1" style="white-space: pre-line">
        {{ params.content }}
      </v-card-text>
      <v-card-actions v-show="!params.hideActions" class="pa-2">
        <v-spacer />
        <v-btn
          v-show="!params.hideCancel"
          :disabled="loading"
          :color="params.cancelColor||'grey'"
          text
          @click="cancel"
        >
          {{ params.cancelText || i18n.$t('cancel') }}
        </v-btn>
        <v-btn
          v-show="!params.hideConfirm"
          :color="params.confirmColor||'primary'"
          :loading="loading"
          text
          @click="confirm"
        >
          {{ params.confirmText || i18n.$t('confirm') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data () {
    return {
      dialog: false,
      params: {},
      isConfirm: null,
      loading: false
    }
  },
  methods: {
    execute (arg, framework) {
      this.dialog = true
      this.params.dark = framework.theme.isDark
      if (typeof arg === 'string') {
        this.params.title = arg
      } else {
        Object.assign(this.params, arg)
      }
    },
    confirm () {
      if (this.params.loading) {
        this.isConfirm = true
        this.loading = true
      } else {
        this.dialog = false
        setTimeout(() => {
          this.isConfirm = true
          this.$destroy()
        }, 300)
      }
    },
    cancel () {
      this.dialog = false
      setTimeout(() => {
        this.isConfirm = false
        this.$destroy()
      }, 300)
    },
    destroyDom (v) {
      if (!v) {
        this.dialog = false
        setTimeout(() => {
          this.$destroy()
        }, 300)
      }
    }
  }
}
</script>
