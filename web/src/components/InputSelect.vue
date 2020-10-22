<template>
  <div
    v-bind:id="componentId"
    class="cw-input-select_wrap"
    onselectstart="return false"
  >
    <div class="cw-input-select">
      <div class="cw-input-select_box" v-on:click="selectHandle">
        <span v-if="typeof selectedValue == 'object'">
          {{ selectedValue[labelName] }}
        </span>
        <span v-else>{{ selectedValue || "请选择" }}</span>
        <i class="cw-arrow" v-bind:class="{ up: isShowPop }"></i>
      </div>
      <div class="cw-input-select_pop" v-if="isShowPop">
        <div class="cw-input-select_ipt_wrap">
          <input
            type="text"
            v-model.trim="searchTxt"
            v-on:input="searchHandle(searchTxt)"
            class="cw-input-select_ipt"
            placeholder="搜索"
          />
          <span class="icon-clear" v-if="searchTxt" v-on:click="clearHandle">
            <svg
              t="1575258400555"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="2468"
              width="16"
              height="16"
            >
              <path
                d="M509.866667 32C245.333333 32 32 247.466667 32 512s213.333333 480 477.866667 480S987.733333 776.533333 987.733333 512 774.4 32 509.866667 32z m0 896C281.6 928 96 742.4 96 512S281.6 96 509.866667 96 923.733333 281.6 923.733333 512s-185.6 416-413.866666 416z"
                fill="#8a8a8a"
                p-id="2469"
              />
              <path
                d="M693.333333 330.666667c-12.8-12.8-32-12.8-44.8 0L512 467.2l-136.533333-136.533333c-12.8-12.8-32-12.8-44.8 0-12.8 12.8-12.8 32 0 44.8l136.533333 136.533333-136.533333 136.533333c-12.8 12.8-12.8 32 0 44.8 6.4 6.4 14.933333 8.533333 23.466666 8.533334s17.066667-2.133333 23.466667-8.533334l136.533333-136.533333 136.533334 136.533333c6.4 6.4 14.933333 8.533333 23.466666 8.533334s17.066667-2.133333 23.466667-8.533334c12.8-12.8 12.8-32 0-44.8L556.8 512l136.533333-136.533333c12.8-12.8 12.8-32 0-44.8z"
                fill="#8a8a8a"
                p-id="2470"
              />
            </svg>
          </span>
        </div>
        <ul class="cw-input-select_options">
          <li
            v-for="(option, index) in optionsList"
            v-on:click="selected(option)"
            :key="index"
          >
            <span v-if="typeof option == 'object'">
              {{ option[labelName] }}
            </span>
            <span v-else>{{ option }}</span>
          </li>
        </ul>
        <span class="cw-input-select_arrow"></span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "InputSelect",
  props: ["componentId", "options", "labelName", "searchFunc"],
  data: function () {
    return {
      optionsList: [],
      isShowPop: false,
      selectedValue: "",
      searchTxt: "", // 搜索词
    };
  },
  created: function () {
    // 深拷贝一份源数据
    this.optionsList = JSON.parse(JSON.stringify(this.options));
    // 点全局范围收起下拉框
    var that = this;
    document.addEventListener("click", (e) => {
      if (
        e.target.id == that.componentId ||
        that.getParentTagWithClass(e.target, "cw-input-select_wrap").length > 0
      ) {
        return;
      }
      that.hidePop();
    });
  },
  methods: {
    // 显示或隐藏选项列表盒子
    selectHandle: function () {
      this.isShowPop = !this.isShowPop;
      this.optionsList = JSON.parse(JSON.stringify(this.options));
    },
    // 隐藏选项列表盒子
    hidePop: function () {
      this.isShowPop = false;
    },
    // 点击选项
    selected: function (val) {
      // 根据选项类型给名称赋值
      this.selectedValue = val;
      this.isShowPop = false;
      this.$emit("on-selected", val);
    },
    // 选项搜索
    searchHandle(val) {
      // 如果定义了搜索方法，则使用外部的方法搜索
      if (this.searchFunc) {
        this.searchFunc(val);
      }

      // 深拷贝一份源数据
      var originList = JSON.parse(JSON.stringify(this.options));
      // filter过滤函数
      this.optionsList = originList.filter((item) => {
        // 根据选项类型给名称赋值
        var content = typeof item == "object" ? item[this.labelName] : item;
        return content.indexOf(val) > -1;
      });
    },
    // 清除搜索
    clearHandle(e) {
      e.stopPropagation();
      this.searchTxt = "";
      // 深拷贝一份源数据
      this.optionsList = JSON.parse(JSON.stringify(this.options));
    },
    hasClass(element, cls) {
      return (" " + element.className + " ").indexOf(" " + cls + " ") > -1;
    },
    getParentTagWithClass(startTag, className, parentTagList = []) {
      if (!startTag.parentElement) {
        return parentTagList;
      }

      // 父级标签是否是body,是着停止返回集合,反之继续
      if ("BODY" !== startTag.parentElement.nodeName) {
        // 放入集合
        if (this.hasClass(startTag.parentElement, className)) {
          parentTagList.push(startTag.parentElement);
        }
        // 再上一层寻找
        return this.getParentTagWithClass(startTag.parentElement,
          className,
          parentTagList);
      }
      // 返回集合,结束
      else {return parentTagList;}
    },
  },
};
</script>

<style>
/* 基础样式reset */
input {
  box-sizing: border-box;
  outline: 0;
}
ul {
  margin: 0;
  padding: 0;
}
ul,
li {
  list-style: none;
}

/* 组件整体容器 */
.cw-input-select_wrap {
  position: relative;
  width: 198px;
  height: 30px;
  font-size: 14px;
}

/* 组件内容 */
.cw-input-select {
  width: 198px;
  position: absolute;
}

/* 基本下拉框 */
.cw-input-select_box {
  height: 30px;
  border: 1px solid #b7b7b7;
  border-radius: 4px;
  background-color: white;
  position: relative;
  cursor: pointer;
}

/* 基本下拉框里面右边的线体上下箭头（可旋转） */

.cw-arrow {
  content: "";
  display: block;
  position: absolute;
  right: 10px;
  top: 8px;
  border-top: 1px solid #c0c4cc;
  border-right: 1px solid #c0c4cc;
  border-radius: 1px;
  width: 8px;
  height: 8px;
  background: transparent;
  transition: transform 0.3s, -webkit-transform 0.3s;
  transform: rotate(135deg);
  z-index: 10;
}
/* 箭头向上 */
.cw-arrow.up {
  transform: rotate(-45deg);
  top: 12px;
}

/* 基本下拉框 文字 */
.cw-input-select_box > span {
  display: inline-block;
  line-height: 30px;
  padding: 0 30px 0 15px;
  font-size: 12px;
  color: #606266;
  /* 文字超出用省略号 */
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  overflow: hidden;
}

/* 选项列表盒子 */
.cw-input-select_pop {
  position: relative;
  background-color: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  max-height: 274px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-top: 12px;
  padding: 5px;
  box-sizing: border-box;
  z-index: 9;
}

/* 选项列表盒子上方的三角形箭头 */
.cw-input-select_arrow {
  position: absolute;
  display: block;
  width: 0;
  height: 0;
  border-color: transparent;
  border-style: solid;
  border-width: 6px;
  filter: drop-shadow(0 2px 12px rgba(0, 0, 0, 0.03));
  top: -6px;
  left: 35px;
  margin-right: 3px;
  border-top-width: 0;
  border-bottom-color: #fff;
  z-index: 99;
}

/* 选项列表盒子里面的输入框 */
.cw-input-select .cw-input-select_pop .cw-input-select_ipt {
  position: absolute;
  top: 5px;
  z-index: 99;
  height: 24px;
  line-height: 20px;
  width: 94%;
  border: 1px solid #dcdfe6;
  padding: 1px 5px;
  font-size: 12px;
}

/* 选项列表内容 */
.cw-input-select_options {
  display: block;
  margin-top: 26px;
  max-height: 234px;
  overflow-y: scroll;
}

/* 选项单元 */
.cw-input-select_options li {
  padding: 8px 15px;
  background-color: white;
  cursor: pointer;
}

/* 选项单元hover */
.cw-input-select_options li:hover {
  background-color: #f5f7fa;
}

/*自定义滚动条样式*/
.cw-input-select_options::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 6px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 0;
}

.cw-input-select_options::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 6px;
  /*-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);*/
  background-color: rgba(144, 147, 153, 0);
  transition: background-color 0.3s;
}

.cw-input-select_options::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  /*-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);*/
  border-radius: 6px;
  background: transparent;
}
.cw-input-select_options:hover::-webkit-scrollbar-thumb {
  background-color: rgba(144, 147, 153, 0.3);
}
.cw-input-select_ipt_wrap {
  position: relative;
}
.icon-clear {
  color: #aaa;
  position: absolute;
  right: 18px;
  top: 9px;
  z-index: 99;
  cursor: pointer;
}
</style>
