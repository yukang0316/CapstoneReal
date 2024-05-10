import torch
import cv2

model = torch.hub.load('ultralytics/yolov5', 'yolov5s') #실 사용모델로 변경
    
img = cv2.imread('KakaoTalk_20240503_215142411.png') #이미지파일 경로 Image.open(request.files['file'].stream)
img = cv2.resize(img,(1000, 650))

result = model(img)
print('result: ', result)

data_frame = result.pandas().xyxy[0]
print('data_frame:')
print(data_frame)

indexes = data_frame.index
for index in indexes:
    # 좌상단 시작점
    x1 = int(data_frame['xmin'][index])
    y1 = int(data_frame['ymin'][index])
    # 우하단 끝점
    x2 = int(data_frame['xmax'][index])
    y2 = int(data_frame['ymax'][index ])

    # 분류명
    label = data_frame['name'][index ]
    # confidence
    conf = data_frame['confidence'][index]
    text = label + ' ' + str(conf.round(decimals= 2))

    cv2.rectangle(img, (x1,y1), (x2,y2), (255,255,0), 2) #박싱하기
    cv2.putText(img, text, (x1,y1-5), cv2.FONT_HERSHEY_PLAIN, 2, (255,255,0), 2) #confidence 표시

cv2.imshow('IMAGE', img)
cv2.waitKey(0)