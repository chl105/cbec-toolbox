class Category:
    def __init__(self, name, url, description, vendor=""):
        self.name = name
        self.url = url
        self.description = description
        self.vendor = vendor


class ScrollResult:
    def __init__(self, current_cursor, next_cursor, results):
        self.current_cursor = current_cursor
        self.next_cursor = next_cursor
        self.results = results


class GoodsInfo:
    def __init__(self, id, subject, category, detail_url, price, image_url, seller_name="", seller_shop_url="", sell_count=-1,
                 platform=""):
        self.id = id
        self.subject = subject
        self.category = category
        self.detail_url = detail_url
        self.image_url = image_url
        self.price = price
        self.seller_name = seller_name
        self.seller_shop_url = seller_shop_url
        self.sell_count = sell_count
        self.platform = platform
