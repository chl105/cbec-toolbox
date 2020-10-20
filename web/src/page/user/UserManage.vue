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
        <template slot-scope="{ row, index }" slot="phone">
          <Input type="text" v-model="edtPhone" v-if="editIndex === index" />
          <span v-else>{{ row.phone }}</span>
        </template>
        <template slot-scope="{ row }" slot="name">
          <span>{{ row.name }}</span>
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
              >重置密码</Button
            >
          </div>
        </template>
      </Table>
      <Modal v-model="isShowReset" title="重置密码" @on-ok="resetPassword">
        <p>确定重置【{{ resetUserName }}】的密码！</p>
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
import { getList, put, post } from "@/http/index";

function users(params, that) {
  getList(
    "/api/auth/admin/users",
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

function save(index, that) {
  that.saving = true;
  let row = that.data[index];
  let id = row.id;
  let params = new URLSearchParams();
  params.append("phone", that.edtPhone);
  params.append("name", that.edtName);
  put(
    `/api/auth/admin/users/${id}`,
    params,
    () => {
      that.editIndex = -1;
      that.saving = false;
      row.name = that.edtName;
      row.phone = that.edtPhone;
    },
    () => {
      that.saving = false;
    }
  );
}

function resetPassword(that) {
  that.reseting = true;
  let row = that.data[that.resetIndex];
  let id = row.id;
  post(
    `/api/auth/admin/users/${id}/reset_password`,
    null,
    () => {
      that.reseting = false;
    },
    () => {
      that.reseting = false;
    }
  );
}

export default {
  data() {
    return {
      resetIndex: 0,
      resetUserName: "",
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
          key: "id",
          width: 120,
        },
        {
          title: "用户名",
          slot: "name",
          width: 180,
        },
        {
          title: "手机号",
          slot: "phone",
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
        save(index, this);
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
      this.resetUserName = row.name;
      this.isShowReset = true;
    },
    resetPassword() {
      resetPassword(this);
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
