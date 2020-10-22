from common import exception
from model import goods_model
from util import dict_util
from ecommerce.ali1688 import alibaba_lib

_BASE_URL = "https://s.1688.com"


def search_goods_by_image(image_url, max_price=0.0, max_seller=5):
    """
    根据图片搜索商品，只返回低于最大价格的商品
    :param image_url: 图片url
    :param max_price: 商品最大价格
    :param max_seller: 最大卖家数量
    :return:
    """
    alibaba_client = alibaba_lib.Alibaba()
    alibaba_client.set_search_page_size(20)
    result = alibaba_client.search(image_url, need_products=True)
    _check_result(result)

    res_goods_list = []

    data = result["data"]
    if not data:
        return res_goods_list
    size = data["offerSize"]
    total = data["totalCount"]
    page_count = data["pageCount"]
    if size <= 0 or "offerList" not in data:
        return res_goods_list

    goods_list = data["offerList"]

    # 多加两个，后面会丢弃一个价格最高的和最低的，求取平均价格
    max_seller = max_seller + 2

    seller_count = 0
    for goods in goods_list:
        information_obj = dict_util.dict2obj(goods["information"])
        image_obj = dict_util.dict2obj(goods["image"])
        company_obj = dict_util.dict2obj(goods["company"])
        price_obj = dict_util.dict2obj(goods["tradePrice"])
        trade_quantity_obj = dict_util.dict2obj(goods["tradeQuantity"])
        offer_price_obj = price_obj.offerPrice

        # 如果没有价格字段，忽略
        if "valueString" not in offer_price_obj:
            print("no price for subject, {}".format(information_obj.subject))
            continue

        # 如果商品价格大于最大价格，忽略
        price = offer_price_obj.valueString
        if float(price) > float(max_price):
            continue

        # 如果卖家数量超过要求的最大卖家数量，结束
        if seller_count > max_seller:
            break
        seller_count = seller_count + 1

        res_goods_list.append(goods_model.GoodsInfo(
            goods["id"],
            information_obj.subject,
            "NO",
            information_obj.detailUrl,
            price,
            image_obj.imgUrl,
            company_obj.name,
            company_obj.url,
            trade_quantity_obj.number,
            platform="1688"
        ))

    # 少于两个时，直接返回
    if len(res_goods_list) <= 2:
        return res_goods_list

    # 按照价格从低到高排序
    res_goods_list = sorted(res_goods_list, key=lambda e: e.price)
    # 去掉一个最低价格，去掉一个最高价格
    res_goods_list.pop()
    res_goods_list.pop(0)
    return res_goods_list


def _check_result(result):
    if not result:
        raise exception.BizException("Invalid result")
    if result["code"] != 200:
        raise exception.BizException(result["msg"])


if __name__ == '__main__':
    image_url = "https://image-tb.vova.com/image/262_262/filler/d5/a1/304ce4f1c049deffaf556a402fd3d5a1.jpg"
    search_goods_by_image(image_url, 100)
