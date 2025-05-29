import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { Dialog, Quasar } from 'quasar'
import { NotifyPlugin } from '@/components/notify/js/notify.js'

import App from './App.vue'
import router from './router'

import '@quasar/extras/material-icons/material-icons.css'
import 'quasar/src/css/index.sass'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(NotifyPlugin)
app.use(router)
app.use(Quasar, {
    plugins: {
        Dialog,
    },
})

app.mount('#app')
