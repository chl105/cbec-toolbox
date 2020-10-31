from common import exception
from ecommerce.ali1688 import alibaba_lib
from model import goods_model
from util import dict_util

import  requests
from bs4 import BeautifulSoup

_BASE_URL = "https://s.1688.com"


def __get_real_price(detail_url):
    detail_data = alibaba_lib.AlibabaDetail().get_detail_data(detail_url)

    return "0"


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

    res_goods_list = []
    page = 1
    # 多加两个，后面会丢弃一个价格最高的和最低的，求取平均价格
    max_seller = max_seller + 2
    need_more_data = True
    seller_count = 0

    while need_more_data:
        result = alibaba_client.search(image_url, need_products=True, begin_page=page)
        _check_result(result)

        data = result["data"]
        if not data:
            break

        page = page + 1
        size = data["offerSize"]
        if size <= 0 or "offerList" not in data:
            break

        goods_list = data["offerList"]
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

            price = 0
            if offer_price_obj.quantityPrices:
                quantity_prices = []
                quantity_prices.extend([float(p["valueString"]) for p in offer_price_obj.quantityPrices])
                price = max(quantity_prices)
            else:
                price = float(offer_price_obj.valueString)

            # 如果商品价格大于最大价格，忽略
            if float(price) > float(max_price):
                continue

            # 如果卖家数量超过要求的最大卖家数量，结束
            if seller_count >= max_seller:
                need_more_data = False
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

    # 按照价格从低到高排序
    res_goods_list = sorted(res_goods_list, key=lambda e: e.price)
    # 大于两个时，去掉一个最低价格，去掉一个最高价格
    if len(res_goods_list) > 2:
        res_goods_list.pop()
        res_goods_list.pop(0)
    return res_goods_list


def _check_result(result):
    if not result:
        raise exception.BizException("Invalid result")
    if result["code"] != 200:
        raise exception.BizException(result["msg"])


if __name__ == '__main__':
    #image_url = "https://image-tb.vova.com/image/262_262/filler/6b/cc/1035aa9aefc6b869ad9ce4e985966bcc.jpg"
    image_url = "https://image-tb.vova.com/image/262_262/filler/89/c5/fdcecee8b782bcc1ac6f726ab4b889c5.jpg"
    search_goods_by_image(image_url, 40)
