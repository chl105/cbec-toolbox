class GoodsInfo:
    def __init__(self, id, subject, detail_url, price, image_url, seller_name="", seller_shop_url="", sell_count=-1):
        self.id = id
        self.subject = subject
        self.detail_url = detail_url
        self.image_url = image_url
        self.price = price
        self.seller_name = seller_name
        self.seller_shop_url = seller_shop_url
        self.sell_count = sell_count
