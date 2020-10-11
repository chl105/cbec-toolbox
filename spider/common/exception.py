class BizException(Exception):
    def __init__(self, reason):
        self.reason = reason

    def __str__(self):
        return "业务异常: " + self.reason
