<template>
  <div class="mobile-app">
    <div class="bg-animate"></div>
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>
    <div class="orb orb-3"></div>
    <div class="orb orb-4"></div>
    <div class="noise-overlay"></div>

    <div class="toast" :class="{ show: toastVisible }">
      <div class="toast-t">{{ toastTitle }}</div>
      <div class="toast-d">{{ toastDetail }}</div>
    </div>

    <div class="login-page" :class="{ hidden: !showLogin }">
      <div class="bg-animate"></div>
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
      <div class="noise-overlay"></div>
      <div class="login-logo">🦞</div>
      <div class="login-title">NeoClaw</div>
      <div class="login-sub">AI车队智能体管理平台 · DEMO</div>
      <div class="login-form">
        <template v-if="!isRegisterMode">
          <div class="form-group">
            <label class="form-label">手机号 / 工号</label>
            <input class="form-input" :class="{ error: loginAccountErr }" v-model="loginAccount" type="text" placeholder="请输入手机号或工号" autocomplete="username">
            <div class="form-error" :class="{ show: loginAccountErr }">{{ loginAccountErr }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">密码</label>
            <input class="form-input" :class="{ error: loginPasswordErr }" v-model="loginPassword" type="password" placeholder="请输入密码" autocomplete="current-password" @keydown.enter="handleAuth">
            <div class="form-error" :class="{ show: loginPasswordErr }">{{ loginPasswordErr }}</div>
          </div>
        </template>
        <template v-else>
          <div class="form-group">
            <label class="form-label">姓名</label>
            <input class="form-input" :class="{ error: regNameErr }" v-model="regName" type="text" placeholder="请输入真实姓名">
            <div class="form-error" :class="{ show: regNameErr }">{{ regNameErr }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">手机号</label>
            <input class="form-input" :class="{ error: regPhoneErr }" v-model="regPhone" type="tel" placeholder="请输入手机号" maxlength="11">
            <div class="form-error" :class="{ show: regPhoneErr }">{{ regPhoneErr }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">密码</label>
            <input class="form-input" :class="{ error: regPasswordErr }" v-model="regPassword" type="password" placeholder="至少6位密码">
            <div class="form-error" :class="{ show: regPasswordErr }">{{ regPasswordErr }}</div>
          </div>
          <div class="form-group">
            <label class="form-label">选择身份</label>
            <div class="role-selector">
              <div v-for="role in roleOptions" :key="role.value" class="role-option" :class="{ selected: selectedRole === role.value }" @click="selectedRole = role.value">
                <div class="role-icon">{{ role.icon }}</div>
                <div class="role-name">{{ role.name }}</div>
                <div class="role-desc">{{ role.desc }}</div>
              </div>
            </div>
          </div>
        </template>
        <button class="login-btn" @click="handleAuth">{{ isRegisterMode ? '注 册' : '登 录' }}</button>
        <div class="login-switch">
          <template v-if="!isRegisterMode">还没有账号？<a @click="toggleAuthMode">立即注册</a></template>
          <template v-else>已有账号？<a @click="toggleAuthMode">返回登录</a></template>
        </div>
      </div>
    </div>

    <div class="security-modal" :class="{ show: securityModalVisible }">
      <div class="security-content">
        <div class="security-icon">🔐</div>
        <div class="security-title">安全验证</div>
        <div class="security-sub">{{ securitySub }}</div>
        <div>
          <div v-for="(check, i) in securityChecks" :key="i" class="security-check">
            <div class="security-check-icon">{{ check.passed ? '✅' : '⏳' }}</div>
            <div class="security-check-text">
              <div class="security-check-label">{{ check.label }}</div>
              <div class="security-check-detail">{{ check.detail }}</div>
            </div>
          </div>
        </div>
        <button class="security-btn" :disabled="securityBtnDisabled" @click="confirmSecurity">{{ securityBtnDisabled ? '验证中...' : '验证通过，确认执行' }}</button>
      </div>
    </div>

    <div class="app-header">
      <div class="header-row">
        <div class="brand">
          <div class="brand-icon">🦞</div>
          <div class="brand-name">轻行Claw</div>
        </div>
        <div style="display:flex;align-items:center;gap:8px">
          <span class="perm-badge">{{ currentRoleLabel }}</span>
          <div class="header-badge">在线</div>
        </div>
      </div>
      <div class="header-metrics">
        <div class="hm"><div class="hm-val blue">{{ V.length }}</div><div class="hm-lbl">车辆</div></div>
        <div class="hm-dot"></div>
        <div class="hm"><div class="hm-val green">1,847</div><div class="hm-lbl">今日单</div></div>
        <div class="hm-dot"></div>
        <div class="hm"><div class="hm-val orange">98.2%</div><div class="hm-lbl">完成率</div></div>
      </div>
    </div>

    <div class="main-area">
      <div class="page" :class="{ active: curTab === 'fleet' }">
        <div class="view-tabs">
          <button class="view-tab" :class="{ active: fleetViewMode === 'data' }" @click="switchFleetView('data')">📊 数据</button>
          <button class="view-tab" :class="{ active: fleetViewMode === 'fleet' }" @click="switchFleetView('fleet')">🚗 车队</button>
        </div>
        <div v-show="fleetViewMode === 'fleet'">
          <div class="sec-head">实时概况</div>
          <div class="fleet-summary">
            <div class="fs-card glass"><div class="fs-num" style="color:var(--green)">{{ idleCount }}</div><div class="fs-lbl">待命</div></div>
            <div class="fs-card glass"><div class="fs-num" style="color:var(--blue)">{{ busyCount }}</div><div class="fs-lbl">配送</div></div>
            <div class="fs-card glass"><div class="fs-num" style="color:var(--orange)">{{ chargeCount }}</div><div class="fs-lbl">充电</div></div>
            <div class="fs-card glass"><div class="fs-num" style="color:var(--red)">{{ faultCount }}</div><div class="fs-lbl">故障</div></div>
          </div>
          <div class="sec-head">全部车辆</div>
          <div class="fleet-list">
            <div v-for="v in V" :key="v.id" class="v-row glass" @click="tapV(v.id)">
              <div class="v-ind" :class="v.status"></div>
              <div class="v-info">
                <div class="v-name">{{ v.id }}</div>
                <div class="v-detail"><span>{{ v.type }}</span><span>{{ v.loc }}</span></div>
              </div>
              <div class="v-batt">
                <div class="v-batt-bar"><div class="v-batt-fill" :style="{ width: v.battery + '%', background: battColor(v.battery) }"></div></div>
                <span class="v-batt-pct" :style="{ color: battColor(v.battery) }">{{ v.battery }}%</span>
              </div>
              <span class="v-st-tag" :class="'tag-' + v.status">{{ statusText(v.status) }}</span>
            </div>
          </div>
        </div>
        <div v-show="fleetViewMode === 'data'">
          <div class="data-page-inner">
            <div class="sec-head">核心指标</div>
            <div class="d-card glass">
              <div class="d-card-title">今日配送</div>
              <div><span class="d-big">1,847</span><span class="d-big-sub">单</span></div>
              <div class="d-trend">↑ 较昨日 +12.3%</div>
              <div class="d-grid">
                <div class="d-item"><div class="d-val" style="color:var(--green)">98.2%</div><div class="d-lbl">完成率</div></div>
                <div class="d-item"><div class="d-val" style="color:var(--blue)">23</div><div class="d-lbl">平均时效(分)</div></div>
                <div class="d-item"><div class="d-val" style="color:var(--orange)">1,246</div><div class="d-lbl">总里程(km)</div></div>
                <div class="d-item"><div class="d-val" style="color:var(--red)">0.3%</div><div class="d-lbl">故障率</div></div>
              </div>
            </div>
            <div class="sec-head">车型分布</div>
            <div class="d-card glass">
              <div class="d-bar-row"><span class="d-bar-lbl">小型</span><div class="d-bar-track"><div class="d-bar-fill" style="width:58%;background:var(--blue)"></div></div><span class="d-bar-pct">58%</span></div>
              <div class="d-bar-row"><span class="d-bar-lbl">中型</span><div class="d-bar-track"><div class="d-bar-fill" style="width:33%;background:var(--purple)"></div></div><span class="d-bar-pct">33%</span></div>
              <div class="d-bar-row"><span class="d-bar-lbl">大型</span><div class="d-bar-track"><div class="d-bar-fill" style="width:8%;background:var(--orange)"></div></div><span class="d-bar-pct">8%</span></div>
            </div>
            <div class="sec-head">任务动态</div>
            <div class="d-card glass">
              <div class="act-list">
                <div v-for="l in logs.slice(0, 6)" :key="l.time + l.text" class="act-row">
                  <div class="act-dot" :class="l.type"></div>
                  <div class="act-txt">{{ l.text }}</div>
                  <div class="act-t">{{ l.time }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="page" :class="{ active: curTab === 'chat', 'chat-active': curTab === 'chat' }" ref="chatPageRef">
        <div class="chat-list" ref="chatListRef" @click="handleChatClick">
          <div v-for="(msg, idx) in messages" :key="idx" class="msg" :class="msg.from">
            <div class="msg-ava" :class="msg.from === 'ai' ? 'ai' : 'u'">{{ msg.from === 'ai' ? '莫' : '我' }}</div>
            <div class="msg-body">
              <div class="msg-bbl" v-html="msg.html"></div>
              <div class="msg-time" :style="msg.from === 'user' ? 'text-align:right' : ''">{{ msg.time }}</div>
            </div>
          </div>
          <div v-if="isTyping" class="msg ai">
            <div class="msg-ava ai">莫</div>
            <div class="typing"><div class="t-dot"></div><div class="t-dot"></div><div class="t-dot"></div></div>
          </div>
        </div>
      </div>

      <div class="page" :class="{ active: curTab === 'profile' }">
        <div class="profile-header">
          <div class="profile-avatar" :style="currentUser.avatar ? { background: `url('${currentUser.avatar}') center/cover` } : {}" @click="triggerAvatarUpload">
            <template v-if="!currentUser.avatar">{{ currentUser.name.charAt(0) }}</template>
            <div class="profile-avatar-edit">📷</div>
          </div>
          <input ref="avatarInputRef" type="file" accept="image/png,image/jpeg,image/gif,image/webp" style="display:none" @change="handleAvatarChange">
          <div class="profile-name">{{ currentUser.name }}</div>
          <div class="profile-role">{{ currentRoleLabel }} · {{ currentUser.level }}</div>
        </div>
        <div class="profile-section">
          <div class="profile-section-title">个人信息</div>
          <div class="profile-card">
            <div class="profile-item" @click="showEditProfile"><div class="profile-item-icon blue">👤</div><div class="profile-item-text"><div class="profile-item-label">个人资料</div><div class="profile-item-desc">修改姓名、手机号</div></div><div class="profile-item-arrow">›</div></div>
            <div class="profile-item" @click="showPermissions"><div class="profile-item-icon green">🔐</div><div class="profile-item-text"><div class="profile-item-label">权限管理</div><div class="profile-item-desc">{{ currentPermissions.length }}项权限已授权</div></div><div class="profile-item-arrow">›</div></div>
            <div class="profile-item"><div class="profile-item-icon purple">🔑</div><div class="profile-item-text"><div class="profile-item-label">修改密码</div><div class="profile-item-desc">更新登录密码</div></div><div class="profile-item-arrow">›</div></div>
          </div>
        </div>
        <div class="profile-section">
          <div class="profile-section-title">业务管理</div>
          <div class="profile-card">
            <div class="profile-item" @click="openVehicleModal"><div class="profile-item-icon orange">🚗</div><div class="profile-item-text"><div class="profile-item-label">车辆管理</div><div class="profile-item-desc">添加、编辑车辆信息</div></div><div class="profile-item-arrow">›</div></div>
            <div class="profile-item" @click="openCompanyModal"><div class="profile-item-icon cyan">🏭</div><div class="profile-item-text"><div class="profile-item-label">公司信息</div><div class="profile-item-desc">配置公司/工厂信息</div></div><div class="profile-item-arrow">›</div></div>
            <div class="profile-item" @click="showSecurityLog"><div class="profile-item-icon red">📋</div><div class="profile-item-text"><div class="profile-item-label">操作日志</div><div class="profile-item-desc">查看历史操作记录</div></div><div class="profile-item-arrow">›</div></div>
          </div>
        </div>
        <div class="profile-section">
          <div class="profile-section-title">系统</div>
          <div class="profile-card">
            <div class="profile-item" @click="showApiInfo"><div class="profile-item-icon blue">⚡</div><div class="profile-item-text"><div class="profile-item-label">API 状态</div><div class="profile-item-desc">GLM-4-Flash 连接状态</div></div><div class="profile-item-arrow">›</div></div>
            <div class="profile-item" @click="showAbout"><div class="profile-item-icon blue">ℹ️</div><div class="profile-item-text"><div class="profile-item-label">关于轻行Claw</div><div class="profile-item-desc">v1.0.0 · GLM-4-Flash</div></div><div class="profile-item-arrow">›</div></div>
          </div>
        </div>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </div>

    <div class="modal-overlay" :class="{ show: vehicleModalVisible }" @click.self="vehicleModalVisible = false">
      <div class="modal-sheet">
        <div class="modal-handle"></div>
        <div class="modal-title">🚗 车辆管理</div>
        <template v-if="vehicleModalView === 'list'">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px">
            <span style="font-size:13px;color:var(--text2)">共 {{ V.length }} 辆车</span>
            <button style="padding:6px 14px;border-radius:10px;border:none;background:rgba(74,158,255,0.15);color:var(--blue);font-size:12px;font-weight:600;cursor:pointer;font-family:inherit" @click="showAddVehicleForm">+ 添加车辆</button>
          </div>
          <div style="display:flex;flex-direction:column;gap:6px">
            <div v-for="(v, i) in V" :key="v.id" style="display:flex;align-items:center;gap:10px;padding:10px 12px;border-radius:12px;background:rgba(255,255,255,0.04);border:0.5px solid rgba(255,255,255,0.06)">
              <div :style="{width:'8px',height:'8px',borderRadius:'50%',background:v.status==='idle'?'var(--green)':v.status==='busy'?'var(--blue)':v.status==='charge'?'var(--orange)':'var(--red)',flexShrink:0}"></div>
              <div style="flex:1">
                <div style="font-size:13px;font-weight:600">{{ v.id }} <span style="font-size:11px;color:var(--text3)">{{ v.type }}</span></div>
                <div style="font-size:11px;color:var(--text3)">{{ v.loc }} · {{ statusText(v.status) }} · {{ v.battery }}%</div>
              </div>
              <button style="padding:4px 10px;border-radius:8px;border:0.5px solid rgba(255,255,255,0.1);background:rgba(255,255,255,0.06);color:var(--text2);font-size:11px;cursor:pointer;font-family:inherit" @click="openEditVehicle(i)">编辑</button>
            </div>
          </div>
        </template>
        <template v-if="vehicleModalView === 'add'">
          <div class="form-group"><label class="form-label">车辆编号</label><input class="form-input" v-model="vehicleForm.id"></div>
          <div class="form-group"><label class="form-label">车型</label><input class="form-input" v-model="vehicleForm.type"></div>
          <div class="form-group"><label class="form-label">位置</label><input class="form-input" v-model="vehicleForm.loc" placeholder="如：A区-1号点"></div>
          <div style="display:flex;gap:8px;margin-top:12px">
            <button class="login-btn" style="flex:1" @click="saveNewVehicle">添加</button>
            <button style="flex:1;padding:14px;border-radius:14px;border:0.5px solid rgba(255,255,255,0.1);background:rgba(255,255,255,0.06);color:var(--text2);font-size:15px;font-weight:600;cursor:pointer;font-family:inherit" @click="vehicleModalView='list'">取消</button>
          </div>
        </template>
        <template v-if="vehicleModalView === 'edit'">
          <div class="form-group"><label class="form-label">车辆编号</label><input class="form-input" :value="V[vehicleEditIdx]?.id" disabled></div>
          <div class="form-group"><label class="form-label">车型</label><input class="form-input" v-model="vehicleForm.type"></div>
          <div class="form-group"><label class="form-label">位置</label><input class="form-input" v-model="vehicleForm.loc"></div>
          <div class="form-group"><label class="form-label">电量(%)</label><input class="form-input" v-model="vehicleForm.battery" type="number" min="0" max="100"></div>
          <div style="display:flex;gap:8px;margin-top:12px">
            <button class="login-btn" style="flex:1" @click="saveEditVehicle">保存</button>
            <button style="flex:1;padding:14px;border-radius:14px;border:0.5px solid rgba(248,113,113,0.2);background:rgba(248,113,113,0.08);color:var(--red);font-size:15px;font-weight:600;cursor:pointer;font-family:inherit" @click="deleteVehicle">删除</button>
          </div>
        </template>
      </div>
    </div>

    <div class="modal-overlay" :class="{ show: companyModalVisible }" @click.self="companyModalVisible = false">
      <div class="modal-sheet">
        <div class="modal-handle"></div>
        <div class="modal-title">🏭 公司信息</div>
        <div class="form-group"><label class="form-label">公司名称</label><input class="form-input" v-model="companyForm.name"></div>
        <div class="form-group"><label class="form-label">公司地址</label><input class="form-input" v-model="companyForm.address"></div>
        <div class="form-group"><label class="form-label">联系电话</label><input class="form-input" v-model="companyForm.contact"></div>
        <div class="form-group"><label class="form-label">车队规模</label><input class="form-input" v-model="companyForm.fleetSize" type="number"></div>
        <button class="login-btn" style="margin-top:12px" @click="saveCompanyInfo">保存</button>
      </div>
    </div>

    <div class="modal-overlay" :class="{ show: editProfileModalVisible }" @click.self="editProfileModalVisible = false">
      <div class="modal-sheet">
        <div class="modal-handle"></div>
        <div class="modal-title">👤 个人资料</div>
        <div class="form-group"><label class="form-label">姓名</label><input class="form-input" v-model="profileForm.name"></div>
        <div class="form-group"><label class="form-label">手机号</label><input class="form-input" v-model="profileForm.phone"></div>
        <div class="form-group"><label class="form-label">身份</label><input class="form-input" :value="currentRoleLabel" disabled></div>
        <div class="form-group"><label class="form-label">权限等级</label><input class="form-input" :value="currentUser.level" disabled></div>
        <button class="login-btn" style="margin-top:12px" @click="saveProfile">保存修改</button>
      </div>
    </div>

    <div class="modal-overlay" :class="{ show: infoModalVisible }" @click.self="infoModalVisible = false">
      <div class="modal-sheet"><div class="modal-handle"></div><div class="modal-title">{{ infoModalTitle }}</div><div v-html="infoModalContent"></div></div>
    </div>

    <div class="nav-island" :class="{ expanded: curTab === 'chat' }">
      <div class="island-glass">
        <div class="island-chat">
          <div class="chips-scroll" ref="chipsScrollRef">
            <div v-for="chip in sceneChips" :key="chip.text" class="chip scene-chip" :style="{ background: 'rgba(74,158,255,0.1)', color: 'var(--blue)', borderColor: 'rgba(74,158,255,0.2)' }" @click="sendChip(chip.cmd)">{{ chip.text }}</div>
            <div class="chip" @click="sendChip('查看所有车辆状态')">📋 车队状态</div>
            <div class="chip" @click="sendChip('把电量低于30%的车调度去充电')">🔋 低电量充电</div>
            <div class="chip" @click="sendChip('查看故障车辆')">⚠️ 故障处理</div>
            <div class="chip" @click="sendChip('调度车辆去A区')">🚗 调度派车</div>
            <div class="chip" @click="sendChip('制定明日的调度计划')">📅 明日计划</div>
            <div class="chip" @click="sendChip('统计今日运营数据')">📊 运营数据</div>
            <div class="chip" @click="sendChip('打开DM-08的车门')">🚪 开车门</div>
            <div class="chip" @click="sendChip('帮助')">❓ 更多指令</div>
          </div>
          <div class="input-row">
            <textarea class="input-field" :class="{ recording: isRecording }" ref="chatInputRef" rows="1" :placeholder="isRecording ? '🎤 说话中...' : '告诉轻行Claw你要做什么...'" v-model="inputText" @input="autoResize" @keydown.enter.exact.prevent="sendMsg"></textarea>
            <button class="voice-btn" :class="{ recording: isRecording }" @click="toggleVoice">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 1a3 3 0 0 0-3 3v8a3 3 0 0 0 6 0V4a3 3 0 0 0-3-3z"></path><path d="M19 10v2a7 7 0 0 1-14 0v-2"></path><line x1="12" y1="19" x2="12" y2="23"></line><line x1="8" y1="23" x2="16" y2="23"></line></svg>
            </button>
            <button class="send-btn" @click="sendMsg">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22,2 15,22 11,13 2,9 22,2"></polygon></svg>
            </button>
          </div>
        </div>
        <div class="island-divider" v-show="curTab === 'chat'"></div>
        <div class="nav-pill">
          <button class="nav-btn" :class="{ active: curTab === 'chat' }" @click="goTab('chat')"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>智能体</button>
          <button class="nav-btn" :class="{ active: curTab === 'fleet' }" @click="goTab('fleet')"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path></svg>车队数据</button>
          <button class="nav-btn" :class="{ active: curTab === 'profile' }" @click="goTab('profile')"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>我的</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import * as api from './api.js'
import { connectWebSocket, disconnectWebSocket, on as wsOn, off as wsOff, getWsStatus } from './sync.js'

const API_URL = '/api/chat'
const MODEL = 'glm-4-flash'
let currentConversationId = null

const STATUS_MAP = { idle: 'standby', busy: 'delivering', charge: 'charging', fault: 'fault' }
const STATUS_MAP_REV = { standby: 'idle', delivering: 'busy', charging: 'charge', fault: 'fault' }
function toBackendStatus(s) { return STATUS_MAP[s] || s }
function toFrontendStatus(s) { return STATUS_MAP_REV[s] || s }

const roleConfig = {
  admin: { label: '管理员', level: 'A级', permissions: ['车队管理','任务调度','能源管理','运营分析','AI智能体','故障管理','车辆管理','系统设置'] },
  operator: { label: '操作员', level: 'B级', permissions: ['车队管理','任务调度','能源管理','运营分析','AI智能体'] },
  driver: { label: '驾驶员', level: 'C级', permissions: ['车辆操作','查看状态','AI智能体'] },
  viewer: { label: '观察者', level: 'D级', permissions: ['查看状态','运营分析'] }
}

const roleOptions = [
  { value: 'admin', icon: '👔', name: '管理员', desc: '全权限管理' },
  { value: 'operator', icon: '👷', name: '操作员', desc: '调度与查看' },
  { value: 'driver', icon: '🚗', name: '驾驶员', desc: '车辆操作' },
  { value: 'viewer', icon: '👀', name: '观察者', desc: '仅查看数据' }
]

const registeredUsers = reactive([
  { account: 'admin', password: '123456', name: '张运营', role: 'admin', phone: '13800000001' },
  { account: 'operator1', password: '123456', name: '李调度', role: 'operator', phone: '13800000002' },
  { account: 'driver1', password: '123456', name: '王师傅', role: 'driver', phone: '13800000003' }
])

const V = reactive([])

const logs = reactive([])

const companyInfo = reactive({ name: '轻行Claw智行科技有限公司', address: '北京市海淀区中关村科技园', contact: '010-88888888', fleetSize: 12 })

const sceneRecommendations = {
  morning: [
    { text: '🔋 检查低电量车辆', cmd: '查看哪些车需要充电' },
    { text: '📊 今日首单统计', cmd: '统计今日运营数据' },
    { text: '🚗 早高峰调度', cmd: '调配5辆车去A区' },
    { text: '⚠️ 晨检报告', cmd: '查看所有车辆状态' }
  ],
  noon: [
    { text: '🔋 午间充电调度', cmd: '把电量低于30%的车调度去充电' },
    { text: '📦 午高峰准备', cmd: '调配3辆车去B区' },
    { text: '📊 半日数据', cmd: '统计今日运营数据' },
    { text: '🚪 车厢检查', cmd: '打开DM-08的车门' }
  ],
  afternoon: [
    { text: '🚗 下午配送调度', cmd: '调配3辆车去C区' },
    { text: '⚠️ 故障巡检', cmd: '查看故障车辆' },
    { text: '📊 运营进度', cmd: '统计今日运营数据' },
    { text: '🔋 充电提醒', cmd: '检查车辆电量' }
  ],
  evening: [
    { text: '📊 今日总结', cmd: '统计今日运营数据' },
    { text: '🔋 夜间充电', cmd: '安排低电量车辆充电' },
    { text: '🚗 末班调度', cmd: '查看待命车辆' },
    { text: '📋 明日计划', cmd: '制定明日调度计划' }
  ]
}

const curTab = ref('fleet')
const fleetViewMode = ref('data')
const showLogin = ref(false)
const isRegisterMode = ref(false)
const selectedRole = ref('admin')
const loginAccount = ref('')
const loginPassword = ref('')
const loginAccountErr = ref('')
const loginPasswordErr = ref('')
const regName = ref('')
const regPhone = ref('')
const regPassword = ref('')
const regNameErr = ref('')
const regPhoneErr = ref('')
const regPasswordErr = ref('')
const inputText = ref('')
const isTyping = ref(false)
const isRecording = ref(false)
const messages = ref([])
const conversationHistory = ref([])
const toastVisible = ref(false)
const toastTitle = ref('')
const toastDetail = ref('')
const securityModalVisible = ref(false)
const securitySub = ref('')
const securityChecks = ref([])
const securityBtnDisabled = ref(false)
const vehicleModalVisible = ref(false)
const vehicleModalView = ref('list')
const vehicleEditIdx = ref(-1)
const vehicleForm = reactive({ id: '', type: '小型', loc: '', battery: 100 })
const companyModalVisible = ref(false)
const companyForm = reactive({ name: '', address: '', contact: '', fleetSize: 0 })
const editProfileModalVisible = ref(false)
const profileForm = reactive({ name: '', phone: '' })
const infoModalVisible = ref(false)
const infoModalTitle = ref('')
const infoModalContent = ref('')
const confirmCardCounter = ref(0)
const sceneChips = ref([])

let mediaRecorder = null
let audioChunks = []
let audioStream = null
let pendingSecurityAction = null
let toastTimer = null
let sceneTimer = null

const currentUser = reactive({
  name: '张运营',
  role: 'admin',
  permissions: roleConfig.admin.permissions,
  level: 'A级',
  phone: '13800000001',
  avatar: null
})

const chatPageRef = ref(null)
const chatListRef = ref(null)
const chatInputRef = ref(null)
const chipsScrollRef = ref(null)
const avatarInputRef = ref(null)

const currentRoleLabel = computed(() => (roleConfig[currentUser.role] || roleConfig.admin).label)
const currentPermissions = computed(() => (roleConfig[currentUser.role] || roleConfig.admin).permissions)
const idleCount = computed(() => V.filter(v => v.status === 'idle').length)
const busyCount = computed(() => V.filter(v => v.status === 'busy').length)
const chargeCount = computed(() => V.filter(v => v.status === 'charge').length)
const faultCount = computed(() => V.filter(v => v.status === 'fault').length)

function statusText(s) { return s === 'idle' ? '待命' : s === 'busy' ? '配送中' : s === 'charge' ? '充电中' : '故障' }
function battColor(b) { return b > 50 ? 'var(--green)' : b > 20 ? 'var(--orange)' : 'var(--red)' }
function esc(t) { return t.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;') }
function now() { const d = new Date(); return String(d.getHours()).padStart(2,'0') + ':' + String(d.getMinutes()).padStart(2,'0') }

function showToast(title, text) {
  toastTitle.value = title; toastDetail.value = text; toastVisible.value = true
  if (toastTimer) clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toastVisible.value = false }, 2500)
}

function addLog(text, type) {
  const d = new Date(), t = String(d.getHours()).padStart(2,'0') + ':' + String(d.getMinutes()).padStart(2,'0')
  logs.unshift({ time: t, text, type })
}

function addMsg(html, from) {
  messages.value.push({ html, from, time: now() })
  nextTick(() => { scrollChat() })
}

function scrollChat() { if (chatPageRef.value) chatPageRef.value.scrollTop = chatPageRef.value.scrollHeight }
function showTyping() { isTyping.value = true; nextTick(scrollChat) }
function hideTyping() { isTyping.value = false }

function autoResize() {
  if (chatInputRef.value) {
    chatInputRef.value.style.height = 'auto'
    chatInputRef.value.style.height = Math.min(chatInputRef.value.scrollHeight, 80) + 'px'
  }
}

function tapV(id) {
  const v = V.find(x => x.id === id)
  if (v) showToast(v.id, statusText(v.status) + ' · ' + v.battery + '% · ' + v.loc)
}

function goTab(tab) {
  curTab.value = tab
  if (tab === 'chat') { nextTick(() => { setTimeout(() => { scrollChat() }, 350) }) }
  if (tab === 'fleet') { fleetViewMode.value = 'data' }
}

function switchFleetView(view) { fleetViewMode.value = view }
function toggleAuthMode() { isRegisterMode.value = !isRegisterMode.value; clearFormErrors() }
function clearFormErrors() { loginAccountErr.value = ''; loginPasswordErr.value = ''; regNameErr.value = ''; regPhoneErr.value = ''; regPasswordErr.value = '' }
function handleAuth() { clearFormErrors(); isRegisterMode.value ? handleRegister() : handleLogin() }

async function handleLogin() {
  let valid = true
  if (!loginAccount.value.trim()) { loginAccountErr.value = '请输入账号'; valid = false }
  if (!loginPassword.value) { loginPasswordErr.value = '请输入密码'; valid = false }
  if (!valid) return
  try {
    const res = await api.login(loginAccount.value.trim(), loginPassword.value)
    const data = res.data?.data || res.data
    const token = data.accessToken || data.token
    if (token) {
      localStorage.setItem('token', token)
      if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken)
    }
    const user = data.user || data
    currentUser.name = user.name || user.username || loginAccount.value.trim()
    currentUser.role = user.roleKey || user.role || 'admin'
    currentUser.permissions = (roleConfig[currentUser.role] || roleConfig.admin).permissions
    currentUser.level = (roleConfig[currentUser.role] || roleConfig.admin).level
    currentUser.phone = user.phone || ''
    enterApp()
  } catch (err) {
    const localUser = registeredUsers.find(u => u.account === loginAccount.value.trim() && u.password === loginPassword.value)
    if (localUser) {
      currentUser.name = localUser.name; currentUser.role = localUser.role; currentUser.permissions = roleConfig[localUser.role].permissions; currentUser.level = roleConfig[localUser.role].level; currentUser.phone = localUser.phone
      enterApp()
    } else {
      loginAccountErr.value = err.response?.data?.message || '账号或密码错误'
    }
  }
}

async function handleRegister() {
  let valid = true
  if (!regName.value.trim()) { regNameErr.value = '请输入姓名'; valid = false }
  if (!/^1\d{10}$/.test(regPhone.value.trim())) { regPhoneErr.value = '请输入有效手机号'; valid = false }
  if (regPassword.value.length < 6) { regPasswordErr.value = '密码至少6位'; valid = false }
  if (!valid) return
  try {
    const account = regPhone.value.trim()
    const res = await api.register(account, regPassword.value, regName.value.trim(), regPhone.value.trim(), selectedRole.value)
    const data = res.data?.data || res.data
    const token = data.accessToken || data.token
    if (token) {
      localStorage.setItem('token', token)
      if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken)
    }
    currentUser.name = regName.value.trim(); currentUser.role = selectedRole.value; currentUser.permissions = roleConfig[selectedRole.value].permissions; currentUser.level = roleConfig[selectedRole.value].level; currentUser.phone = regPhone.value.trim()
    showToast('注册成功', `欢迎，${currentUser.name}！`)
    enterApp()
  } catch (err) {
    if (err.response?.data?.message?.includes('已存在') || err.response?.data?.message?.includes('已注册')) {
      regPhoneErr.value = '该手机号已注册'
    } else {
      const account = 'user' + (registeredUsers.length + 1)
      registeredUsers.push({ account, password: regPassword.value, name: regName.value.trim(), role: selectedRole.value, phone: regPhone.value.trim() })
      currentUser.name = regName.value.trim(); currentUser.role = selectedRole.value; currentUser.permissions = roleConfig[selectedRole.value].permissions; currentUser.level = roleConfig[selectedRole.value].level; currentUser.phone = regPhone.value.trim()
      showToast('注册成功', `欢迎，${currentUser.name}！`)
      enterApp()
    }
  }
}

function enterApp() {
  showLogin.value = false; renderSceneRecommendations()
  loadVehiclesFromBackend()
  loadAlertsFromBackend()
  loadCompanyInfo()
  connectWebSocket(
    () => { console.log('[Sync] WebSocket connected for real-time sync') },
    () => { console.warn('[Sync] WebSocket connection failed, will retry') }
  )
  setTimeout(() => {
    goTab('chat')
    setTimeout(() => {
      addMsg(`<div class="plan-card" style="border-left:3px solid var(--blue)"><div class="plan-header"><div class="plan-header-icon" style="background:linear-gradient(135deg,#4a9eff,#6366f1)">🤖</div><div class="plan-header-text"><div class="plan-title">轻行Claw 智能体已就绪</div><div class="plan-date">GLM-4-Flash 驱动 · 数据实时同步中</div></div></div><div class="plan-section"><div class="plan-section-title">我能做什么</div><div style="display:grid;grid-template-columns:1fr 1fr;gap:6px"><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📋</span><span style="font-size:11px;text-align:center">查看车队状态</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">🔋</span><span style="font-size:11px;text-align:center">调度充电</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📊</span><span style="font-size:11px;text-align:center">运营数据分析</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📅</span><span style="font-size:11px;text-align:center">制定调度计划</span></div></div></div><div class="plan-section"><div class="plan-section-title">使用方式</div><div style="display:flex;flex-direction:column;gap:6px"><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(74,158,255,0.12);color:var(--blue);border-color:rgba(74,158,255,0.2)">1</span><span class="plan-time-task">用自然语言告诉我要做什么</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(192,132,252,0.12);color:var(--purple);border-color:rgba(192,132,252,0.2)">2</span><span class="plan-time-task">我自动拆解任务并执行</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(74,222,128,0.12);color:var(--green);border-color:rgba(74,222,128,0.2)">3</span><span class="plan-time-task">你只需确认即可</span></div></div></div><div class="plan-alert-box ok"><span class="plan-alert-icon">✅</span><span class="plan-alert-text"><strong>提示：</strong>试试说"需要5辆车去A区"或"低电量充电"</span></div></div>`, 'ai')
    }, 400)
  }, 500)
}

function handleLogout() {
  showLogin.value = true; loginAccount.value = ''; loginPassword.value = ''; conversationHistory.value = []; messages.value = []; isRegisterMode.value = false; currentConversationId = null
  localStorage.removeItem('token'); localStorage.removeItem('refreshToken')
  disconnectWebSocket()
}

function triggerAvatarUpload() { avatarInputRef.value?.click() }

function handleAvatarChange(e) {
  const file = e.target.files[0]; if (!file) return
  if (file.size > 5 * 1024 * 1024) { showToast('文件过大', '请选择5MB以内的图片'); return }
  const reader = new FileReader()
  reader.onload = (ev) => { currentUser.avatar = ev.target.result; showToast('头像已更新', file.name) }
  reader.readAsDataURL(file); e.target.value = ''
}

function showEditProfile() { profileForm.name = currentUser.name; profileForm.phone = currentUser.phone || ''; editProfileModalVisible.value = true }
function saveProfile() { if (profileForm.name.trim()) currentUser.name = profileForm.name.trim(); if (profileForm.phone.trim()) currentUser.phone = profileForm.phone.trim(); editProfileModalVisible.value = false; showToast('保存成功', '个人资料已更新') }

function showPermissions() {
  const rc = roleConfig[currentUser.role] || roleConfig.admin
  const allPerms = ['车队管理','任务调度','能源管理','运营分析','AI智能体','故障管理','车辆管理','系统设置','车辆操作','查看状态']
  infoModalTitle.value = '🔐 权限管理'
  infoModalContent.value = `<div style="margin-bottom:12px;font-size:13px;color:var(--text2)">当前身份：<strong style="color:var(--blue)">${rc.label}</strong>（${rc.level}）</div><div style="display:flex;flex-direction:column;gap:6px">${allPerms.map(p => { const has = rc.permissions.includes(p); return `<div style="display:flex;align-items:center;justify-content:space-between;padding:10px 12px;border-radius:10px;background:rgba(255,255,255,0.04);border:0.5px solid rgba(255,255,255,0.06)"><span style="font-size:13px">${p}</span><span style="font-size:12px;font-weight:600;color:${has?'var(--green)':'var(--text3)'}">${has?'✓ 已授权':'✗ 未授权'}</span></div>` }).join('')}</div>`
  infoModalVisible.value = true
}

function showSecurityLog() {
  infoModalTitle.value = '📋 操作日志'
  infoModalContent.value = `<div style="display:flex;flex-direction:column;gap:6px">${logs.slice(0, 10).map(l => `<div style="display:flex;align-items:center;gap:8px;padding:8px 10px;border-radius:8px;background:rgba(255,255,255,0.04)"><div style="width:6px;height:6px;border-radius:50%;background:var(--${l.type==='ok'?'green':l.type==='warn'?'orange':'blue'});flex-shrink:0"></div><span style="flex:1;font-size:12px">${l.text}</span><span style="font-size:11px;color:var(--text3)">${l.time}</span></div>`).join('')}</div>`
  infoModalVisible.value = true
}

function showApiInfo() {
  infoModalTitle.value = '⚡ API 状态'
  infoModalContent.value = `<div style="display:flex;flex-direction:column;gap:8px"><div style="display:flex;align-items:center;gap:10px;padding:12px;border-radius:12px;background:rgba(74,158,255,0.08);border:0.5px solid rgba(74,158,255,0.15)"><div style="font-size:28px">⚡</div><div><div style="font-size:15px;font-weight:700">GLM-4-Flash</div><div style="font-size:12px;color:var(--text3)">智谱AI 高性能对话模型</div></div></div><div style="padding:10px 12px;border-radius:10px;background:rgba(255,255,255,0.04);border:0.5px solid rgba(255,255,255,0.06)"><div style="display:flex;justify-content:space-between;padding:6px 0;font-size:13px"><span style="color:var(--text2)">模型</span><span style="font-weight:600">glm-4-flash</span></div><div style="display:flex;justify-content:space-between;padding:6px 0;font-size:13px;border-top:0.5px solid rgba(255,255,255,0.06)"><span style="color:var(--text2)">最大Token</span><span style="font-weight:600">131,072</span></div><div style="display:flex;justify-content:space-between;padding:6px 0;font-size:13px;border-top:0.5px solid rgba(255,255,255,0.06)"><span style="color:var(--text2)">用途</span><span style="font-weight:600;color:var(--blue)">对话理解·任务拆解</span></div></div><div style="display:grid;grid-template-columns:1fr 1fr;gap:6px"><div style="padding:10px;border-radius:10px;background:rgba(255,255,255,0.04);text-align:center;border:0.5px solid rgba(255,255,255,0.06)"><div style="font-size:18px">🧠</div><div style="font-size:11px;margin-top:4px">自然语言理解</div></div><div style="padding:10px;border-radius:10px;background:rgba(255,255,255,0.04);text-align:center;border:0.5px solid rgba(255,255,255,0.06)"><div style="font-size:18px">📋</div><div style="font-size:11px;margin-top:4px">任务拆解</div></div><div style="padding:10px;border-radius:10px;background:rgba(255,255,255,0.04);text-align:center;border:0.5px solid rgba(255,255,255,0.06)"><div style="font-size:18px">🚀</div><div style="font-size:11px;margin-top:4px">快速推理</div></div><div style="padding:10px;border-radius:10px;background:rgba(255,255,255,0.04);text-align:center;border:0.5px solid rgba(255,255,255,0.06)"><div style="font-size:18px">🔒</div><div style="font-size:11px;margin-top:4px">安全网关</div></div></div></div>`
  infoModalVisible.value = true; checkAPIHealth()
}

function showAbout() {
  infoModalTitle.value = '🦞 关于轻行Claw'
  infoModalContent.value = `<div style="display:flex;flex-direction:column;align-items:center;gap:10px;padding:8px 0"><div style="width:56px;height:56px;border-radius:16px;background:linear-gradient(135deg,#ef4444,#f97316);display:flex;align-items:center;justify-content:center;font-size:28px;box-shadow:0 4px 16px rgba(239,68,68,0.3)">🦞</div><div style="font-size:20px;font-weight:700">轻行Claw</div><div style="font-size:12px;color:var(--text3)">OpenClaw · 版本 1.0.0</div></div><div style="padding:12px;border-radius:12px;background:rgba(255,255,255,0.04);border:0.5px solid rgba(255,255,255,0.06);margin-top:4px"><div style="font-size:13px;line-height:1.7;color:var(--text2)">轻行Claw是一款基于<strong style="color:var(--red)">OpenClaw</strong>架构的AI车队智能体管理平台，由<strong style="color:var(--orange)">橙紫Challenge</strong>团队开发。如同龙虾在深海中精准捕食，轻行Claw以OpenClaw之力智能调度——<strong style="color:var(--blue)">开放、敏捷、有力</strong>。</div></div><div style="padding:12px;border-radius:12px;background:rgba(255,255,255,0.04);border:0.5px solid rgba(255,255,255,0.06);margin-top:8px"><div style="font-size:12px;font-weight:700;color:var(--text2);margin-bottom:8px">核心能力</div><div style="display:flex;flex-direction:column;gap:6px"><div style="display:flex;align-items:center;gap:8px;font-size:12px;color:var(--text2)"><span style="color:var(--blue)">●</span> 自然语言理解</div><div style="display:flex;align-items:center;gap:8px;font-size:12px;color:var(--text2)"><span style="color:var(--green)">●</span> 任务拆解引擎</div><div style="display:flex;align-items:center;gap:8px;font-size:12px;color:var(--text2)"><span style="color:var(--purple)">●</span> 实时数据流</div><div style="display:flex;align-items:center;gap:8px;font-size:12px;color:var(--text2)"><span style="color:var(--orange)">●</span> 安全确认网关</div><div style="display:flex;align-items:center;gap:8px;font-size:12px;color:var(--text2)"><span style="color:var(--red)">●</span> 语音交互</div></div></div><div style="text-align:center;margin-top:12px;font-size:11px;color:var(--text3)">© 2026 橙紫Challenge · OpenClaw</div>`
  infoModalVisible.value = true
}

function openVehicleModal() { vehicleModalView.value = 'list'; vehicleModalVisible.value = true }
function showAddVehicleForm() { vehicleForm.id = 'DM-' + String(V.length + 1).padStart(2, '0'); vehicleForm.type = '小型'; vehicleForm.loc = ''; vehicleForm.battery = 100; vehicleModalView.value = 'add' }

async function saveNewVehicle() {
  if (!vehicleForm.id.trim()) { showToast('错误', '请输入车辆编号'); return }
  if (V.find(v => v.id === vehicleForm.id.trim())) { showToast('错误', '车辆编号已存在'); return }
  const newV = { id: vehicleForm.id.trim(), type: vehicleForm.type || '小型', status: 'standby', battery: 100, location: vehicleForm.loc.trim() || 'A区-1号点', mileage: 0 }
  try {
    await api.createVehicle(newV)
    await loadVehiclesFromBackend()
  } catch (e) { console.error('[API] createVehicle failed:', e.message); showToast('保存失败', '车辆数据无法同步到服务器'); return }
  showToast('添加成功', `${vehicleForm.id.trim()} 已加入车队`); vehicleModalView.value = 'list'
}

function openEditVehicle(idx) { vehicleEditIdx.value = idx; vehicleForm.type = V[idx].type; vehicleForm.loc = V[idx].loc; vehicleForm.battery = V[idx].battery; vehicleModalView.value = 'edit' }

async function saveEditVehicle() {
  const v = V[vehicleEditIdx.value]; if (!v) return
  v.type = vehicleForm.type || v.type; v.loc = vehicleForm.loc || v.loc; v.battery = Math.min(100, Math.max(0, parseInt(vehicleForm.battery) || v.battery))
  try {
    await api.updateVehicle(v.id, { type: v.type, location: v.loc, battery: v.battery })
    await loadVehiclesFromBackend()
  } catch (e) { console.warn('[API] updateVehicle failed, using local:', e.message) }
  showToast('保存成功', `${v.id} 信息已更新`); vehicleModalView.value = 'list'
}

async function deleteVehicle() {
  const idx = vehicleEditIdx.value; const id = V[idx]?.id
  if (id) {
    try {
      await api.deleteVehicle(id)
      await loadVehiclesFromBackend()
    } catch (e) { console.warn('[API] deleteVehicle failed, using local:', e.message); V.splice(idx, 1) }
    showToast('已删除', `${id} 已从车队移除`)
  }
  vehicleModalView.value = 'list'
}

function openCompanyModal() { companyForm.name = companyInfo.name; companyForm.address = companyInfo.address; companyForm.contact = companyInfo.contact; companyForm.fleetSize = companyInfo.fleetSize; companyModalVisible.value = true }
async function saveCompanyInfo() {
  const data = { name: companyForm.name.trim() || companyInfo.name, address: companyForm.address.trim() || companyInfo.address, contact: companyForm.contact.trim() || companyInfo.contact, fleetSize: parseInt(companyForm.fleetSize) || companyInfo.fleetSize }
  try { await api.updateCompanyInfo(data) } catch (e) { console.warn('[API] updateCompanyInfo failed:', e.message) }
  Object.assign(companyInfo, data)
  companyModalVisible.value = false; showToast('保存成功', '公司信息已更新')
}
async function loadCompanyInfo() {
  try {
    const res = await api.getCompanyInfo()
    const d = res.data?.data || res.data
    if (d) { companyInfo.name = d.name || companyInfo.name; companyInfo.address = d.address || companyInfo.address; companyInfo.contact = d.contact || companyInfo.contact; companyInfo.fleetSize = d.fleetSize || companyInfo.fleetSize }
  } catch (e) { console.warn('[API] loadCompanyInfo failed:', e.message) }
}

function getCurrentScene() { const h = new Date().getHours(); return h < 11 ? 'morning' : h < 14 ? 'noon' : h < 17 ? 'afternoon' : 'evening' }
function renderSceneRecommendations() { sceneChips.value = sceneRecommendations[getCurrentScene()] || [] }

async function toggleVoice() {
  if (isRecording.value) { stopVoice(); return }
  try {
    audioStream = await navigator.mediaDevices.getUserMedia({ audio: true })
  } catch (e) {
    showToast('麦克风权限', '请在浏览器设置中允许麦克风访问')
    return
  }
  audioChunks = []
  try {
    mediaRecorder = new MediaRecorder(audioStream, { mimeType: 'audio/webm;codecs=opus' })
  } catch (e) {
    mediaRecorder = new MediaRecorder(audioStream)
  }
  mediaRecorder.ondataavailable = (e) => { if (e.data.size > 0) audioChunks.push(e.data) }
  mediaRecorder.onstop = async () => {
    if (audioStream) { audioStream.getTracks().forEach(t => t.stop()); audioStream = null }
    if (audioChunks.length === 0) { showToast('录音失败', '未录到音频数据'); return }
    const blob = new Blob(audioChunks, { type: mediaRecorder.mimeType || 'audio/webm' })
    audioChunks = []
    if (blob.size < 1000) { showToast('录音太短', '请多说几句再试'); return }
    inputText.value = '🎤 识别中...'
    try {
      const res = await api.uploadVoice(blob)
      const text = res.data?.data?.text || res.data?.text || ''
      if (text) { inputText.value = text; nextTick(autoResize) }
      else { inputText.value = ''; showToast('识别结果为空', '请再试一次') }
    } catch (err) {
      inputText.value = ''
      showToast('识别失败', err.response?.data?.message || err.message || '语音识别服务异常')
    }
  }
  isRecording.value = true
  mediaRecorder.start(250)
}

function stopVoice() {
  isRecording.value = false
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    try { mediaRecorder.stop() } catch (e) {}
  }
  mediaRecorder = null
}

function buildSystemPrompt() {
  const statusMap = { idle: '待命', busy: '配送中', charge: '充电中', fault: '故障' }
  const vehicleList = V.map(v => `${v.id}: ${v.type}, ${statusMap[v.status]}, 电量${v.battery}%, ${v.loc}, ${v.task}, 今日已跑${v.todayKm}km, 今日完成${v.todayOrders}单, 百公里能耗${v.energyPer100km}kWh, 累计里程${v.totalKm}km`).join('\n')
  const idle = V.filter(v => v.status === 'idle'), busy = V.filter(v => v.status === 'busy'), charging = V.filter(v => v.status === 'charge'), fault = V.filter(v => v.status === 'fault'), lowBat = V.filter(v => v.battery < 30)
  const totalOrders = V.reduce((a,v)=>a+v.todayOrders, 0), totalKm = V.reduce((a,v)=>a+v.todayKm, 0), avgEnergy = (V.reduce((a,v)=>a+v.energyPer100km,0)/V.length).toFixed(1)
  return `你是"轻行Claw"，一个无人车队AI智能体。你的核心职责是**主动帮运营人员完成工作**，而不是让他们来操作。\n\n===== 核心行为准则 =====\n1. **你要先干活**：收到指令后，你自己去查数据、分析情况、得出结论\n2. **用对话告诉用户结果**：像真人助手一样，把查到的情况用自然语言说清楚\n3. **最后才问确认**：只有在需要实际执行操作时，才让用户确认\n4. **数据都是现成的**：下面提供的数据就是当前系统的真实数据，直接用来回答就行\n5. **给完整方案**：当用户提出调度需求时，你要给出完整的调度方案\n\n===== 当前实时车队数据 =====\n${vehicleList}\n\n概况：\n- 总车辆：${V.length}辆\n- 待命：${idle.length}辆（${idle.map(v=>v.id).join('、')}）\n- 配送中：${busy.length}辆（${busy.map(v=>v.id).join('、')}）\n- 充电中：${charging.length}辆（${charging.map(v=>v.id).join('、')}）\n- 故障：${fault.length}辆（${fault.map(v=>v.id).join('、')}）\n- 低电量(<30%)：${lowBat.length}辆\n- 平均电量：${Math.round(V.reduce((a,v)=>a+v.battery,0)/V.length)}%\n\n===== 今日运营数据 =====\n- 今日总配送单量：${totalOrders}单\n- 今日总里程：${totalKm}km\n- 完成率：98.2%\n- 平均时效：23分钟/单\n- 较昨日：+12.3%\n- 平均百公里能耗：${avgEnergy}kWh\n\n===== 回复格式 =====\n返回严格JSON格式（不要加markdown代码块标记）：\n对于需要执行操作的指令：{"reply":"自然语言回复","need_confirm":true,"action_summary":"操作摘要","affected_vehicles":["DM-01"],"action_type":"调度|充电|开门|维修|其他"}\n对于纯查询或闲聊：{"reply":"自然语言回复","need_confirm":false}\n\n===== 关键规则 =====\n- 必须基于提供的数据回答\n- 不要说"没有数据"\n- 调度类指令必须给出完整方案\n- 电量低于30%的车辆需要关注\n- 故障车辆和配送中的车辆不能被调度`
}

async function callGLM(userMessage) {
  conversationHistory.value.push({ role: 'user', content: userMessage })
  try {
    const res = await api.sendAIMessage(userMessage, currentConversationId)
    const data = res.data?.data || res.data
    if (data.conversationId) currentConversationId = data.conversationId
    const content = data.reply || data.content || data.message || ''
    conversationHistory.value.push({ role: 'assistant', content })
    try { let cleanContent = content.replace(/```json\s*/g, '').replace(/```\s*/g, '').trim(); const jsonMatch = cleanContent.match(/\{[\s\S]*\}/); if (jsonMatch) return JSON.parse(jsonMatch[0]) } catch(e) {}
    return { reply: content, need_confirm: false }
  } catch(err) {
    console.error('AI API Error:', err)
    return { reply: null, error: err.message }
  }
}

async function checkAPIHealth() {
  try {
    const res = await fetch('/api/health')
    const data = await res.json()
    if (data.status === 'ok') {
      showToast('API 状态', 'GLM-4-Flash 服务可用（代理模式）')
    } else if (data.status === 'partial') {
      showToast('API 状态', data.message || 'AI服务部分可用')
    } else {
      showToast('API 状态', '服务异常')
    }
  } catch(e) {
    showToast('API 状态', '后端服务未启动，请先启动 claw-server')
  }
}

function shouldUseLocalProcessing(t) {
  const tl = t.toLowerCase()
  return tl.includes('明日') && (tl.includes('计划') || tl.includes('调度')) || tl.includes('制定') && tl.includes('计划') || tl.includes('车辆状态') || tl.includes('车队状态') || tl.includes('所有车辆') || tl.includes('今日数据') || tl.includes('运营数据') || tl.includes('运营') && tl.includes('数据') || tl.includes('能耗') || tl.includes('耗电') || tl.includes('电耗') || tl.includes('充电') || tl.includes('调配') || tl.includes('调度') || tl.includes('派车') || tl.includes('开门') || tl.includes('打开') && tl.includes('门') || tl.includes('故障') || tl.includes('维修') || tl.includes('批量') || tl.includes('帮助') || tl.includes('能做什么') || tl.includes('状态') || tl.includes('情况') || tl.includes('怎么样') || tl.includes('多少') || tl.includes('统计') || tl.includes('低电量') || tl.includes('电量')
}

function buildInfoCard(title, items, type = 'info') {
  const tc = { info: '', success: 'success', danger: 'danger', highlight: 'highlight', purple: 'purple' }[type] || ''
  const ic = { info: '📋', success: '✅', danger: '⚠️', highlight: '💡', purple: '🔮' }[type] || '📋'
  return `<div class="info-card ${tc}"><div class="info-card-title">${ic} ${title}</div><div class="info-card-body">${items.map(([k,v]) => `<div class="info-row"><div class="info-label">${k}</div><div class="info-value">${v}</div></div>`).join('')}</div></div>`
}

function buildStatCard(title, stats, type = 'info') {
  const tc = { info: '', success: 'success', danger: 'danger', highlight: 'highlight', purple: 'purple' }[type] || ''
  const ic = { info: '📊', success: '✅', danger: '⚠️', highlight: '💡', purple: '🔮' }[type] || '📊'
  const colors = ['#4a9eff','#4ade80','#fbbf24','#f87171','#c084fc','#22d3ee']
  return `<div class="info-card ${tc}"><div class="info-card-title">${ic} ${title}</div><div class="stat-grid">${stats.map((s,i) => `<div class="stat-item"><div class="stat-num" style="color:${colors[i%colors.length]}">${s.num}</div><div class="stat-lbl">${s.lbl}</div></div>`).join('')}</div></div>`
}

function buildTagCard(title, tags, type = 'info') {
  const tc = { info: '', success: 'success', danger: 'danger', highlight: 'highlight', purple: 'purple' }[type] || ''
  const ic = { info: '🏷️', success: '✅', danger: '⚠️', highlight: '💡', purple: '🔮' }[type] || '🏷️'
  const tagColor = { info: 'tag-blue', success: 'tag-green', danger: 'tag-red', highlight: 'tag-orange', purple: 'tag-blue' }[type] || 'tag-blue'
  return `<div class="info-card ${tc}"><div class="info-card-title">${ic} ${title}</div><div class="tag-list">${tags.map(t => `<span class="tag-item ${tagColor}">${t}</span>`).join('')}</div></div>`
}

function buildNotice(text, type = 'tip') { const icons = { tip: '💡', warn: '⚠️', error: '❌' }; return `<div class="notice-box ${type}"><span class="notice-icon">${icons[type]||'💡'}</span><span class="notice-text">${text}</span></div>` }

function buildProgress(label, value, max = 100, color = 'var(--blue)') {
  const pct = Math.min((value / max) * 100, 100); const barColor = pct < 30 ? 'var(--red)' : pct < 50 ? 'var(--orange)' : color
  return `<div class="info-row"><div class="info-label">${label}</div><div class="info-value" style="flex:2"><div style="display:flex;justify-content:space-between;font-size:12px"><span>${value}${max===100?'%':''}</span></div><div class="progress-bar"><div class="progress-fill" style="width:${pct}%;background:${barColor}"></div></div></div></div>`
}

function showConfirmCard(vehicleIds, actionSummary, actionType) {
  confirmCardCounter.value++; const cardUid = 'cc_' + confirmCardCounter.value
  const vehicles = vehicleIds.map(id => V.find(v => v.id === id)).filter(Boolean)
  let vehicleCardsHtml = ''
  if (vehicles.length > 0) { vehicleCardsHtml = `<div class="v-card-list">${vehicles.map(v => { const bc = v.battery>50?'var(--green)':v.battery>20?'var(--orange)':'var(--red)'; return `<div class="v-card-mini"><div class="v-ind ${v.status}"></div><div class="v-card-mini-info"><div class="v-card-mini-name">${v.id}</div><div class="v-card-mini-detail">${v.type} · ${statusText(v.status)} · ${v.loc}</div></div><div class="v-card-mini-batt" style="color:${bc}">${v.battery}%</div></div>` }).join('')}</div>` }
  addMsg(`<div class="confirm-card"><div class="confirm-card-title">⚡ 待确认操作</div><div class="confirm-card-summary">${esc(actionSummary)}</div>${vehicleCardsHtml}<div class="confirm-actions" data-card-uid="${cardUid}"><button class="confirm-btn ok" data-action="confirm-ok" data-card-uid="${cardUid}" data-vehicle-ids='${JSON.stringify(vehicleIds)}' data-action-type="${esc(actionType)}">✓ 确认执行</button><button class="confirm-btn cancel" data-action="confirm-cancel" data-card-uid="${cardUid}">取消</button></div></div>`, 'ai')
}

async function confirmAction(cardUid, vehicleIds, actionType) {
  const msgIdx = messages.value.findIndex(m => m.html && m.html.includes(`data-card-uid="${cardUid}"`))
  if (msgIdx !== -1) {
    const msg = messages.value[msgIdx]
    const oldActions = msg.html.match(/<div class="confirm-actions" data-card-uid="[^"]*">[\s\S]*?<\/div>/)
    if (oldActions) { msg.html = msg.html.replace(oldActions[0], `<div class="confirm-actions" data-card-uid="${cardUid}"><button class="confirm-btn ok" disabled>🔐 等待安全验证...</button><button class="confirm-btn cancel" data-action="confirm-cancel" data-card-uid="${cardUid}">取消</button></div>`) }
  }
  showSecurityModal(actionType, vehicleIds, [{ label: '身份验证', detail: `操作员：${currentUser.name}（已验证）`, passed: true }, { label: '车辆唯一性判定', detail: `目标车辆: ${vehicleIds.join(', ')} 已确认`, passed: true }, { label: '权限校验', detail: `${currentUser.role} - ${actionType}权限已授权`, passed: true }, { label: '安全确认', detail: '物理操作需人工最终确认', passed: true }])
}

function cancelAction(cardUid) {
  const msgIdx = messages.value.findIndex(m => m.html && m.html.includes(`data-card-uid="${cardUid}"`))
  if (msgIdx !== -1) {
    const msg = messages.value[msgIdx]
    const oldActions = msg.html.match(/<div class="confirm-actions"[^>]*>[\s\S]*?<\/div>/)
    if (oldActions) { msg.html = msg.html.replace(oldActions[0], `<div class="confirm-actions"><button class="confirm-btn cancel" disabled style="opacity:0.5">已取消</button></div>`) }
  }
  addMsg(`好的，操作已取消。有其他需要随时说。`, 'ai')
}

function showSecurityModal(action, vehicleIds, checks) { pendingSecurityAction = { action, vehicleIds }; securityChecks.value = checks || []; securitySub.value = `操作: ${action}`; securityBtnDisabled.value = false; securityModalVisible.value = true }

function confirmSecurity() {
  securityBtnDisabled.value = true
  setTimeout(() => { securityModalVisible.value = false; securityBtnDisabled.value = false; if (pendingSecurityAction) { showToast('安全验证通过', `正在执行: ${pendingSecurityAction.action}`); executeAction(pendingSecurityAction.vehicleIds, pendingSecurityAction.action); showToast('操作完成', `${pendingSecurityAction.vehicleIds.length} 辆车状态已更新`); pendingSecurityAction = null } }, 1500)
}

async function executeAction(vehicleIds, actionType) {
  const type = (actionType || '').toLowerCase()
  for (const id of vehicleIds) {
    const v = V.find(x => x.id === id); if (!v) continue
    let newStatus = v.status
    if (type.includes('充电') || type.includes('调度充电')) { newStatus = 'charge'; v.task = '前往充电站'; addLog(`${v.id} 已调度前往充电站`, 'ok') }
    else if (type.includes('开门') || type.includes('打开')) { addLog(`${v.id} 车门已远程开启`, 'info') }
    else if (type.includes('调度') || type.includes('派')) { newStatus = 'busy'; v.task = '执行配送任务'; addLog(`${v.id} 已调度执行任务`, 'info') }
    else if (type.includes('维修')) { addLog(`${v.id} 已提交维修工单`, 'warn') }
    else { addLog(`${v.id} 操作已完成`, 'info') }
    if (newStatus !== v.status || type.includes('充电') || type.includes('调度') || type.includes('派')) {
      v.status = newStatus
      try { await api.updateVehicleStatus(id, toBackendStatus(newStatus)) } catch (e) { console.warn('[API] updateVehicleStatus failed:', e.message) }
    }
  }
}

function renderBatchPanel(vehicleIds, actionType) {
  if (!vehicleIds.length) return ''
  const idsJson = JSON.stringify(vehicleIds)
  return `<div class="batch-panel"><div class="batch-panel-header"><div class="batch-panel-title">⚡ 批量操作 ${vehicleIds.length} 辆车</div><div class="batch-count">${actionType}</div></div><div class="batch-vehicles">${vehicleIds.slice(0, 10).map(id => `<span class="batch-vehicle-tag">${id}</span>`).join('')}${vehicleIds.length > 10 ? `<span class="batch-vehicle-tag">+${vehicleIds.length - 10}</span>` : ''}</div><div class="batch-actions"><button class="batch-btn batch-btn-charge" data-action="batch-charge" data-vehicle-ids='${idsJson}'>🔋 充电</button><button class="batch-btn batch-btn-dispatch" data-action="batch-dispatch" data-vehicle-ids='${idsJson}'>🚗 调度</button><button class="batch-btn batch-btn-repair" data-action="batch-repair" data-vehicle-ids='${idsJson}'>⚠️ 维修</button></div></div>`
}

async function executeBatchAction(vehicleIds, actionType) {
  showToast('批量操作执行中', `正在处理 ${vehicleIds.length} 辆车...`)
  setTimeout(async () => {
    for (const id of vehicleIds) {
      const v = V.find(x => x.id === id); if (!v) continue
      let newStatus = v.status
      if (actionType === '充电') { newStatus = 'charge'; v.task = '充电中' }
      else if (actionType === '调度') { newStatus = 'busy'; v.task = '执行配送任务' }
      else if (actionType === '维修') { v.task = '提交维修工单' }
      v.status = newStatus
      try { await api.updateVehicleStatus(id, toBackendStatus(newStatus)) } catch (e) { console.warn('[API] batch updateStatus failed:', e.message) }
    }
    showToast('批量操作完成', `${vehicleIds.length} 辆车已${actionType}`); addLog(`${vehicleIds.length} 辆车批量${actionType}完成`, 'ok')
  }, 1500)
}

function handleChatClick(e) {
  const target = e.target.closest('[data-action]')
  if (!target) return
  const action = target.dataset.action
  if (action === 'confirm-ok') { confirmAction(target.dataset.cardUid, JSON.parse(target.dataset.vehicleIds), target.dataset.actionType) }
  else if (action === 'confirm-cancel') { cancelAction(target.dataset.cardUid) }
  else if (action === 'batch-charge') { executeBatchAction(JSON.parse(target.dataset.vehicleIds), '充电') }
  else if (action === 'batch-dispatch') { executeBatchAction(JSON.parse(target.dataset.vehicleIds), '调度') }
  else if (action === 'batch-repair') { executeBatchAction(JSON.parse(target.dataset.vehicleIds), '维修') }
}

function processLocalCmd(text) {
  const t = text.toLowerCase()

  if(t.includes('帮助')||t.includes('能做什么')){
    let html=buildInfoCard('🦞 轻行Claw AI 智能体', [['驱动模型', 'GLM-4-Flash'], ['核心能力', '自然语言理解 + 智能调度'], ['交互方式', '对话式 → AI 分析 → 确认执行']], 'purple')
    html+=buildTagCard('支持功能', ['车队查询','调度派车','充电管理','物理控制','数据分析','故障处理'], 'info')
    html+=buildNotice('告诉我你的需求，我会自动查数据、分析并生成方案，最后由你确认执行', 'tip')
    addMsg(html,'ai'); return
  }

  if (V.length === 0) {
    addMsg(buildNotice('当前没有车辆数据。请先在「车辆管理」中添加车辆，或检查后端服务是否正常。', 'warn'), 'ai')
    return
  }

  if(t.includes('送了')||t.includes('单量')||t.includes('多少单')||t.includes('配送')&&t.includes('多少')||t.includes('运营')&&t.includes('数据')||t.includes('今天')&&(t.includes('单')||t.includes('送'))){
    const totalOrders = V.reduce((a,v)=>a+v.todayOrders, 0), totalKm = V.reduce((a,v)=>a+v.todayKm, 0), avgBat = Math.round(V.reduce((a,v)=>a+v.battery,0)/V.length)
    const busyCount = V.filter(v=>v.status==='busy').length
    const idleCount = V.filter(v=>v.status==='idle').length
    let html = buildStatCard('今日运营', [{ num: totalOrders, lbl: '配送单' }, { num: busyCount, lbl: '配送中' }, { num: idleCount, lbl: '待命' }, { num: totalKm+'km', lbl: '总里程' }], 'purple')
    html += buildInfoCard('运营概览', [['车队规模', V.length + ' 辆'], ['配送车辆', busyCount + ' 辆'], ['平均电量', buildProgress('', avgBat, 100, avgBat>50?'var(--green)':'var(--orange)')]])
    addMsg(html, 'ai'); return
  }

  if(t.includes('能耗')||t.includes('耗电')||t.includes('电耗')||t.includes('电量')&&!t.includes('低')&&!t.includes('充电')){
    const totalKm = V.reduce((a,v)=>a+v.todayKm, 0), avgE = (V.reduce((a,v)=>a+v.energyPer100km,0)/V.length).toFixed(1), totalConsump = (totalKm * avgE / 100).toFixed(1), highEnergy = V.filter(v => v.energyPer100km > 10)
    const smallAvg = V.filter(v=>v.type==='小型').length ? (V.filter(v=>v.type==='小型').reduce((a,v)=>a+v.energyPer100km,0)/V.filter(v=>v.type==='小型').length).toFixed(1) : '-'
    const midAvg = V.filter(v=>v.type==='中型').length ? (V.filter(v=>v.type==='中型').reduce((a,v)=>a+v.energyPer100km,0)/V.filter(v=>v.type==='中型').length).toFixed(1) : '-'
    const largeAvg = V.filter(v=>v.type==='大型').length ? (V.filter(v=>v.type==='大型').reduce((a,v)=>a+v.energyPer100km,0)/V.filter(v=>v.type==='大型').length).toFixed(1) : '-'
    const maxE = Math.max(...V.map(v=>v.energyPer100km))
    let html = buildStatCard('能耗概览', [{ num: avgE, lbl: 'kWh/100km' }, { num: totalConsump, lbl: '今日耗电kWh' }, { num: V.length, lbl: '车辆总数' }, { num: totalKm+'km', lbl: '总里程' }], 'highlight')
    html += buildInfoCard('各车型能耗对比', [['小型车', `<div class="progress-bar"><div class="progress-fill" style="width:${(smallAvg/maxE*100)||0}%;background:var(--blue)"></div></div><span style="font-size:11px;color:var(--text3)">${smallAvg} kWh/100km</span>`], ['中型车', `<div class="progress-bar"><div class="progress-fill" style="width:${(midAvg/maxE*100)||0}%;background:var(--purple)"></div></div><span style="font-size:11px;color:var(--text3)">${midAvg} kWh/100km</span>`], ['大型车', `<div class="progress-bar"><div class="progress-fill" style="width:${(largeAvg/maxE*100)||0}%;background:var(--orange)"></div></div><span style="font-size:11px;color:var(--text3)">${largeAvg} kWh/100km</span>`]])
    if(highEnergy.length) html += buildNotice(`发现 ${highEnergy.length} 辆车能耗偏高（>10 kWh/100km），建议检查车辆状态`, 'warn')
    addMsg(html, 'ai'); return
  }

  if(t.includes('状态')||t.includes('情况')||t.includes('怎么样')||t.includes('车辆')) {
    if(t.includes('所有')||t.includes('全部')||t.includes('车队')||t.includes('多少')||t.includes('查看')) {
      const idle=V.filter(v=>v.status==='idle'), busy=V.filter(v=>v.status==='busy'), chg=V.filter(v=>v.status==='charge'), flt=V.filter(v=>v.status==='fault'), low=V.filter(v=>v.battery<30), avgBat = Math.round(V.reduce((a,v)=>a+v.battery,0)/V.length), totalOrders = V.reduce((a,v)=>a+v.todayOrders, 0), totalKm = V.reduce((a,v)=>a+v.todayKm, 0)
      let html = `<div class="plan-card"><div class="plan-header"><div class="plan-header-icon" style="background:linear-gradient(135deg,#4a9eff,#22d3ee)">🚗</div><div class="plan-header-text"><div class="plan-title">车队实时状态</div><div class="plan-date">共 ${V.length} 辆车在线</div></div></div><div class="plan-section"><div style="display:grid;grid-template-columns:repeat(4,1fr);gap:6px"><div class="plan-time-item" style="flex-direction:column;padding:10px 6px"><span class="plan-time-badge" style="background:rgba(74,222,128,0.12);color:var(--green);border-color:rgba(74,222,128,0.2);font-size:16px;min-width:auto">${idle.length}</span><span class="plan-time-task" style="font-size:10px;text-align:center">待命</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px 6px"><span class="plan-time-badge" style="background:rgba(74,158,255,0.12);color:var(--blue);border-color:rgba(74,158,255,0.2);font-size:16px;min-width:auto">${busy.length}</span><span class="plan-time-task" style="font-size:10px;text-align:center">配送中</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px 6px"><span class="plan-time-badge" style="background:rgba(251,191,36,0.12);color:var(--orange);border-color:rgba(251,191,36,0.2);font-size:16px;min-width:auto">${chg.length}</span><span class="plan-time-task" style="font-size:10px;text-align:center">充电中</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px 6px"><span class="plan-time-badge" style="background:rgba(248,113,113,0.12);color:var(--red);border-color:rgba(248,113,113,0.2);font-size:16px;min-width:auto">${flt.length}</span><span class="plan-time-task" style="font-size:10px;text-align:center">故障</span></div></div></div><div class="plan-section"><div style="display:grid;grid-template-columns:1fr 1fr;gap:6px"><div class="plan-time-item"><span style="font-size:11px;color:var(--text3)">平均电量</span><span style="font-size:14px;font-weight:700;color:${avgBat>50?'var(--green)':avgBat>30?'var(--orange)':'var(--red)'}">${avgBat}%</span></div><div class="plan-time-item"><span style="font-size:11px;color:var(--text3)">今日配送</span><span style="font-size:14px;font-weight:700;color:var(--blue)">${totalOrders}单</span></div></div></div>`
      if(low.length) html += `<div class="plan-alert-box warn"><span class="plan-alert-icon">⚠️</span><span class="plan-alert-text"><strong>低电量预警：</strong>${low.map(v=>`${v.id}(${v.battery}%)`).join('、')}，建议尽快充电</span></div>`
      html += `<div class="plan-section" style="margin-top:8px"><div class="plan-section-title">全部车辆</div><div class="plan-vehicle-list">`
      V.forEach(v => { const statusColorMap = {idle:'var(--green)',busy:'var(--blue)',charge:'var(--orange)',fault:'var(--red)'}; const bc = v.battery>50?'var(--green)':v.battery>20?'var(--orange)':'var(--red)'; html += `<div class="plan-vehicle-item" style="padding:10px 12px"><div style="width:8px;height:8px;border-radius:50%;background:${statusColorMap[v.status]};box-shadow:0 0 6px ${statusColorMap[v.status]};flex-shrink:0"></div><span class="plan-vehicle-id">${v.id}</span><span class="plan-vehicle-detail">${v.type} · ${v.loc} · ${statusText(v.status)}</span><span class="plan-vehicle-batt" style="color:${bc}">${v.battery}%</span></div>` })
      html += `</div></div>`; addMsg(html,'ai'); return
    }
    const sv = V.find(v => t.includes(v.id.toLowerCase()))
    if(sv) { const sc = sv.status==='idle'?'success':sv.status==='busy'?'info':sv.status==='charge'?'highlight':'danger'; const scIcon = sv.status==='idle'?'🟢':sv.status==='busy'?'🔵':sv.status==='charge'?'🟡':'🔴'; let html = buildInfoCard(`${scIcon} ${sv.id} 详情`, [['当前状态', `<span style="color:var(--${sc==='success'?'green':sc==='info'?'blue':sc==='highlight'?'orange':'red'})">${statusText(sv.status)}</span>`], ['车型', sv.type], ['位置', sv.loc], ['任务', sv.task], ['今日里程', `${sv.todayKm} km`], ['今日单数', `${sv.todayOrders} 单`]], sc); html += buildProgress('电量', sv.battery); addMsg(html,'ai'); return }
  }

  if(t.includes('充电')){
    const tg=V.filter(v=>v.battery<30&&v.status!=='charge')
    if(!tg.length){ const avgBat = Math.round(V.reduce((a,v)=>a+v.battery,0)/V.length); let html = buildInfoCard('充电状态检查', [['平均电量', avgBat+'%'], ['低电量车辆', '0 辆']], 'success'); html += buildNotice('当前没有需要充电的车辆，所有车辆电量均在 30% 以上', 'tip'); addMsg(html,'ai');return }
    let html = buildInfoCard(`低电量预警`, [['需要充电', `${tg.length} 辆`], ['车辆列表', tg.map(v=>`<span style="color:var(--red)">${v.id}</span>(${v.battery}%)`).join('、')]], 'danger'); html += buildNotice('建议立即调度充电', 'warn'); addMsg(html,'ai'); showConfirmCard(tg.map(v=>v.id), `调度 ${tg.length} 辆低电量车辆前往充电站`, '充电'); return
  }

  if(t.includes('调配')||t.includes('调度')||t.includes('派')||t.includes('需要')||t.includes('运输')||t.includes('运货')||t.includes('车去')) {
    const numMatch = text.match(/(\d+)\s*辆/); const needNum = numMatch ? parseInt(numMatch[1]) : 3; const available = V.filter(v => v.status === 'idle')
    let dest = ''; const destMatch = text.match(/([A-E])区/); if(destMatch) dest = destMatch[1] + '区'
    let cargo = ''; if(t.includes('红酒')) cargo = '红酒'; else if(t.includes('生鲜')||t.includes('冷链')) cargo = '生鲜'; else if(t.includes('电子')) cargo = '电子产品'; else if(t.includes('药品')) cargo = '药品'
    const sorted = [...available].sort((a, b) => { const aNear = dest && a.loc.includes(dest) ? 1 : 0; const bNear = dest && b.loc.includes(dest) ? 1 : 0; if(aNear !== bNear) return bNear - aNear; return b.battery - a.battery })
    if(available.length === 0) { addMsg(`当前没有可调度的车辆。配送中 ${V.filter(v=>v.status==='busy').length} 辆，充电中 ${V.filter(v=>v.status==='charge').length} 辆，故障 ${V.filter(v=>v.status==='fault').length} 辆。建议等待配送完成或充电结束后再调度。`,'ai'); return }
    const dispatch = sorted.slice(0, Math.min(needNum, available.length))
    let replyHtml = `收到！安排 <strong>${dispatch.length} 辆</strong> 车${dest ? '去'+dest : ''}${cargo ? '运'+cargo : ''}<br>`
    replyHtml += `<div class="chat-sec-title">车辆选型</div><div class="v-card-list">`
    dispatch.forEach((v) => { const isNear = dest && v.loc.includes(dest); const batteryOk = v.battery > 50; const bc = batteryOk ? 'var(--green)' : v.battery < 30 ? 'var(--red)' : 'var(--orange)'; let tags = []; if(isNear) tags.push(`<span class="tag-pill" style="color:var(--blue);border-color:rgba(74,158,255,0.2)"><span class="dot" style="background:var(--blue)"></span>就近</span>`); if(batteryOk) tags.push(`<span class="tag-pill" style="color:var(--green);border-color:rgba(74,222,128,0.2)"><span class="dot" style="background:var(--green)"></span>电量足</span>`); replyHtml += `<div class="v-card-mini"><div class="v-ind ${v.status}"></div><div class="v-card-mini-info"><div class="v-card-mini-name">${v.id} <span style="font-size:11px;color:var(--text3)">${v.type}</span></div><div class="v-card-mini-detail">${v.loc}${tags.length ? ' · ' + tags.join(' ') : ''}</div></div><div class="v-card-mini-batt" style="color:${bc}">${v.battery}%</div></div>` })
    replyHtml += `</div><div class="chat-sec-title">贴心提醒</div><div style="display:flex;flex-wrap:wrap;gap:6px">`
    if(dest) replyHtml += `<span class="tag-pill" style="color:var(--blue);border-color:rgba(74,158,255,0.2)"><span class="dot" style="background:var(--blue)"></span>${dest}预留缓冲</span>`
    const backup = available.find(v => !dispatch.includes(v)); if(backup) replyHtml += `<span class="tag-pill" style="color:var(--green);border-color:rgba(74,222,128,0.2)"><span class="dot" style="background:var(--green)"></span>备选:${backup.id}</span>`
    replyHtml += `<span class="tag-pill" style="color:var(--cyan);border-color:rgba(34,211,238,0.2)"><span class="dot" style="background:var(--cyan)"></span>检查温控</span></div>`
    addMsg(replyHtml, 'ai'); showConfirmCard(dispatch.map(v=>v.id), `调配 ${dispatch.length} 辆车${dest ? '去'+dest : ''}${cargo ? '运输'+cargo : ''}`, '调度'); return
  }

  if(t.includes('明日')&&(t.includes('计划')||t.includes('调度'))||t.includes('制定')&&t.includes('计划')){
    const idle=V.filter(v=>v.status==='idle'), low=V.filter(v=>v.battery<30), busy=V.filter(v=>v.status==='busy'), charge=V.filter(v=>v.status==='charge')
    const tomorrow=new Date(); tomorrow.setDate(tomorrow.getDate()+1); const dateStr = `${tomorrow.getFullYear()}年${tomorrow.getMonth()+1}月${tomorrow.getDate()}日`
    let html = `<div class="plan-card"><div class="plan-header"><div class="plan-header-icon">📅</div><div class="plan-header-text"><div class="plan-title">明日调度计划</div><div class="plan-date">${dateStr}</div></div></div><div class="plan-section"><div class="plan-section-title">车队概况</div><div style="display:grid;grid-template-columns:repeat(2,1fr);gap:6px"><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(74,222,128,0.12);color:var(--green);border-color:rgba(74,222,128,0.2)">${idle.length}</span><span class="plan-time-task">待命</span></div><div class="plan-time-item"><span class="plan-time-badge">${busy.length}</span><span class="plan-time-task">配送中</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(251,191,36,0.12);color:var(--orange);border-color:rgba(251,191,36,0.2)">${charge.length}</span><span class="plan-time-task">充电中</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(248,113,113,0.12);color:var(--red);border-color:rgba(248,113,113,0.2)">${V.filter(v=>v.status==='fault').length}</span><span class="plan-time-task">故障</span></div></div></div><div class="plan-section"><div class="plan-section-title">时间规划</div><div class="plan-time-grid"><div class="plan-time-item"><span class="plan-time-badge">08:30</span><span class="plan-time-task">晨检：检查车辆状态</span></div><div class="plan-time-item"><span class="plan-time-badge">09:00</span><span class="plan-time-task">低电量车辆开始充电</span></div><div class="plan-time-item"><span class="plan-time-badge">10:00</span><span class="plan-time-task">早高峰配送准备</span></div><div class="plan-time-item"><span class="plan-time-badge">12:00</span><span class="plan-time-task">午间充电调度</span></div><div class="plan-time-item"><span class="plan-time-badge">14:00</span><span class="plan-time-task">下午配送高峰</span></div><div class="plan-time-item"><span class="plan-time-badge">17:00</span><span class="plan-time-task">晚高峰前充电补充</span></div><div class="plan-time-item"><span class="plan-time-badge">19:00</span><span class="plan-time-task">夜间充电安排</span></div></div></div>`
    if(low.length > 0) { html += `<div class="plan-section"><div class="plan-section-title">低电量优先处理</div><div class="plan-vehicle-list">`; low.forEach(v => { const bc = v.battery > 20 ? 'var(--orange)' : 'var(--red)'; html += `<div class="plan-vehicle-item"><span class="plan-vehicle-id">${v.id}</span><span class="plan-vehicle-detail">${v.type} · ${v.loc} · ${v.battery < 20 ? '紧急充电' : '建议充电'}</span><span class="plan-vehicle-batt" style="color:${bc}">${v.battery}%</span></div>` }); html += `</div></div>` }
    html += `<div class="plan-section">`
    if(low.length > 0) html += `<div class="plan-alert-box warn"><span class="plan-alert-icon">⚠️</span><span class="plan-alert-text"><strong>优先处理：</strong>${low.map(v=>v.id).join('、')} 电量低于30%</span></div>`
    html += `<div class="plan-alert-box ok"><span class="plan-alert-icon">✅</span><span class="plan-alert-text"><strong>建议：</strong>明日早高峰前确保至少 ${Math.min(idle.length + 2, V.length - charge.length)} 辆车处于待命状态</span></div></div><div class="plan-tag-list"><span class="plan-tag blue">早高峰 10:00-12:00</span><span class="plan-tag orange">午间充电 12:00-14:00</span><span class="plan-tag green">晚高峰 17:00-18:00</span></div></div>`
    addMsg(html,'ai'); return
  }

  if((t.includes('开门')||t.includes('打开'))&&t.includes('门')){
    const v=V.find(x=>t.includes(x.id.toLowerCase()))
    if(!v){addMsg(buildNotice('请指定车辆编号，例如："打开 DM-08 的车门"', 'warn'),'ai');return}
    let html=buildInfoCard(`🚪 ${v.id} 车门控制`, [['当前状态', `<span style="color:${v.status==='busy'?'var(--blue)':'var(--green)'}">${v.status==='busy'?'配送中':'可操作'}</span>`], ['电量', v.battery+'%'], ['位置', v.loc], ['安全提醒', '开门前请确认周边无人']], v.status==='busy'?'highlight':'success')
    if(v.status==='busy') html+=buildNotice('车辆正在配送中，确认要开门吗？', 'warn')
    addMsg(html,'ai'); showConfirmCard([v.id], `远程开启 ${v.id} 车厢门`, '开门'); return
  }

  if(t.includes('故障')||t.includes('维修')){
    const flt=V.filter(v=>v.status==='fault')
    if(!flt.length){addMsg(buildInfoCard('✅ 故障巡检', [['状态', '当前无故障车辆']], 'success'),'ai');return}
    let html=buildInfoCard(`⚠️ 故障车辆`, [['数量', `${flt.length} 辆`], ['详情', flt.map(v=>`${v.id} — ${v.task}`).join('、')]], 'danger')
    addMsg(html,'ai'); showConfirmCard(flt.map(v=>v.id), `为 ${flt.length} 辆故障车辆提交维修工单`, '维修'); return
  }

  if(t.includes('批量')&&t.includes('充电')){
    const lowBat=V.filter(v=>v.battery<30&&v.status!=='charge')
    if(!lowBat.length){addMsg(buildNotice('当前没有需要批量充电的车辆', 'tip'),'ai');return}
    addMsg(buildInfoCard('🔋 批量充电调度', [['待处理车辆', `${lowBat.length} 辆`], ['平均电量', `${Math.round(lowBat.reduce((a,v)=>a+v.battery,0)/lowBat.length)}%`]], 'danger'),'ai')
    addMsg(renderBatchPanel(lowBat.map(v=>v.id), '批量充电'),'ai'); return
  }

  if(t.includes('批量')&&t.includes('调度')){
    const available=V.filter(v=>v.status==='idle')
    if(!available.length){addMsg(buildNotice('当前没有可调度的车辆', 'warn'),'ai');return}
    const typeDist={}; available.forEach(v=>{typeDist[v.type]=(typeDist[v.type]||0)+1;})
    addMsg(buildInfoCard('🚗 批量调度', [['可用车辆', `${available.length} 辆`], ['车型分布', Object.entries(typeDist).map(([k,v])=>`${k}${v}辆`).join('、')]], 'info'),'ai')
    addMsg(renderBatchPanel(available.map(v=>v.id), '批量调度'),'ai'); return
  }

  let html=buildInfoCard('💬 收到指令', [['你的输入', text]])
  html+=buildNotice('试试告诉我你需要什么，例如："需要5辆车"、"低电量充电"、"今日数据"', 'tip')
  html+=buildTagCard('快捷指令', ['车队状态','今日数据','充电管理','调度派车','帮助'], 'info')
  addMsg(html,'ai')
}

function sendChip(cmd) { if(curTab.value !== 'chat') { goTab('chat'); setTimeout(() => doSend(cmd), 300) } else doSend(cmd) }
function sendMsg() { const t = inputText.value.trim(); if (!t) return; inputText.value = ''; if (chatInputRef.value) chatInputRef.value.style.height = 'auto'; doSend(t) }

async function doSend(t) {
  addMsg(esc(t), 'user'); showTyping()
  if (shouldUseLocalProcessing(t)) { await loadVehiclesFromBackend(); hideTyping(); processLocalCmd(t); return }
  const result = await callGLM(t); hideTyping()
  if (result.error) { addMsg(`⚠️ API连接异常，已切换到本地模式。<br><br>${result.error}`, 'ai'); processLocalCmd(t); return }
  if (result.reply) {
    let cleanReply = result.reply.replace(/```[\s\S]*?```/g, '').replace(/`[^`]+`/g, '').replace(/<style[^>]*>[\s\S]*?<\/style>/gi, '').replace(/<script[^>]*>[\s\S]*?<\/script>/gi, '').replace(/\$\{[^}]+\}/g, '').trim()
    let formattedReply = cleanReply.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>').replace(/\*(.+?)\*/g, '<em>$1</em>').replace(/^### (.+)$/gm, '<div style="font-weight:700;margin:8px 0 4px">$1</div>').replace(/\n/g, '<br>')
    addMsg(formattedReply, 'ai')
  }
  if (result.need_confirm) { showConfirmCard(result.affected_vehicles || [], result.action_summary || '执行操作', result.action_type || '其他') }
}

async function loadVehiclesFromBackend() {
  try {
    const res = await api.getAllVehicles()
    const raw = res.data
    let list = raw?.data ?? raw
    if (!Array.isArray(list)) {
      if (raw?.records) list = raw.records
      else if (raw?.list) list = raw.list
      else { console.warn('[API] Unexpected vehicle response:', raw); return }
    }
    V.splice(0, V.length)
    list.forEach(v => {
      const fs = toFrontendStatus(v.status)
      V.push({
        id: v.id || v.vehicleId || v.name || '',
        type: v.type || '小型',
        status: fs,
        battery: v.battery != null ? Math.round(Number(v.battery)) : 0,
        loc: v.location || v.loc || '',
        task: fs === 'idle' ? '待命' : fs === 'busy' ? '配送中' : fs === 'charge' ? '充电中' : fs === 'fault' ? '故障' : '待命',
        todayKm: v.mileage ? Math.round(Number(v.mileage) % 100) : 0,
        todayOrders: v.todayOrders || 0,
        energyPer100km: v.type === '大型' ? 12.0 : v.type === '中型' ? 8.5 : 6.0,
        totalKm: v.mileage ? Math.round(Number(v.mileage)) : 0
      })
    })
    console.log(`[API] Loaded ${V.length} vehicles from backend`)
  } catch (e) {
    console.error('[API] loadVehicles failed:', e.message)
    showToast('数据加载失败', '无法从服务器获取车辆数据: ' + (e.response?.data?.message || e.message))
  }
}

async function loadAlertsFromBackend() {
  try {
    const res = await api.getAlerts({ page: 1, size: 10 })
    const list = res.data?.data?.list || res.data?.data?.records || res.data?.records || []
    if (Array.isArray(list) && list.length > 0) {
      logs.splice(0, logs.length)
      list.forEach(a => {
        const t = a.createdAt ? new Date(a.createdAt) : new Date()
        logs.push({
          time: String(t.getHours()).padStart(2, '0') + ':' + String(t.getMinutes()).padStart(2, '0'),
          text: a.description || a.message || a.title || '系统通知',
          type: a.level === 'critical' || a.level === 'high' ? 'warn' : a.level === 'medium' ? 'info' : 'ok'
        })
      })
      console.log(`[API] Loaded ${logs.length} alerts from backend`)
    }
  } catch (e) {
    console.warn('[API] loadAlerts failed, using local data:', e.message)
  }
}

function handleWsVehicleUpdate(data) {
  if (!data || !data.vehicleId) return
  const v = V.find(x => x.id === data.vehicleId)
  if (v) {
    const newStatus = toFrontendStatus(data.status)
    if (v.status !== newStatus) {
      v.status = newStatus
      v.task = newStatus === 'idle' ? '待命' : newStatus === 'busy' ? '配送中' : newStatus === 'charge' ? '充电中' : newStatus === 'fault' ? '故障' : v.task
      addLog(`${v.id} 状态更新为${statusText(newStatus)}`, newStatus === 'fault' ? 'warn' : 'ok')
    }
  } else {
    loadVehiclesFromBackend()
  }
}

function handleWsAlertUpdate(data) {
  if (!data) return
  const t = new Date()
  logs.unshift({
    time: String(t.getHours()).padStart(2, '0') + ':' + String(t.getMinutes()).padStart(2, '0'),
    text: data.description || data.message || data.title || '新告警',
    type: data.level === 'critical' || data.level === 'high' ? 'warn' : 'info'
  })
}

function handleWsTaskUpdate(data) {
  if (!data) return
  const t = new Date()
  logs.unshift({
    time: String(t.getHours()).padStart(2, '0') + ':' + String(t.getMinutes()).padStart(2, '0'),
    text: data.description || data.title || '任务更新',
    type: 'info'
  })
}

onMounted(() => {
  renderSceneRecommendations()
  wsOn('vehicle-update', handleWsVehicleUpdate)
  wsOn('alert-update', handleWsAlertUpdate)
  wsOn('task-update', handleWsTaskUpdate)
  window.addEventListener('auth-expired', () => { showLogin.value = true; showToast('登录已过期', '请重新登录') })
  const savedToken = localStorage.getItem('token')
  if (savedToken) {
    try {
      const payload = JSON.parse(atob(savedToken.split('.')[1]))
      if (payload.exp && payload.exp * 1000 < Date.now()) {
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
        showLogin.value = true
        return
      }
    } catch (e) { localStorage.removeItem('token'); localStorage.removeItem('refreshToken'); showLogin.value = true; return }
    currentUser.name = '用户'
    loadVehiclesFromBackend()
    loadAlertsFromBackend()
    connectWebSocket(
      () => { console.log('[Sync] WebSocket reconnected') },
      () => { console.warn('[Sync] WebSocket reconnect failed') }
    )
  }
  setTimeout(() => {
    goTab('chat')
    setTimeout(() => {
      addMsg(`<div class="plan-card" style="border-left:3px solid var(--blue)"><div class="plan-header"><div class="plan-header-icon" style="background:linear-gradient(135deg,#4a9eff,#6366f1)">🤖</div><div class="plan-header-text"><div class="plan-title">轻行Claw 智能体已就绪</div><div class="plan-date">GLM-4-Flash 驱动 · 数据实时同步中</div></div></div><div class="plan-section"><div class="plan-section-title">我能做什么</div><div style="display:grid;grid-template-columns:1fr 1fr;gap:6px"><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📋</span><span style="font-size:11px;text-align:center">查看车队状态</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">🔋</span><span style="font-size:11px;text-align:center">调度充电</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📊</span><span style="font-size:11px;text-align:center">运营数据分析</span></div><div class="plan-time-item" style="flex-direction:column;padding:10px"><span style="font-size:18px">📅</span><span style="font-size:11px;text-align:center">制定调度计划</span></div></div></div><div class="plan-section"><div class="plan-section-title">使用方式</div><div style="display:flex;flex-direction:column;gap:6px"><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(74,158,255,0.12);color:var(--blue);border-color:rgba(74,158,255,0.2)">1</span><span class="plan-time-task">用自然语言告诉我要做什么</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(192,132,252,0.12);color:var(--purple);border-color:rgba(192,132,252,0.2)">2</span><span class="plan-time-task">我自动拆解任务并执行</span></div><div class="plan-time-item"><span class="plan-time-badge" style="background:rgba(74,222,128,0.12);color:var(--green);border-color:rgba(74,222,128,0.2)">3</span><span class="plan-time-task">你只需确认即可</span></div></div></div><div class="plan-alert-box ok"><span class="plan-alert-icon">✅</span><span class="plan-alert-text"><strong>提示：</strong>试试说"需要5辆车去A区"或"低电量充电"</span></div></div>`, 'ai')
    }, 400)
  }, 500)
  sceneTimer = setInterval(renderSceneRecommendations, 3600000)
})

onUnmounted(() => { if (sceneTimer) clearInterval(sceneTimer); if (toastTimer) clearTimeout(toastTimer); wsOff('vehicle-update', handleWsVehicleUpdate); wsOff('alert-update', handleWsAlertUpdate); wsOff('task-update', handleWsTaskUpdate); disconnectWebSocket() })
</script>

<style>
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; -webkit-tap-highlight-color: transparent; }
:root {
  --bg: #0a0a1a; --glass: rgba(255,255,255,0.08); --glass-heavy: rgba(255,255,255,0.12);
  --glass-border: rgba(255,255,255,0.15); --glass-shadow: rgba(0,0,0,0.3);
  --text1: #f0f0f8; --text2: #a0a0b8; --text3: #606078;
  --blue: #4a9eff; --green: #4ade80; --orange: #fbbf24; --red: #f87171;
  --purple: #c084fc; --cyan: #22d3ee; --separator: rgba(255,255,255,0.06);
  --safe-b: env(safe-area-inset-bottom, 0px);
}
html { font-size: 16px; height: 100vh; height: 100dvh; scrollbar-color: rgba(255,255,255,0.15) transparent; scrollbar-width: thin; }
html::-webkit-scrollbar { width: 6px; }
html::-webkit-scrollbar-track { background: transparent; }
html::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.15); border-radius: 3px; }
html::-webkit-scrollbar-thumb:hover { background: rgba(255,255,255,0.25); }
body { margin: 0; padding: 0; height: 100vh; height: 100dvh; overflow: hidden; background: #0a0a1a; }
#app { height: 100vh; height: 100dvh; }
.mobile-app { font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "SF Pro Display", "Helvetica Neue", "Noto Sans SC", sans-serif; background: var(--bg); color: var(--text1); height: 100vh; height: 100dvh; overflow: hidden; display: flex; flex-direction: column; -webkit-font-smoothing: antialiased; position: relative; font-size: 16px; }
.bg-animate { position: fixed; inset: 0; z-index: 0; overflow: hidden; pointer-events: none; }
.bg-animate::before { content: ''; position: absolute; width: 200%; height: 200%; top: -50%; left: -50%; background: radial-gradient(ellipse 600px 600px at 20% 30%, rgba(74,158,255,0.15) 0%, transparent 70%), radial-gradient(ellipse 500px 500px at 80% 20%, rgba(192,132,252,0.12) 0%, transparent 70%), radial-gradient(ellipse 400px 400px at 60% 80%, rgba(34,211,238,0.10) 0%, transparent 70%), radial-gradient(ellipse 500px 500px at 10% 70%, rgba(74,222,128,0.08) 0%, transparent 70%), radial-gradient(ellipse 350px 350px at 90% 60%, rgba(251,191,36,0.06) 0%, transparent 70%); animation: bgDrift 25s ease-in-out infinite alternate; }
.bg-animate::after { content: ''; position: absolute; width: 200%; height: 200%; top: -50%; left: -50%; background: radial-gradient(ellipse 450px 450px at 70% 40%, rgba(192,132,252,0.10) 0%, transparent 70%), radial-gradient(ellipse 550px 550px at 30% 60%, rgba(74,158,255,0.12) 0%, transparent 70%), radial-gradient(ellipse 350px 350px at 50% 20%, rgba(248,113,113,0.06) 0%, transparent 70%), radial-gradient(ellipse 400px 400px at 85% 85%, rgba(34,211,238,0.08) 0%, transparent 70%); animation: bgDrift2 30s ease-in-out infinite alternate; }
@keyframes bgDrift { 0% { transform: translate(0,0) rotate(0deg) scale(1); } 33% { transform: translate(3%,-5%) rotate(3deg) scale(1.02); } 66% { transform: translate(-2%,3%) rotate(-2deg) scale(0.98); } 100% { transform: translate(5%,-2%) rotate(5deg) scale(1.01); } }
@keyframes bgDrift2 { 0% { transform: translate(0,0) rotate(0deg) scale(1.01); } 33% { transform: translate(-4%,3%) rotate(-3deg) scale(0.99); } 66% { transform: translate(2%,-4%) rotate(4deg) scale(1.02); } 100% { transform: translate(-3%,5%) rotate(-2deg) scale(1); } }
.orb { position: fixed; border-radius: 50%; pointer-events: none; z-index: 0; filter: blur(80px); opacity: 0.5; }
.orb-1 { width: 300px; height: 300px; background: rgba(74,158,255,0.3); top: 10%; left: 5%; animation: orbFloat1 20s ease-in-out infinite; }
.orb-2 { width: 250px; height: 250px; background: rgba(192,132,252,0.25); top: 50%; right: 5%; animation: orbFloat2 18s ease-in-out infinite; }
.orb-3 { width: 200px; height: 200px; background: rgba(34,211,238,0.2); bottom: 20%; left: 30%; animation: orbFloat3 22s ease-in-out infinite; }
.orb-4 { width: 180px; height: 180px; background: rgba(74,222,128,0.15); top: 30%; right: 25%; animation: orbFloat4 16s ease-in-out infinite; }
@keyframes orbFloat1 { 0%,100%{transform:translate(0,0)} 25%{transform:translate(40px,-30px)} 50%{transform:translate(-20px,50px)} 75%{transform:translate(30px,20px)} }
@keyframes orbFloat2 { 0%,100%{transform:translate(0,0)} 25%{transform:translate(-50px,20px)} 50%{transform:translate(30px,-40px)} 75%{transform:translate(-20px,-30px)} }
@keyframes orbFloat3 { 0%,100%{transform:translate(0,0)} 25%{transform:translate(30px,40px)} 50%{transform:translate(-40px,-20px)} 75%{transform:translate(50px,-10px)} }
@keyframes orbFloat4 { 0%,100%{transform:translate(0,0)} 25%{transform:translate(-30px,-40px)} 50%{transform:translate(40px,30px)} 75%{transform:translate(-10px,50px)} }
.noise-overlay { position: fixed; inset: 0; z-index: 1; pointer-events: none; opacity: 0.03; background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)'/%3E%3C/svg%3E"); }
.glass { background: var(--glass); backdrop-filter: blur(40px) saturate(180%); -webkit-backdrop-filter: blur(40px) saturate(180%); border: 0.5px solid var(--glass-border); box-shadow: inset 0 1px 0 rgba(255,255,255,0.08), 0 2px 8px rgba(0,0,0,0.2), 0 8px 24px rgba(0,0,0,0.1); }
.glass-heavy { background: var(--glass-heavy); backdrop-filter: blur(60px) saturate(200%); -webkit-backdrop-filter: blur(60px) saturate(200%); border: 0.5px solid rgba(255,255,255,0.15); box-shadow: inset 0 1px 0 rgba(255,255,255,0.1), 0 1px 6px rgba(0,0,0,0.2), 0 8px 32px rgba(0,0,0,0.15); }
.app-header { position: sticky; top: 0; z-index: 60; flex-shrink: 0; padding: 10px 20px 10px; background: rgba(255,255,255,0.06); backdrop-filter: blur(60px) saturate(200%); -webkit-backdrop-filter: blur(60px) saturate(200%); border-bottom: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 4px 24px rgba(0,0,0,0.3), 0 1px 4px rgba(0,0,0,0.2); }
.header-row { display: flex; align-items: center; justify-content: space-between; }
.brand { display: flex; align-items: center; gap: 10px; }
.brand-icon { width: 34px; height: 34px; border-radius: 9px; background: linear-gradient(135deg, #ef4444, #f97316); display: flex; align-items: center; justify-content: center; font-size: 17px; box-shadow: 0 2px 12px rgba(239,68,68,0.3), inset 0 1px 0 rgba(255,255,255,0.2); }
.brand-name { font-size: 18px; font-weight: 700; letter-spacing: -0.4px; }
.header-badge { display: flex; align-items: center; gap: 5px; background: rgba(74,222,128,0.12); border-radius: 14px; padding: 4px 10px; font-size: 12px; font-weight: 600; color: var(--green); border: 0.5px solid rgba(74,222,128,0.2); }
.header-badge::before { content: ''; width: 6px; height: 6px; border-radius: 50%; background: var(--green); box-shadow: 0 0 6px var(--green); }
.header-metrics { display: flex; gap: 16px; margin-top: 8px; }
.hm { display: flex; align-items: center; gap: 6px; }
.hm-val { font-size: 16px; font-weight: 700; letter-spacing: -0.3px; }
.hm-val.blue { color: var(--blue); } .hm-val.green { color: var(--green); } .hm-val.orange { color: var(--orange); }
.hm-lbl { font-size: 11px; color: var(--text3); font-weight: 500; }
.hm-dot { width: 4px; height: 4px; border-radius: 50%; background: rgba(255,255,255,0.15); }
.main-area { flex: 1; min-height: 0; position: relative; overflow: hidden; z-index: 2; display: flex; flex-direction: column; }
.page { flex: 1; overflow-y: auto; overflow-x: hidden; display: none; -webkit-overflow-scrolling: touch; padding-bottom: calc(72px + var(--safe-b)); }
.page.active { display: block; }
.page.chat-active { padding-bottom: calc(170px + var(--safe-b)); }
.page::-webkit-scrollbar { width: 4px; } .page::-webkit-scrollbar-track { background: transparent; } .page::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 2px; }
.sec-head { padding: 20px 20px 10px; font-size: 13px; font-weight: 600; color: var(--text2); text-transform: uppercase; letter-spacing: 0.3px; }
.view-tabs { display: flex; gap: 4px; padding: 12px 20px 0; margin-bottom: 4px; }
.view-tab { flex: 1; padding: 8px; border-radius: 12px; border: none; background: rgba(255,255,255,0.04); color: var(--text3); font-size: 13px; font-weight: 600; cursor: pointer; font-family: inherit; transition: all 0.2s; text-align: center; border: 0.5px solid rgba(255,255,255,0.06); }
.view-tab.active { background: rgba(74,158,255,0.12); color: var(--blue); border-color: rgba(74,158,255,0.2); box-shadow: 0 2px 8px rgba(74,158,255,0.15); }
.view-tab:active { transform: scale(0.97); }
.fleet-summary { display: flex; gap: 10px; padding: 0 20px; overflow-x: auto; scroll-snap-type: x mandatory; -webkit-overflow-scrolling: touch; }
.fleet-summary::-webkit-scrollbar { display: none; }
.fs-card { min-width: 90px; flex-shrink: 0; scroll-snap-align: start; border-radius: 18px; padding: 14px; text-align: center; }
.fs-num { font-size: 26px; font-weight: 700; } .fs-lbl { font-size: 11px; color: var(--text2); margin-top: 2px; font-weight: 500; }
.fleet-list { padding: 0 20px; display: flex; flex-direction: column; gap: 8px; }
.v-row { border-radius: 18px; padding: 14px 16px; display: flex; align-items: center; gap: 14px; transition: transform 0.15s ease, box-shadow 0.2s ease; }
.v-row:active { transform: scale(0.985); }
.v-ind { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.v-ind.idle { background: var(--green); box-shadow: 0 0 6px var(--green); } .v-ind.busy { background: var(--blue); box-shadow: 0 0 6px var(--blue); } .v-ind.charge { background: var(--orange); box-shadow: 0 0 6px var(--orange); } .v-ind.fault { background: var(--red); box-shadow: 0 0 6px var(--red); }
.v-info { flex: 1; min-width: 0; } .v-name { font-size: 15px; font-weight: 600; } .v-detail { font-size: 12px; color: var(--text2); margin-top: 2px; display: flex; gap: 10px; }
.v-batt { display: flex; align-items: center; gap: 6px; } .v-batt-bar { width: 48px; height: 4px; background: rgba(255,255,255,0.1); border-radius: 2px; overflow: hidden; } .v-batt-fill { height: 100%; border-radius: 2px; } .v-batt-pct { font-size: 12px; font-weight: 600; min-width: 30px; text-align: right; }
.v-st-tag { font-size: 11px; font-weight: 600; padding: 3px 8px; border-radius: 8px; }
.tag-idle { background: rgba(74,222,128,0.12); color: var(--green); border: 0.5px solid rgba(74,222,128,0.2); }
.tag-busy { background: rgba(74,158,255,0.1); color: var(--blue); border: 0.5px solid rgba(74,158,255,0.2); }
.tag-charge { background: rgba(251,191,36,0.1); color: var(--orange); border: 0.5px solid rgba(251,191,36,0.2); }
.tag-fault { background: rgba(248,113,113,0.1); color: var(--red); border: 0.5px solid rgba(248,113,113,0.2); }
.chat-list { display: flex; flex-direction: column; gap: 12px; padding: 12px 16px 16px; }
.msg { display: flex; gap: 8px; max-width: 92%; } .msg.user { align-self: flex-end; flex-direction: row-reverse; }
.msg-ava { width: 30px; height: 30px; border-radius: 50%; flex-shrink: 0; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 700; margin-top: 2px; }
.msg-ava.ai { background: linear-gradient(135deg, #4a9eff, #c084fc); color: #fff; box-shadow: 0 2px 8px rgba(74,158,255,0.3); }
.msg-ava.u { background: rgba(255,255,255,0.1); color: var(--text2); }
.msg-body { display: flex; flex-direction: column; }
.msg-bbl { padding: 10px 14px; border-radius: 18px; font-size: 14.5px; line-height: 1.55; word-break: break-word; max-width: 100%; overflow-wrap: break-word; }
.msg.ai .msg-bbl { background: rgba(255,255,255,0.06); backdrop-filter: blur(50px) saturate(180%); -webkit-backdrop-filter: blur(50px) saturate(180%); border: 0.5px solid rgba(255,255,255,0.1); border-top-left-radius: 4px; color: var(--text1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 2px 8px rgba(0,0,0,0.15); }
.msg.user .msg-bbl { background: linear-gradient(135deg, #4a9eff, #6366f1); color: #fff; border-top-right-radius: 4px; box-shadow: 0 2px 12px rgba(74,158,255,0.3), inset 0 1px 0 rgba(255,255,255,0.15); }
.msg-time { font-size: 10px; color: var(--text3); margin-top: 3px; padding: 0 6px; } .msg.user .msg-time { text-align: right; }
.typing { display: flex; align-items: center; gap: 5px; padding: 12px 14px; border-radius: 18px; background: rgba(255,255,255,0.06); backdrop-filter: blur(50px) saturate(180%); -webkit-backdrop-filter: blur(50px) saturate(180%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06); border-top-left-radius: 4px; width: fit-content; }
.t-dot { width: 6px; height: 6px; border-radius: 50%; background: var(--cyan); box-shadow: 0 0 6px var(--cyan); }
.t-dot:nth-child(1) { animation: tB 1.4s ease-in-out infinite; } .t-dot:nth-child(2) { animation: tB 1.4s ease-in-out 0.15s infinite; } .t-dot:nth-child(3) { animation: tB 1.4s ease-in-out 0.3s infinite; }
@keyframes tB { 0%,60%,100%{transform:translateY(0)} 30%{transform:translateY(-5px)} }
.v-card-list { display: flex; flex-direction: column; gap: 6px; margin-top: 8px; }
.v-card-mini { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 12px; background: rgba(255,255,255,0.05); backdrop-filter: blur(16px) saturate(130%); -webkit-backdrop-filter: blur(16px) saturate(130%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 1px 3px rgba(0,0,0,0.1); }
.v-card-mini .v-ind { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.v-card-mini-info { flex: 1; } .v-card-mini-name { font-size: 13px; font-weight: 600; } .v-card-mini-detail { font-size: 11px; color: var(--text2); margin-top: 1px; } .v-card-mini-batt { font-size: 12px; font-weight: 700; min-width: 36px; text-align: right; }
.tag-pill { display: inline-flex; align-items: center; gap: 4px; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; background: rgba(255,255,255,0.06); border: 0.5px solid rgba(255,255,255,0.1); }
.tag-pill .dot { width: 5px; height: 5px; border-radius: 50%; }
.chat-sec-title { font-size: 12px; font-weight: 700; color: var(--text2); text-transform: uppercase; letter-spacing: 0.5px; margin: 12px 0 6px; display: flex; align-items: center; gap: 6px; }
.chat-sec-title::before { content: ''; width: 3px; height: 14px; border-radius: 2px; background: linear-gradient(180deg, var(--blue), var(--purple)); }
.confirm-card { background: rgba(255,255,255,0.06); backdrop-filter: blur(24px) saturate(150%); -webkit-backdrop-filter: blur(24px) saturate(150%); border: 0.5px solid rgba(255,255,255,0.1); border-radius: 16px; padding: 14px; margin-top: 10px; box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 2px 8px rgba(0,0,0,0.15); }
.confirm-card-title { font-size: 12px; font-weight: 700; color: var(--orange); text-transform: uppercase; letter-spacing: 0.3px; margin-bottom: 8px; display: flex; align-items: center; gap: 5px; text-shadow: 0 0 12px rgba(251,191,36,0.3); }
.confirm-card-summary { font-size: 13.5px; line-height: 1.5; color: var(--text1); margin-bottom: 12px; }
.confirm-actions { display: flex; gap: 8px; }
.confirm-btn { flex: 1; padding: 10px 0; border-radius: 12px; border: none; font-size: 13px; font-weight: 600; cursor: pointer; font-family: inherit; transition: opacity 0.15s, transform 0.1s; }
.confirm-btn:active { opacity: 0.7; transform: scale(0.97); }
.confirm-btn.ok { background: linear-gradient(135deg, #4a9eff, #6366f1); color: #fff; box-shadow: 0 2px 12px rgba(74,158,255,0.3), inset 0 1px 0 rgba(255,255,255,0.15); }
.confirm-btn.cancel { background: rgba(255,255,255,0.08); backdrop-filter: blur(16px); -webkit-backdrop-filter: blur(16px); color: var(--text2); box-shadow: inset 0 1px 0 rgba(255,255,255,0.08); border: 0.5px solid rgba(255,255,255,0.1); }
.confirm-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.data-page-inner { padding: 0 20px; }
.d-card { border-radius: 20px; padding: 18px; margin-bottom: 12px; }
.d-card-title { font-size:12px; color:var(--text2); font-weight:600; text-transform:uppercase; letter-spacing:0.3px; margin-bottom:14px; }
.d-big { font-size:34px; font-weight:700; letter-spacing:-0.5px; color:var(--blue); text-shadow: 0 0 20px rgba(74,158,255,0.3); }
.d-big-sub { font-size:14px; color:var(--text2); font-weight:400; margin-left:4px; }
.d-trend { font-size:12px; color:var(--green); font-weight:600; margin-top:4px; }
.d-grid { display:grid; grid-template-columns:1fr 1fr; gap:8px; margin-top:14px; }
.d-item { text-align:center; padding:12px; border-radius:14px; background: rgba(255,255,255,0.05); backdrop-filter: blur(20px) saturate(150%); -webkit-backdrop-filter: blur(20px) saturate(150%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 1px 4px rgba(0,0,0,0.1); }
.d-val { font-size:20px; font-weight:700; } .d-lbl { font-size:11px; color:var(--text2); margin-top:2px; }
.d-bar-row { display:flex; align-items:center; gap:8px; margin-bottom:10px; }
.d-bar-lbl { font-size:12px; color:var(--text2); min-width:30px; }
.d-bar-track { flex:1; height:6px; background:rgba(255,255,255,0.06); border-radius:3px; overflow:hidden; }
.d-bar-fill { height:100%; border-radius:3px; }
.d-bar-pct { font-size:11px; color:var(--text2); min-width:28px; text-align:right; }
.act-list { display:flex; flex-direction:column; gap:6px; }
.act-row { display:flex; align-items:center; gap:10px; padding:10px 12px; border-radius:12px; background: rgba(255,255,255,0.04); backdrop-filter: blur(16px) saturate(130%); -webkit-backdrop-filter: blur(16px) saturate(130%); border: 0.5px solid rgba(255,255,255,0.08); box-shadow: inset 0 1px 0 rgba(255,255,255,0.05), 0 1px 3px rgba(0,0,0,0.1); }
.act-dot { width:6px; height:6px; border-radius:50%; flex-shrink:0; }
.act-dot.ok { background:var(--green); box-shadow: 0 0 4px var(--green); } .act-dot.warn { background:var(--orange); box-shadow: 0 0 4px var(--orange); } .act-dot.info { background:var(--blue); box-shadow: 0 0 4px var(--blue); }
.act-txt { flex:1; font-size:13px; } .act-t { font-size:11px; color:var(--text3); }
.nav-btn { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 2px; padding: 8px 0; border: none; background: none; color: var(--text3); font-size: 10px; font-weight: 600; cursor: pointer; font-family: inherit; transition: color 0.2s; border-radius: 14px; min-height: 44px; }
.nav-btn svg { width: 20px; height: 20px; transition: transform 0.2s; }
.nav-btn.active { color: var(--blue); }
.nav-btn.active svg { transform: scale(1.1); }
.nav-btn:active { transform: scale(0.92); }
.nav-island { position: fixed; bottom: 0; left: 0; right: 0; z-index: 100; padding: 0 14px calc(10px + var(--safe-b)); pointer-events: none; transition: padding 0.35s cubic-bezier(0.34, 1.2, 0.64, 1); -webkit-overflow-scrolling: touch; }
.nav-island > * { pointer-events: auto; }
.island-glass { border-radius: 26px; overflow: hidden; background: rgba(255,255,255,0.06); backdrop-filter: blur(60px) saturate(200%); -webkit-backdrop-filter: blur(60px) saturate(200%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 4px 24px rgba(0,0,0,0.3), 0 1px 4px rgba(0,0,0,0.2); }
.nav-pill { display: flex; gap: 2px; padding: 4px; }
.nav-island.expanded { padding: 0 14px calc(10px + var(--safe-b)); }
.island-chat { padding: 0 10px; max-height: 0; overflow: hidden; transition: max-height 0.35s cubic-bezier(0.34, 1.2, 0.64, 1), padding 0.35s; }
.nav-island.expanded .island-chat { max-height: 120px; padding: 10px 10px 6px; }
.chips-scroll { display: flex; gap: 6px; overflow-x: auto; padding-bottom: 6px; scroll-snap-type: x mandatory; -webkit-overflow-scrolling: touch; }
.chips-scroll::-webkit-scrollbar { display: none; }
.chip { flex-shrink: 0; scroll-snap-align: start; padding: 5px 12px; border-radius: 16px; font-size: 12px; font-weight: 600; background: rgba(255,255,255,0.06); color: var(--text2); border: 0.5px solid rgba(255,255,255,0.1); cursor: pointer; font-family: inherit; white-space: nowrap; transition: all 0.15s; }
.chip:active { transform: scale(0.95); opacity: 0.7; }
.scene-chip { background: rgba(74,158,255,0.1); color: var(--blue); border-color: rgba(74,158,255,0.2); }
.input-row { display: flex; gap: 8px; align-items: flex-end; margin-top: 6px; }
.input-field { flex: 1; resize: none; border: none; outline: none; background: rgba(255,255,255,0.06); color: var(--text1); font-size: 14px; font-family: inherit; padding: 10px 14px; border-radius: 16px; max-height: 80px; line-height: 1.4; backdrop-filter: blur(16px) saturate(130%); -webkit-backdrop-filter: blur(16px) saturate(130%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06); }
.input-field::placeholder { color: var(--text3); }
.input-field.recording { border-color: rgba(248,113,113,0.3); box-shadow: 0 0 12px rgba(248,113,113,0.15), inset 0 1px 0 rgba(255,255,255,0.06); }
.voice-btn, .send-btn { width: 38px; height: 38px; border-radius: 50%; border: none; display: flex; align-items: center; justify-content: center; cursor: pointer; flex-shrink: 0; transition: transform 0.15s, opacity 0.15s; }
.voice-btn { background: rgba(255,255,255,0.06); color: var(--text2); backdrop-filter: blur(16px); -webkit-backdrop-filter: blur(16px); border: 0.5px solid rgba(255,255,255,0.1); }
.voice-btn svg { width: 18px; height: 18px; }
.voice-btn.recording { background: rgba(248,113,113,0.15); color: var(--red); border-color: rgba(248,113,113,0.3); animation: pulse 1.5s ease-in-out infinite; }
@keyframes pulse { 0%,100% { box-shadow: 0 0 0 0 rgba(248,113,113,0.3); } 50% { box-shadow: 0 0 0 8px rgba(248,113,113,0); } }
.send-btn { background: linear-gradient(135deg, #4a9eff, #6366f1); color: #fff; box-shadow: 0 2px 12px rgba(74,158,255,0.3), inset 0 1px 0 rgba(255,255,255,0.15); }
.send-btn svg { width: 16px; height: 16px; }
.send-btn:active, .voice-btn:active { transform: scale(0.9); }
.island-divider { height: 0.5px; background: rgba(255,255,255,0.08); margin: 0 10px; }

.perm-badge { display: inline-flex; align-items: center; gap: 4px; padding: 3px 10px; border-radius: 12px; font-size: 11px; font-weight: 600; background: rgba(74,158,255,0.1); color: var(--blue); border: 0.5px solid rgba(74,158,255,0.2); }

.login-page { position: fixed; inset: 0; z-index: 200; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 40px 24px; transition: opacity 0.4s ease, transform 0.4s ease; }
.login-page.hidden { opacity: 0; pointer-events: none; transform: scale(0.96); }
.login-logo { font-size: 56px; margin-bottom: 12px; animation: logoFloat 3s ease-in-out infinite; }
@keyframes logoFloat { 0%,100% { transform: translateY(0); } 50% { transform: translateY(-8px); } }
.login-title { font-size: 32px; font-weight: 700; letter-spacing: -0.5px; background: linear-gradient(135deg, #fff, #a0a0b8); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.login-sub { font-size: 13px; color: var(--text3); margin-top: 6px; margin-bottom: 32px; font-weight: 500; }
.login-form { width: 100%; max-width: 340px; }
.form-group { margin-bottom: 16px; }
.form-label { display: block; font-size: 12px; font-weight: 600; color: var(--text2); margin-bottom: 6px; text-transform: uppercase; letter-spacing: 0.3px; }
.form-input { width: 100%; padding: 14px 16px; border-radius: 14px; border: 0.5px solid rgba(255,255,255,0.1); background: rgba(255,255,255,0.06); color: var(--text1); font-size: 15px; font-family: inherit; outline: none; backdrop-filter: blur(16px) saturate(130%); -webkit-backdrop-filter: blur(16px) saturate(130%); box-shadow: inset 0 1px 0 rgba(255,255,255,0.06); transition: border-color 0.2s, box-shadow 0.2s; }
.form-input::placeholder { color: var(--text3); }
.form-input:focus { border-color: rgba(74,158,255,0.3); box-shadow: 0 0 12px rgba(74,158,255,0.1), inset 0 1px 0 rgba(255,255,255,0.06); }
.form-input.error { border-color: rgba(248,113,113,0.3); box-shadow: 0 0 12px rgba(248,113,113,0.1); }
.form-error { font-size: 11px; color: var(--red); margin-top: 4px; padding-left: 4px; max-height: 0; overflow: hidden; transition: max-height 0.2s, opacity 0.2s; opacity: 0; }
.form-error.show { max-height: 20px; opacity: 1; }
.role-selector { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; }
.role-option { padding: 12px; border-radius: 14px; background: rgba(255,255,255,0.04); border: 0.5px solid rgba(255,255,255,0.08); cursor: pointer; text-align: center; transition: all 0.2s; }
.role-option.selected { background: rgba(74,158,255,0.1); border-color: rgba(74,158,255,0.2); box-shadow: 0 2px 8px rgba(74,158,255,0.15); }
.role-icon { font-size: 24px; margin-bottom: 4px; }
.role-name { font-size: 13px; font-weight: 600; }
.role-desc { font-size: 10px; color: var(--text3); margin-top: 2px; }
.login-btn { width: 100%; padding: 14px; border-radius: 14px; border: none; background: linear-gradient(135deg, #4a9eff, #6366f1); color: #fff; font-size: 15px; font-weight: 600; cursor: pointer; font-family: inherit; box-shadow: 0 2px 12px rgba(74,158,255,0.3), inset 0 1px 0 rgba(255,255,255,0.15); transition: transform 0.15s, opacity 0.15s; margin-top: 8px; }
.login-btn:active { transform: scale(0.97); opacity: 0.9; }
.login-switch { text-align: center; margin-top: 16px; font-size: 13px; color: var(--text3); }
.login-switch a { color: var(--blue); cursor: pointer; font-weight: 600; text-decoration: none; }

.modal-overlay { position: fixed; inset: 0; z-index: 150; background: rgba(0,0,0,0.5); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); display: flex; align-items: flex-end; justify-content: center; opacity: 0; pointer-events: none; transition: opacity 0.25s; }
.modal-overlay.show { opacity: 1; pointer-events: auto; }
.modal-sheet { width: 100%; max-width: 520px; max-height: 85vh; overflow-y: auto; background: rgba(20,20,40,0.95); backdrop-filter: blur(60px) saturate(200%); -webkit-backdrop-filter: blur(60px) saturate(200%); border-radius: 24px 24px 0 0; padding: 16px 20px calc(20px + var(--safe-b)); border: 0.5px solid rgba(255,255,255,0.1); border-bottom: none; transform: translateY(100%); transition: transform 0.35s cubic-bezier(0.34, 1.2, 0.64, 1); }
.modal-overlay.show .modal-sheet { transform: translateY(0); }
.modal-handle { width: 36px; height: 4px; border-radius: 2px; background: rgba(255,255,255,0.2); margin: 0 auto 14px; }
.modal-title { font-size: 18px; font-weight: 700; margin-bottom: 16px; }

.profile-header { text-align: center; padding: 24px 20px 16px; }
.profile-avatar { width: 64px; height: 64px; border-radius: 50%; background: linear-gradient(135deg, #4a9eff, #c084fc); display: flex; align-items: center; justify-content: center; font-size: 24px; font-weight: 700; margin: 0 auto 10px; position: relative; cursor: pointer; box-shadow: 0 4px 16px rgba(74,158,255,0.3); }
.profile-avatar-edit { position: absolute; bottom: -2px; right: -2px; width: 22px; height: 22px; border-radius: 50%; background: var(--bg); border: 2px solid rgba(255,255,255,0.15); display: flex; align-items: center; justify-content: center; font-size: 10px; }
.profile-name { font-size: 20px; font-weight: 700; }
.profile-role { font-size: 13px; color: var(--text2); margin-top: 4px; }
.profile-section { padding: 0 20px; margin-top: 16px; }
.profile-section-title { font-size: 12px; font-weight: 600; color: var(--text3); text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; padding-left: 4px; }
.profile-card { border-radius: 16px; background: rgba(255,255,255,0.04); backdrop-filter: blur(16px) saturate(130%); -webkit-backdrop-filter: blur(16px) saturate(130%); border: 0.5px solid rgba(255,255,255,0.08); overflow: hidden; }
.profile-item { display: flex; align-items: center; gap: 12px; padding: 14px 16px; cursor: pointer; transition: background 0.15s; border-bottom: 0.5px solid rgba(255,255,255,0.04); }
.profile-item:last-child { border-bottom: none; }
.profile-item:active { background: rgba(255,255,255,0.04); }
.profile-item-icon { width: 32px; height: 32px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0; }
.profile-item-icon.blue { background: rgba(74,158,255,0.12); }
.profile-item-icon.green { background: rgba(74,222,128,0.12); }
.profile-item-icon.purple { background: rgba(192,132,252,0.12); }
.profile-item-icon.orange { background: rgba(251,191,36,0.12); }
.profile-item-icon.cyan { background: rgba(34,211,238,0.12); }
.profile-item-icon.red { background: rgba(248,113,113,0.12); }
.profile-item-text { flex: 1; }
.profile-item-label { font-size: 14px; font-weight: 600; }
.profile-item-desc { font-size: 12px; color: var(--text3); margin-top: 1px; }
.profile-item-arrow { font-size: 18px; color: var(--text3); }
.logout-btn { display: block; width: calc(100% - 40px); margin: 24px auto; padding: 14px; border-radius: 14px; border: 0.5px solid rgba(248,113,113,0.2); background: rgba(248,113,113,0.08); color: var(--red); font-size: 15px; font-weight: 600; cursor: pointer; font-family: inherit; transition: transform 0.15s, opacity 0.15s; }
.logout-btn:active { transform: scale(0.97); opacity: 0.8; }

.toast { position: fixed; top: 20px; left: 50%; transform: translateX(-50%) translateY(-100px); z-index: 300; padding: 12px 20px; border-radius: 16px; background: rgba(20,20,40,0.9); backdrop-filter: blur(40px) saturate(180%); -webkit-backdrop-filter: blur(40px) saturate(180%); border: 0.5px solid rgba(255,255,255,0.1); box-shadow: 0 4px 24px rgba(0,0,0,0.3); min-width: 200px; text-align: center; transition: transform 0.35s cubic-bezier(0.34, 1.2, 0.64, 1); pointer-events: none; }
.toast.show { transform: translateX(-50%) translateY(0); }
.toast-t { font-size: 14px; font-weight: 700; }
.toast-d { font-size: 12px; color: var(--text2); margin-top: 2px; }

.security-modal { position: fixed; inset: 0; z-index: 250; background: rgba(0,0,0,0.6); backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px); display: flex; align-items: center; justify-content: center; padding: 24px; opacity: 0; pointer-events: none; transition: opacity 0.25s; }
.security-modal.show { opacity: 1; pointer-events: auto; }
.security-content { width: 100%; max-width: 340px; background: rgba(20,20,40,0.95); backdrop-filter: blur(60px) saturate(200%); -webkit-backdrop-filter: blur(60px) saturate(200%); border-radius: 24px; padding: 24px; border: 0.5px solid rgba(255,255,255,0.1); box-shadow: 0 8px 32px rgba(0,0,0,0.4); transform: scale(0.9); transition: transform 0.3s cubic-bezier(0.34, 1.2, 0.64, 1); }
.security-modal.show .security-content { transform: scale(1); }
.security-icon { font-size: 36px; text-align: center; margin-bottom: 8px; }
.security-title { font-size: 18px; font-weight: 700; text-align: center; margin-bottom: 4px; }
.security-sub { font-size: 12px; color: var(--text3); text-align: center; margin-bottom: 16px; }
.security-check { display: flex; align-items: center; gap: 10px; padding: 10px 0; border-bottom: 0.5px solid rgba(255,255,255,0.06); }
.security-check:last-of-type { border-bottom: none; }
.security-check-icon { font-size: 18px; }
.security-check-text { flex: 1; }
.security-check-label { font-size: 13px; font-weight: 600; }
.security-check-detail { font-size: 11px; color: var(--text3); margin-top: 1px; }
.security-btn { width: 100%; padding: 14px; border-radius: 14px; border: none; background: linear-gradient(135deg, #4ade80, #22c55e); color: #fff; font-size: 15px; font-weight: 600; cursor: pointer; font-family: inherit; margin-top: 16px; box-shadow: 0 2px 12px rgba(74,222,128,0.3); transition: transform 0.15s, opacity 0.15s; }
.security-btn:active { transform: scale(0.97); }
.security-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.plan-card { background: rgba(255,255,255,0.06); backdrop-filter: blur(24px) saturate(150%); -webkit-backdrop-filter: blur(24px) saturate(150%); border: 0.5px solid rgba(255,255,255,0.1); border-radius: 16px; padding: 14px; box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 2px 8px rgba(0,0,0,0.15); }
.plan-header { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.plan-header-icon { width: 36px; height: 36px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
.plan-header-text { flex: 1; }
.plan-title { font-size: 15px; font-weight: 700; }
.plan-date { font-size: 11px; color: var(--text3); margin-top: 1px; }
.plan-section { margin-top: 10px; padding-top: 10px; border-top: 0.5px solid rgba(255,255,255,0.06); }
.plan-section-title { font-size: 12px; font-weight: 700; color: var(--text2); text-transform: uppercase; letter-spacing: 0.3px; margin-bottom: 8px; }
.plan-time-grid { display: flex; flex-direction: column; gap: 6px; }
.plan-time-item { display: flex; align-items: center; gap: 8px; padding: 8px 10px; border-radius: 10px; background: rgba(255,255,255,0.04); border: 0.5px solid rgba(255,255,255,0.06); }
.plan-time-badge { min-width: 44px; padding: 4px 8px; border-radius: 8px; font-size: 12px; font-weight: 700; text-align: center; background: rgba(255,255,255,0.06); border: 0.5px solid rgba(255,255,255,0.1); }
.plan-time-task { flex: 1; font-size: 12px; color: var(--text2); }
.plan-vehicle-list { display: flex; flex-direction: column; gap: 4px; }
.plan-vehicle-item { display: flex; align-items: center; gap: 8px; padding: 8px 10px; border-radius: 10px; background: rgba(255,255,255,0.04); border: 0.5px solid rgba(255,255,255,0.06); }
.plan-vehicle-id { font-size: 13px; font-weight: 600; min-width: 48px; }
.plan-vehicle-detail { flex: 1; font-size: 11px; color: var(--text2); }
.plan-vehicle-batt { font-size: 12px; font-weight: 700; min-width: 36px; text-align: right; }
.plan-alert-box { display: flex; align-items: flex-start; gap: 8px; padding: 10px 12px; border-radius: 12px; margin-top: 8px; font-size: 12px; line-height: 1.5; }
.plan-alert-box.ok { background: rgba(74,222,128,0.08); border: 0.5px solid rgba(74,222,128,0.15); }
.plan-alert-box.warn { background: rgba(251,191,36,0.08); border: 0.5px solid rgba(251,191,36,0.15); }
.plan-alert-icon { font-size: 14px; flex-shrink: 0; margin-top: 1px; }
.plan-alert-text { flex: 1; color: var(--text2); }
.plan-tag-list { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 10px; }
.plan-tag { padding: 4px 10px; border-radius: 8px; font-size: 11px; font-weight: 600; }
.plan-tag.blue { background: rgba(74,158,255,0.1); color: var(--blue); }
.plan-tag.orange { background: rgba(251,191,36,0.1); color: var(--orange); }
.plan-tag.green { background: rgba(74,222,128,0.1); color: var(--green); }

.info-card { background: rgba(255,255,255,0.06); backdrop-filter: blur(24px) saturate(150%); -webkit-backdrop-filter: blur(24px) saturate(150%); border: 0.5px solid rgba(255,255,255,0.1); border-radius: 14px; padding: 12px; margin-top: 8px; box-shadow: inset 0 1px 0 rgba(255,255,255,0.06), 0 2px 8px rgba(0,0,0,0.15); }
.info-card.success { border-color: rgba(74,222,128,0.2); }
.info-card.danger { border-color: rgba(248,113,113,0.2); }
.info-card.highlight { border-color: rgba(251,191,36,0.2); }
.info-card.purple { border-color: rgba(192,132,252,0.2); }
.info-card-title { font-size: 13px; font-weight: 700; margin-bottom: 8px; }
.info-card-body { display: flex; flex-direction: column; gap: 6px; }
.info-row { display: flex; align-items: center; justify-content: space-between; gap: 8px; padding: 6px 0; }
.info-label { font-size: 12px; color: var(--text3); }
.info-value { font-size: 12px; font-weight: 600; }

.stat-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; }
.stat-item { text-align: center; padding: 10px; border-radius: 12px; background: rgba(255,255,255,0.04); border: 0.5px solid rgba(255,255,255,0.06); }
.stat-num { font-size: 20px; font-weight: 700; }
.stat-lbl { font-size: 11px; color: var(--text3); margin-top: 2px; }

.tag-list { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 8px; }
.tag-item { padding: 4px 10px; border-radius: 8px; font-size: 11px; font-weight: 600; background: rgba(255,255,255,0.06); border: 0.5px solid rgba(255,255,255,0.1); }
.tag-blue { background: rgba(74,158,255,0.1); color: var(--blue); border-color: rgba(74,158,255,0.2); }
.tag-green { background: rgba(74,222,128,0.1); color: var(--green); border-color: rgba(74,222,128,0.2); }
.tag-red { background: rgba(248,113,113,0.1); color: var(--red); border-color: rgba(248,113,113,0.2); }
.tag-orange { background: rgba(251,191,36,0.1); color: var(--orange); border-color: rgba(251,191,36,0.2); }

.notice-box { display: flex; align-items: flex-start; gap: 8px; padding: 10px 12px; border-radius: 12px; margin-top: 8px; font-size: 12px; line-height: 1.5; }
.notice-box.tip { background: rgba(74,158,255,0.08); border: 0.5px solid rgba(74,158,255,0.15); }
.notice-box.warn { background: rgba(251,191,36,0.08); border: 0.5px solid rgba(251,191,36,0.15); }
.notice-box.error { background: rgba(248,113,113,0.08); border: 0.5px solid rgba(248,113,113,0.15); }
.notice-icon { font-size: 14px; flex-shrink: 0; margin-top: 1px; }
.notice-text { flex: 1; color: var(--text2); }

.progress-bar { width: 100%; height: 4px; background: rgba(255,255,255,0.1); border-radius: 2px; overflow: hidden; margin-top: 4px; }
.progress-fill { height: 100%; border-radius: 2px; transition: width 0.3s ease; }

.batch-panel { background: rgba(255,255,255,0.06); backdrop-filter: blur(24px) saturate(150%); -webkit-backdrop-filter: blur(24px) saturate(150%); border: 0.5px solid rgba(255,255,255,0.1); border-radius: 14px; padding: 12px; margin-top: 8px; }
.batch-panel-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; }
.batch-panel-title { font-size: 13px; font-weight: 700; }
.batch-count { font-size: 11px; color: var(--text3); padding: 2px 8px; border-radius: 6px; background: rgba(255,255,255,0.06); }
.batch-vehicles { display: flex; flex-wrap: wrap; gap: 4px; margin-bottom: 10px; }
.batch-vehicle-tag { padding: 3px 8px; border-radius: 6px; font-size: 11px; font-weight: 600; background: rgba(74,158,255,0.1); color: var(--blue); border: 0.5px solid rgba(74,158,255,0.2); }
.batch-actions { display: flex; gap: 6px; }
.batch-btn { flex: 1; padding: 8px; border-radius: 10px; border: none; font-size: 12px; font-weight: 600; cursor: pointer; font-family: inherit; transition: transform 0.15s; }
.batch-btn:active { transform: scale(0.95); }
.batch-btn-charge { background: rgba(74,222,128,0.12); color: var(--green); border: 0.5px solid rgba(74,222,128,0.2); }
.batch-btn-dispatch { background: rgba(74,158,255,0.12); color: var(--blue); border: 0.5px solid rgba(74,158,255,0.2); }
.batch-btn-repair { background: rgba(251,191,36,0.12); color: var(--orange); border: 0.5px solid rgba(251,191,36,0.2); }

@media (orientation: landscape) and (max-height: 500px) {
  .app-header { padding-top: 6px; padding-bottom: 6px; }
  .header-metrics { display: none; }
  .profile-header { padding: 12px 20px 8px; }
  .profile-avatar { width: 48px; height: 48px; font-size: 18px; }
  .nav-island { padding-bottom: calc(4px + var(--safe-b)); }
  .nav-btn { padding: 6px 0; min-height: 36px; }
  .page { padding-bottom: calc(56px + var(--safe-b)); }
  .page.chat-active { padding-bottom: calc(140px + var(--safe-b)); }
}
@media (min-width: 768px) {
  .main-area { padding: 0 24px; }
  .page { padding-left: 24px; padding-right: 24px; }
}
@media (min-width: 1024px) {
  .main-area { padding: 0 48px; }
  .page { padding-left: 48px; padding-right: 48px; }
}
@media (min-width: 1200px) {
  .main-area { max-width: 680px; margin-left: auto; margin-right: auto; padding: 0; }
}
@media (max-height: 400px) {
  .app-header { padding-top: 4px; padding-bottom: 4px; }
  .header-metrics { display: none; }
  .brand-name { font-size: 15px; }
  .brand-icon { width: 28px; height: 28px; font-size: 14px; }
  .nav-island { padding: 0 14px calc(4px + var(--safe-b)); }
  .nav-btn { padding: 4px 0; min-height: 32px; font-size: 9px; }
  .nav-btn svg { width: 16px; height: 16px; }
  .island-glass { border-radius: 20px; }
  .page { padding-bottom: calc(48px + var(--safe-b)); }
  .page.chat-active { padding-bottom: calc(120px + var(--safe-b)); }
}
@supports (height: 100dvh) {
  html, body, #app, .mobile-app { height: 100dvh; }
}
</style>