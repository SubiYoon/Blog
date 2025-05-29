<template>
    <q-header elevated class="bg-white text-black">
        <q-toolbar>
            <q-btn flat dense round icon="menu" @click="$emit('toggle-left-drawer')" />

            <q-toolbar-title class="text-weight-bold">Pukkok's</q-toolbar-title>

            <q-btn flat label="스터디" />
            <q-btn flat label="프로젝트" />

            <q-space />

            <q-btn flat icon="code" label="GitHub" @click="goToGitHub" />
            <q-btn flat icon="brightness_6" @click="toggleDarkMode" />
        </q-toolbar>
    </q-header>
</template>

<script setup>
function goToGitHub() {
    window.open('https://github.com/SubiYoon', '_blank')
}

function toggleDarkMode() {
    document.body.classList.toggle('body--dark')
}
</script>

<style scoped>
.q-toolbar-title {
    font-size: 20px;
}
</style>
