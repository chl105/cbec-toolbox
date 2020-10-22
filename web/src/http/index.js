import axios from 'axios';
import router from '@/router/index';
import Vue from 'vue';

function isSuccess(res) {
    return res.data && res.data.status == 0;
}

function getList(url, params, onSuccess, onError) {
    axios.get(url, {
        params: params
    }).then(res => {
        let data = res.data.data;
        let total = res.data.total;
        data = data ? data : [];
        total = total ? total : 0;
        if (onSuccess) {
            if (isSuccess(res)) {
                onSuccess(total, data);
            } else {
                Vue.prototype.$Message.error(res.data.message);
            }
        }
    }).catch(err => {
        if (onError) {
            onError(err);
        }

        let res = err.response;
        if (res.status == 401) {
            router.replace("/login");
            return;
        }
        if (res.data.message) {
            Vue.prototype.$Message.error(res.data.message);
        } else {
            Vue.prototype.$Message.error(`${res.status}请求失败`);
        }
    });
}

function put(url, params, onSuccess, onError) {
    axios.put(url, params).then((res) => {
        if (onSuccess) {
            if (isSuccess(res)) {

                onSuccess(res.data);
            } else {
                Vue.prototype.$Message.error(res.data.message);
            }
        }
    }).catch(err => {
        if (onError) {
            onError(err);
        }

        let res = err.response;
        if (res.status == 401) {
            router.replace("/login");
            return;
        }
        if (res.data.message) {
            Vue.prototype.$Message.error(res.data.message);
        } else {
            Vue.prototype.$Message.error(`${res.status}请求失败`);
        }
    });
}

function post(url, params, onSuccess, onError) {
    axios.post(url, params).then((res) => {
        if (onSuccess) {
            if (isSuccess(res)) {

                onSuccess(res.data);
            } else {
                Vue.prototype.$Message.error(res.data.message);
            }
        }
    }).catch(err => {
        let res = err.response;
        if (res.status == 401) {
            router.replace("/login");
            return;
        }
        if (onError) {
            onError(err);
        }
        if (res.data.message) {
            Vue.prototype.$Message.error(res.data.message);
        } else {
            Vue.prototype.$Message.error(`${res.status}请求失败`);
        }
    });
}

function del(url, params, onSuccess, onError) {
    axios.delete(url, params).then((res) => {
        if (onSuccess) {
            if (isSuccess(res)) {

                onSuccess(res.data);
            } else {
                Vue.prototype.$Message.error(res.data.message);
            }
        }
    }).catch(err => {
        if (onError) {
            onError(err);
        }

        let res = err.response;
        if (res.status == 401) {
            router.replace("/login");
            return;
        }
        if (res.data.message) {
            Vue.prototype.$Message.error(res.data.message);
        } else {
            Vue.prototype.$Message.error(`${res.status}请求失败`);
        }
    });
}

export {
    getList,
    put,
    post,
    del
};