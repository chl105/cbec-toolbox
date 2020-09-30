import itchat


def main():
    itchat.auto_login()
    itchat.send('Hello, filehelper', toUserName='filehelper')


if __name__ == '__main__':
    main()