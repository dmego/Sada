# -*- coding: utf-8 -*-
import SocketServer
import cgi
import json
import os
import uuid
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer

import cv2
from idcard import idcardocr
from studentcard import studentcardocr

from idcard import findidcard
from studentcard import findstudentcard


def processIdCard(img_name):
    try:
        idfind = findidcard.findidcard()
        idcard_img = idfind.find('idcard/mask/idcard_mask.jpg', img_name)
        result_dict = idcardocr.idcardocr(idcard_img)
        result_dict['error'] = 0
    except StandardError as e:
        result_dict = {'error':1}
        print e
    return result_dict

def processStudentCard(img_name):
    try:
        sfind = findstudentcard.findstudentcard()
        studentcard_img = sfind.find('studentcard/mask/studentcard_mask.png', img_name)#对的
        # studentcard_img = sfind.find('studentcard_mask.png', img_name)#t
        result_dict = studentcardocr.studentcardocr(studentcard_img)
        result_dict['error'] = 0
    except StandardError as e:
        result_dict = {'error':1}
        print e
    return result_dict




#SocketServer.ForkingMixIn, SocketServer.ThreadingMixIn
class ForkingServer(SocketServer.ForkingMixIn, HTTPServer):
    pass

class S(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        # self.wfile.write("<html><body><h1>hi!</h1></body></html>")

    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
        # post_data = self.rfile.read(content_length) # <--- Gets the data itself
        ctype, pdict = cgi.parse_header(self.headers['content-type'])
        print pdict
        multipart_data = cgi.parse_multipart(self.rfile, pdict)
        filename = uuid.uuid1()
        fo = open("tmp/%s.jpg" % filename, "w")
        fo.write(multipart_data['pic'][0])
        fo.close()
        # print multipart_data
        #判断识别的图片的类型   身份证/学生卡/pdf
        picType = multipart_data['type'][0]
        if picType == "idCard":
            result = processIdCard("tmp/%s.jpg" % filename)
        elif picType == "studentCard":
            result = processStudentCard("tmp/%s.jpg" % filename)
        if os.path.exists("tmp/%s.jpg" % filename):
            # 删除文件
            os.remove("tmp/%s.jpg" % filename)
        # print result
        print '完成'
        self._set_headers()
        self.wfile.write(json.dumps(result))

def http_server(server_class=ForkingServer, handler_class=S, port=8080):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)  
    print 'Starting httpd...'
    httpd.serve_forever()

if __name__=="__main__":
    http_server()
