import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { server } from '@/api/index.js'
import router from '@/router/index.js'
import { $alert } from '@/components/notify/js/notify.js'

const USER_DEFAULT_VALUE = {
    id: '',
    name: '',
    auth: '',
    role: '',
    expireTime: 0,
}

export const useAuthStore = defineStore(
    'auth',
    () => {
        const user = ref({
            ...USER_DEFAULT_VALUE,
        })

        async function setUser(token) {
            let userInfo = token.data.userInfo

            user.value.name = userInfo.name
            user.value.alias = userInfo.alias
            user.value.auth = userInfo.authorities
            user.value.isSignedin = true
            user.value.expireTime = 6 * 60 * 60
        }

        async function expireTimeRefresh() {}

        async function logout() {
            server
                .post('/api/logout', {})
                .then(data => {
                    if (data.data.result === 'success') {
                        $alert('로그아웃 되었습니다.').then(() => {
                            router.push('/')
                        })

                        deleteUser()
                    }
                })
                .catch(data => {
                    $alert(data.response.data.message)
                })
        }

        /**
         * 유저 정보 삭제
         */
        async function deleteUser() {
            user.value = { ...USER_DEFAULT_VALUE }
        }

        return { user, setUser, deleteUser, logout, expireTimeRefresh }
    },
    {
        persist: true,
    },
)
