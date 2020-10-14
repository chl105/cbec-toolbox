FROM python:alpine

WORKDIR /root/spider

ADD . .

RUN pip3 install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple 

CMD python /root/spider/main.py
