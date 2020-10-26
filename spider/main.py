import logging

import flask

from controller.goods_controller import *
from controller.order_controller import *

app = flask.Flask(__name__)

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s')


def main():
    app.register_blueprint(order, url_prefix='/order')
    app.register_blueprint(goods, url_prefix='/goods')
    app.run(host="0.0.0.0", port=33023, debug=False, threaded=True)


if __name__ == '__main__':
    main()
