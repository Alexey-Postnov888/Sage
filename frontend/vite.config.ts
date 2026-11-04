import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/api/voice': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/api/summarize': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})