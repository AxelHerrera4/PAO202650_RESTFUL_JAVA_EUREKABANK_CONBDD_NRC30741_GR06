import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/eureka-api': {
        target: 'http://localhost:8080/ws_EUREKABANK_Java_Restful_GR6',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/eureka-api/, ''),
      },
    },
  },
})
