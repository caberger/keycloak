import { defineConfig } from 'vite'

export default defineConfig({
    server: {
      port: 4200,
      open: "index.html",
      proxy: {
        '/api': {
          target: "http://localhost:8080",
          changeOrigin: true,
        },
        '/auth': {
          target: "http://localhost:8000",
          changeOrigin: true,
        },
      },
    },
  })