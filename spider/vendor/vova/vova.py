from common import exception

import requests
from bs4 import BeautifulSoup

from model import goods_model

_BASE_URL = "https://www.vova.com"

'''
VOVA买家
'''

class Category:
    def __init__(self, name, url, description):
        self.name = name
        self.url = url
        self.description = description
        self.vendor = "vova"


def _build_category_url(category):
    return _BASE_URL + "/" + category


_CATEGORY_BAG_WATCHES = Category("bags-watches-accessories", _build_category_url("Bags-Watches-Accessories-r9876"), "包、手表、配件")
_CATEGORY_WOMEN_CLOTHING = Category("women-s-clothing", _build_category_url("Women-S-Clothing-r9560"), "女士衣服")
_CATEGORY_MOBILE_PHONES = Category("mobile-phones-accessories", _build_category_url("Mobile-Phones-Accessories-r10045"), "移动电话、配件")
_CATEGORY_MEN_CLOTHING = Category("men-s-clothing", _build_category_url("Men-S-Clothing-r9881"), "男士衣服")
_CATEGORY_HOME_GARDEN = Category("home-garden", _build_category_url("Home-Garden-r9873"), "家庭")
_CATEGORY_ELECTRONICS = Category("electronics", _build_category_url("Electronics-r9874"), "电子产品")


def get_all_category():
    return [
        _CATEGORY_BAG_WATCHES
    ]


def get_recommended_goods(category):
    assert isinstance(category, Category)

    url = category.url
    res = requests.get(url + "/recommended")
    _check_response(res)

    goods_list = []

    soup = BeautifulSoup(res.text)
    goods_tag_list = soup.find_all(class_='cat-grid-link-wrap')
    for goods_tag in goods_tag_list:
        id_tag = goods_tag.find(class_="grid-link")
        if not id_tag:
            continue
        id = id_tag.attrs["data-gid"]

        price_tag = goods_tag.find(class_="shop-price")
        if not price_tag:
            continue
        shop_price = str(price_tag.string).strip()

        thumb_image_tag = goods_tag.find(class_="grid-link-image").find("img")
        if not thumb_image_tag:
            continue
        thumb_url = "https:" + thumb_image_tag.attrs["data-src"]
        goods_list.append(goods_model.Goods(id, shop_price, thumb_url))

    return goods_list


def _check_response(response):
    if response.status_code != 200:
        raise exception.BizException(response.text)


def main():
    get_recommended_goods(_CATEGORY_BAG_WATCHES)


if __name__ == '__main__':
    main()
