<template>
  <div class="scene-list">
    <div class="page-header">
      <h2>场景管理</h2>
      <el-button type="primary" size="small">+ 新建场景</el-button>
    </div>
    <div class="scene-grid">
      <div v-for="scene in scenes" :key="scene.id" class="scene-card glass-card">
        <div class="scene-icon">{{ scene.icon }}</div>
        <div class="scene-info">
          <div class="scene-name">{{ scene.name }}</div>
          <div class="scene-desc">{{ scene.desc }}</div>
          <div class="scene-meta">
            <span class="tag">{{ scene.vehicles }} 辆车</span>
            <span class="tag">{{ scene.status }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const scenes = ref([
  { id: 1, icon: '🏭', name: '工业园区配送', desc: '园区内快递包裹自动配送', vehicles: 4, status: '运行中' },
  { id: 2, icon: '🏥', name: '医院物资运输', desc: '医疗用品冷链运输', vehicles: 2, status: '运行中' },
  { id: 3, icon: '🏫', name: '校园配送', desc: '校园快递和外卖配送', vehicles: 3, status: '暂停' },
  { id: 4, icon: '🏘️', name: '社区配送', desc: '社区生活用品配送', vehicles: 3, status: '运行中' }
])
</script>

<style scoped>
.scene-list { max-width: 1200px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.scene-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 16px; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 20px; }
.scene-card { display: flex; gap: 16px; transition: var(--transition); cursor: pointer; }
.scene-card:hover { background: var(--glass-heavy); transform: translateY(-2px); }
.scene-icon { font-size: 36px; flex-shrink: 0; }
.scene-info { flex: 1; }
.scene-name { font-size: 16px; font-weight: 600; margin-bottom: 4px; }
.scene-desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 8px; }
.scene-meta { display: flex; gap: 8px; }
.tag { padding: 2px 8px; border-radius: 6px; font-size: 11px; font-weight: 600; background: rgba(74,158,255,0.1); color: var(--blue); }
</style>
