from flask import Blueprint, request

from util import json_util
from vendor.ali1688 import ali1688
from vendor.vova import vova
from common import exception

goods = Blueprint('goods', __name__)


@goods.route('/list_all_category', methods=['GET'])
def list_all_category():
    category_list = vova.get_all_category()
    return json_util.obj2json(category_list)


@goods.route('/list_category_goods', methods=['GET'])
def list_category_goods():
    category_list = []
    category_name = request.args.get("category")
    if not category_name:
        raise exception.BizException("类目不能为空")

    sort = request.args.get("sort")
    if not sort:
        sort = "recommended"

    category = vova.get_category_by_name(category_name)
    if category:
        category_list.append(category)
    else:
        raise exception.BizException("无效的类目")

    return json_util.obj2json(vova.get_category_goods(category, sort))


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
