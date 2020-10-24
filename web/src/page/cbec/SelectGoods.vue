<template>
  <table-layout :data="data"
                :columns="columns"
                :total="total"
                :pageSize="pageSize"
                :loading="loading">
    <Form ref="form"
          style="
        padding: 5px;
        border-bottom-left-radius: 0;
        border-bottom-right-radius: 0;
      "
          inline
          :label-width="labelWidth"
          :model="form"
          :rules="formRule">
      <div class="form-group">
        <form-item label="平台："
                   prop="platform">
          <Select v-model="form.platform"
                  class="input-content">
            <Option v-for="item in platformList"
                    :value="item.value"
                    :key="item.value">{{ item.name }}</Option>
          </Select>
        </form-item>
        <form-item label="类目："
                   prop="category">
          <InputSelect label-name="description"
                       v-bind:options="categoryList"
                       v-bind:searchFunc="listCategory"
                       @on-selected="
              (v) => {
                form.category = v.name;
              }
            "></InputSelect>
        </form-item>
        <form-item :label-width="50">
          <Button icon="ios-search"
                  style="margin-left: 10px"
                  @click="searchCategoryGoods(1)">搜索</Button>
        </form-item>
      </div>
    </Form>
  </table-layout>
</template>


<script>
import TableLayout from "@/components/TableLayout";
import InputSelect from "@/components/InputSelect";
import { getList } from "@/http/index";

export default {
  components: {
    TableLayout,
    InputSelect,
  },
  data() {
    return {
      imgWidth: 140,
      labelWidth: 125,
      total: 0,
      page: 1,
      pageSize: 8,
      loading: true,
      form: {
        platform: "",
        category: "",
        goodsAttr: [],
        types: [],
      },
      formRule: {
        platform: [{ required: true, message: "数据错误", type: "string" }],
        category: [{ required: true, message: "数据错误", type: "string" }],
      },
      platformList: [
        {
          value: "vova",
          name: "VOVA",
        },
      ],
      goodsAttrList: [],
      categoryList: [],
      dstAddressList: [],
      typeList: [],
      columns: [
        {
          type: "index",
          width: 60,
          align: "center",
        },
        {
          title: "商品名称",
          key: "detailUrl",
          width: 300,
          render: (h, params) => {
            return h("a",
              {
                attrs: {
                  href: params.row.detailUrl,
                  target: "_blank",
                  title: params.row.detailUrl,
                },
              },
              params.row.subject);
          },
        },
        {
          title: "图片",
          key: "imageUrl",
          width: this.imgWidth,
          align: "center",
          render: (h, params) => {
            return h("img", {
              style: {
                //设置样式
                width: "100px",
                height: "80px",
                "border-radius": "5%",
                "margin": "auto",
              },
              attrs: {
                //设置属性
                src: params.row.imageUrl,
              },
            });
          },
        },
        {
          title: "售价($)",
          key: "price",
          align: "center",
        },
        {
          title: "预估进价(￥)",
          key: "predictPurchasePrice",
          align: "center",
        },
        {
          title: "供应商1",
          key: "supplier1",
          width: this.imgWidth,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h("a", {
                style: {
                  display: (params.row.supplier1.detailUrl != undefined) ? "inline" : "none",
                },
                attrs: { href: params.row.supplier1.detailUrl, target: '_blank', title: params.row.supplier1.detailUrl }
              }, "￥: " + params.row.supplier1.price),
              h("img", {
                style: {
                  width: "100px",
                  height: "80px",
                  "border-radius": "5%",
                  "margin": "auto",
                  display: (params.row.supplier1.imageUrl != undefined) ? "inline" : "none",
                },
                attrs: {
                  src: params.row.supplier1.imageUrl,
                },
              })
            ]);
          },
        },
        {
          title: "供应商2",
          key: "supplier2",
          width: this.imgWidth,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h("a", {
                style: {
                  display: (params.row.supplier2.detailUrl != undefined) ? "inline" : "none",
                },
                attrs: { href: params.row.supplier2.detailUrl, target: '_blank', title: params.row.supplier2.detailUrl }
              }, "￥: " + params.row.supplier2.price),
              h("img", {
                style: {
                  width: "100px",
                  height: "80px",
                  "border-radius": "5%",
                  "margin": "auto",
                  display: (params.row.supplier2.imageUrl != undefined) ? "inline" : "none",
                },
                attrs: {
                  src: params.row.supplier2.imageUrl,
                },
              })
            ]);
          },
        },
        {
          title: "供应商3",
          key: "supplier3",
          width: this.imgWidth,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h("a", {
                style: {
                  display: (params.row.supplier3.detailUrl != undefined) ? "inline" : "none",
                },
                attrs: { href: params.row.supplier3.detailUrl, target: '_blank', title: params.row.supplier3.detailUrl }
              }, "￥: " + params.row.supplier3.price),
              h("img", {
                style: {
                  width: "100px",
                  height: "80px",
                  "border-radius": "5%",
                  "margin": "auto",
                  display: (params.row.supplier3.imageUrl != undefined) ? "inline" : "none",
                },
                attrs: {
                  src: params.row.supplier3.imageUrl,
                },
              })
            ]);
          },
        },
        {
          title: "平均进价(￥)",
          key: "avgPurchasePrice",
          align: "center",
        },
        {
          title: "预估售价($)",
          key: "predictSellPrice",
          align: "center",
        },
      ],
      data: [],
    };
  },
  mounted() {
    this.loading = false;
    this.form.platform = this.platformList[0].value;
    this.listCategory(this.form.platform);
  },
  methods: {
    changePage(page) {
      this.searchCategoryGoods(page);
    },
    searchCategoryGoods(pageNo) {
      let validateSucceed = true;
      this.$refs["form"].validate((v) => {
        if (!v) {
          validateSucceed = false;
        }
      });

      if (!validateSucceed) {
        return;
      }

      let params = {};
      params.pageNo = pageNo;
      params.pageSize = this.pageSize;
      params.category = this.form.category;
      this.listGoods(params);
    },
    listCategory(platform) {
      getList("/api/goods/goods/list_all_category/" + platform,
        {},
        (total, data) => {
          this.categoryList = data;
        },
        () => {
          this.categoryList = [];
        });
    },
    listGoods(params) {
      this.loading = true;
      getList("/api/goods/goods/list_category_goods",
        params,
        (total, data) => {
          this.loading = false;
          for (var idx in data) {
            // var predictPurchasePrice =
            //   (data[idx].price * this.form.exchangeRate) / 3;
            // data[idx].predictPurchasePrice = predictPurchasePrice.toFixed(2);
            //初始化空的供应商
            data[idx].supplier1 = {};
            data[idx].supplier2 = {};
            data[idx].supplier3 = {};
            this.listGoodsSupplier(data[idx].id, idx);
          }
          this.data = data;
          this.total = total;
        },
        () => {
          this.loading = false;
          this.data = [];
          this.total = 0;
        });
    },
    listGoodsSupplier(goodsId, idx) {
      getList(`/api/goods/goods/list_goods_supplier/${goodsId}`,
        {},
        (total, data) => {
          if (total >= 1) {
            this.data[idx].supplier1 = data[0];
          }
          if (total >= 2) {
            this.data[idx].supplier2 = data[1];
          }
          if (total >= 3) {
            this.data[idx].supplier3 = data[2];
          }
        },
        () => {
          this.data[idx].supplier1 = {};
          this.data[idx].supplier2 = {};
          this.data[idx].supplier3 = {};
        });
    },
  },
};
</script>

<style>
.span-info {
  width: 90px;
}
.input-content {
  margin-left: 2px;
  width: 120px !important;
}

/* .cw-input-select {
  width: 200px !important;
}

.cw-input-select_wrap {
  width: 120px !important;
} */
</style>
