import json

from util import dict_util


def obj2json(obj):
    return json.dumps(obj, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)


def json2dict(json_str):
    return json.loads(json_str)


def json2obj(json_str):
    return dict_util.dict2obj(json.loads(json_str))
