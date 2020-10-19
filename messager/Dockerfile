FROM python:alpine

WORKDIR /root/notify-robot

ADD . .

RUN pip3 install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple 

CMD python /root/notify-robot/main.py
