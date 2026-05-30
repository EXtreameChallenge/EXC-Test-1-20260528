import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const GLM_API_KEY = env.VITE_GLM_API_KEY || ''
  const GLM_API_TARGET = 'https://open.bigmodel.cn/api/paas/v4'

  return {
    plugins: [vue()],
    server: {
      port: 3001,
      proxy: {
        '/api/v1': {
          target: 'http://localhost:8080',
          changeOrigin: true
        },
        '/api/chat': {
          target: GLM_API_TARGET,
          changeOrigin: true,
          secure: false,
          rewrite: (path) => path.replace(/^\/api\/chat/, '/chat/completions'),
          headers: {
            'Authorization': `Bearer ${GLM_API_KEY}`,
            'Content-Type': 'application/json'
          },
          configure: (proxy) => {
            proxy.on('error', (err, req, res) => {
              console.error('[Proxy Error]', err.message)
              if (!res.headersSent) {
                res.writeHead(502, { 'Content-Type': 'application/json' })
                res.end(JSON.stringify({ error: '代理连接失败: ' + err.message }))
              }
            })
          }
        },
      '/api/health': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/api/vehicles': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/api/analytics': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/api/energy': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
        '/api/dispatch': {
          target: 'http://localhost:8080',
          changeOrigin: true
        },
        '/ws': {
          target: 'http://localhost:8080',
          ws: true,
          changeOrigin: true
        }
      }
    }
  }
})
