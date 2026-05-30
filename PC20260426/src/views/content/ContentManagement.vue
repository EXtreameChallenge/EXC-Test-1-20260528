<template>
  <div class="content-management">
    <div class="page-header">
      <h2>内容管理</h2>
      <el-button type="primary" size="small">+ 新建内容</el-button>
    </div>
    <div class="glass-card">
      <el-table :data="contents" style="width: 100%" :header-cell-style="{ background: 'transparent', color: 'var(--text-secondary)' }" :cell-style="{ background: 'transparent', color: 'var(--text-primary)' }">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <span :style="{ color: row.status === 'published' ? 'var(--green)' : 'var(--orange)' }">{{ row.status === 'published' ? '已发布' : '草稿' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="updatedAt" label="更新时间" />
        <el-table-column label="操作" width="120">
          <template #default>
            <el-button text size="small" type="primary">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const contents = ref([
  { title: '平台使用指南', type: '文档', status: 'published', author: '管理员', updatedAt: '2026-04-28' },
  { title: '调度操作手册', type: '文档', status: 'draft', author: '运营员', updatedAt: '2026-04-27' },
  { title: '安全规范', type: '公告', status: 'published', author: '管理员', updatedAt: '2026-04-25' }
])
</script>

<style scoped>
.content-management { max-width: 1200px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; }
</style>
