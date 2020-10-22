import requests

from common import exception
from model import goods_model
from util import json_util, dict_util

_BASE_URL = "https://www.vova.com"

'''
VOVA买家
'''


def _build_vova_url(uri):
    if uri.startswith("/"):
        return _BASE_URL + uri
    return _BASE_URL + "/" + uri


_CATEGORY_BAG_WATCHES = goods_model.Category("bags-watches-accessories", _build_vova_url("Bags-Watches-Accessories-r9876"),
                                 "包、手表、配件")
_CATEGORY_WOMEN_CLOTHING = goods_model.Category("women-s-clothing", _build_vova_url("Women-S-Clothing-r9560"), "女士衣服")
_CATEGORY_MOBILE_PHONES = goods_model.Category("mobile-phones-accessories", _build_vova_url("Mobile-Phones-Accessories-r10045"),
                                   "移动电话、配件")
_CATEGORY_MEN_CLOTHING = goods_model.Category("men-s-clothing", _build_vova_url("Men-S-Clothing-r9881"), "男士衣服")
_CATEGORY_HOME_GARDEN = goods_model.Category("home-garden", _build_vova_url("Home-Garden-r9873"), "家庭")
_CATEGORY_ELECTRONICS = goods_model.Category("electronics", _build_vova_url("Electronics-r9874"), "电子产品")


def get_category_by_name(name):
    all_category = get_all_category()
    for i in all_category:
        if str(i.name).strip() == str(name).strip():
            return i
    return None


def get_all_category():
    return [
        _CATEGORY_BAG_WATCHES
    ]


def get_category_goods(category, sort="recommended", max_page=5):
    assert isinstance(category, goods_model.Category)

    next_page_cursor = None
    goods_list = []

    while max_page > 0:
        url = category.url + "?limit=60&is_ajax=1"
        if next_page_cursor:
            url += "&after=" + next_page_cursor
        res = requests.get(url + "/" + sort)
        _check_response(res)

        res_dict = json_util.json2dict(res.text)

        next_page_cursor = res_dict["data"]["pagination"]["cursors"]["after"]
        if not next_page_cursor:
            break

        product_list = res_dict["data"]["productList"]
        for product_dict in product_list:
            product_obj = dict_util.dict2obj(product_dict)
            goods_list.append(goods_model.GoodsInfo(
                product_obj.virtual_goods_id,
                product_obj.name,
                category.name,
                _build_vova_url(product_obj.url),
                product_obj.shop_price_exchange,
                "",
                platform="vova"
            ))

        idx = 0
        product_list_ext = dict_util.dict2obj(res_dict["data"]["arEcommerce"]["listProducts"][0]["product_list"])
        for product_ext_dict in product_list_ext:
            product_ext_obj = dict_util.dict2obj(product_ext_dict)
            goods_list[idx].image_url = "https://" + product_ext_obj.picture
            idx = idx + 1

        max_page = max_page - 1

    return goods_list


def _check_response(response):
    if response.status_code != 200:
        raise exception.BizException(response.text)


def main():
    goods_list = get_category_goods(_CATEGORY_BAG_WATCHES)
    print(json_util.obj2json(goods_list))


if __name__ == '__main__':
    main()
