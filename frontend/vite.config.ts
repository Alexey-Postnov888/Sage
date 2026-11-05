import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    host: true,
    port: 5173,
    watch: { usePolling: true },
    proxy: {
      '/api/notes': {
        target: 'http://content-service:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/project': {
        target: 'http://content-service:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // ДОБАВЛЯЕМ ЭТОТ БЛОК:
      '/api/files': {
        target: 'http://content-service:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/workspace': {
        target: 'http://content-service:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      '/api/voice': {
        target: 'http://voice-service:8000',
        changeOrigin: true,
      },
      '/api/summarize': {
        target: 'http://summary-service:8080',
        changeOrigin: true,
      },
      '/api/ocr': {
        target: 'http://ocr-service:8080',
        changeOrigin: true,
      },
    }
  }
})