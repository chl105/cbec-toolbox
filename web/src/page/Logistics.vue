<template>
  <table-layout
    :data="data"
    :columns="columns"
    :total="total"
    :pageSize="pageSize"
    :loading="loading"
  >
    <Form
      ref="logisticsModelForm"
      style="padding: 5px;border-bottom-left-radius: 0;border-bottom-right-radius: 0;"
      inline
      :label-width="90"
      :model="logisticsModelForm"
      :rules="logisticsModelFormRules"
    >
      <div class="form-group">
        <form-item label="物流：">
          <Select v-model="logisticsModelForm.vendor" class="input-content">
            <Option
              v-for="item in vendorList"
              :value="item.value"
              :key="item.value"
              >{{ item.name }}</Option
            >
          </Select>
        </form-item>
        <form-item label="出发地：" prop="srcAddress">
          <InputSelect
            label-name="name"
            v-bind:options="srcAddressList"
            @onSelected="
              (v) => {
                logisticsModelForm.srcAddress = v;
              }
            "
          ></InputSelect>
        </form-item>
        <form-item label="目的地：" prop="dstAddress">
          <InputSelect
            label-name="name"
            v-bind:options="dstAddressList"
            v-bind:searchFunc="searchCountryList"
            @onSelected="
              (v) => {
                logisticsModelForm.dstAddress = v;
              }
            "
          ></InputSelect>
        </form-item>
        <form-item label="重量(克)" prop="weight">
          <Input
            v-model="logisticsModelForm.weight"
            class="input-content"
            clearable
          />
        </form-item>
        <form-item label="产品类型" prop="types">
          <Select
            v-model="logisticsModelForm.types"
            multiple
            class="input-content"
            clearable
          >
            <Option
              v-for="item in typeList"
              :value="item.value"
              :key="item.value"
              >{{ item.name }}</Option
            >
          </Select>
        </form-item>
        <form-item label="货品属性" prop="goodsAttr">
          <Select
            v-model="logisticsModelForm.goodsAttr"
            class="input-content"
            clearable
          >
            <Option
              v-for="item in goodsAttrList"
              :value="item.value"
              :key="item.value"
              >{{ item.name }}</Option
            >
          </Select>
        </form-item>
        <form-item :label-width="50">
          <Button icon="ios-search" style="margin-left:10px" @click="search"
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
      total: 0,
      pageSize: 12,
      loading: true,
      logisticsModelForm: {
        weight: 500,
        srcAddress: {},
        dstAddress: {},
        vendor: "",
        goodsAttr: [],
        types: [],
      },
      logisticsModelFormRules: {
        srcAddress: [
          { required: true, message: " ", trigger: "blur", type: "string" },
        ],
        dstAddress: [
          { required: true, message: " ", trigger: "blur", type: "string" },
        ],
        weight: [
          {
            required: true,
            message: " ",
            trigger: "blur",
            type: "number",
          },
        ],
        goodsAttr: [
          { required: true, message: " ", trigger: "change", type: "string" },
        ],
        types: [
          { required: true, message: " ", trigger: "change", type: "string" },
        ],
      },
      vendorList: [
        {
          value: "0",
          name: "燕文物流",
        },
      ],
      goodsAttrList: [],
      srcAddressList: [],
      dstAddressList: [],
      typeList: [],
      columns: [
        {
          type: "index",
          width: 60,
          align: "center",
        },
        {
          title: "产品名称",
          key: "productName",
          width: 250,
        },
        {
          title: "价格代码",
          key: "priceCode",
          width: 200,
        },
        {
          title: "资费（元）",
          key: "expense",
          minWidth: 100,
        },
      ],
      productModelForm: {
        num: 1,
        price: 0.0,
        cost: 0.0,
        weight: 0,
        exchangeRate: 7.02,
        commission: 0.7975,
        bankCharge: 0.009,
        dstAddress: {},
      },
      data: [],
    };
  },
  mounted() {
    this.loading = false;
    this.logisticsModelForm.vendor = this.vendorList[0].value;
    this.initGoodsAttrbuteList();
    this.initProductTypeList();
    this.searchCityList("");
    this.searchCountryList("");
  },
  methods: {
    initParams(that) {
      let form = that.logisticsModelForm;
      let params = {};
      params.srcCountryCode = "8";
      params.srcAreaCode = "";
      params.srcCityCode = form.srcAddress.id;
      params.dstCountryCode = form.dstAddress.id;
      params.dstAreaCode = "0";
      params.dstCityCode = "0";
      params.types = form.types.toString();
      params.goodsAttr = form.goodsAttr.toString();
      params.weight = form.weight;
      return params;
    },
    initProductTypeList() {
      getList(
        "/trade/logistics/product_type_list",
        null,
        (total, data) => {
          this.typeList = data;
        },
        () => {
          this.typeList = [];
        }
      );
    },
    initGoodsAttrbuteList() {
      getList(
        "/trade/logistics/goods_attribute_list",
        null,
        (total, data) => {
          this.goodsAttrList = data;
        },
        () => {
          this.goodsAttrList = [];
        }
      );
    },
    searchCityList(val) {
      let params = {};
      params.fuzzy = val;
      getList(
        "/trade/address/city_list",
        params,
        (total, data) => {
          this.srcAddressList = data;
        },
        () => {
          this.srcAddressList = [];
        }
      );
    },
    searchCountryList(val) {
      let params = {};
      params.fuzzy = val;
      getList(
        "/trade/address/country_list",
        params,
        (total, data) => {
          this.dstAddressList = data;
        },
        () => {
          this.dstAddressList = [];
        }
      );
    },
    changePage(page) {
      let params = this.initParams(this);
      params.start = (page - 1) * this.pageSize;
      params.limit = this.pageSize;
      this.getLogisticsPrice(params, this);
    },
    search() {
      let params = this.initParams(this);
      params.start = 0;
      params.limit = this.pageSize;
      this.getLogisticsPrice(params, this);
    },
    getLogisticsPrice(params, that) {
      that.loading = true;
      getList(
        "/trade/logistics/price",
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
    },
  },
};
</script>

<style lang="less">
.span-info {
  width: 70px;
}
.input-content {
  margin-left: 2px;
  width: 100px !important;
}

.form-group {
  display: flex;
}
</style>
