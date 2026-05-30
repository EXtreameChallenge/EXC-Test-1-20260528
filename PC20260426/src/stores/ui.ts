import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUiStore = defineStore('ui', () => {
  const pageTitle = ref('轻行Claw')
  const sidebarCollapsed = ref(false)
  const loading = ref(false)

  function setPageTitle(title) {
    pageTitle.value = title
    document.title = `${title} - 轻行Claw`
  }

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function setLoading(val) {
    loading.value = val
  }

  return {
    pageTitle,
    sidebarCollapsed,
    loading,
    setPageTitle,
    toggleSidebar,
    setLoading
  }
})
