<template>
    <q-drawer show-if-above :model-value="modelValue" side="left" bordered @update:model-value="$emit('update:modelValue', $event)">
        <q-list>
            <q-item-label header>프로젝트</q-item-label>

            <q-item clickable v-ripple to="/image-tool/start">
                <q-item-section>이미지 똑딱기</q-item-section>
            </q-item>

            <q-item clickable v-ripple to="/merge-table">
                <q-item-section>React Merge Table</q-item-section>
            </q-item>

            <q-item clickable v-ripple to="/streamo">
                <q-item-section>Streamo</q-item-section>
            </q-item>
        </q-list>
    </q-drawer>
</template>

<script setup>
const props = defineProps({
    modelValue: Boolean,
})
const emit = defineEmits(['update:modelValue'])
</script>
