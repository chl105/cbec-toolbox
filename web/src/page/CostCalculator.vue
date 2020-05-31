<template>
  <table-layout
    :data="data"
    :columns="columns"
    :total="total"
    :pageSize="pageSize"
    :loading="loading"
  >
    <Form
      ref="ratioModelForm"
      style="padding: 5px;border-bottom-left-radius: 0;border-bottom-right-radius: 0;"
      inline
      :label-width="labelWidth"
      :model="ratioModelForm"
      :rules="ratioModelFormRules"
    >
      <form-item label="汇率(￥/$)：" prop="exchangeRate">
        <InputNumber
          :active-change="false"
          v-model="ratioModelForm.exchangeRate"
          class="input-content"
        />
      </form-item>
      <form-item label="佣金点数($)：" prop="commission">
        <InputNumber
          v-model="ratioModelForm.commission"
          class="input-content"
        />
      </form-item>
      <form-item label="转账手续费($)：" prop="bankCharge">
        <InputNumber
          v-model="ratioModelForm.bankCharge"
          class="input-content"
        />
      </form-item>
    </Form>

    <Form
      ref="productModelForm"
      style="padding: 5px;border-bottom-left-radius: 0;border-bottom-right-radius: 0;"
      inline
      :label-width="labelWidth"
      :model="productModelForm"
      :rules="productModelFormRules"
    >
      <form-item label="折后价($)：" prop="salePrice">
        <InputNumber
          :active-change="false"
          v-model="productModelForm.salePrice"
          class="input-content"
          clearable
          @on-change="
            () => {
              totalCost.commission =
                productModelForm.salePrice * ratioModelForm.commission;
              totalCost.bankCharge =
                productModelForm.salePrice * ratioModelForm.bankCharge;
            }
          "
        />
      </form-item>
      <form-item label="进价(￥)：" prop="inputPriceRMB">
        <InputNumber
          :active-change="false"
          v-model="productModelForm.inputPriceRMB"
          class="input-content"
          clearable
          @on-change="
            () => {
              productModelForm.inputPrice = exchangeRmb2Dollor(
                productModelForm.inputPriceRMB,
                ratioModelForm.exchangeRate
              );
            }
          "
        />
      </form-item>
      <form-item label="单重(g)：" prop="weight">
        <InputNumber
          :active-change="false"
          v-model="productModelForm.weight"
          class="input-content"
          clearable
          @on-change="
            () => {
              logisticsModelForm.weight =
                productModelForm.weight * productModelForm.num;
            }
          "
        />
      </form-item>
      <form-item label="数量：" prop="num">
        <InputNumber
          :active-change="false"
          v-model="productModelForm.num"
          class="input-content"
          @on-change="
            () => {
              logisticsModelForm.weight =
                productModelForm.weight * productModelForm.num;
            }
          "
        />
      </form-item>
    </Form>
    <Form
      ref="logisticsModelForm"
      style="padding: 5px;border-bottom-left-radius: 0;border-bottom-right-radius: 0;"
      inline
      :label-width="labelWidth"
      :model="logisticsModelForm"
      :rules="logisticsModelFormRules"
    >
      <div class="form-group">
        <form-item label="物流：" prop="vendor">
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
                logisticsModelForm.srcAddress = v.id;
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
                logisticsModelForm.dstAddress = v.id;
              }
            "
          ></InputSelect>
        </form-item>
        <!--form-item label="重量(g)" prop="weight">
          <Input
            v-model="logisticsModelForm.weight"
            class="input-content"
            readonly
          />
        </form-item-->
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
      labelWidth: 125,
      total: 0,
      pageSize: 12,
      loading: true,
      totalCost: {
        commission: 0.0,
        bankCharge: 0.0,
      },
      ratioModelForm: {
        exchangeRate: 7.02,
        commission: 0.7975,
        bankCharge: 0.009,
      },
      productModelForm: {
        num: 1,
        salePrice: 0,
        inputPriceRMB: 0.0,
        inputPrice: 0.0,
        weight: 0,
      },
      logisticsModelForm: {
        weight: 0,
        srcAddress: "",
        dstAddress: "",
        vendor: "",
        goodsAttr: [],
        types: [],
      },
      ratioModelFormRules: {
        exchangeRate: {
          required: true,
          message: " ",
          type: "float",
        },
        commission: {
          required: true,
          message: " ",
          type: "number",
        },
        bankCharge: {
          required: true,
          message: " ",
          type: "number",
        },
      },
      productModelFormRules: {
        num: {
          required: true,
          message: " ",
          type: "number",
          min: 1,
        },
        salePrice: {
          required: true,
          message: " ",
          type: "number",
          min: 0.1,
        },
        inputPriceRMB: {
          required: true,
          message: " ",
          type: "number",
          min: 0.1,
        },
        weight: {
          required: true,
          message: " ",
          type: "number",
          min: 1,
        },
      },
      logisticsModelFormRules: {
        srcAddress: [{ required: true, message: " ", type: "string" }],
        dstAddress: [{ required: true, message: " ", type: "string" }],
        weight: [
          {
            required: true,
            message: " ",
            type: "number",
            min: 1,
          },
        ],
        goodsAttr: [{ required: true, message: " ", type: "string" }],
        types: [{ required: true, message: " ", type: "array" }],
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
          title: "资费(￥)",
          key: "expense",
          minWidth: 100,
        },
        {
          title: "毛利($)",
          key: "profit",
          minWidth: 100,
        },
        {
          title: "毛利率",
          key: "profitRate",
          minWidth: 100,
        },
      ],
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
      let params = this.buildQueryParam(this);
      params.start = (page - 1) * this.pageSize;
      params.limit = this.pageSize;
      this.getLogisticsPrice(params, this);
    },
    exchangeRmb2Dollor(rmb, exchangeRate) {
      return rmb / exchangeRate;
    },
    exchangeDollor2Rmb(dollor, exchangeRate) {
      return dollor * exchangeRate;
    },
    search() {
      let validateSucceed = true;
      this.$refs["ratioModelForm"].validate((v) => {
        if (!v) validateSucceed = false;
      });
      this.$refs["productModelForm"].validate((v) => {
        if (!v) validateSucceed = false;
      });
      this.$refs["logisticsModelForm"].validate((v) => {
        if (!v) validateSucceed = false;
      });

      if (!validateSucceed) {
        return;
      }

      let params = this.buildQueryParam(this);
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
          for (var idx in data) {
            var profit =
              this.productModelForm.salePrice -
              this.productModelForm.inputPrice -
              this.exchangeRmb2Dollor(
                data[idx].expense,
                this.ratioModelForm.exchangeRate
              ) -
              this.totalCost.commission -
              this.totalCost.bankCharge;

            var profitRate = (profit / this.productModelForm.salePrice) * 100;

            data[idx].profit = profit.toFixed(2);
            data[idx].profitRate = profitRate.toFixed(2) + "%";
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
    buildQueryParam(that) {
      let form = that.logisticsModelForm;
      let params = {};
      params.srcCountryCode = "8";
      params.srcAreaCode = "";
      params.srcCityCode = form.srcAddress;
      params.dstCountryCode = form.dstAddress;
      params.dstAreaCode = "0";
      params.dstCityCode = "0";
      params.types = form.types.toString();
      params.goodsAttr = form.goodsAttr.toString();
      params.weight = form.weight;
      return params;
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

.cw-input-select {
  width: 120px !important;
}

.cw-input-select_wrap {
  width: 120px !important;
}
</style>
