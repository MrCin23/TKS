import { defineConfig } from 'vite';
import react from "@vitejs/plugin-react"
import mkcert from "vite-plugin-mkcert"


export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081/REST/api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    sourcemap: true,
  },
  plugins: [
      react(),mkcert()
  ]
})