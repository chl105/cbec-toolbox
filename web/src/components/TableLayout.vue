<template>
  <div class="main">
    <div class="search-bar">
      <slot></slot>
    </div>
    <div class="list-content">
      <Table border :columns="columns" :data="data" :loading="loading" style="margin-top:10px"></Table>
    </div>

    <div class="page-bar">
      <Page
        :total="total"
        style="margin-top:10px;float:right;"
        @on-change="changePage"
        :page-size="pageSize"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "TableLayout",
  props: {
    loading: Boolean,
    data: Array,
    columns: Array,
    pageSize: Number,
    total: Number
  },
  methods: {
    changePage(page) {
      if (this.$parent.changePage) {
        this.$parent.changePage(page);
      }
    }
  }
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
