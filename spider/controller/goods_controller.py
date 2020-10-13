from flask import Blueprint, request

from util import json_util
from vendor.ali1688 import ali1688
from vendor.vova import vova

goods = Blueprint('goods', __name__)


@goods.route('/list_all_category', methods=['GET'])
def list_all_category():
    category_list = vova.get_all_category()
    return json_util.obj2json(category_list)


@goods.route('/list_recommended_goods', methods=['GET'])
def list_recommended_goods():
    category_list = vova.get_all_category()
    category_goods_list = []
    for category in category_list:
        goods_list = vova.get_recommended_goods(category)
        category_goods_list.append(goods_list)
    return json_util.obj2json(category_goods_list)


@goods.route('/search_goods_by_image', methods=['GET'])
def search_goods_by_image():
    image_url = request.args.get("image_url")
    max_price = request.args.get("max_price")
    if not image_url or not max_price:
        raise ValueError("无效参数")

    goods_list = ali1688.search_goods_by_image(image_url)

    filtered_goods_list = []
    filtered_goods_list.extend([g for g in goods_list if float(g.price) <= float(max_price)])

    return json_util.obj2json(filtered_goods_list)
