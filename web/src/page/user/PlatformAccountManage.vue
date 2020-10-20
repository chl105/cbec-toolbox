<template>
  <div class="main">
    <div class="search-bar">
      <Input
        v-model="searchKey"
        placeholder="请输入关键字搜索"
        style="width: 300px"
        clearable
        @keyup.enter.native="search"
      />
      <Button icon="ios-search" style="margin-left: 10px" @click="search"
        >搜索</Button
      >
    </div>
    <div class="list-content">
      <Table
        border
        :columns="columns"
        :data="data"
        :loading="loading"
        style="margin-top: 10px"
      >
        <template slot-scope="{ row }" slot="id">
          <span>{{ row.id }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="platformPassword">
          <Input
            type="text"
            v-model="editPassword"
            v-if="editIndex === index"
          />
          <span v-else>{{ row.platformPassword }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="operation">
          <div v-if="editIndex === index">
            <Button @click="handleSave(row, index)" :loading="saving"
              >保存</Button
            >
            <Button @click="editIndex = -1" style="margin-left: 10px"
              >取消</Button
            >
          </div>
          <div v-else>
            <Button @click="handleEdit(row, index)">操作</Button>
            <Button @click="showReset(row, index)" style="margin-left: 10px"
              >删除</Button
            >
          </div>
        </template>
      </Table>
      <Modal v-model="isShowReset" title="删除" @on-ok="deletePlatformAccount">
        <p>确定删除【{{ deleteUserName }}】账户！</p>
      </Modal>
    </div>
    <div class="page-bar">
      <Page
        :total="total"
        style="margin-top: 10px; float: right"
        @on-change="changePage"
        :page-size="pageSize"
      />
    </div>
  </div>
</template>
<script>
import { getList, put } from "@/http/index";

function users(params, that) {
  getList(
    "/api/auth/platform_account",
    params,
    (total, data) => {
      that.loading = false;
      that.data = data;
      that.total = total;
    },
    () => {
      that.loading = false;
      that.data = [];
      that.total = 0;
    }
  );
}

function initParams(that) {
  let params = {};
  if (that.searchKey) {
    params.keyword = that.searchKey;
  }

  return params;
}

export default {
  data() {
    return {
      resetIndex: 0,
      deleteUserName: "",
      isShowReset: false,
      saving: false,
      reseting: false,
      editIndex: -1,
      searchKey: "",
      pageSize: 12,
      total: 0,
      loading: false,
      data: [],
      columns: [
        {
          type: "index",
          width: 60,
          align: "center",
        },
        {
          title: "ID",
          slot: "id",
          width: 120,
        },
        {
          title: "平台名称",
          key: "platform",
          width: 150,
        },
        {
          title: "平台用户名",
          key: "platformUser",
          width: 200,
        },
        {
          title: "平台密码",
          slot: "platformPassword",
          minWidth: 200,
        },
        {
          title: "编辑",
          slot: "operation",
          width: 240,
        },
      ],
    };
  },
  mounted() {
    let params = initParams(this);
    users(params, this);
  },
  methods: {
    changePage(page) {
      let params = initParams(this);
      params.start = (page - 1) * this.pageSize;
      params.limit = this.pageSize;
      users(params, this);
    },
    search() {
      let params = initParams(this);
      params.start = 0;
      params.limit = this.pageSize;
      users(params, this);
    },
    handleSave(row, index) {
      if (this.edtName != row.name || this.edtPhone != row.phone) {
        this.save(index, this);
      } else {
        this.editIndex = -1;
        this.saving = false;
      }
    },
    handleEdit(row, index) {
      this.editIndex = index;
      this.edtName = row.name;
      this.edtPhone = row.phone;
    },
    showReset(row, index) {
      this.resetIndex = index;
      this.deleteUserName = row.name;
      this.isShowReset = true;
    },
    save(index) {
      this.saving = true;
      let row = this.data[index];
      let id = row.id;
      let params = new URLSearchParams();
      params.append("phone", this.edtPhone);
      params.append("name", this.edtName);
      put(
        `/api/auth/admin/users/${id}`,
        params,
        () => {
          this.editIndex = -1;
          this.saving = false;
          row.name = this.edtName;
          row.phone = this.edtPhone;
        },
        () => {
          this.saving = false;
        }
      );
    },
  },
};
</script>
<style lang='less' scoped>
.search-bar {
  display: inline;
}

.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  .list-content {
    flex: 1;
    overflow: auto;
  }
}
</style>
