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
      <Button
        type="primary"
        icon="ios-search"
        style="margin-left: 10px"
        @click="search"
        >搜索</Button
      >
      <Button
        type="primary"
        icon="ios-add"
        style="margin-left: 10px; float: right; margin-right: 10px"
        @click="showAddDialog(true)"
        >新增</Button
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
            v-model="editPlatformPassword"
            v-if="editIndex === index"
          />
          <span v-else>{{ row.platformPassword }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="operation">
          <div v-if="editIndex === index">
            <Button
              type="primary"
              @click="handleUpdate(row, index)"
              :loading="saving"
              >保存</Button
            >
            <Button @click="editIndex = -1" style="margin-left: 10px"
              >取消</Button
            >
          </div>
          <div v-else>
            <Button type="primary" @click="handleEdit(row, index)">操作</Button>
            <Button
              type="error"
              @click="showDeleteDialog(true, row, index)"
              style="margin-left: 10px"
              >删除</Button
            >
          </div>
        </template>
      </Table>

      <Modal v-model="isshowDeleteDialog" title="删除" @on-ok="handleDelete">
        <p>确定删除【{{ deletePlatformUser }}】账户！</p>
      </Modal>

      <Modal
        title="新增"
        v-model="isShowAdd"
        :footer-hide="true"
        :mask-closable="false"
        @on-visible-change="handleResetInput(formValidate)"
      >
        <Form
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
          :label-width="80"
        >
          <FormItem label="平台用户名" prop="platform">
            <Select v-model="formValidate.platform">
              <Option
                :value="item.name"
                v-for="item in platformList"
                v-bind:key="item.name"
                >{{ item.name }}</Option
              >
            </Select>
          </FormItem>

          <FormItem label="平台用户名" prop="platformUser">
            <Input
              v-model="formValidate.platformUser"
              placeholder="请输入平台用户名"
            ></Input>
          </FormItem>

          <FormItem label="平台密码" prop="platformPassword">
            <Input
              v-model="formValidate.platformPassword"
              placeholder="请输入平台密码"
            ></Input>
          </FormItem>

          <FormItem>
            <Button type="primary" @click="handleAdd(formValidate)"
              >提交</Button
            >
            <Button
              @click="handleResetInput(formValidate)"
              style="margin-left: 8px"
              >重置</Button
            >
          </FormItem>
        </Form>
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
import { getList, put, post, del } from "@/http/index";

export default {
  data() {
    return {
      deleteIndex: 0,
      deletePlatformUser: "",
      isshowDeleteDialog: false,
      isShowAdd: false,
      saving: false,
      reseting: false,
      editIndex: -1,
      searchKey: "",
      pageSize: 12,
      total: 0,
      loading: false,
      data: [],
      platformList: [
        {
          name: "vova",
        },
      ],
      formValidate: {
        platform: "",
        platformUser: "",
        platformPassword: "",
      },
      ruleValidate: {
        platform: [
          { required: true, message: " ", trigger: "blur", type: "string" },
        ],
        platformUser: [
          { required: true, message: " ", trigger: "blur", type: "string" },
        ],
        platformPassword: [
          { required: true, message: " ", trigger: "blur", type: "string" },
        ],
      },
      columns: [
        {
          type: "index",
          width: 60,
          align: "center",
        },
        {
          title: "ID",
          key: "id",
        },
        {
          title: "平台名称",
          key: "platform",
        },
        {
          title: "平台用户名",
          key: "platformUser",
        },
        {
          title: "平台密码",
          slot: "platformPassword",
        },
        {
          title: "操作",
          slot: "operation",
          width: 170,
        },
      ],
    };
  },
  mounted() {
    this.reset();
  },
  methods: {
    reset() {
      let params = this.initParams();
      params.pageNo = 1;
      params.pageSize = this.pageSize;
      this.listPlatformAccount(params);
    },
    listPlatformAccount(params) {
      getList(
        "/api/auth/platform_account",
        params,
        (total, data) => {
          this.loading = false;
          this.data = data;
          this.total = total;
        },
        () => {
          this.loading = false;
          this.data = [];
          this.total = 0;
        }
      );
    },
    initParams() {
      let params = {};
      if (this.searchKey) {
        this.keyword = this.searchKey;
      }
      return params;
    },
    changePage(page) {
      let params = this.initParams();
      params.pageNo = page;
      params.pageSize = this.pageSize;
      this.listPlatformAccount(params, this);
    },
    search() {
      let params = this.initParams();
      params.pageNo = 1;
      params.pageSize = this.pageSize;
      this.listPlatformAccount(params);
    },
    showAddDialog(show) {
      this.isShowAdd = show;
    },
    showDeleteDialog(show, row, index) {
      this.deleteIndex = index;
      this.deletePlatformId = row.id;
      this.deletePlatformUser = row.platformUser;
      this.isshowDeleteDialog = show;
    },
    handleAdd(data) {
      let params = {};
      params.platform = data.platform;
      params.platformUser = data.platformUser;
      params.platformPassword = data.platformPassword;
      post(
        "/api/auth/platform_account",
        params,
        () => {
          this.reset();
        },
        () => {
          this.reset();
        }
      );
      this.showAddDialog(false);
    },
    handleUpdate(row, index) {
      if (this.editPlatformPassword != row.platformPassword) {
        this.saving = true;
        let row = this.data[index];
        let params = {};
        params.id = row.id;
        params.platformPassword = this.editPlatformPassword;
        put(
          `/api/auth/platform_account`,
          params,
          () => {
            this.editIndex = -1;
            this.saving = false;
            row.platformPassword = this.editPlatformPassword;
          },
          () => {
            this.saving = false;
          }
        );
      } else {
        this.editIndex = -1;
        this.saving = false;
      }
      this.reset();
    },
    handleDelete() {
      del(
        `/api/auth/platform_account/${this.deletePlatformId}`,
        () => {
          this.reset();
        },
        () => {
          this.reset();
        }
      );
    },
    handleEdit(row, index) {
      this.editIndex = index;
      this.editPlatformPassword = row.platformPassword;
    },
    handleResetInput(data) {
      data.platformUser = "";
      data.platformPassword = "";
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
