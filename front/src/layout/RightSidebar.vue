<template>
    <q-drawer show-if-above side="right" bordered :width="240" class="q-pa-sm bg-white">
        <q-list bordered class="rounded-borders">
            <q-item-label header>빠른 이동</q-item-label>
            <q-item clickable v-ripple>
                <q-item-section>메인 화면</q-item-section>
            </q-item>
            <q-item clickable v-ripple>
                <q-item-section>이미지 자르기</q-item-section>
            </q-item>
            <q-item clickable v-ripple>
                <q-item-section>이미지 분할</q-item-section>
            </q-item>
            <q-item clickable v-ripple>
                <q-item-section>사용 목적</q-item-section>
            </q-item>
            <q-item clickable v-ripple>
                <q-item-section>기술 스택</q-item-section>
            </q-item>
            <q-item clickable v-ripple>
                <q-item-section>데모 사용서</q-item-section>
            </q-item>
        </q-list>
    </q-drawer>
</template>

<script setup>
// 현재는 정적 목록이나, 필요 시 defineProps로 동적 메뉴 구성 가능
</script>

<style scoped>
.rounded-borders {
    border-radius: 12px;
    background-color: white;
    padding: 12px;
}
</style>
