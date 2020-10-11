import logging
import flask

from controller.order_controller import *
from controller.goods_controller import *

app = flask.Flask(__name__)

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s')


def main():
    app.register_blueprint(order, url_prefix='/order')
    app.register_blueprint(goods, url_prefix='/goods')
    app.run(port=33023, debug=False)


if __name__ == '__main__':
    main()
