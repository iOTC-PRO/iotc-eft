# -*- coding=utf-8-*-
import getpass

import requests
from Crypto.Cipher import AES
import base64


class AESUtil:

    __BLOCK_SIZE_16 = BLOCK_SIZE_16 = AES.block_size

    @staticmethod
    def encryt(str, key):
        cipher = AES.new(key, AES.MODE_ECB)
        x = AESUtil.__BLOCK_SIZE_16 - (len(str) % AESUtil.__BLOCK_SIZE_16)
        if x != 0:
            str = str + chr(x)*x
        msg = cipher.encrypt(str)
        msg = base64.b64encode(msg)
        return msg

    @staticmethod
    def decrypt(enStr, key):
        cipher = AES.new(key, AES.MODE_ECB)
        enStr += (len(enStr) % 4)*"="
        decryptByts = base64.urlsafe_b64decode(enStr)
        msg = cipher.decrypt(decryptByts)
        paddingLen = ord(msg[len(msg)-1])
        return msg[0:-paddingLen]

pwd = getpass.getpass('pwd:')
url = 'http://localhost:8080/local/pwd'
data={
    'pwd':AESUtil.encryt(pwd, "spfdguxog%sonchg"),
    'sign':'4VEp3HbeXXBvjuLbcbvnSJB26mZPYNymhADP+o8XVZk='
}
r = requests.post(url=url,data=data)
print r.text
