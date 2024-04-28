from flask import Flask, request, jsonify, redirect, url_for
from PIL import Image
import torch

# YOLO 모델 로드
model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

app = Flask(__name__)

@app.route('/')
def index():
    return redirect(url_for('static', filename='index.html'))

@app.route('/detect', methods=['POST'])
def detect():
    image = Image.open(request.files['file'].stream)  # 클라이언트에서 이미지 파일 받기
    results = model(image)  # 이미지에서 객체 감지
    results_data = results.pandas().xyxy[0].to_json(orient="records")  # 결과를 JSON 형식으로 변환
    return jsonify(results_data)

if __name__ == '__main__':
    app.run(debug=True)