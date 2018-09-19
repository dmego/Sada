# -*- coding: utf-8 -*-
from PIL import Image
import pytesseract
import cv2
import numpy as np
import re
import os

x = 1280.00 / 3840.00
pixel_x = int(x * 3840)
print x, pixel_x

currentPath = os.getcwd()


def studentcardocr(imgname):
    # generate_mask(x)
    img_data_gray, img_org = img_resize_gray(imgname)
    cv2.imwrite("img_data_gray.jpg",img_data_gray)
    cv2.imwrite("img_org.jpg",img_org)
    result_dict = dict()
    name_pic = find_name(img_data_gray, img_org)
    # showimg(name_pic)
    # print 'name'
    result_dict['姓名'] = get_name(name_pic)
    # print result_dict['name']

    idnum_pic = find_idnum(img_data_gray, img_org)
    # showimg(idnum_pic)
    # print 'idnum'
    result_dict['学号'] = get_idnum(idnum_pic)
    # print result_dict['idnum']

    class_pic = find_class(img_data_gray, img_org)
    # showimg(class_pic)
    cv2.imwrite('9.png',class_pic)
    # print 'idnum'
    result_dict['班级'] = get_class(class_pic)
    # print result_dict['idnum']

    return result_dict


#生成模块模板 将不标准的模板转换成标准的模板
def generate_mask(x):
    name_mask_pic = cv2.UMat(cv2.imread('mask/name_mask.jpg'))
    idnum_mask_pic = cv2.UMat(cv2.imread('mask/idnum_mask.jpg'))
    class_mask_pic = cv2.UMat(cv2.imread('mask/class_mask.jpg'))

    name_mask_pic = img_resize_x(name_mask_pic)
    idnum_mask_pic = img_resize_x(idnum_mask_pic)
    class_mask_pic = img_resize_x(class_mask_pic)

    cv2.imwrite('mask/name_mask_%s.jpg' % pixel_x, name_mask_pic)
    cv2.imwrite('mask/idnum_mask_%s.jpg' % pixel_x, idnum_mask_pic)
    cv2.imwrite('mask/class_mask_%s.jpg' % pixel_x, class_mask_pic)


# 用于生成模板
def img_resize_x(imggray):
    # print 'dheight:%s' % dheight
    crop = imggray
    size = crop.get().shape
    dheight = int(size[0] * x)
    dwidth = int(size[1] * x)
    crop = cv2.resize(src=crop, dsize=(dwidth, dheight), interpolation=cv2.INTER_CUBIC)
    return crop


def img_resize(imggray, dheight):
    # print 'dheight:%s' % dheight
    crop = imggray
    size = crop.get().shape
    height = size[0]
    width = size[1]
    width = width * dheight / height
    crop = cv2.resize(src=crop, dsize=(width, dheight), interpolation=cv2.INTER_CUBIC)
    return crop


def img_resize_gray(imgorg):
    # imgorg = cv2.imread(imgname)
    crop = imgorg
    size = cv2.UMat.get(crop).shape
    # print size
    height = size[0]
    width = size[1]
    # 参数是根据3840调的
    height = int(height * 3840 * x / width)
    # print height
    crop = cv2.resize(src=crop, dsize=(int(3840 * x), height), interpolation=cv2.INTER_CUBIC)
    return hist_equal(cv2.cvtColor(crop, cv2.COLOR_BGR2GRAY)), crop


def find_name(crop_gray, crop_org):
    template = cv2.UMat(cv2.imread('%s/studentcard/mask/name_mask.jpg' % (currentPath), 0))
    # showimg(crop_org)
    w, h = cv2.UMat.get(template).shape[::-1]
    res = cv2.matchTemplate(crop_gray, template, cv2.TM_CCOEFF_NORMED)
    min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)
    print max_loc
    top_left = (max_loc[0] + w, max_loc[1] - int(20 * x))
    bottom_right = (top_left[0] + int(1000 * x), top_left[1] + int(400 * x))
    result = cv2.UMat.get(crop_org)[top_left[1]:bottom_right[1], top_left[0]:bottom_right[0]]
    cv2.rectangle(crop_gray, top_left, bottom_right, 255, 2)
    # showimg(result)
    return cv2.UMat(result)




def find_idnum(crop_gray, crop_org):
    template = cv2.UMat(cv2.imread('%s/studentcard/mask/idnum_mask.jpg' % (currentPath), 0))
    # showimg(template)
    # showimg(crop_gray)
    w, h = cv2.UMat.get(template).shape[::-1]
    res = cv2.matchTemplate(crop_gray, template, cv2.TM_CCOEFF_NORMED)
    min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)
    top_left = (max_loc[0] + w, max_loc[1] - int(20 * x))
    bottom_right = (top_left[0] + int(1000 * x), top_left[1] + int(400 * x))
    result = cv2.UMat.get(crop_org)[top_left[1]:bottom_right[1], top_left[0]:bottom_right[0]]
    cv2.rectangle(crop_gray, top_left, bottom_right, 255, 2)
    # showimg(crop_gray)
    return cv2.UMat(result)



def find_class(crop_gray, crop_org):
    template = cv2.UMat(cv2.imread('%s/studentcard/mask/class_mask.jpg' % (currentPath), 0))
    # showimg(template)
    # showimg(crop_gray)
    w, h = cv2.UMat.get(template).shape[::-1]
    res = cv2.matchTemplate(crop_gray, template, cv2.TM_CCOEFF_NORMED)
    min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)
    top_left = (max_loc[0] + w, max_loc[1] - int(20 * x))
    bottom_right = (top_left[0] + int(1000 * x), top_left[1] + int(400 * x))
    result = cv2.UMat.get(crop_org)[top_left[1]:bottom_right[1], top_left[0]:bottom_right[0]]
    cv2.rectangle(crop_gray, top_left, bottom_right, 255, 2)
    # showimg(crop_gray)
    return cv2.UMat(result)



def showimg(img):
    cv2.namedWindow("contours", 0);
    cv2.resizeWindow("contours", 1280, 720);
    cv2.imshow("contours", img)
    cv2.waitKey()


# psm model:
#  0    Orientation and script detection (OSD) only.
#  1    Automatic page segmentation with OSD.
#  2    Automatic page segmentation, but no OSD, or OCR.
#  3    Fully automatic page segmentation, but no OSD. (Default)
#  4    Assume a single column of text of variable sizes.
#  5    Assume a single uniform block of vertically aligned text.
#  6    Assume a single uniform block of text.
#  7    Treat the image as a single text line.
#  8    Treat the image as a single word.
#  9    Treat the image as a single word in a circle.
#  10    Treat the image as a single character.
#  11    Sparse text. Find as much text as possible in no particular order.
#  12    Sparse text with OSD.
#  13    Raw line. Treat the image as a single text line,
# 			bypassing hacks that are Tesseract-specific

def get_name(img):
    #    cv2.imshow("method3", img)
    #    cv2.waitKey()
    _, _, red = cv2.split(img)  # split 会自动将UMat转换回Mat
    red = cv2.UMat(red)
    red = hist_equal(red)
    red = cv2.adaptiveThreshold(red, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 151, 50)
    #    red = cv2.medianBlur(red, 3)
    red = img_resize(red, 150)
    # cv2.imwrite('name.png', red)
    #    img2 = Image.open('address.png')
    img = Image.fromarray(cv2.UMat.get(red).astype('uint8'))
    return punc_filter(pytesseract.image_to_string(img, lang='chi_sim', config='-psm 13').replace(" ", ""))


def get_idnum(img):
    #    cv2.imshow("method3", img)
    #    cv2.waitKey()
    _, _, red = cv2.split(img)
    red = cv2.UMat(red)
    red = hist_equal(red)
    red = cv2.adaptiveThreshold(red, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 151, 50)
    red = img_resize(red, 150)
    # cv2.imwrite('idnum_red.png', red)
    img = Image.fromarray(cv2.UMat.get(red).astype('uint8'))
    return pytesseract.image_to_string(img, lang='chi_sim', config='-psm 13').replace(" ", "")



def get_class(img):
    #    cv2.imshow("method3", img)
    #    cv2.waitKey()
    _, _, red = cv2.split(img)
    red = cv2.UMat(red)
    red = hist_equal(red)
    red = cv2.adaptiveThreshold(red, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 151, 50)
    red = img_resize(red, 150)
    # cv2.imwrite('idnum_red.png', red)
    img = Image.fromarray(cv2.UMat.get(red).astype('uint8'))
    return pytesseract.image_to_string(img, lang='cls', config='-psm 7').replace(" ", "")



def punc_filter(str):
    temp = str
    xx = u"([\u4e00-\u9fff0-9]+)"
    pattern = re.compile(xx)
    results = pattern.findall(temp)
    string = ""
    for result in results:
        string += result
    return string


# 这里使用直方图拉伸，不是直方图均衡
def hist_equal(img):
    # clahe_size = 8
    # clahe = cv2.createCLAHE(clipLimit=1.0, tileGridSize=(clahe_size, clahe_size))
    # result = clahe.apply(img)
    # test

    # result = cv2.equalizeHist(img)

    image = img.get()  # UMat to Mat
    # result = cv2.equalizeHist(image)
    lut = np.zeros(256, dtype=image.dtype)  # 创建空的查找表
    # lut = np.zeros(256)
    hist = cv2.calcHist([image],  # 计算图像的直方图
                        [0],  # 使用的通道
                        None,  # 没有使用mask
                        [256],  # it is a 1D histogram
                        [0, 256])
    minBinNo, maxBinNo = 0, 255
    # 计算从左起第一个不为0的直方图柱的位置
    for binNo, binValue in enumerate(hist):
        if binValue != 0:
            minBinNo = binNo
            break
    # 计算从右起第一个不为0的直方图柱的位置
    for binNo, binValue in enumerate(reversed(hist)):
        if binValue != 0:
            maxBinNo = 255 - binNo
            break
    # print minBinNo, maxBinNo
    # 生成查找表
    for i, v in enumerate(lut):
        if i < minBinNo:
            lut[i] = 0
        elif i > maxBinNo:
            lut[i] = 255
        else:
            lut[i] = int(255.0 * (i - minBinNo) / (maxBinNo - minBinNo) + 0.5)
    # 计算,调用OpenCV cv2.LUT函数,参数 image --  输入图像，lut -- 查找表
    # print lut
    result = cv2.LUT(image, lut)
    # print type(result)
    # showimg(result)
    return cv2.UMat(result)


if __name__ == "__main__":
    generate_mask(1)
