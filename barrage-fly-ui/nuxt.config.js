import en from 'vuetify/lib/locale/en'
import zhHans from 'vuetify/lib/locale/zh-Hans'

export default {
  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: '%s - Barrage Fly',
    title: 'Barrage Fly',
    htmlAttrs: {
      lang: 'zh-CN'
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
      { name: 'referrer', content: 'no-referrer' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      { rel: 'stylesheet', type: 'text/css', href: 'https://npm.elemecdn.com/@mdi/font@latest/css/materialdesignicons.min.css' },
      { rel: 'stylesheet', type: 'text/css', href: 'https://fastly.jsdelivr.net/npm/@fontsource/roboto@latest/400.css' }
    ]
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    '@/assets/css/ordinaryroad.css'
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    // 权限插件
    '~/plugins/access/index.js',
    // api插件
    '@/plugins/api/index',
    // axios拦截器等
    '~/plugins/axios/index.js',
    // darkmodejs识别系统主题 Client Mode
    {
      src: '~/plugins/darkmodejs/index.js',
      mode: 'client'
    },
    // dayjs
    '~/plugins/dayjs/index.js',
    // echarts
    {
      src: '~/plugins/echarts/index.js',
      mode: 'client'
    },
    // fullscreen
    {
      src: '~/plugins/fullscreen/index.js',
      mode: 'client'
    },
    // 国际化插件
    '~/plugins/i18n/index.js',
    // 自定义常量 工具类等
    '~/plugins/ordinaryroad/index.js',
    // rule插件
    '~/plugins/rules/index.js',
    // vuetify client mode
    {
      src: '~/plugins/vuetify/index.js',
      mode: 'client'
    }
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/eslint
    '@nuxtjs/eslint-module',
    // https://go.nuxtjs.dev/vuetify
    '@nuxtjs/vuetify'
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
    '@nuxtjs/dayjs'
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    progress: false,
    // debug: true,
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    // baseURL: process.env.DOMAIN,
    prefix: '/api',
    // https://axios.nuxtjs.org/options/#proxy
    proxy: true // Can be also an object with default options
  },

  proxy: {
    '/api': {
      target: process.env.BASE_URL,
      pathRewrite: {
        '^/api': ''
      }
    }
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    defaultAssets: {
      font: false,
      icons: false
    },
    font: {
      family: 'Roboto'
    },
    icons: {
      iconfont: 'mdi'
    },
    theme: {
      options: {
        // https://vuetifyjs.com/zh-Hans/features/theme/#section-81ea5b9a4e495c5e6027
        customProperties: true
      }
    },
    lang: {
      locales: {
        en,
        'zh-Hans': zhHans
      },
      current: 'zh-Hans'
    },
    customVariables: ['~/assets/variables.scss'],
    treeShake: true
  },

  dayjs: {
    locales: ['en', 'zh-cn'],
    defaultLocale: 'zh-cn'
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    // https://nuxtjs.org/docs/configuration-glossary/configuration-build/#transpile
    transpile: [
      'ordinaryroad-vuetify/src/components'
    ]
  },

  // https://www.nuxtjs.cn/guide/runtime-config#runtime-config-213
  publicRuntimeConfig: {
    APP_VERSION: require('./package.json').version,
    SUB_BASE_URL: process.env.SUB_BASE_URL,
    SPRING_BOOT_ADMIN_BASE_URL: process.env.SPRING_BOOT_ADMIN_BASE_URL,
    FOOTER_LINKS: [
      {
        href: 'https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client',
        title: 'Live-Chat-Client'
      },
      {
        href: 'https://blog.ordinaryroad.tech',
        title: 'OR BLOG'
      }
    ]
  },
  privateRuntimeConfig: {}
}
