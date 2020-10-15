<template>
  <div class="app">
    <div class="header">
      <div class="title">外贸工具箱</div>
      <div class="user">
        <Icon type="md-person" color="#fff" size="24" />
        {{ this.$store.state.user }}
        <Icon type="ios-exit-outline" color="#fff" size="24" @click="logout" />
      </div>
    </div>
    <div class="main">
      <div class="sider">
        <Sider>
          <Menu :active-name="$route.meta.key" theme="dark" width="auto">
            <MenuItem
              v-for="(item, index) in menus"
              :key="index"
              :name="item.index"
              :to="item.path"
              >{{ item.name }}</MenuItem
            >
          </Menu>
        </Sider>
      </div>
      <div class="content-wrapper">
        <div class="breadcrumb">
          <Breadcrumb :style="{ margin: '10px 0' }">
            <BreadcrumbItem
              v-for="item in crumbs"
              :key="item.index"
              :to="item.path"
              >{{ item.name }}</BreadcrumbItem
            >
          </Breadcrumb>
        </div>
        <div class="content">
          <router-view></router-view>
        </div>
        <div class="footer">
          <Footer class="layout-footer-center">2011-2020 &copy; trade</Footer>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
function name(key) {
  switch (key) {
    case "cost-calculator":
      return "成本计算";
    case "logistics":
      return "物流查询";
    case "select-goods":
      return "自动选品";
    case "address-manage":
      return "地址管理";
    default:
      return;
  }
}

function crumbs(path) {
  let regex = /\/admin\/([^/]+)[/]*([^/]+)*/;
  let res = path.match(regex);
  if (res) {
    let k1 = res[1];
    let c1 = {
      name: name(k1),
      index: k1,
      path: `/trade/${k1}`,
    };
    let arr = [c1];
    let k2 = res[2];
    if (k2) {
      let c2 = {
        name: name(k2),
        index: k2,
        path: path,
      };
      arr.push(c2);
    }

    return arr;
  }
  return [];
}

export default {
  data() {
    return {
      menus: [
        {
          name: "成本预估",
          index: "cost-calculator",
          path: "/trade/cost-calculator",
        },
        {
          name: "自动选品",
          index: "select-goods",
          path: "/trade/select-goods",
        },
        // {
        //   name: "地址管理",
        //   index: "address-manage",
        //   path: "/trade/address-manage"
        // }
      ],
      crumbs: crumbs(this.$route.path),
    };
  },
  watch: {
    $route(cur) {
      this.crumbs = crumbs(cur.path);
    },
  },
  methods: {
    logout() {
      this.$store.commit("setToken", "");
      this.$router.replace("/login");
    },
  },
};
</script>

<style lang="less">
.app {
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
}
.header {
  height: 60px;
  background-color: #17233d;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  .title {
    height: 60px;
    display: flex;
    padding-left: 20px;
    align-items: center;
    color: #fff;
    font-size: 20px;
  }
  .user {
    height: 30px;
    line-height: 60px;
    margin-right: 50px;
    color: #fff;
    font-size: 16px;
  }
}
.main {
  flex: 1;
  width: 100%;
  height: 100%;
  overflow: auto;
  display: inline-flex;
  flex-direction: row;
}
.sider {
  height: 100%;
  background: #515a6e;
}
.content-wrapper {
  flex: 1;
  width: 100%;
  height: 100%;
  overflow: auto;
  padding: 10px;
  background: #f5f7f9;
  display: flex;
  flex-direction: column;
  padding: "0 24px 10px";
}
.breadcrumb {
  width: 100%;
}
.content {
  flex: 1;
  width: 100%;
  height: 100%;
  overflow: auto;
  padding: 24px;
  background: white;
}
.footer {
  height: 60px;
  width: 100%;
}
.layout-footer-center {
  width: 100%;
  text-align: center;
}
</style>
