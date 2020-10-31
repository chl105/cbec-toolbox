#!/usr/bin/env python
# -*- coding: utf-8 -*-

import base64
import io
import os.path
import time
from urllib.parse import urlparse
from bs4 import BeautifulSoup
import re, json

import requests

from ecommerce.ali1688.func_txy import get_random_str
from ecommerce.ali1688.func_txy import request_get
from ecommerce.ali1688.func_txy import request_post


class Alibaba(object):
    """
    ali1688 PC 端接口获取相似商品的接口
    """

    def __init__(self):
        self.upload_url = 'https://cbusearch.oss-cn-shanghai.aliyuncs.com/'  # 上传图片
        self.sign_url = "https://open-s.1688.com/openservice/ossDataService"  # 获取 sign 加密
        self.imageSearch_service_url = "https://open-s.1688.com/openservice/imageSearchOfferResultViewService"
        self._headers()
        self.search_page_size = 40

    def set_search_page_size(self, pageSize):
        self.search_page_size = pageSize

    def _headers(self):
        headres = {
            'Origin': "https://www.1688.com",
            "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36",
            "Accept": "*/*",
            "Cache-Control": "no-cache",
            "refer": "https://www.1688.com/",
        }
        self.headers = headres

    def get_key(self):
        '''
            cbuimgsearch/Z5Qja4fXPH1562293605000.jpg 生成规则是 cbuimgsearch/ + 随机10位 + 毫秒级别时间戳
            :return:
            '''
        key = "cbuimgsearch/" + "".join(get_random_str(10)) + str(int(time.time() * 1000))
        return key

    def get_dateset(self):
        '''
        获取加密时间
        :return:
        '''
        url = "https://open-s.1688.com/openservice/.htm?"  # 获取加密时间
        params = {
            "serviceIds": "cbu.searchweb.config.system.currenttime",
            "outfmt": "json",
        }

        status, data = request_get(url, params, self.headers)
        return status, data

    def get_sign(self, data_set):
        '''
        用于获取 sign 用于加密
        :return:
        '''
        url = 'https://open-s.1688.com/openservice/ossDataService'
        key = str(base64.b64decode("cGNfdHVzb3U=".encode("utf-8")), encoding="utf-8")
        appkey = "{};{}".format(key, str(data_set))

        params = {
            "appName": key,
            "appKey": base64.b64encode(appkey.encode("utf-8")),
        }

        status, data = request_get(url, params, self.headers)

        data = data.get('data', {})

        signature = data.get('signature', '')
        policy = data.get('policy', '')
        accessid = data.get('accessid', '')

        return status, signature, policy, accessid

    def upload_img(self, filename, signature, policy, accessid):
        """
        用于上传图片
        :return:
        """

        url = 'https://cbusearch.oss-cn-shanghai.aliyuncs.com/'
        key = "cbuimgsearch/" + get_random_str(10) + str(int(time.time()) * 1000) + ".jepg"
        name = get_random_str(5) + ".jpeg"

        if os.path.exists(filename):
            bytestream = open(filename, "rb").read()
        else:
            us = urlparse(filename)
            if not us:
                return 'fail', None
            r = requests.get(filename)
            bytestream = io.BytesIO(r.content)

        files = {
            "name": (None, name),
            "key": (None, key),
            "policy": (None, policy),
            "OSSAccessKeyId": (None, accessid),
            "success_action_status": (None, 200),
            "callback": (None, ""),
            "signature": (None, signature),
            "file": (name, bytestream)
        }

        status, res = request_post(url, data=None, files=files, headers=self.headers)
        return status, key

    def img_search(self, img_key, dataSet, beginPage=1):
        """
        用于上传图片并搜索商品列表
        从1688官网图搜页面扒出来的jsonp接口
        :return: dict o None
        """
        app_name = "pc_tusou"

        app_key = base64.b64encode(f'{app_name};{dataSet}'.encode("utf-8"))

        appKey = str(app_key, encoding="utf8")

        request_params = {
            "tab": "imageSearch",
            "spm": "a260k.dacugeneral.search.0",
            "imageAddress": img_key,
            "imageType": "oss",
            "pageSize": self.search_page_size,
            "beginPage": beginPage,
            "categoryId": "null",
            "appName": app_name,
            "appKey": appKey,
            "callback": "",
            "sortField": "price",
            "sortType": "asc#sm-filtbar"
        }
        status_desc, data = request_get(self.imageSearch_service_url, request_params, headers=self.headers)
        if status_desc == "succ":
            return 'succ', data
        else:
            return 'fail', None

    def search(self, filename, need_products=False, begin_page=1):
        status, data = self.get_dateset()

        # json 直接解析
        data_set = data.get('cbu.searchweb.config.system.currenttime', {}).get('dataSet', '')

        # 获取相关的 接口验证参数。同时 获取生成的 图片key
        status, signature, policy, accessid = self.get_sign(data_set)

        # uoload image file
        status, key = self.upload_img(filename, signature, policy, accessid)
        # 上传成功后，拼接生成的 查询 URL
        if status == "succ":
            url_res = 'https://s.1688.com/youyuan/index.htm?tab=imageSearch&imageType=oss&sortField=sold_quantity&sortType=desc#sm-filtbar&imageAddress={}&spm='.format(
                key)
            if not need_products:
                return url_res
            else:
                status_desc, data = self.img_search(key, data_set, begin_page)
                if status_desc == 'succ':
                    return data
                return None
        else:
            return ""


class AlibabaDetail:
    headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6,ja;q=0.5',
        'cache-control': 'max-age=0',
        'cookie': 'cna=6B2NFMQceWkCATy/9iZmJ/r2; ali_ab=60.191.246.38.1543910908671.4; lid=%E4%B9%89%E4%B9%8C2010; __last_userid__=375685501; hng=CN%7Czh-CN%7CCNY%7C156; UM_distinctid=16b40021a50161-08fe1f8fb068e8-37657e03-1fa400-16b40021a51abb; ali_apache_id=11.15.106.128.1564454978766.321081.5; h_keys="%u7537%u68c9%u670d#%u73a9%u5177#%u4e49%u4e4c%u5e02%u4e00%u6db5%u5236%u7ebf#%u91d1%u5b9d%u8d1d#%u4e00%u6db5%u5236%u7ebf#2017%u5723%u8bde%u9996%u9970#%u5723%u8bde%u9996%u9970#%u9996%u9970#%u7ea2%u85af#%u4e49%u4e4c%u817e%u535a%u793c%u54c1"; ad_prefer="2019/08/08 09:38:58"; ali_beacon_id=60.191.246.38.1566810215744.002451.6; ali_apache_track=c_mid=b2b-375685501ncisr|c_lid=%E4%B9%89%E4%B9%8C2010|c_ms=1|c_mt=2; taklid=9d140935ba3b4a8f9c20e255b4a99dd0; _csrf_token=1569548292989; cookie2=11e8d4b69091a1157b038c714385c9a6; t=4c47e32627e4d9d5c08008789ed65a34; _tb_token_=ab7d81831375; uc4=id4=0%40UgDLKslxx%2F5KKbIzCKEbS9CpADM%3D&nk4=0%40s5u8VZNrKh1Ipk4a6%2FKiHZj80A%3D%3D; __cn_logon__=false; alicnweb=homeIdttS%3D99025414611281355176293308315884802540%7Ctouch_tb_at%3D1569548305483%7ChomeIdttSAction%3Dtrue%7Clastlogonid%3D%25E4%25B9%2589%25E4%25B9%258C2010%7Cshow_inter_tips%3Dfalse; l=cBMFFQcuvPgtaQebKOfalurza77T5IRb4sPzaNbMiICP_j1y5CQAWZCBm382CnGVp626R3zP_tquBeYBc1bnLjDSik2H9; isg=BNTUjkpcdQqEKuDNKwS17J7fpRRMK8akMa0MD261lt_jWXSjlj_fp6CTWRHkoTBv',
        'sec-fetch-mode': 'navigate',
        'sec-fetch-site': 'same-origin',
        'sec-fetch-user': '?1',
        'upgrade-insecure-requests': '1',
        'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36',
    }

    def get_detail_data(self, url):
        dic = {}
        response = requests.get(url, headers=self.headers, allow_redirects=False)
        soup = BeautifulSoup(response.text)
        title_tag = soup.find(attrs={"property": "og:title"})
        if title_tag:
            dic["title"] = title_tag.attrs["content"]
        price_tag = soup.find(attrs={"property": "og:product:price"})
        if price_tag:
            price = str(price_tag.attrs["content"])
            print(price)
            # 如果价格中间存在-，例如 1.0-2.0，表示是区间价格，取高区间
            if price.find("-") > 0:
                price = price[price.find("-")+1:]
            dic["price"] = price
        org_price_tag = soup.find(attrs={"property": "og:product:orgprice"})
        if org_price_tag:
            dic["org_price"] = org_price_tag.attrs["content"]
        return dic

