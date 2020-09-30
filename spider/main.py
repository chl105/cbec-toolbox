import logging
import flask

from order_controller import *

app = flask.Flask(__name__)

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(filename)s[line:%(lineno)d] - %(levelname)s: %(message)s')


def main():
    app.register_blueprint(order, url_prefix='/order')
    app.run(port=33023, debug=True)


if __name__ == '__main__':
    main()
