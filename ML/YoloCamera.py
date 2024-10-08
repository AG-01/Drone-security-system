import uuid
import cv2
from yolov8 import YOLOv8
import time
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import Config
import requests
# Initialize yolov8 object detector
model_path = r"model/best.onnx"
yolov8_detector = YOLOv8(model_path, confidence_threshold=0.2, iou_threshold=0.3)

def send_alert_email(prediction):
     # Log alert to API
    api_url = Config.domain+'api/log_alert'  # Replace with your actual URL
    alert_data = {
        "type": str(prediction).upper(),
        "description": f"{str(prediction).capitalize()} detected in building",
        "address": "Camera location address",  # You should replace this with actual address
        "latitude": "79.66444",
        "longitude": "76.344",
        "cam_type": "CCTV"  # Or whatever type your camera is
    }
    try:
        response = requests.post(api_url, json=alert_data)
        if response.status_code == 201:
            print("Alert logged successfully")
        else:
            print(f"Failed to log alert. Status code: {response.status_code}")
    except requests.RequestException as e:
        print(f"Error sending alert to API: {e}")

 
 
def detect_from_camera(video_path):
    cap = cv2.VideoCapture(video_path)
    random_filename = ""
    
    last_prediction = None
    prediction_start_time = None
 
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Detect Objects
        boxes, scores, class_ids = yolov8_detector(frame)
        
        # Draw detections
        combined_img, predicted_class = yolov8_detector.draw_detections(frame)
        
        # Check for continuous prediction
        current_time = time.time()
        if predicted_class in ['FIRE', 'SMOKE']:
            if predicted_class == last_prediction:
                if prediction_start_time is None:
                    prediction_start_time = current_time
                elif current_time - prediction_start_time >= 2:
                    prediction_start_time = None
                    send_alert_email(predicted_class)
            else:
                prediction_start_time = current_time
        else:
            prediction_start_time = None
         
        last_prediction = predicted_class

        # Show the result with label and bounding box
        cv2.imshow("Detection", combined_img)
        
        key = cv2.waitKey(1) & 0xFF
        if key == 27:  # 'esc' key
            break
        elif key == ord('q'):
            break
        elif key == ord('s'):
            random_filename = "uploaded_datasets/" + str(uuid.uuid4()) + '.jpg'
            cv2.imwrite(random_filename, combined_img)
            print("Image saved as:", random_filename)

    # Release the camera and close all windows
    cap.release()
    cv2.destroyAllWindows()
    return random_filename, predicted_class

# Call the function
# detect_from_camera(0)  # 0 for default webcam, or provide a video file path