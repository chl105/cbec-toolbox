#!/usr/bin/python

import json
import logging

import requests
from bs4 import BeautifulSoup
import exception

VOVA_BASE_URL = "https://merchant.vova.com.hk"


class Order:
    def __init__(self, id, type, confirm_time, sn, delivery_count_down, order_cancel_count_down,
                 order_collection_count_down, num, price, total_price, pay_status):
        self.id = id
        self.type = type
        self.confirm_time = confirm_time
        self.sn = sn
        self.delivery_count_down = delivery_count_down
        self.order_cancel_count_down = order_cancel_count_down
        self.order_collection_count_down = order_collection_count_down
        self.num = num
        self.price = price
        self.total_price = total_price
        self.pay_status = pay_status

    def __str__(self):
        return json.dumps(self)


def build_url(uri):
    return VOVA_BASE_URL + uri


def check_response(resp):
    if not resp.ok:
        raise exception.BizException("Http response error".format(resp.status))
    result = resp.json()
    if result["code"] != "SUCCESS":
        raise exception.BizException(result["msg"])


def login(user, password):
    uri = "/index.php?q=admin/main/index/login"

    logging.info("Login with user {}".format(user))

    data = {
        'acct': user,
        'pswd': password,
        'H_sbmt': "yes",
        'verify_code': "",
        'equipment': ""
    }

    resp = requests.post(build_url(uri), data=data, verify=False, timeout=20)
    check_response(resp)
    cookies = requests.utils.dict_from_cookiejar(resp.cookies)
    cookie = ""
    for name, value in cookies.items():
        cookie += '{0}={1};'.format(name, value)
    return cookie


def get_unhandled_order(cookie):
    uri = "/index.php?q=admin/main/unhandledOrder/index&perPage=100"
    response = requests.get(build_url(uri), headers={"cookie": cookie})
    soup = BeautifulSoup(response.text)
    order_list = []
    '''
        def __init__(self, id, type, confirm_time, sn, delivery_count_down, order_cancel_count_down,
                 order_collection_count_down, num, price, total_price, pay_status):
    '''
    order_list_tag = soup.find_all(lambda tag: tag.has_attr('data-order-goods-sn'))
    for order_tag in order_list_tag:
        td_tags = order_tag.find_all("td")

        order = Order(td_tags[6].a.string,
                      td_tags[0].a.string,
                      td_tags[1].string,
                      td_tags[2].a.string,
                      td_tags[3].attrs["data-time"],
                      td_tags[4].attrs["data-time"],
                      td_tags[5].attrs["data-time"],
                      td_tags[11].string,
                      td_tags[12].string,
                      td_tags[13].string,
                      td_tags[14].a.string)
        order_list.append(order)

    return order_list


def main():
    user = "jianggangjiav"
    password = "Zhuanghe11"
    cookie = login(user, password)
    aa = get_unhandled_order(cookie)
    print(aa)


if __name__ == '__main__':
    main()
