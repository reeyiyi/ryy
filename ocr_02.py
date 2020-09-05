from PIL import Image
from pytesseract import *
import sys
import cv2

#img = Image.open(path) #path is the path of image file

# 이미지를 문자열로 바꾸는 함수
# filename : 받은 파일 명 (파일경로여야함)
def ocr_to_string(filename):
    image = cv2.imread(filename)
    # 그레이 스케일 임계값 조정
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY) # 그레이 스케일 변환
    gray = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]
    # gray = cv2.medianBlur(gray, 10)
    # image=Image.open(filename)
    # 추출(이미지파일, 추출언어, 옵션)
    # preseve_interword_spaces : 단어 간격 옵션을 조절하면서 추출 정확도를 확인
    # psm(페이지 세그먼트 모드 : 이미지 )
    cv2.imshow("gray", gray)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
    text = image_to_string(gray, lang="kor", config='--psm 6 --oem 3 -c preserve_interword_spaces=3')
    # print(text)

"""
# 문자열을 텍스트 파일로 바꿈
def str_to_txt(in_text, out_text):
    with open(in_text + '.txt', 'w', encoding='utf-8') as f:
        f.write(out_text)
"""


ocr_to_string("C:/Users/HOME/Documents/GitHub/study/test04.PNG")
