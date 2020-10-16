<template>
  <table-layout
    :data="data"
    :columns="columns"
    :total="total"
    :pageSize="pageSize"
    :loading="loading"
  >
    <Form
      ref="form"
      style="
        padding: 5px;
        border-bottom-left-radius: 0;
        border-bottom-right-radius: 0;
      "
      inline
      :label-width="labelWidth"
      :model="form"
      :rules="formRule"
    >
      <div class="form-group">
        <form-item label="平台：" prop="platform">
          <Select v-model="form.platform" class="input-content">
            <Option
              v-for="item in platformList"
              :value="item.value"
              :key="item.value"
              >{{ item.name }}</Option
            >
          </Select>
        </form-item>
        <form-item label="类目：" prop="category">
          <InputSelect
            label-name="description"
            v-bind:options="categoryList"
            v-bind:searchFunc="searchCategoryList"
            @onSelected="
              (v) => {
                form.category = v.name;
              }
            "
          ></InputSelect>
        </form-item>
        <form-item label="汇率(￥/$)：" prop="exchangeRate">
          <InputNumber v-model="form.exchangeRate" class="input-content" />
        </form-item>
        <form-item label="价格公式：" prop="priceFormula">
          <Input v-model="form.priceFormula" class="input-content" />
        </form-item>
        <form-item :label-width="50">
          <Button
            icon="ios-search"
            style="margin-left: 10px"
            @click="searchCategoryGoods"
            >搜索</Button
          >
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
      labelWidth: 125,
      total: 0,
      pageSize: 12,
      loading: true,
      form: {
        platform: "",
        category: "",
        goodsAttr: [],
        types: [],
        exchangeRate: 7,
        priceFormula: "售价=进价*3",
      },
      formRule: {
        platform: [{ required: true, message: "数据错误", type: "string" }],
        exchangeRate: {
          required: true,
          message: "数据错误",
          type: "number",
        },
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
          width: 200,
          render: (h, params) => {
            return h(
              "a",
              {
                attrs: {
                  href: params.row.detailUrl,
                  target: "_blank",
                  title: params.row.detailUrl,
                },
              },
              params.row.subject
            );
          },
        },
        {
          title: "图片",
          key: "imageUrl",
          width: 140,
          align: "center",
          render: (h, params) => {
            return h("img", {
              style: {
                //设置样式
                width: "100px",
                height: "80px",
                "border-radius": "5%",
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
          width: 150,
          align: "center",
        },
        {
          title: "供应商2",
          key: "supplier2",
          width: 150,
          align: "center",
        },
        {
          title: "供应商3",
          key: "supplier3",
          width: 150,
          align: "center",
        },
        {
          title: "供应商4",
          key: "supplier4",
          width: 150,
          align: "center",
        },
        {
          title: "供应商5",
          key: "supplier5",
          width: 150,
          align: "center",
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
    this.searchCategoryList(this.form.platform);
  },
  methods: {
    searchCategoryList(platform) {
      getList(
        "/goods/goods/list_all_category/" + platform,
        {},
        (total, data) => {
          this.categoryList = data;
        },
        () => {
          this.categoryList = [];
        }
      );
    },

    searchCategoryGoods() {
      let validateSucceed = true;
      this.$refs["form"].validate((v) => {
        if (!v) validateSucceed = false;
      });

      if (!validateSucceed) {
        return;
      }

      let params = {};
      params.start = 0;
      params.limit = this.pageSize;
      params.category = this.form.category;
      params.sort = "most-popular";
      this.listCategoryGoods(params, this);
    },
    listCategoryGoods(params, that) {
      that.loading = true;
      getList(
        "/goods/goods/list_category_goods",
        params,
        (total, data) => {
          that.loading = false;
          for (var idx in data) {
            var predictPurchasePrice =
              (data[idx].price * that.form.exchangeRate) / 3;
            data[idx].predictPurchasePrice = predictPurchasePrice.toFixed(2);
            that.searchGoodsByImage(
              data[idx].imageUrl,
              predictPurchasePrice,
              data[idx]
            );
          }
          that.data = data;
          that.total = total;
        },
        () => {
          that.loading = false;
          that.data = [];
          that.total = 0;
        }
      );
    },
    searchGoodsByImage(imageUrl, maxPrice, goods) {
      let params = {};
      params.imageUrl = imageUrl;
      params.maxPrice = maxPrice;
      getList(
        "/goods/goods/search_goods_by_image",
        params,
        (total, data) => {
          goods.supplier1 = data[0].subject;
          goods.supplier2 = data[1].subject;
          goods.supplier3 = data[2].subject;
          goods.supplier4 = data[3].subject;
        },
        () => {}
      );
    },
    exchangeRmb2Dollor(rmb, exchangeRate) {
      return rmb / exchangeRate;
    },
    exchangeDollor2Rmb(dollor, exchangeRate) {
      return dollor * exchangeRate;
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
