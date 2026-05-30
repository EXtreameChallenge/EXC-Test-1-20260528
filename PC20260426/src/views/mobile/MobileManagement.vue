<template>
  <div class="mobile-management">
    <div class="page-header">
      <h2>移动端管理</h2>
    </div>
    <div class="mobile-cards">
      <div class="mobile-card glass-card">
        <div class="mobile-icon">📱</div>
        <h3>手机端状态</h3>
        <div class="mobile-info">
          <div class="info-row"><span>版本</span><span>v1.0.0</span></div>
          <div class="info-row"><span>在线用户</span><span style="color:var(--green)">3</span></div>
          <div class="info-row"><span>最后同步</span><span>刚刚</span></div>
          <div class="info-row"><span>连接状态</span><span style="color:var(--green)">正常</span></div>
        </div>
      </div>
      <div class="mobile-card glass-card">
        <div class="mobile-icon">🔗</div>
        <h3>API连接</h3>
        <div class="mobile-info">
          <div class="info-row"><span>后端地址</span><span>localhost:8080</span></div>
          <div class="info-row"><span>WebSocket</span><span style="color:var(--green)">已连接</span></div>
          <div class="info-row"><span>AI服务</span><span style="color:var(--green)">GLM-4-Flash</span></div>
          <div class="info-row"><span>数据同步</span><span style="color:var(--green)">实时</span></div>
        </div>
      </div>
      <div class="mobile-card glass-card">
        <div class="mobile-icon">📊</div>
        <h3>使用统计</h3>
        <div class="mobile-info">
          <div class="info-row"><span>今日登录</span><span>12次</span></div>
          <div class="info-row"><span>AI对话</span><span>47条</span></div>
          <div class="info-row"><span>调度操作</span><span>8次</span></div>
          <div class="info-row"><span>平均在线</span><span>23分钟</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.mobile-management { max-width: 1200px; }
.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 20px; font-weight: 600; }
.mobile-cards { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 16px; }
.glass-card { background: var(--glass); backdrop-filter: blur(24px); border: 1px solid var(--glass-border); border-radius: var(--border-radius); padding: 24px; }
.mobile-card { text-align: center; }
.mobile-icon { font-size: 40px; margin-bottom: 12px; }
.mobile-card h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
.mobile-info { display: flex; flex-direction: column; gap: 8px; text-align: left; }
.info-row { display: flex; justify-content: space-between; font-size: 13px; padding: 6px 0; border-bottom: 1px solid var(--glass-border); }
.info-row span:first-child { color: var(--text-muted); }
</style>
