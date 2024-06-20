import cv2
import numpy as np
from sklearn.cluster import KMeans
import os
import datetime
from flask import Flask, request, jsonify, render_template
from PIL import Image, ExifTags
from inference_sdk import InferenceHTTPClient
from werkzeug.utils import secure_filename

# Flask 앱 초기화
app = Flask(__name__)

# 상수 정의
IMAGE_FOLDER = 'Brand'
TEMP_FOLDER = 'temp'
os.makedirs(IMAGE_FOLDER, exist_ok=True)
os.makedirs(TEMP_FOLDER, exist_ok=True)

CLIENT = InferenceHTTPClient(
    api_url="https://detect.roboflow.com",
    api_key="SC3i5IxjBihxekARxSUL"
)

BRAND_COLORS = {
    'Dart': ((0, 0, 80), (30, 20, 255)),
    'Deer': ((50, 100, 100), (200, 255, 255)),
    'Swing': ((100, 100, 100), (255, 255, 255)),
    'Beam': ((50, 0, 50), (100, 200, 200)),
    'XingXing': ((20, 80, 100), (100, 200, 255)),
    'Alphaka': ((0, 30, 80), (50, 100, 255)),
    'Gcoo': ((30, 80, 60), (130, 200, 160)),
    'Kickgoing': ((80, 90, 0), (170, 190, 100)),
}

# Flask 루트 경로
@app.route('/')
def index():
    return render_template('index.html')

# 이미지 처리 및 객체 탐지 함수
@app.route('/detect', methods=['POST'])
def detect():
        file = request.files['file']
        if not file:
            return jsonify({'error': 'No file provided'}), 400

        try:
            timestamp = datetime.datetime.now().strftime("%Y%m%d%H%M%S")
            filename = secure_filename(f"{timestamp}.{file.filename.split('.')[-1]}")
            temp_path = os.path.join(TEMP_FOLDER, filename)
            file.save(temp_path)

            # Check if file is valid image
            with Image.open(temp_path) as image:
                lat, lon = get_image_location(image)
                if lat is None or lon is None:
                    os.remove(temp_path)
                    return jsonify({'error': 'No location data available, cannot upload the photo.'}), 400

                result = CLIENT.infer(temp_path, model_id="dku-opensourceai-15-helmet/1")
                image_cv = cv2.imread(temp_path)
                boxes = detect_brand(result['predictions'])
                estimated_brand = estimate_brand(image_cv, boxes)
                helmet_status = get_helmet_status(result['predictions'])

                if helmet_status == '미착용':
                    for brand in estimated_brand:
                        save_filename = f"{filename[:-4]}_lat{lat}_lon{lon}.jpg"
                        save_path_root = os.path.join(IMAGE_FOLDER, brand)
                        save_path = os.path.join(save_path_root, save_filename)
                        os.makedirs(save_path_root, exist_ok=True)
                        image.save(save_path)  # Ensure image is saved properly
                    os.remove(temp_path)
                    return jsonify({'helmet_status': helmet_status, 'Brand': brand})

                os.remove(temp_path)
                return jsonify({'helmet_status': helmet_status})

        except Exception as e:
            print(f"Exception in /detect: {e}")
            return jsonify({'error': str(e)}), 500

# 이미지에서 GPS 정보 추출
def get_image_location(image):
    try:
        exif_data = image._getexif()
        if not exif_data:
            return None, None

        gps_info = exif_data.get(34853)
        if not gps_info:
            return None, None

        def convert_to_degrees(value):
            d, m, s = value
            return (d + (m / 60.0) + (s / 3600.0)) 

        lat_ref = gps_info.get(1)
        lon_ref = gps_info.get(3)
        lat = gps_info.get(2)
        lon = gps_info.get(4)
        if lat and lon:
            lat = convert_to_degrees(lat)
            lon = convert_to_degrees(lon)
            if lat_ref != 'N':
                lat = -lat
            if lon_ref != 'E':
                lon = -lon
            return lat, lon
        return None, None
    except Exception:
        return None, None


# YOLOv5를 이용한 스쿠터 객체 탐지
def detect_brand(predictions):
    boxes = []
    for prediction in predictions:
        if prediction['class'] == 'NoHelmet':
            x, y, width, height = int(prediction['x']), int(prediction['y']), int(prediction['width']/2), int(prediction['height']/2)
            
            boxes.append([x -width, y, x + width, y - height])
            
    print(boxes)
    return boxes

# K-평균 군집화를 이용한 주요 색상 분석
def get_dominant_colors(image, k=3):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = image.reshape((image.shape[0] * image.shape[1], 3))
    clt = KMeans(n_clusters=k)
    clt.fit(image)
    hist = centroid_histogram(clt)
    return clt.cluster_centers_, hist

def centroid_histogram(clt):
    num_labels = np.arange(0, len(np.unique(clt.labels_)) + 1)
    hist, _ = np.histogram(clt.labels_, bins=num_labels)
    hist = hist.astype("float")
    hist /= hist.sum()
    return hist

def is_color_in_range(color, lower_hsv, upper_hsv):
    color_hsv = cv2.cvtColor(np.uint8([[color]]), cv2.COLOR_BGR2HSV)[0][0]
    return all(lower_hsv <= color_hsv) and all(color_hsv <= upper_hsv)

def analyze_colors_with_contouring(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    edged = cv2.Canny(blurred, 50, 150)
    contours, _ = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    mask = np.zeros(image.shape[:2], dtype="uint8")
    cv2.drawContours(mask, contours, -1, 255, -1)
    masked_image = cv2.bitwise_and(image, image, mask=mask)

    return masked_image

def estimate_brand(image, boxes):
    estimated_brands = []

    for box in boxes:
        x, y, w, h = box
        roi = image[y:y+h, x:x+w]
        
        masked_roi = analyze_colors_with_contouring(roi)
        dominant_colors, hist = get_dominant_colors(masked_roi, k=3)
        
        brand_ratios = {brand: 0 for brand in BRAND_COLORS}
        for color, ratio in zip(dominant_colors, hist):
            for brand, (lower_hsv, upper_hsv) in BRAND_COLORS.items():
                if is_color_in_range(color, np.array(lower_hsv), np.array(upper_hsv)):
                    brand_ratios[brand] += ratio
        
        estimated_brand = max(brand_ratios, key=brand_ratios.get)
        estimated_brands.append(estimated_brand)
    
    return estimated_brands

def get_helmet_status(predictions):
    return '미착용' if any(item['class'] == 'NoHelmet' or (item['class'] == 'Helmet' and item['confidence'] < 0.8) for item in predictions) else '착용'


if __name__ == '__main__':
    app.run(debug=True)
