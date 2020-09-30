import json

from flask import request, Blueprint

import exception
import vova

order = Blueprint('', __name__)


@order.route('/list_unhandled_order', methods=['GET'])
def list_unhandled_order():
    user = request.args.get("user")
    password = request.args.get("password")
    if not user or not password:
        raise exception.BizException("用户或者密码不存在")

    cookie = vova.login(user, password)
    order_list = vova.get_unhandled_order(cookie)
    return json.dumps(order_list, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)
