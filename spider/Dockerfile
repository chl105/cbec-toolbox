FROM python3:alpine

WORKDIR /root/spider

ADD . .

RUN pip3 install -r requirements.txt

ENTRYPOINT ["python3 main.py"]