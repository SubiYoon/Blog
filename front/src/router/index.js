import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { $alert } from '@/components/notify/js/notify.js'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // {
        //     path: '/login',
        //     name: 'login',
        //     component: () => import('@/views/Login.vue'),
        // },
    ],
})

router.beforeEach(async (to, from) => {
    const userStore = useAuthStore().user
})
router.afterEach((to, from) => {})

export default router
