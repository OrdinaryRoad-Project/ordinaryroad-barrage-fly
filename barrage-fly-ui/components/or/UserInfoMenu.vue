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
  <div>
    <div v-if="userInfo">
      <v-menu offset-y open-on-hover>
        <template #activator="{ on, attrs }">
          <v-btn
            color="transparent"
            large
            class="pa-2 text-none"
            depressed
            v-bind="attrs"
            v-on="on"
          >
            <or-avatar
              :size="$vuetify.breakpoint.smAndDown?32:38"
              avatar-class="v-list-item__avatar"
              :username="username"
            />
            <v-list-item-title v-if="!$vuetify.breakpoint.smAndDown">
              {{ username }}
            </v-list-item-title>
            <v-spacer />
            <v-icon right>
              mdi-chevron-down
            </v-icon>
          </v-btn>
        </template>
        <or-base-tree-list
          :nav="false"
          :items="accessibleUserMenuItemsModel"
          @clickListItem="onClickListItem"
        />
      </v-menu>
      <v-btn
        v-if="!$vuetify.breakpoint.smAndDown"
        color="primary"
        depressed
        @click="$emit('clickCreate')"
      >
        <v-icon left>
          mdi-plus
        </v-icon>
        {{ $t('barrageFlyTask.actions.create') }}
      </v-btn>
    </div>
    <div v-else>
      <v-btn
        text
        :to="`/user/login?redirect=${$route.path}`"
        color="primary"
      >
        {{ $t('barrageFlyUser.actions.login') }}
        <v-icon>mdi-login</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'OrUserInfoMenu',
  props: {
  },
  computed: {
    ...mapGetters('user', {
      userInfo: 'getUserInfo',
      username: 'getUsername'
    }),

    accessibleUserMenuItemsModel: {
      get () {
        const items = [
          {
            titleKey: 'logout',
            icon: 'mdi-logout'
          }
        ]
        if (this.$vuetify.breakpoint.smAndDown) {
          items.unshift({
            titleKey: 'barrageFlyTask.actions.create',
            icon: 'mdi-plus'
          })
        }
        return items
      }
    }
  },
  methods: {
    onClickListItem ({ titleKey }) {
      if (titleKey === 'barrageFlyTask.actions.create') {
        this.$emit('clickCreate')
      } else {
        this.$dialog({
          persistent: false,
          content: this.$t('barrageFlyUser.actions.logoutHint'),
          loading: true
        }).then((dialog) => {
          if (dialog.isConfirm) {
            this.$store.dispatch('user/logout', {
              $apis: this.$apis,
              $router: this.$router,
              $route: this.$route
            }).then(() => dialog.cancel())
          }
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
