const path = require("path");
const resolve = (dir) => path.join(__dirname, dir);

module.exports = {
  runtimeCompiler: true,
  chainWebpack: (config) => {
    config.resolve.alias.set("@", resolve("src"));
  },
  devServer: {
    proxy: {
      "/admin": {
        // target: 'http://band.gosuncs.com:7756',
        target: "http://localhost:28888",
        ws: true,
        changeOrigin: true,
      },
      "/": {
        // target: 'http://band.gosuncs.com:7756',
        target: "http://localhost:28888",
        ws: true,
        changeOrigin: true,
      },
    },
  },
};
