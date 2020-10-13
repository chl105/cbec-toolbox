from flask import request, Blueprint

from common import exception
from util import json_util
from vendor.vova import vova_merchant

order = Blueprint('order', __name__)


@order.route('/list_unhandled_order', methods=['GET'])
def list_unhandled_order():
    user = request.args.get("user")
    password = request.args.get("password")
    if not user or not password:
        raise exception.BizException("用户或者密码不存在")

    cookie = vova_merchant.login(user, password)
    order_list = vova_merchant.get_unhandled_order(cookie)
    return json_util.obj2json(order_list)
