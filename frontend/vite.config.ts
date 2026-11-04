import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    host: true,
    port: 5173,
    watch: {
      usePolling: true,
    },
    proxy: {
      '/api/voice': {
        target: 'http://voice-service:8000',
        changeOrigin: true,
      },
      '/api/summarize': {
        target: 'http://summary-service:8080',
        changeOrigin: true,
      }
    }
  }
})