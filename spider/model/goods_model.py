
class Goods:
    def __init__(self, id, price, thumb_image_url):
        self.id = id
        self.price = price
        self.thumb_image_url = thumb_image_url


class GoodsPurchaseInfo:
    def __init__(self, subject, detail_url, price, image_url, company_name, company_shop_url, sell_count):
        self.subject = subject
        self.detail_url = detail_url
        self.image_url = image_url
        self.price = price
        self.company_name = company_name
        self.company_shop_url = company_shop_url
        self.sell_count = sell_count
