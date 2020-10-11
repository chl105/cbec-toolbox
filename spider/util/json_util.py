import json
from collections import namedtuple

def obj2json(obj):
    return json.dumps(obj, default=lambda o: o.__dict__, sort_keys=True, indent=4, ensure_ascii=False)

def json2obj(json_str):
    json.loads(json_str, object_hook=lambda d: namedtuple('data', d.keys())(*d.values()))