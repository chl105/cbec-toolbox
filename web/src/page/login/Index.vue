<!-- eslint-disable -->
<template>
  <div class="parent">
    <div class="child">
      <Card style="width: 400px">
        <p slot="title">工具箱</p>
        <Form ref="formInline"
              :model="formInline"
              :rules="ruleInline">
          <FormItem prop="user">
            <Input type="text"
                   v-model="formInline.user"
                   placeholder="用户名">
            <Icon type="ios-person-outline"
                  slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem prop="password">
            <Input type="password"
                   v-model="formInline.password"
                   placeholder="密码"
                   @keyup.enter.native="handleSubmit('formInline')">
            <Icon type="ios-lock-outline"
                  slot="prepend"></Icon>
            </Input>
          </FormItem>
          <FormItem>
            <Button type="primary"
                    long
                    @click="handleSubmit('formInline')">登录</Button>
          </FormItem>
        </Form>
      </Card>
    </div>
  </div>
</template>


<script>
import md5 from "blueimp-md5";
import axios from "axios";
export default {
  data() {
    return {
      formInline: {
        user: this.$store.state.user,
        password: "",
      },
      ruleInline: {
        user: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
          {
            type: "string",
            min: 6,
            message: "密码长度最少 6 位",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          var data = {
            username: this.formInline.user,
            password: md5(this.formInline.password),
            grant_type: "password",
            scope: "all",
            client_id: "web",
            client_secret: "web-secret",
          };
          axios
            .post("/api/auth/oauth/token", "", { params: data })
            .then((response) => {
              let token = response.data.access_token;
              if (token) {
                // 更新 token
                this.$store.commit("setToken", token);
                this.$store.commit("setUser", this.formInline.user);
                // 跳转
                this.$router.replace("/");
              } else {
                this.$Message.error("token 解析失败");
              }
            })
            .catch((error) => {
              let des = "";
              try {
                des = "";
                if (error.response.data.error_description) {
                  des = error.response.data.error_description;
                } else if (error.response.data.message) {
                  des = error.response.data.message;
                }
                this.$Message.error(des);
                // eslint-disable-next-line no-empty
              } catch (error) { }
              if (!des) {
                this.$Message.error(error);
              }
            });
        }
      });
    },
  },
};
</script>

<style>
.parent {
  width: 100%;
  height: 100%;
  position: relative;
}

.child {
  position: absolute;
  top: 40%;
  left: 50%;
  margin-top: 100px;
  margin-left: -200px;
}
</style>