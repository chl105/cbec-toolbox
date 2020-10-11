from common import exception
from model import goods_model
from util import dict_util
from vendor.ali1688 import alibaba_lib

_BASE_URL = "https://s.1688.com"


def search_goods_by_image(image_url):
    result = alibaba_lib.Alibaba().search(image_url, need_products=True)
    _check_result(result)
    data = result["data"]
    if not data:
        return None
    size = data["offerSize"]
    total = data["totalCount"]
    page_count = data["pageCount"]
    goods_list = data["offerList"]
    if size <= 0:
        return None

    res_goods_list = []
    for goods in goods_list:
        information_obj = dict_util.dict2obj(goods["information"])
        image_obj = dict_util.dict2obj(goods["image"])
        company_obj = dict_util.dict2obj(goods["company"])
        price_obj = dict_util.dict2obj(goods["tradePrice"])
        trade_quantity_obj = dict_util.dict2obj(goods["tradeQuantity"])
        res_goods_list.append(goods_model.GoodsPurchaseInfo(information_obj.subject, information_obj.detailUrl,
                                                            price_obj.offerPrice.valueString, image_obj.imgUrl,
                                                            company_obj.name, company_obj.url,
                                                            trade_quantity_obj.number))
    return res_goods_list


def _check_result(result):
    if result["code"] != 200:
        raise exception.BizException(result["msg"])
