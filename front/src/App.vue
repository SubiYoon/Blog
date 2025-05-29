<template>
    <q-layout view="hHh lpR fFf">
        <!-- 최상단 레이아웃 (헤더 포함) -->
        <MainHeader @toggle-left-drawer="toggleLeftDrawer" />

        <!-- 좌측 사이드 네비게이션 -->
        <LeftSidebar v-model="leftDrawerOpen" />

        <!-- 우측 레이아웃 (콘텐츠 및 우측 메뉴) -->
        <q-page-container>
            <q-page class="q-pa-md row">
                <!-- 중앙 콘텐츠 영역 -->
                <div class="col-9">
                    <div class="text-h5 q-mb-md">시작하기</div>
                    <div class="text-subtitle1 q-mb-lg">이미지를 동일한 크기로 자르고, 용지 기준으로 나누고, 저장까지 똑딱!</div>

                    <div class="text-h6 q-mb-sm">메인 화면</div>
                    <q-img src="/images/main-screen.png" :ratio="16 / 9" alt="메인 화면 이미지" class="q-mb-md" />
                    <div class="q-mb-lg">
                        처음 접속하면 아래와 같은 화면이 보입니다. 기능은 두 가지로 나뉘며, 각각 "이미지 자르기", "이미지 분할" 작업을 할 수 있습니다.
                    </div>

                    <div class="text-h6 q-mb-sm">이미지 자르기</div>
                    <q-img src="/images/image-crop.png" :ratio="16 / 9" alt="이미지 자르기 화면" />
                </div>

                <!-- 우측 레이아웃 메뉴 -->
                <RightSidebar class="col-3" />
            </q-page>
        </q-page-container>
    </q-layout>
</template>

<script setup>
import { ref } from 'vue'
import MainHeader from '@/layout/MainHeader.vue'
import LeftSidebar from '@/layout/LeftSidebar.vue'
import RightSidebar from '@/layout/RightSidebar.vue'

const leftDrawerOpen = ref(true)

function toggleLeftDrawer() {
    leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>

<style scoped>
.q-toolbar-title {
    font-size: 20px;
}
</style>
