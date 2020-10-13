class DictEx(dict):
    __setattr__ = dict.__setitem__
    __getattr__ = dict.__getitem__


def dict2obj(dict_obj):
    if not isinstance(dict_obj, dict):
        return dict_obj
    d = DictEx()
    for k, v in dict_obj.items():
        d[k] = dict2obj(v)
    return d
