const path = require('path')
const resolve = dir => path.join(__dirname, dir)

module.exports = {
  runtimeCompiler: true,
  chainWebpack: config => {
    config.resolve.alias
      .set('@', resolve('src'))
  },
  devServer: {
    proxy: {
      '/admin': {
        // target: 'http://band.gosuncs.com:7756',
        target: 'http://127.0.0.1:28888',
        ws: true,
        changeOrigin: true
      },
      '/': {
        // target: 'http://band.gosuncs.com:7756',
        target: 'http://127.0.0.1:28888',
        ws: true,
        changeOrigin: true
      }
    }
  }
}