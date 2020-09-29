#!/usr/bin/python

import requests
import json
import logging
from bs4 import BeautifulSoup


_COOKIE = ""
VOVA_BASE_URL = "https://merchant.vova.com.hk"

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s')


def build_vova_url(uri):
    return VOVA_BASE_URL + uri


def check_response(resp):
    if not resp.ok:
        raise Exception(print("Http response error".format(resp.status)))
    result = resp.json()
    if result["code"] != "SUCCESS":
        raise Exception(print("Biz response error, code={}, msg={}".format(result["code"], result["msg"])))


def login(user, password):
    uri = "/index.php?q=admin/main/index/login"

    data = {
        'acct': user,
        'pswd': password,
        'H_sbmt': "yes",
        'verify_code': "",
        'equipment': ""
    }

    global _COOKIE
    try:
        resp = requests.post(build_vova_url(uri), data=data, verify=False, timeout=20)
        check_response(resp)
        cookies = requests.utils.dict_from_cookiejar(resp.cookies)
        for name, value in cookies.items():
            _COOKIE += '{0}={1};'.format(name, value)
        return _COOKIE
    except Exception as err:
        print("获取cookie失败: {}".format(err))


def get_unhandled_order():
    uri = "/index.php?q=admin/main/unhandledOrder/index&perPage=100"
    response = requests.get(build_vova_url(uri), headers={"cookie": _COOKIE})
    soup = BeautifulSoup(response.text)
    aa = soup.find_all("table", class_="order-list-table")
    print(aa)


if __name__ == '__main__':
    user = "jianggangjiav"
    password = "Zhuanghe11"
    login(user, password)
    get_unhandled_order()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
