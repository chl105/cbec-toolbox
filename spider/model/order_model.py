
class Order:
    def __init__(self, id, type, confirm_time, sn, delivery_count_down, order_cancel_count_down,
                 order_collection_count_down, num, price, total_price, pay_status):
        self.id = id
        self.type = type
        self.confirm_time = confirm_time
        self.sn = sn
        self.delivery_count_down = delivery_count_down
        self.order_cancel_count_down = order_cancel_count_down
        self.order_collection_count_down = order_collection_count_down
        self.num = num
        self.price = price
        self.total_price = total_price
        self.pay_status = pay_status
